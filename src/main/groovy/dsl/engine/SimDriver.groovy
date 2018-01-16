package main.groovy.dsl.engine

import desmoj.core.simulator.Experiment
import desmoj.core.simulator.SingleUnitTimeFormatter
import desmoj.core.simulator.TimeInstant
import main.java.com.sim.network.NetworkModel

import java.util.concurrent.TimeUnit

class SimDriver {

    private static TimeUnit referenceUnit = TimeUnit.SECONDS;
    private static TimeUnit epsilon = TimeUnit.MICROSECONDS;


    static void main(String[] args) {
        NetworkModel model = new NetworkModel(null,"SMOL Simulation Engine",true,true);
        Experiment exp = new Experiment("SMOLSimulationExperiment", new SingleUnitTimeFormatter(referenceUnit, epsilon,6,false));

        model.connectToExperiment(exp);
        exp.setShowProgressBar(false);
        exp.stop(new TimeInstant(11, TimeUnit.MICROSECONDS));


        exp.tracePeriod(new TimeInstant(0), new TimeInstant(11,TimeUnit.MICROSECONDS));
        exp.debugPeriod(new TimeInstant(0), new TimeInstant(11,TimeUnit.MICROSECONDS));

        exp.start();
        exp.report();

        exp.finish();

    }

}
