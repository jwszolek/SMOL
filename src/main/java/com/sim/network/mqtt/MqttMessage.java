package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.Model;
import lombok.extern.slf4j.Slf4j;
import main.java.com.sim.network.TCPMessage;

import java.util.Objects;
import java.util.UUID;

@Slf4j
class MqttMessage extends TCPMessage {
    private static final String UUID_SEPARATOR = ":";

    MqttMessage(Model owner, String topic, UUID uuid) {
        super(owner, "Mqtt Message", true);
        setTopicAndUUID(topic, uuid.toString());
    }

    MqttMessage(MqttMessage message) {
        this(message.getModel(), message.topic(), UUID.fromString(Objects.requireNonNull(message.uuid())));
    }

    private void setTopicAndUUID(String topic, String uuid) {
        setData(topic + UUID_SEPARATOR + uuid);
    }

    String topic() {
        String topic = getData();
        if (topic.contains(UUID_SEPARATOR))
            return topic.substring(0, topic.indexOf(UUID_SEPARATOR));

        return null;
    }

    private String uuid() {
        String name = getData();
        if (name.contains(UUID_SEPARATOR))
            return name.substring(name.indexOf(UUID_SEPARATOR) + 1);

        return null;
    }
}

