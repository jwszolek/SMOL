package main.java.com.sim.network;


import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;

import java.util.concurrent.TimeUnit;

public class Runner {

    public static void main(String[] args) {

        NetworkModel model = new NetworkModel(null,"SMOL Simulation Engine",true,true);
        Experiment exp = new Experiment("SMOLSimulationExperiment");

        model.connectToExperiment(exp);
        exp.setShowProgressBar(false);
        exp.stop(new TimeInstant(10, TimeUnit.SECONDS));

        exp.tracePeriod(new TimeInstant(0), new TimeInstant(10,TimeUnit.SECONDS));
        exp.debugPeriod(new TimeInstant(0), new TimeInstant(10,TimeUnit.SECONDS));

        exp.start();
        exp.report();
        exp.finish();
    }
}
