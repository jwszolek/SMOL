package main.java.com.sim.network;


import desmoj.core.report.ASCIIDebugOutput;
import desmoj.core.report.Message;
import desmoj.core.report.MessageReceiver;
import desmoj.core.report.Reporter;
import desmoj.core.simulator.*;
import desmoj.extensions.xml.report.XMLTraceOutput;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Runner {

    private static TimeUnit referenceUnit = TimeUnit.SECONDS;
    private static TimeUnit epsilon = TimeUnit.MICROSECONDS;

    public static void main(String[] args) throws IOException {

        NetworkModel model = new NetworkModel(null,"SMOL Simulation Engine",true,false);
        Experiment exp = new Experiment("SMOLSimulationExperiment", new SingleUnitTimeFormatter(referenceUnit, epsilon,6,false));

        model.connectToExperiment(exp);
        exp.setShowProgressBar(false);
        exp.stop(new TimeInstant(1000, TimeUnit.MILLISECONDS));


        exp.tracePeriod(new TimeInstant(0), new TimeInstant(1000,TimeUnit.MILLISECONDS));
        exp.debugPeriod(new TimeInstant(0), new TimeInstant(1000,TimeUnit.MILLISECONDS));

        LogsWritrer lw = new LogsWritrer();
//
        exp.register(lw);
//        exp.traceIsOn();
        exp.addTraceReceiver(lw);
//        exp.addTraceReceiver(new MessageReceiver() {
//            @Override
//            public void receive(Message message) {
//                System.out.println(message.getDescription());
//                System.out.println(message.getTime());
//                System.out.println(message.getSendTime());
//            }
//
//            @Override
//            public void receive(Reporter reporter) {
//                //System.out.println(reporter.);
//            }
//        });


        exp.setShowProgressBarAutoclose(true);
        exp.setShowProgressBar(true);
        exp.start();



        exp.report();

        //lw.closeFile();
        exp.finish();
        //lw.closeFile();
        //exp.register()
        //exp.deRegister(lw);

    }
}
