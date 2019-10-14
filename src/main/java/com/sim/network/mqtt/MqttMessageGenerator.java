package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import main.java.com.sim.network.NetworkModel;

import java.util.concurrent.TimeUnit;

@Slf4j
public class MqttMessageGenerator extends ExternalEvent {
    @Getter
    private MqttAdapter mqttBroker;

    private double scheduleValue;
    private String[] pubTopics;

    public MqttMessageGenerator(Model owner, String name, MqttAdapter mqttBroker, double scheduleValue, String pubTopics) {
        super(owner, name, true);
        this.mqttBroker = mqttBroker;
        this.scheduleValue = scheduleValue;
        this.pubTopics = (pubTopics != null) ? pubTopics.split(",") : new String[0];
    }

    @Override
    public void eventRoutine() {
        if (pubTopics.length > 0) {
            NetworkModel model = (NetworkModel) getModel();
            int randValue = 1 + (int) (Math.random() * pubTopics.length);

            MqttMessage msg = new MqttMessage(model, pubTopics[randValue - 1]);
            sendTraceNote(msg.getName() + " Created");

            MqttConverterEvent converter = new MqttConverterEvent(model, mqttBroker);
            converter.schedule(msg, new TimeSpan(10, TimeUnit.MICROSECONDS));
        }

        schedule(new TimeSpan(scheduleValue, TimeUnit.MILLISECONDS));
    }
}

