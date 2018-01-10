package main.java.com.sim.network;

import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;

import java.util.concurrent.TimeUnit;


public class NetworkModel extends Model{


    private ContDistUniform tcpMessageGeneratorTime;

    protected Queue<TCPMessage> ethQueue;
    protected Queue<TCPMessage> ethLink;

    public NetworkModel(Model owner, String modelName, boolean showInReport, boolean showInTrace) {
        super(owner, modelName, showInReport, showInTrace);
    }


    @Override
    public String description() {
        return null;
    }

    @Override
    public void doInitialSchedules() {
        EthAdapter adapter = new EthAdapter(this, "ethadap",true);
        adapter.schedule(new TimeSpan(0));

    }

    @Override
    public void init() {
        ethQueue = new Queue<TCPMessage>(this,"ethQueue",true,true);
        tcpMessageGeneratorTime= new ContDistUniform(this, "ServiceTimeStream", 5.0, 5.0, true, false);


    }
}
