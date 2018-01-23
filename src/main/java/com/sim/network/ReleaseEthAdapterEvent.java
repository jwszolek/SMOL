package main.java.com.sim.network;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;

public class ReleaseEthAdapterEvent extends Event<EthFrame> {

    private EthAdapter adapter;

    public ReleaseEthAdapterEvent(Model owner, String name, boolean showInTrace, EthAdapter adapter) {
        super(owner, name, showInTrace);
        this.adapter = adapter;

    }

    @Override
    public void eventRoutine(EthFrame frame) throws SuspendExecution {
        if(adapter != null){
            adapter.setCollisionDetected(false);
            sendTraceNote("Zdjeta blokada " + adapter.getName());
        }


    }


}
