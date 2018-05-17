package main.groovy.dsl.engine

import desmoj.core.simulator.Experiment
import desmoj.core.simulator.SingleUnitTimeFormatter
import desmoj.core.simulator.TimeInstant
import main.java.com.sim.network.LogsWritrer
import main.java.com.sim.network.NetworkModel

import java.util.concurrent.TimeUnit

class SimDriver {

    private static TimeUnit referenceUnit = TimeUnit.SECONDS;
    private static TimeUnit epsilon = TimeUnit.MICROSECONDS;


    static void run(String properties) {

        println "Starting simulation module"
        println properties

        NetworkModel model = new NetworkModel(null,"SMOL Simulation Engine",true,true);
        Experiment exp = new Experiment("SMOLSimulationExperiment", new SingleUnitTimeFormatter(referenceUnit, epsilon,6,false));

        model.connectToExperiment(exp)
        exp.stop(new TimeInstant(11, TimeUnit.MICROSECONDS))


        exp.tracePeriod(new TimeInstant(0), new TimeInstant(11,TimeUnit.MICROSECONDS))
        exp.debugPeriod(new TimeInstant(0), new TimeInstant(11,TimeUnit.MICROSECONDS))


        LogsWritrer lw = new LogsWritrer()
        exp.register(lw)
        exp.addTraceReceiver(lw)

        exp.setShowProgressBarAutoclose(true)
        exp.setShowProgressBar(true)


        exp.start()
        exp.report()

        exp.finish()
    }
}
