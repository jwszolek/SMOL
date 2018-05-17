package main.java.com.sim.network.monitors;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import main.java.com.sim.network.EthFrame;
import main.java.com.sim.network.NetworkModel;

public class EthAdapterArrivalMonitor extends Event<EthFrame> {

    private NetworkModel myModel;

    public EthAdapterArrivalMonitor(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        this.myModel = (NetworkModel)owner;
    }

    @Override
    public void eventRoutine(EthFrame ethFrame) {

//        myModel.getEthQueue().insert(ethFrame);
//
//        if(myModel.getEthQueue().size() > 0){
//            sendTraceNote("test122");
//        }


    }

}
