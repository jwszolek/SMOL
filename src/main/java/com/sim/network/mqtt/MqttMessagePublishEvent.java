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
        if (message instanceof MqttMessage) {
            TCPMessage tcpMessage = new MqttMessage((MqttMessage) message);
            tcpMessage.setSrcAddress(srcAddress);
            tcpMessage.setDstAddress(dstAddress);
            tcpMessage.setStartTransmission(presentTime());
            tcpMessage.setStopTransmission(null);

            mqttAdapter.getInMqttAdapterQueue().insert(tcpMessage);
            log.error(logInfo + " | " + mqttAdapter.getEthAdapter().getAdapterAddress() + ": " + tcpMessage);
        }
    }
}
