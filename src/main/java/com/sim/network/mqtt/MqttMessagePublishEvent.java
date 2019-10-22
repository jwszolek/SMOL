package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.Model;
import lombok.extern.slf4j.Slf4j;
import main.java.com.sim.network.TCPMessage;

@Slf4j
public class MqttMessagePublishEvent extends MqttMessageEvent {
    private MqttAdapter mqttAdapter;
    private String srcAddress;
    private String dstAddress;
    private String logInfo;

    MqttMessagePublishEvent(Model owner, MqttAdapter mqttAdapter, String srcAddress, String dstAddress, String logInfo) {
        super(owner);

        this.mqttAdapter = mqttAdapter;
        this.srcAddress = srcAddress;
        this.dstAddress = dstAddress;
        this.logInfo = logInfo;
    }

    @Override
    public void eventRoutine(TCPMessage message) {
        TCPMessage tcpMessage = new MqttMessage(message.getModel(), message.getData());
        tcpMessage.setStartTransmission(presentTime());
        tcpMessage.setStopTransmission(null);

        tcpMessage.setSrcAddress(srcAddress);
        tcpMessage.setDstAddress(dstAddress);

        mqttAdapter.getInMqttAdapterQueue().insert(tcpMessage);

        log.error(logInfo + " | " + mqttAdapter.getEthAdapter().getAdapterAddress() + ": " + tcpMessage.toString());
    }
}
