package main.java.com.sim.network;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EthLinkRouter extends ExternalEvent {
    private final List<EthAdapter> adapters;

    @Getter
    @Setter
    private boolean interframeGap;

    EthLinkRouter(Model owner, String name, boolean showInTrace, List<EthAdapter> adapters) {
        super(owner, name, showInTrace);
        this.adapters = new ArrayList<>(adapters);
    }

    @Override
    public void eventRoutine() {
        NetworkModel model = (NetworkModel) getModel();
        if (!isInterframeGap()) {
            if (!model.getEthLink().isEmpty()) {
                EthFrame frame = model.getEthLink().first();
                sendTraceNote("FRAME-START " + frame.getName());
                sendTraceNote("DEST ADDRESS = " + frame.getDestAddress());

                EthAdapter adapter = getAdapterAddress(frame);
                if (adapter == null) sendTraceNote("NOT FOUND ADAPTER");
                else sendTraceNote("FOUND ADAPTER = " + adapter.getAdapterAddress());

                //interfame gap -> for 10Mb = 9,6 microsec
                ReleaseIFGEvent ifgEvent = new ReleaseIFGEvent(model, "release-IFG-model", true, this, adapter);
                //Max Message size = 1518 B => 12144 b
                ifgEvent.schedule(frame, new TimeSpan(12144 + 9.6, TimeUnit.MICROSECONDS));

                setInterframeGap(true);
            }
            schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
        }
    }

    private EthAdapter getAdapterAddress(EthFrame frame) {
        return adapters.stream().filter(ethAdapter -> ethAdapter.getAdapterAddress().equals(frame.getDestAddress())).findFirst().orElse(null);
    }
}
