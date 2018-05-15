package main.java.com.sim.network;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.concurrent.TimeUnit;

public class TCPMessageGenerator extends ExternalEvent {

    private EthAdapter ethAdapter;
    private String destAddress;
    private double scheduleValue;

    public TCPMessageGenerator(Model owner, String name, boolean showInTrace, EthAdapter adapter, String destAddress, double scheduleValue){
        super(owner, name, showInTrace);
        this.ethAdapter = adapter;
        this.destAddress = destAddress;
        this.scheduleValue = scheduleValue;
    }

    @Override
    public void eventRoutine() {
        NetworkModel model = (NetworkModel)getModel();

        TCPMessage msg = new TCPMessage(model,"TCP Message",true);
        msg.setSrcAddress(ethAdapter.getAdapterAddress());
        msg.setDstAddress(destAddress);

        ethAdapter.outMsgQueue.insert(msg);

        //double randValue = model.getRandGeneratorValue();
        schedule(new TimeSpan(this.scheduleValue, TimeUnit.MILLISECONDS));

    }
}
