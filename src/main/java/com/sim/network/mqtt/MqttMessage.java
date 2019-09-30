package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class MqttMessage extends Entity {
    private final String dstAddr;

    public MqttMessage(Model owner, String name, boolean showInTrace, String dstAddr) {
        super(owner, name, showInTrace);
        this.dstAddr = dstAddr;
    }

    public String getDstAddr() {
        return dstAddr;
    }
}

