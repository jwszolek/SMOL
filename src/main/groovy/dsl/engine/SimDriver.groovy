package main.groovy.dsl.engine

import desmoj.core.simulator.Experiment
import desmoj.core.simulator.SingleUnitTimeFormatter
import desmoj.core.simulator.TimeInstant
import main.groovy.smoh.HBase
import main.java.com.sim.network.LogsWritrer
import main.java.com.sim.network.NetworkModel

import java.util.concurrent.TimeUnit

class SimDriver {

    private static TimeUnit referenceUnit = TimeUnit.SECONDS;
    private static TimeUnit epsilon = TimeUnit.MICROSECONDS;


    static void run(def properties) {

        println "Starting simulation module"
        println properties

        def varList = new GraphProducer().formatInput(properties)

        NetworkModel model = new NetworkModel(null,"SMOL Simulation Engine",true,true, varList)
        Experiment exp = new Experiment("SMOLSimulationExperiment", new SingleUnitTimeFormatter(referenceUnit, epsilon,6,false))

        def simObject = (Map<String,String>)new GraphProducer().parseSimObject(properties)
        def simTime = Double.parseDouble(simObject.getOrDefault("simTime",""))

        model.connectToExperiment(exp)

        if(simTime != 0) {
            exp.stop(new TimeInstant(simTime, TimeUnit.MILLISECONDS));
            exp.tracePeriod(new TimeInstant(0), new TimeInstant(simTime, TimeUnit.MILLISECONDS));
            exp.debugPeriod(new TimeInstant(0), new TimeInstant(simTime, TimeUnit.MILLISECONDS));
        }else{
            exp.stop(new TimeInstant(1000, TimeUnit.MILLISECONDS));
            exp.tracePeriod(new TimeInstant(0), new TimeInstant(1000, TimeUnit.MILLISECONDS));
            exp.debugPeriod(new TimeInstant(0), new TimeInstant(1000, TimeUnit.MILLISECONDS));
        }

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
