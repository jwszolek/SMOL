package main.java.com.sim.network;


import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import desmoj.core.simulator.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class EthAdapter extends ExternalEvent {
    @Getter
    private Queue<TCPMessage> inMsgQueue;

    @Getter
    private Queue<TCPMessage> outMsgQueue;

    @Getter
    @Setter
    private boolean collisionDetected = false;

    @Getter
    private String adapterAddress;

    Queue<EthFrame> inAdapterQueue;
    Queue<EthFrame> outAdapterQueue;

    public EthAdapter(Model owner, String name, boolean showInTrace, String address) {
        super(owner, name, showInTrace);

        this.inAdapterQueue = new Queue<>(owner, "in-adapterQueue-" + address, true, true);
        this.outAdapterQueue = new Queue<>(owner, "out-adapterQueue-" + address, true, true);
        this.inMsgQueue = new Queue<>(owner, "in-messageQueue-" + address, true, true);
        this.outMsgQueue = new Queue<>(owner, "out-messageQueue-" + address, true, true);
        this.adapterAddress = address;
    }

    @Override
    public void eventRoutine() {
        NetworkModel model = (NetworkModel) getModel();

        if (!this.collisionDetected) {
            if (!outMsgQueue.isEmpty()) {

                TCPMessage tcpMessage = outMsgQueue.first();

                outMsgQueue.remove(tcpMessage);

                EthAdapter adapter = null;
                List<EthAdapter> adapterList = model.getEthAdapterList().stream().filter(x -> this.getName() == x.getName()).collect(Collectors.toList());
                if (adapterList.size() == 1) {
                    adapter = adapterList.get(0);
                } else {
                    sendTraceNote("illegal number of instance");
                }

                EthFrame frame = new EthFrame(model, "ETH-Frame", true, adapter, presentTime());

                frame.setDestAddress(tcpMessage.getDstAddress());
                frame.setTcpMessage(tcpMessage);

                outAdapterQueue.insert(frame);
            }


            if (!outAdapterQueue.isEmpty()) {
                EthFrame frame = outAdapterQueue.first();
                outAdapterQueue.remove(frame);
                model.ethPendingBuffer.insert(frame);
            }
        }

        if (!inAdapterQueue.isEmpty()) {
            EthFrame inFrame = inAdapterQueue.first();
            inAdapterQueue.remove(inFrame);
            sendTraceNote("FRAME-STOP " + inFrame.getName());
            inFrame.getTcpMessage().setStopTransmission(presentTime());

            sendTraceNote("TCPMSG-LEFT-TIME|" + inFrame.getTcpMessage().getTransmissionTime().toString() + "|" + inFrame.adapter.getName());

            TCPMessage inTCPMessage = inFrame.getTcpMessage();
            inTCPMessage.rename("IN-TCP-Message");

            inMsgQueue.insert(inTCPMessage);
        }

        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }
}
