package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import main.java.com.sim.network.EthAdapter;
import main.java.com.sim.network.TCPMessage;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MqttAdapter extends ExternalEvent {
    @Getter
    private final Queue<TCPMessage> inMqttAdapterQueue;

    @Getter
    private final Queue<TCPMessage> outMqttAdapterQueue;

    @Getter
    private final EthAdapter ethAdapter;

    @Getter
    private final Map<String, List<MqttMessageGenerator>> subscribers;

    @Getter
    private final String[] bridges;

    public MqttAdapter(Model owner, String name, boolean showInTrace, EthAdapter ethAdapter, String bridges) {
        super(owner, name, showInTrace);

        this.inMqttAdapterQueue = new Queue<>(owner, "in-mqtt-adapterQueue-", true, true);
        this.outMqttAdapterQueue = new Queue<>(owner, "out-mqtt-adapterQueue-", true, true);

        this.ethAdapter = ethAdapter;
        this.subscribers = new HashMap<>();

        this.bridges = (bridges != null) ? bridges.split(",") : new String[0];
    }

    public void subscribe(MqttMessageGenerator mqttClient, String subTopics) {
        String[] topics = (subTopics != null) ? subTopics.split(",") : new String[0];
        if (topics.length > 0) {
            Arrays.stream(topics).forEach(topic -> {
                if (subscribers.containsKey(topic)) {
                    subscribers.get(topic).add(mqttClient);
                } else {
                    subscribers.put(topic, Collections.singletonList(mqttClient));
                }
            });
        }
    }

    @Override
    public void eventRoutine() {
        if (!ethAdapter.getInMsgQueue().isEmpty()) {
            TCPMessage message = ethAdapter.getInMsgQueue().first();

            ethAdapter.getInMsgQueue().remove(message);
            log.error("Received (" + getName() + " | " + ethAdapter.getAdapterAddress() + "): " + message);
        }


        if (!outMqttAdapterQueue.isEmpty()) {
            log.error("MqttAdapter: outMqttAdapterQueue not empty !!!");
        }

        if (!inMqttAdapterQueue.isEmpty()) {
            TCPMessage mqttMessage = inMqttAdapterQueue.first();
            inMqttAdapterQueue.remove(mqttMessage);

            log.error("Sent (" + getName() + " | " + ethAdapter.getAdapterAddress() + "): " + mqttMessage.toString());
            ethAdapter.getOutMsgQueue().insert(mqttMessage);
        }

        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }

    Map<String, List<MqttMessageGenerator>> getSubscribers() {
        return subscribers;
    }

    List<MqttMessageGenerator> getSubscribers(String topic) {
        return this.subscribers.entrySet().stream()
                .filter(entry -> entry.getKey().equals(topic)).findAny()
                .map(Map.Entry::getValue).orElse(new ArrayList<>());
    }
}
