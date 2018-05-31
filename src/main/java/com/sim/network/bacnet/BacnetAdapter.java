package main.java.com.sim.network.bacnet;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;
import main.java.com.sim.network.EthAdapter;
import main.java.com.sim.network.TCPMessage;

import java.util.concurrent.TimeUnit;


//TODO: adapters base class should be added
public class BacnetAdapter extends ExternalEvent {

    public Queue<TCPMessage> inBacnetAdapterQueue;
    public Queue<TCPMessage> outBacnetAdapterQueue;
    private EthAdapter ethAdapter;

    public BacnetAdapter(Model owner, String name, boolean showInTrace, EthAdapter ethAdapter) {
        super(owner, name, showInTrace);
        this.ethAdapter = ethAdapter;
        this.inBacnetAdapterQueue = new Queue<>(owner,"in-bacnet-adapterQueue-",true,true);
        this.outBacnetAdapterQueue = new Queue<>(owner,"out-bacnet-adapterQueue-",true,true);
    }

    @Override
    public void eventRoutine() throws SuspendExecution {

        if(!inBacnetAdapterQueue.isEmpty()){
            TCPMessage msg = inBacnetAdapterQueue.first();
            inBacnetAdapterQueue.remove(msg);
            getEthAdapter().outMsgQueue.insert(msg);
        }

        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }

    public EthAdapter getEthAdapter() {
        return ethAdapter;
    }
}
