package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttConverterEvent extends Event<MqttMessage> {
    private String clientId;
    private MqttAdapter mqttBroker;
    private String dstAddress;

    MqttConverterEvent(Model owner, String clientId, MqttAdapter mqttBroker, String dstAddress) {
        super(owner, "Mqtt Converter Event", true);
        this.clientId = clientId;
        this.mqttBroker = mqttBroker;

        this.dstAddress = dstAddress;
    }

    @Override
    public void eventRoutine(MqttMessage mqttMessage) {
        mqttMessage.setSrcAddress(mqttBroker.getEthAdapter().getAdapterAddress());
        mqttMessage.setDstAddress(dstAddress);
        mqttBroker.getInMqttAdapterQueue().insert(mqttMessage);

        log.error("Client -> broker | " + clientId + " | " + mqttBroker.getEthAdapter().getAdapterAddress() + ": " + mqttMessage.toString());
    }
}
