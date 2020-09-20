package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import main.java.com.sim.network.TCPMessage;

abstract class MqttMessageEvent extends Event<TCPMessage> {
    MqttMessageEvent(Model owner) {
        super(owner, "Mqtt Converter Event", true);
    }
}
