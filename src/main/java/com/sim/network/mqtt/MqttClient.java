package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import main.java.com.sim.network.TCPMessage;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MqttClient extends ExternalEvent {
    @Getter
    private MqttAdapter mqttAdapter;

    private double scheduleValue;
    private String dstAddress;
    private String[] pubTopics;
    private double lastPublishRoutine;

    public MqttClient(Model owner, String name, MqttAdapter mqttAdapter, double scheduleValue, String dstAddress, String pubTopics) {
        super(owner, name, true);
        this.mqttAdapter = mqttAdapter;
        this.scheduleValue = scheduleValue;
        this.dstAddress = dstAddress;
        this.pubTopics = (pubTopics != null) ? pubTopics.split(",") : new String[0];

        lastPublishRoutine = presentTimeAsDouble();
    }

    @Override
    public void eventRoutine() {
        incoming();
        publishRoutine();

        schedule(new TimeSpan(1, TimeUnit.MILLISECONDS));
    }

    private void incoming() {
        while (!mqttAdapter.getOutMqttAdapterQueue().isEmpty()) {
            TCPMessage message = mqttAdapter.getOutMqttAdapterQueue().first();
            mqttAdapter.getOutMqttAdapterQueue().remove(message);

            MqttMessageSubscribeEvent subscribeEvent = new MqttMessageSubscribeEvent(getModel(), this);
            subscribeEvent.schedule(message, new TimeSpan(1, TimeUnit.MICROSECONDS));
        }
    }

    private void publishRoutine() {
        double presentTime = presentTime().getTimeAsDouble(TimeUnit.MILLISECONDS);
        if (pubTopics.length > 0 && ((presentTime - scheduleValue) > lastPublishRoutine)) {
            lastPublishRoutine = presentTimeAsDouble();

            MqttMessage message = new MqttMessage(getModel(), randomTopic(), UUID.randomUUID());
            sendTraceNote(message.getName() + " Created");

            publish("Client -> broker | " + getName())
                    .schedule(message, new TimeSpan(10, TimeUnit.MICROSECONDS));
        }
    }

    private double presentTimeAsDouble() {
        return presentTime().getTimeAsDouble(TimeUnit.MILLISECONDS);
    }

    private String randomTopic() {
        int randValue = 1 + (int) (Math.random() * pubTopics.length);
        return pubTopics[randValue - 1];
    }

    private MqttMessagePublishEvent publish(String logInfo) {
        return publish(mqttAdapter, mqttAdapter.getEthAdapter().getAdapterAddress(), dstAddress, logInfo);
    }

    MqttMessagePublishEvent publish(MqttAdapter adapter, String srcAddress, String dstAddress, String logInfo) {
        return new MqttMessagePublishEvent(getModel(), adapter, srcAddress, dstAddress, logInfo);
    }

    void received(TCPMessage message) {
        log.error("Received | " + getName() + " | " + mqttAdapter.getEthAdapter().getAdapterAddress() + ": " + message);
    }
}

