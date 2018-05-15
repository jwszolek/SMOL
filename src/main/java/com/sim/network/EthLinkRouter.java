package main.java.com.sim.network;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EthLinkRouter extends ExternalEvent {

    private List<EthAdapter> adapters;

    public void setInterframeGap(boolean interframeGap) {
        this.interframeGap = interframeGap;
    }
    private boolean interframeGap = false;

    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
    }
    private boolean firstRun = true;


    public EthLinkRouter(Model owner, String name, boolean showInTrace, List<EthAdapter> adapters) {
        super(owner, name, showInTrace);
        this.adapters = new ArrayList<EthAdapter>(adapters);
    }

    @Override
    public void eventRoutine() {

        NetworkModel model = (NetworkModel) getModel();
        if (!interframeGap) {
            if (!model.ethLink.isEmpty()) {
                EthFrame frame = model.ethLink.first();
                sendTraceNote("FRAME-START " + frame.getName());

                sendTraceNote("DEST ADDRESS = " + frame.getDestAddress());
                EthAdapter adapter = getAdapterAddress(frame);
                sendTraceNote("FOUND ADAPTER = " + adapter.getAdapterAddress());
                frame.setStartTransmission(presentTime());

                //interfame gap -> for 10Mb = 9,6 microsec
                ReleaseIFGEvent ifgEvent = new ReleaseIFGEvent(model, "release-IFG-model", true, this, adapter);
                //Max Message size = 1518 B => 12144 b
                ifgEvent.schedule(frame, new TimeSpan(12144 + 9.6, TimeUnit.MICROSECONDS));

                this.interframeGap = true;
            }
            schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
        }
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
