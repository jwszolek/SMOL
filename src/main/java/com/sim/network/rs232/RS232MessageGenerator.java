package main.java.com.sim.network.rs232;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import main.java.com.sim.network.NetworkModel;

import java.util.concurrent.TimeUnit;

public class RS232MessageGenerator extends ExternalEvent {

    private RS232Adapter rs232Adapter;
    private String destAddress;
    private double scheduleValue;


    public RS232MessageGenerator(Model owner, String name, boolean showInTrace, RS232Adapter rsConverter, String destAddress, double scheduleValue) {
        super(owner, name, showInTrace);
        this.rs232Adapter = rsConverter;
        this.rs232Adapter = rsConverter;
        this.destAddress = destAddress;
        this.scheduleValue = scheduleValue;
    }

    @Override
    public void eventRoutine() throws SuspendExecution {
        NetworkModel model = (NetworkModel)getModel();
        RS232Message msg = new RS232Message(model, "RS232 Message", true);
        msg.setTcpDstAddress(destAddress);
        sendTraceNote("RS232 Message Created ");


        RS232ConverterEvent converter = new RS232ConverterEvent(model, "RS232 Converter Event", true, this.rs232Adapter);
        converter.schedule(msg, new TimeSpan(10, TimeUnit.MICROSECONDS));


        schedule(new TimeSpan(this.scheduleValue, TimeUnit.MILLISECONDS));
    }
}
