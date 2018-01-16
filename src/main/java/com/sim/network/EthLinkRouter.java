package main.java.com.sim.network;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EthLinkRouter extends ExternalEvent {

    private List<EthAdapter> adapters;

    public EthLinkRouter(Model owner, String name, boolean showInTrace, List<EthAdapter> adapters) {
        super(owner, name, showInTrace);
        this.adapters = new ArrayList<EthAdapter>(adapters);
    }

    @Override
    public void eventRoutine() {

        NetworkModel model = (NetworkModel)getModel();
        if(!model.ethLink.isEmpty()){
            EthFrame frame = model.ethLink.first();
            sendTraceNote("DEST ADDRESS = " + frame.getDestAddress());

            EthAdapter adapter = getAdapterAddress(frame);
            sendTraceNote("FOUND ADAPTER = " + adapter.getAdapterAddress());

            if(adapter != null){
                adapter.inAdapterQueue.insert(frame);
            }

            model.ethLink.remove(frame);
        }
        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }


    private EthAdapter getAdapterAddress(EthFrame frame) {

        for(EthAdapter adapter: adapters){
            if(adapter.getAdapterAddress() == frame.getDestAddress()){
                return adapter;
            }
        }
        return null;
    }
}
