package main.java.com.sim.network.mqtt;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import main.java.com.sim.network.NetworkModel;

import java.util.concurrent.TimeUnit;

public class MqttMessageGenerator extends ExternalEvent {
    private MqttAdapter mqttAdapter;
    private String destAddress;
    private double scheduleValue;

    public MqttMessageGenerator(Model owner, String name, boolean showInTrace, MqttAdapter mqttConverter, String destAddress, double scheduleValue) {
        super(owner, name, showInTrace);
        this.mqttAdapter = mqttConverter;
        this.destAddress = destAddress;
        this.scheduleValue = scheduleValue;
    }

    @Override
    public void eventRoutine() throws SuspendExecution {
        NetworkModel model = (NetworkModel) getModel();
        MqttMessage msg = new MqttMessage(model, "Mqtt Message", true);
        msg.setTcpDstAddress(destAddress);
        sendTraceNote("Mqtt Message Created ");

        MqttConverterEvent converter = new MqttConverterEvent(model, "Mqtt Converter Event", true, this.mqttAdapter);
        converter.schedule(msg, new TimeSpan(10, TimeUnit.MICROSECONDS));

        schedule(new TimeSpan(this.scheduleValue, TimeUnit.MILLISECONDS));
    }
}

