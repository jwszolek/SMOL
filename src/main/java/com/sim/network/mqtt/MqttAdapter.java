package main.java.com.sim.network.mqtt;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;
import main.java.com.sim.network.EthAdapter;
import main.java.com.sim.network.TCPMessage;

import java.util.concurrent.TimeUnit;

public class MqttAdapter extends ExternalEvent {
    public Queue<TCPMessage> inMqttAdapterQueue;
    public Queue<TCPMessage> outMqttAdapterQueue;
    private EthAdapter ethAdapter;

    public MqttAdapter(Model owner, String name, boolean showInTrace, EthAdapter ethAdapter) {
        super(owner, name, showInTrace);
        this.ethAdapter = ethAdapter;
        this.inMqttAdapterQueue = new Queue<>(owner,"in-mqtt-adapterQueue-",true,true);
        this.outMqttAdapterQueue = new Queue<>(owner,"out-mqtt-adapterQueue-",true,true);
    }

    @Override
    public void eventRoutine() throws SuspendExecution {

        if(!inMqttAdapterQueue.isEmpty()){
            TCPMessage msg = inMqttAdapterQueue.first();
            inMqttAdapterQueue.remove(msg);
            getEthAdapter().outMsgQueue.insert(msg);
        }

        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }

    public EthAdapter getEthAdapter() {
        return ethAdapter;
    }
}
