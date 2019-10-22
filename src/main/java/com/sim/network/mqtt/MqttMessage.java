package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.Model;
import main.java.com.sim.network.TCPMessage;

class MqttMessage extends TCPMessage {
    MqttMessage(Model owner, String topic) {
        super(owner, "Mqtt Message", true);
        setTopic(topic);
    }

    private void setTopic(String topic) {
        setData(topic);
    }
}

