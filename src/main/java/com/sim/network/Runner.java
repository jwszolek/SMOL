package main.java.com.sim.network;


import desmoj.core.simulator.*;

import java.util.concurrent.TimeUnit;

public class Runner {

    private static TimeUnit referenceUnit = TimeUnit.SECONDS;
    private static TimeUnit epsilon = TimeUnit.MICROSECONDS;

    public static void main(String[] args) {

        NetworkModel model = new NetworkModel(null,"SMOL Simulation Engine",true,true);
        Experiment exp = new Experiment("SMOLSimulationExperiment", new SingleUnitTimeFormatter(referenceUnit, epsilon,6,false));

        model.connectToExperiment(exp);
        exp.setShowProgressBar(false);
        exp.stop(new TimeInstant(10, TimeUnit.MICROSECONDS));


        exp.tracePeriod(new TimeInstant(0), new TimeInstant(10,TimeUnit.MICROSECONDS));
        exp.debugPeriod(new TimeInstant(0), new TimeInstant(10,TimeUnit.MICROSECONDS));

        exp.start();
        exp.report();

        exp.finish();
    }
}
