package main.java.com.sim.network.knxeib;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;

/**
 * Created by kubaw on 09/05/2018.
 */
public class KnxAdapter extends ExternalEvent {

    public KnxAdapter(Model owner, String name, boolean showInTrace){
        super(owner, name, showInTrace);
    }

    @Override
    public void eventRoutine() throws SuspendExecution {

    }
}
