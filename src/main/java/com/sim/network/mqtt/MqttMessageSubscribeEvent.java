package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lombok.extern.slf4j.Slf4j;
import main.java.com.sim.network.TCPMessage;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MqttMessageSubscribeEvent extends MqttMessageEvent {
    private MqttClient mqttClient;

    MqttMessageSubscribeEvent(Model owner, MqttClient mqttClient) {
        super(owner);
        this.mqttClient = mqttClient;
    }

    @Override
    public void eventRoutine(TCPMessage message) {
        if (message instanceof MqttMessage) {
            MqttMessage mqttMessage = (MqttMessage) message;

            MqttAdapter mqttAdapter = mqttClient.getMqttAdapter();
            if (mqttAdapter.getSubscribers().isEmpty()) mqttClient.received(message);
            else {
                List<MqttAdapter> adapters = mqttAdapter.getSubscribers().get(mqttMessage.topic());
                if (adapters != null) adapters.forEach(adapter ->
                        mqttClient.publish(adapter, mqttAdapter.getEthAdapter().getAdapterAddress(), adapter.getEthAdapter().getAdapterAddress()
                                , "Broker -> client | " + mqttClient.getName())
                                .schedule(message, new TimeSpan(1, TimeUnit.MICROSECONDS))
                );
                else
                    log.error("Topic without subscribers: " + mqttMessage.topic());
            }
        }
    }
}
