package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import lombok.Getter;

class MqttMessage extends Entity {
    @Getter
    private final String dstTopic;

    MqttMessage(Model owner, String name, boolean showInTrace, String dstTopic) {
        super(owner, name, showInTrace);
        this.dstTopic = dstTopic;
    }
}

