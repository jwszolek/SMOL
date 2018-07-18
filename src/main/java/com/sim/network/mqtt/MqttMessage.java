package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class MqttMessage extends Entity {
    private String tcpDstAddress;

    public MqttMessage(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
    }

    public String getTcpDstAddress() {
        return tcpDstAddress;
    }

    public void setTcpDstAddress(String tcpDstAddress) {
        this.tcpDstAddress = tcpDstAddress;
    }
}

