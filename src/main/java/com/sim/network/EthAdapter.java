package main.java.com.sim.network;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import desmoj.core.simulator.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EthAdapter extends ExternalEvent {
    private final Queue<EthFrame> inAdapterQueue;
    private final Queue<EthFrame> outAdapterQueue;
    private final Queue<TCPMessage> inMsgQueue;
    private final Queue<TCPMessage> outMsgQueue;
    private final String adapterAddress;

    private boolean collisionDetected;

    public EthAdapter(Model owner, String name, boolean showInTrace, String address) {
        super(owner, name, showInTrace);
        inAdapterQueue = new Queue<EthFrame>(owner,"in-adapterQueue-"+address,true,true);
        outAdapterQueue = new Queue<EthFrame>(owner,"out-adapterQueue-"+address,true,true);
        inMsgQueue = new Queue<TCPMessage>(owner, "in-messageQueue-"+address, true, true);
        outMsgQueue = new Queue<TCPMessage>(owner, "out-messageQueue-"+address, true, true);
        this.adapterAddress = address;
    }

    public Queue<EthFrame> getInAdapterQueue() {
        return inAdapterQueue;
    }

    public Queue<EthFrame> getOutAdapterQueue() {
        return outAdapterQueue;
    }

    public Queue<TCPMessage> getInMsgQueue() {
        return inMsgQueue;
    }

    public Queue<TCPMessage> getOutMsgQueue() {
        return outMsgQueue;
    }

    public String getAdapterAddress() {
        return adapterAddress;
    }
    public boolean isCollisionDetected() {
        return collisionDetected;
    }
    public void setCollisionDetected(boolean collisionDetected) {
        this.collisionDetected = collisionDetected;
    }

    private EthFrame createFrame(String name){
        NetworkModel model = (NetworkModel) getModel();

        EthAdapter adapter = null;
        List<EthAdapter> adapterList = model.getEthAdapterList().stream().filter(x -> Objects.equals(getName(), x.getName())).collect(Collectors.toList());
        if (adapterList.size() == 1) {
            adapter = adapterList.get(0);
        } else {
            sendTraceNote("illegal number of instance");
        }

        return new EthFrame(model, name, true, adapter, presentTime());
    }

    @Override
    public void eventRoutine() {
        NetworkModel model = (NetworkModel) getModel();

        if(!isCollisionDetected()) {
            if (!outMsgQueue.isEmpty()) {

                TCPMessage tcpMessage = outMsgQueue.first();
                outMsgQueue.remove(tcpMessage);

                EthFrame frame = createFrame("ETH-Frame");
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

            inFrame.getTcpMessage().setStopTransmission(presentTime());
            if(inFrame.getName().contains("ACK")){
                String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS: ").format(new Date());
                System.err.println(timeStamp+"ACK recv: src="+inFrame.getTcpMessage().getSrcAddress()+", dst="+inFrame.getTcpMessage().getDstAddress()
                        +", "+inFrame.getName()+", adapter="+getName());

                sendTraceNote("FRAME-ACK " + inFrame.getName());
                sendTraceNote("TCPMSG-ACK-TIME|"+inFrame.getTcpMessage().getTransmissionTime().toString()+"|"+getName());
            } else {
                TCPMessage msg = new TCPMessage(model,"ACK Message",true);
                msg.setSrcAddress(inFrame.getTcpMessage().getDstAddress());
                msg.setDstAddress(inFrame.getTcpMessage().getSrcAddress());

                EthFrame ackFrame = createFrame("ACK-Frame");
                ackFrame.setDestAddress(msg.getDstAddress());
                ackFrame.setTcpMessage(msg);

                outAdapterQueue.insert(ackFrame);

                sendTraceNote("FRAME-STOP " + inFrame.getName());

                String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS: ").format(new Date());
                System.err.println(timeStamp+"ACK sent: src="+ackFrame.getTcpMessage().getSrcAddress()+", dst="+ackFrame.getTcpMessage().getDstAddress()
                        +", "+ackFrame.getName()+", adapter="+getName());

                sendTraceNote("TCPMSG-LEFT-TIME|"+inFrame.getTcpMessage().getTransmissionTime().toString()+"|"+inFrame.adapter.getName());

                TCPMessage inTCPMessage = new TCPMessage(model, "IN-TCP-Message", true);
                inMsgQueue.insert(inTCPMessage);
            }
        }

        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));

    }
}
