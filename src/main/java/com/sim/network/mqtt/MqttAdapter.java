package main.java.com.sim.network.mqtt;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;
import lombok.Getter;
import main.java.com.sim.network.EthAdapter;
import main.java.com.sim.network.TCPMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MqttAdapter extends ExternalEvent {
    @Getter
    private final Queue<TCPMessage> inMqttAdapterQueue;

    @Getter
    private final Queue<TCPMessage> outMqttAdapterQueue;

    @Getter
    private final EthAdapter ethAdapter;

    @Getter
    private final Map<String, MqttMessageGenerator> subscribers;

    public MqttAdapter(Model owner, String name, boolean showInTrace, EthAdapter ethAdapter) {
        super(owner, name, showInTrace);
        this.ethAdapter = ethAdapter;
        this.inMqttAdapterQueue = new Queue<>(owner, "in-mqtt-adapterQueue-", true, true);
        this.outMqttAdapterQueue = new Queue<>(owner, "out-mqtt-adapterQueue-", true, true);

        subscribers = new HashMap<>();
    }

    @Override
    public void eventRoutine() {
        if (!inMqttAdapterQueue.isEmpty()) {
            TCPMessage msg = inMqttAdapterQueue.first();
            inMqttAdapterQueue.remove(msg);
            ethAdapter.outMsgQueue.insert(msg);
        }

        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }
}
