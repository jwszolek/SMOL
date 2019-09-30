package main.java.com.sim.network;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import desmoj.core.simulator.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class EthAdapter extends ExternalEvent {
    private final boolean ENABLE_ACK = true;

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

    @Override
    public void eventRoutine() {
        NetworkModel model = (NetworkModel) getModel();

        if (!isCollisionDetected()) {
            outMsgQueue(model);
            adapterQueue(model);
        }

        inAdapterQueue(model);

        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }

    private void outMsgQueue(NetworkModel model) {
        if (!outMsgQueue.isEmpty()) {

            TCPMessage tcpMessage = outMsgQueue.first();
            outMsgQueue.remove(tcpMessage);

            EthFrame frame = createFrame(model, (tcpMessage.getName().contains("ACK") ? "ETH-ACK-Frame" : "ETH-Frame"));
            frame.setDestAddress(tcpMessage.getDstAddress());
            frame.setTcpMessage(tcpMessage);

            outAdapterQueue.insert(frame);
        }
    }

    private void adapterQueue(NetworkModel model) {
        if (!outAdapterQueue.isEmpty()) {
            EthFrame frame = outAdapterQueue.first();
            outAdapterQueue.remove(frame);
            model.getEthPendingBuffer().insert(frame);
        }
    }

    private void inAdapterQueue(NetworkModel model) {
        if (!inAdapterQueue.isEmpty()) {
            EthFrame inFrame = inAdapterQueue.first();
            inAdapterQueue.remove(inFrame);

            sendTraceNote("FRAME-STOP " + inFrame.getName());
            inFrame.getTcpMessage().setStopTransmission(presentTime());

            if (ENABLE_ACK) {
                String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS: ").format(new Date());

                if (inFrame.getName().contains("ACK")) {
                    log.info(timeStamp + "ACK recv: src=" + inFrame.getTcpMessage().getSrcAddress() + ", dst=" + inFrame.getTcpMessage().getDstAddress()
                            + ", " + inFrame.getName() + ", adapter=" + getName());

                    sendTraceNote("TCPMSG-ACK-RECV-TIME|" + inFrame.getTcpMessage().getTransmissionTime().toString() + "|" + getName());
                } else {
                    TCPMessage ackMsg = new TCPMessage(model, "ACK Message", true);
                    ackMsg.setSrcAddress(inFrame.getTcpMessage().getDstAddress());
                    ackMsg.setDstAddress(inFrame.getTcpMessage().getSrcAddress());

                    outMsgQueue.insert(ackMsg);

                    sendTraceNote("TCPMSG-ACK-SENT-TIME|" + inFrame.getTcpMessage().getTransmissionTime().toString() + "|" + getName());

                    log.info(timeStamp + "ACK sent: src=" + ackMsg.getSrcAddress() + ", dst=" + ackMsg.getDstAddress()
                            + ", " + ackMsg.getName() + ", adapter=" + getName());
                }
            }

            sendTraceNote("TCPMSG-LEFT-TIME|" + inFrame.getTcpMessage().getTransmissionTime().toString() + "|" + inFrame.adapter.getName());
            inMsgQueue(model);
        }
    }

    private EthFrame createFrame(NetworkModel model, String name) {
        List<EthAdapter> adapters = model.getEthAdapterList().stream()
                .filter(x -> Objects.equals(getName(), x.getName())).collect(Collectors.toList());

        if (adapters.size() != 1) sendTraceNote("illegal number of instance");
        return new EthFrame(model, name, true, adapters.stream().findFirst().orElse(null), presentTime());
    }

    private void inMsgQueue(NetworkModel model) {
        TCPMessage inTCPMessage = new TCPMessage(model, "IN-TCP-Message", true);
        inMsgQueue.insert(inTCPMessage);
    }
}
