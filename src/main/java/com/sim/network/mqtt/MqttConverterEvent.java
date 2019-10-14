package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttConverterEvent extends Event<MqttMessage> {
    private MqttAdapter mqttBroker;

    MqttConverterEvent(Model owner, MqttAdapter mqttBroker) {
        super(owner, "Mqtt Converter Event", true);
        this.mqttBroker = mqttBroker;
    }

    @Override
    public void eventRoutine(MqttMessage mqttMessage) {
        log.error(mqttBroker.getSubscribers().toString());
//        log.error("Created: (" + getName() + "/" + mqttBroker.getEthAdapter().getAdapterAddress() + "): " + mqttMessage.toString());

        mqttBroker.getSubscribers(mqttMessage.getTopic()).forEach(mqttClient ->
        {
            mqttMessage.setSrcAddress(mqttBroker.getEthAdapter().getAdapterAddress());
            mqttMessage.setDstAddress(mqttClient.getMqttBroker().getEthAdapter().getAdapterAddress());

            log.error("Created: (" + getName() + "/" + mqttBroker.getEthAdapter().getAdapterAddress() + "): " + mqttMessage.toString());

            mqttBroker.getInMqttAdapterQueue().insert(mqttMessage);
        });
    }
}
