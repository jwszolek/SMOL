package main.java.com.sim.network;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.concurrent.TimeUnit;

public class ReleaseIFGEvent extends Event<EthFrame> {

    private  EthLinkRouter ethLinkRouter = null;
    private  EthAdapter ethAdapter = null;

    public ReleaseIFGEvent(Model owner, String name, boolean showInTrace, EthLinkRouter router, EthAdapter adapter) {
        super(owner, name, showInTrace);
        this.ethLinkRouter = router;
        this.ethAdapter = adapter;
    }

    @Override
    public void eventRoutine(EthFrame ethFrame) throws SuspendExecution {

        NetworkModel model = (NetworkModel)getModel();
        model.ethLink.remove(ethFrame);

        sendTraceNote("ETHLINK-LEFT-"+ethFrame.adapter.getName());

        if (this.ethAdapter != null) {
            this.ethAdapter.inAdapterQueue.insert(ethFrame);
        }


        this.ethLinkRouter.setInterframeGap(false);
        this.ethLinkRouter.schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }
}
