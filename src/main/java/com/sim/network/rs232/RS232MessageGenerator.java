package main.java.com.sim.network.rs232;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import main.java.com.sim.network.NetworkModel;

import java.util.concurrent.TimeUnit;

public class RS232MessageGenerator extends ExternalEvent {

    private RS232Converter rs232Converter;

    public RS232MessageGenerator(Model owner, String name, boolean showInTrace, RS232Converter rsConverter) {
        super(owner, name, showInTrace);
        this.rs232Converter = rsConverter;
    }

    @Override
    public void eventRoutine() throws SuspendExecution {
        NetworkModel model = (NetworkModel)getModel();
        RS232Message msg = new RS232Message(model, "RS232 Message", true);

        RS232ConverterEvent converter = new RS232ConverterEvent(model, "RS232 Converter Event", true, this.rs232Converter);
        converter.schedule(msg, new TimeSpan(10, TimeUnit.MICROSECONDS));

        schedule(new TimeSpan(20, TimeUnit.MILLISECONDS));

    }
}
