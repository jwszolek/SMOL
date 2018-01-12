package main.java.com.sim.network;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.concurrent.TimeUnit;

public class TCPMessageGenerator extends ExternalEvent {

    private EthAdapter ethAdapter;
    private String destAddress;

    public TCPMessageGenerator(Model owner, String name, boolean showInTrace, EthAdapter adapter, String destAddress){
        super(owner, name, showInTrace);
        this.ethAdapter = adapter;
        this.destAddress = destAddress;
    }

    @Override
    public void eventRoutine() {
        NetworkModel model = (NetworkModel)getModel();

        TCPMessage msg = new TCPMessage(model,"TCP Message",true);
        ethAdapter.outMsgQueue.insert(msg);


        schedule(new TimeSpan(2, TimeUnit.MICROSECONDS));

    }
}
