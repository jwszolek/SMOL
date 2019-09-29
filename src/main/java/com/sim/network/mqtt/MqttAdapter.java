package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;
import main.java.com.sim.network.EthAdapter;
import main.java.com.sim.network.TCPMessage;

import java.util.concurrent.TimeUnit;

public class MqttAdapter extends ExternalEvent {
    private final Queue<TCPMessage> inMqttAdapterQueue;
    private final Queue<TCPMessage> outMqttAdapterQueue;
    private final EthAdapter ethAdapter;

    public MqttAdapter(Model owner, String name, boolean showInTrace, EthAdapter ethAdapter) {
        super(owner, name, showInTrace);
        this.ethAdapter = ethAdapter;
        this.inMqttAdapterQueue = new Queue<>(owner, "in-mqtt-adapterQueue-", true, true);
        this.outMqttAdapterQueue = new Queue<>(owner, "out-mqtt-adapterQueue-", true, true);
    }

    @Override
    public void eventRoutine() {
        if (!inMqttAdapterQueue.isEmpty()) {
            TCPMessage msg = inMqttAdapterQueue.first();
            inMqttAdapterQueue.remove(msg);
            getEthAdapter().getOutMsgQueue().insert(msg);
        }
        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }

    public EthAdapter getEthAdapter() {
        return ethAdapter;
    }

    public Queue<TCPMessage> getInMqttAdapterQueue() {
        return inMqttAdapterQueue;
    }

    public Queue<TCPMessage> getOutMqttAdapterQueue() {
        return outMqttAdapterQueue;
    }
}
