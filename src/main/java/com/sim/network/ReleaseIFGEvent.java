package main.java.com.sim.network;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.concurrent.TimeUnit;

public class ReleaseIFGEvent extends Event<EthFrame> {
    private final EthLinkRouter ethLinkRouter;
    private final EthAdapter ethAdapter;

    public ReleaseIFGEvent(Model owner, String name, boolean showInTrace, EthLinkRouter router, EthAdapter adapter) {
        super(owner, name, showInTrace);
        this.ethLinkRouter = router;
        this.ethAdapter = adapter;
    }

    @Override
    public void eventRoutine(EthFrame ethFrame) {
        NetworkModel model = (NetworkModel)getModel();
        model.ethLink.remove(ethFrame);

        sendTraceNote("ETHLINK-LEFT-"+ethFrame.adapter.getName());
        ethFrame.setStopTransmission(presentTime());

        if (ethAdapter != null) {
            ethAdapter.getInAdapterQueue().insert(ethFrame);
        }
        ethLinkRouter.setInterframeGap(false);
        ethLinkRouter.schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }
}
