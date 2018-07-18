package main.java.com.sim.network.mqtt;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import main.java.com.sim.network.NetworkModel;
import main.java.com.sim.network.TCPMessage;

public class MqttConverterEvent extends Event<MqttMessage> {
    private MqttMessage receivedMsg;
    private MqttAdapter mqttAdapter;

    public MqttConverterEvent(Model owner, String name, boolean showInTrace, MqttAdapter mqttAdapter) {
        super(owner, name, showInTrace);
        this.mqttAdapter = mqttAdapter;
    }

    @Override
    public void eventRoutine(MqttMessage mqttMessage) throws SuspendExecution {
        NetworkModel model = (NetworkModel)getModel();
        this.receivedMsg = mqttMessage;

        TCPMessage tcpMessage = new TCPMessage(model, "Mqtt To TCP", true);
        tcpMessage.setDstAddress(mqttMessage.getTcpDstAddress());
        tcpMessage.setDstAddress(this.mqttAdapter.getEthAdapter().getAdapterAddress());
        if(this.mqttAdapter.inMqttAdapterQueue != null) {
            this.mqttAdapter.inMqttAdapterQueue.insert(tcpMessage);
        }
    }
}
