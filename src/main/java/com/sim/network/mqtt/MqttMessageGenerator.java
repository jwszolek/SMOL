package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import main.java.com.sim.network.NetworkModel;

import java.util.concurrent.TimeUnit;

public class MqttMessageGenerator extends ExternalEvent {
    private MqttAdapter mqttAdapter;
    private String pubTopic;
    private double scheduleValue;

    public MqttMessageGenerator(Model owner, String name, boolean showInTrace, MqttAdapter mqttAdapter, String pubTopic, double scheduleValue) {
        super(owner, name, showInTrace);
        this.mqttAdapter = mqttAdapter;
        this.pubTopic = pubTopic;
        this.scheduleValue = scheduleValue;
    }

    @Override
    public void eventRoutine() {
        NetworkModel model = (NetworkModel) getModel();

        MqttMessage msg = new MqttMessage(model, "Mqtt Message", true, pubTopic);
        sendTraceNote("Mqtt Message Created ");

        MqttConverterEvent converter = new MqttConverterEvent(model, "Mqtt Converter Event", true, this.mqttAdapter);
        converter.schedule(msg, new TimeSpan(10, TimeUnit.MICROSECONDS));

        schedule(new TimeSpan(this.scheduleValue, TimeUnit.MILLISECONDS));
    }
}

