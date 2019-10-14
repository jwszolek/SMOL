package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import main.java.com.sim.network.NetworkModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MqttMessageGenerator extends ExternalEvent {
    @Getter
    private MqttAdapter mqttBroker;

    private double scheduleValue;
    private List<String> topics;

    public MqttMessageGenerator(Model owner, String name, boolean showInTrace, MqttAdapter mqttBroker, double scheduleValue, List<String> topics) {
        super(owner, name, showInTrace);
        this.mqttBroker = mqttBroker;
        this.scheduleValue = scheduleValue;
        this.topics = topics;
    }

    @Override
    public void eventRoutine() {
        NetworkModel model = (NetworkModel) getModel();

        if (!topics.isEmpty()) {
            int randValue = 1 + (int) (Math.random() * topics.size());

            MqttMessage msg = new MqttMessage(model, topics.get(randValue));
            sendTraceNote(msg.getName() + " Created");

            MqttConverterEvent converter = new MqttConverterEvent(model, mqttBroker);
            converter.schedule(msg, new TimeSpan(10, TimeUnit.MICROSECONDS));
        }

        schedule(new TimeSpan(scheduleValue, TimeUnit.MILLISECONDS));
    }
}

