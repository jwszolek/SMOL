package main.java.com.sim.network;



import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.concurrent.TimeUnit;

public class EthAdapter extends ExternalEvent {


    public EthAdapter(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
    }

    @Override
    public void eventRoutine() {
        NetworkModel model = (NetworkModel) getModel();

        if (!model.ethQueue.isEmpty()) {
            EthFrame frame = model.ethQueue.first();
            model.ethQueue.remove(frame);
            model.ethLink.insert(frame);
        }

        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }
}
