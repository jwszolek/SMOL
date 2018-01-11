package main.java.com.sim.network;

import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;


public class NetworkModel extends Model{


    private ContDistUniform tcpMessageGeneratorTime;

    public Queue<EthFrame> getEthQueue() {
        return ethQueue;
    }

    protected Queue<TCPMessage> msgQueue;
    protected Queue<EthFrame> ethQueue;
    protected Queue<EthFrame> ethLink;

    public NetworkModel(Model owner, String modelName, boolean showInReport, boolean showInTrace) {
        super(owner, modelName, showInReport, showInTrace);
    }


    @Override
    public String description() {
        return null;
    }

    @Override
    public void doInitialSchedules() {

        TCPMessageGenerator msgGenertor = new TCPMessageGenerator(this, "msg-generator",true);
        msgGenertor.schedule(new TimeSpan(0));

        EthAdapter monitor = new EthAdapter(this, "eth-adapter",true);
        monitor.schedule(new TimeSpan(0));



    }

    @Override
    public void init() {
        msgQueue = new Queue<TCPMessage>(this,"msgQueue",true,true);
        ethQueue = new Queue<EthFrame>(this,"ethQueue",true,true);
        ethLink = new Queue<EthFrame>(this,"ethLink",true,true);

        tcpMessageGeneratorTime= new ContDistUniform(this, "ServiceTimeStream", 5.0, 5.0, true, false);



    }
}
