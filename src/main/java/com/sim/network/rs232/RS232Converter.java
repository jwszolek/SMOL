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

public class RS232Converter extends ExternalEvent {

    public Queue<TCPMessage> inRS232ConveterQueue;
    private EthAdapter ethAdapter;

    public RS232Converter(Model owner, String name, boolean showInTrace, EthAdapter ethAdapter) {
        super(owner, name, showInTrace);
        this.ethAdapter = ethAdapter;
    }

    @Override
    public void eventRoutine() throws SuspendExecution {

        if(!inRS232ConveterQueue.isEmpty()){
            TCPMessage msg = inRS232ConveterQueue.first();
            ethAdapter.outMsgQueue.insert(msg);
        }

        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }
}
