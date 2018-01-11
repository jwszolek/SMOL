package main.java.com.sim.network;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.concurrent.TimeUnit;

public class TCPMessageGenerator extends ExternalEvent {

    public TCPMessageGenerator(Model owner, String name, boolean showInTrace){
        super(owner, name, showInTrace);
    }

    @Override
    public void eventRoutine() {
        NetworkModel model = (NetworkModel)getModel();

        TCPMessage msg = new TCPMessage(model,"TCP Message",true);
        model.msgQueue.insert(msg);

        TCPMessageQueueMonitor tcpmonitor = new TCPMessageQueueMonitor(model, "msg-converter",true);
        tcpmonitor.schedule(msg, new TimeSpan(0, TimeUnit.MICROSECONDS));


        schedule(new TimeSpan(2, TimeUnit.MICROSECONDS));

    }
}
