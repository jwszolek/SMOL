package main.java.com.sim.network.bacnet;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import main.java.com.sim.network.NetworkModel;

import java.util.concurrent.TimeUnit;

public class BacnetMessageGenerator extends ExternalEvent {

    private BacnetAdapter bacnetAdapter;
    private String destAddress;
    private double scheduleValue;

    public BacnetMessageGenerator(Model owner, String name, boolean showInTrace, BacnetAdapter bnConverter, String destAddress, double scheduleValue) {
        super(owner, name, showInTrace);
        this.bacnetAdapter = bnConverter;
        this.destAddress = destAddress;
        this.scheduleValue = scheduleValue;
    }

    @Override
    public void eventRoutine() {
        NetworkModel model = (NetworkModel) getModel();
        BacnetMessage msg = new BacnetMessage(model, "Bacnet Message", true);
        msg.setTcpDstAddress(destAddress);
        sendTraceNote("Bacnet Message Created ");


        BacnetConverterEvent converter = new BacnetConverterEvent(model, "Bacnet Converter Event", true, this.bacnetAdapter);
        converter.schedule(msg, new TimeSpan(10, TimeUnit.MICROSECONDS));

        schedule(new TimeSpan(this.scheduleValue, TimeUnit.MILLISECONDS));
    }
}
