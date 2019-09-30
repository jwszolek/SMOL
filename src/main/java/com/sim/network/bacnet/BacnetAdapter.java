package main.java.com.sim.network.bacnet;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;
import lombok.Getter;
import main.java.com.sim.network.EthAdapter;
import main.java.com.sim.network.TCPMessage;

import java.util.concurrent.TimeUnit;

//TODO: adapters base class should be added
@Getter
public class BacnetAdapter extends ExternalEvent {
    private Queue<TCPMessage> inBacnetAdapterQueue;
    private EthAdapter ethAdapter;

    public BacnetAdapter(Model owner, String name, boolean showInTrace, EthAdapter ethAdapter) {
        super(owner, name, showInTrace);
        this.ethAdapter = ethAdapter;
        this.inBacnetAdapterQueue = new Queue<>(owner, "in-bacnet-adapterQueue-", true, true);
    }

    @Override
    public void eventRoutine() {
        if (!inBacnetAdapterQueue.isEmpty()) {
            TCPMessage msg = inBacnetAdapterQueue.first();
            inBacnetAdapterQueue.remove(msg);
            ethAdapter.getOutMsgQueue().insert(msg);
        }

        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }
}
