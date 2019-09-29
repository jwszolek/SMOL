package main.java.com.sim.network;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import desmoj.core.simulator.*;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EthAdapter extends ExternalEvent {
    private final boolean ENABLE_ACK = false;

    @Getter
    private final Queue<EthFrame> inAdapterQueue;

    @Getter
    private final Queue<EthFrame> outAdapterQueue;

    private final Queue<TCPMessage> inMsgQueue;

    @Getter
    private final Queue<TCPMessage> outMsgQueue;

    @Getter
    private final String adapterAddress;

    @Getter
    @Setter
    private boolean collisionDetected;

    public EthAdapter(Model owner, String name, boolean showInTrace, String address) {
        super(owner, name, showInTrace);

        inAdapterQueue = new Queue<>(owner, "in-adapterQueue-" + address, true, true);
        outAdapterQueue = new Queue<>(owner, "out-adapterQueue-" + address, true, true);
        inMsgQueue = new Queue<>(owner, "in-messageQueue-" + address, true, true);
        outMsgQueue = new Queue<>(owner, "out-messageQueue-" + address, true, true);

        adapterAddress = address;
        collisionDetected = false;
    }

    private EthFrame createFrame(NetworkModel model, String name) {
        List<EthAdapter> adapters = model.getEthAdapterList().stream()
                .filter(x -> Objects.equals(getName(), x.getName())).collect(Collectors.toList());

        if (adapters.size() != 1) sendTraceNote("illegal number of instance");
        return new EthFrame(model, name, true, adapters.stream().findFirst().orElse(null), presentTime());
    }

    @Override
    public void eventRoutine() {
        NetworkModel model = (NetworkModel) getModel();

        if (!isCollisionDetected()) {
            if (!outMsgQueue.isEmpty()) {

                TCPMessage tcpMessage = outMsgQueue.first();
                outMsgQueue.remove(tcpMessage);

                EthFrame frame = createFrame(model, "ETH-Frame");
                frame.setDestAddress(tcpMessage.getDstAddress());
                frame.setTcpMessage(tcpMessage);

                outAdapterQueue.insert(frame);
            }

            if (!outAdapterQueue.isEmpty()) {
                EthFrame frame = outAdapterQueue.first();
                outAdapterQueue.remove(frame);
                model.getEthPendingBuffer().insert(frame);
            }
        }

        if (!inAdapterQueue.isEmpty()) {
            EthFrame inFrame = inAdapterQueue.first();
            inAdapterQueue.remove(inFrame);

            if (ENABLE_ACK) {
                inFrame.getTcpMessage().setStopTransmission(presentTime());
                if (inFrame.getName().contains("ACK")) {
                    String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS: ").format(new Date());

                    System.err.println(timeStamp + "ACK recv: src=" + inFrame.getTcpMessage().getSrcAddress() + ", dst=" + inFrame.getTcpMessage().getDstAddress()
                            + ", " + inFrame.getName() + ", adapter=" + getName());

                    sendTraceNote("FRAME-ACK " + inFrame.getName());
                    sendTraceNote("TCPMSG-ACK-TIME|" + inFrame.getTcpMessage().getTransmissionTime().toString() + "|" + getName());
                } else {
                    TCPMessage msg = new TCPMessage(model, "ACK Message", true);
                    msg.setSrcAddress(inFrame.getTcpMessage().getDstAddress());
                    msg.setDstAddress(inFrame.getTcpMessage().getSrcAddress());

                    EthFrame ackFrame = createFrame(model, "ACK-Frame");
                    ackFrame.setDestAddress(msg.getDstAddress());
                    ackFrame.setTcpMessage(msg);

                    outAdapterQueue.insert(ackFrame);

                    sendTraceNote("FRAME-STOP " + inFrame.getName());

                    String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS: ").format(new Date());
                    System.err.println(timeStamp + "ACK sent: src=" + ackFrame.getTcpMessage().getSrcAddress() + ", dst=" + ackFrame.getTcpMessage().getDstAddress()
                            + ", " + ackFrame.getName() + ", adapter=" + getName());
                }
            } else {
                sendTraceNote("FRAME-STOP " + inFrame.getName());
                inFrame.getTcpMessage().setStopTransmission(presentTime());
            }

            sendTraceNote("TCPMSG-LEFT-TIME|" + inFrame.getTcpMessage().getTransmissionTime().toString() + "|" + inFrame.adapter.getName());

            TCPMessage inTCPMessage = new TCPMessage(model, "IN-TCP-Message", true);
            inMsgQueue.insert(inTCPMessage);
        }

        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }
}
