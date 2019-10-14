package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.Model;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import main.java.com.sim.network.TCPMessage;

@Getter
@Slf4j
class MqttMessage extends TCPMessage {
    private final String topic;

    MqttMessage(
            Model owner,
            String topic
    ) {
        super(owner, "Mqtt Message", true);

        this.topic = topic;
    }
}

