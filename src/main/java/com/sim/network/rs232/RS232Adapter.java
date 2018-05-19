package main.java.com.sim.network.rs232;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;
import main.java.com.sim.network.EthAdapter;
import main.java.com.sim.network.EthFrame;
import main.java.com.sim.network.TCPMessage;

import java.util.concurrent.TimeUnit;

public class RS232Adapter extends ExternalEvent {

    public Queue<TCPMessage> inRS232AdapterQueue;
    public Queue<TCPMessage> outRS232AdapterQueue;
    private EthAdapter ethAdapter;

    public RS232Adapter(Model owner, String name, boolean showInTrace, EthAdapter ethAdapter) {
        super(owner, name, showInTrace);
        this.ethAdapter = ethAdapter;
        this.inRS232AdapterQueue = new Queue<>(owner,"in-rs232-adapterQueue-",true,true);
        this.outRS232AdapterQueue = new Queue<>(owner,"out-rs232-adapterQueue-",true,true);
    }

    @Override
    public void eventRoutine() throws SuspendExecution {

        if(!inRS232AdapterQueue.isEmpty()){
            TCPMessage msg = inRS232AdapterQueue.first();
            inRS232AdapterQueue.remove(msg);
            ethAdapter.outMsgQueue.insert(msg);
        }

        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }
}
