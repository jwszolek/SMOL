package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import main.java.com.sim.network.NetworkModel;
import main.java.com.sim.network.TCPMessage;

public class MqttConverterEvent extends Event<MqttMessage> {
    private MqttAdapter mqttAdapter;

    public MqttConverterEvent(Model owner, String name, boolean showInTrace, MqttAdapter mqttAdapter) {
        super(owner, name, showInTrace);
        this.mqttAdapter = mqttAdapter;
    }

    @Override
    public void eventRoutine(MqttMessage mqttMessage){
        NetworkModel model = (NetworkModel)getModel();

        TCPMessage tcpMessage = new TCPMessage(model, "Mqtt To TCP", true);
        tcpMessage.setSrcAddress(mqttAdapter.getEthAdapter().getAdapterAddress());
        tcpMessage.setDstAddress(mqttMessage.getDstAddr());

        Queue<TCPMessage> queue = mqttAdapter.getInMqttAdapterQueue();
        if(queue != null) {
            queue.insert(tcpMessage);
        }
    }
}
