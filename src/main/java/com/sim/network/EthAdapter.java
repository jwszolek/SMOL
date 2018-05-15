package main.java.com.sim.network;



import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import desmoj.core.simulator.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EthAdapter extends ExternalEvent {

    protected Queue<EthFrame> inAdapterQueue;
    protected Queue<EthFrame> outAdapterQueue;
    protected Queue<TCPMessage> inMsgQueue;
    public Queue<TCPMessage> outMsgQueue;
    private String adapterAddress;
    private boolean collisionDetected = false;
    private boolean transmits = false;


    public String getAdapterAddress() {
        return adapterAddress;
    }
    public boolean isCollisionDetected() {
        return collisionDetected;
    }
    public void setCollisionDetected(boolean collisionDetected) {
        this.collisionDetected = collisionDetected;
    }


    public EthAdapter(Model owner, String name, boolean showInTrace, String address) {
        super(owner, name, showInTrace);
        inAdapterQueue = new Queue<EthFrame>(owner,"in-adapterQueue-"+address,true,true);
        outAdapterQueue = new Queue<EthFrame>(owner,"out-adapterQueue-"+address,true,true);
        inMsgQueue = new Queue<TCPMessage>(owner, "in-messageQueue-"+address, true, true);
        outMsgQueue = new Queue<TCPMessage>(owner, "out-messageQueue-"+address, true, true);
        this.adapterAddress = address;
    }

    @Override
    public void eventRoutine() {
        NetworkModel model = (NetworkModel) getModel();

        if(!this.collisionDetected) {
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
                //TODO: remove hardcoded address

                frame.setDestAddress("2");

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

            TCPMessage inTCPMessage = new TCPMessage(model, "IN-TCP-Message", true);
            inMsgQueue.insert(inTCPMessage);
        }


        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));

    }
}
