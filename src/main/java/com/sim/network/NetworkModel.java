package main.java.com.sim.network;

import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;

import java.util.ArrayList;
import java.util.List;


public class NetworkModel extends Model{


    private ContDistUniform tcpMessageGeneratorTime;

    protected Queue<EthFrame> ethLink;
    private List<EthAdapter> ethAdapterList;

    public NetworkModel(Model owner, String modelName, boolean showInReport, boolean showInTrace) {
        super(owner, modelName, showInReport, showInTrace);
        this.ethAdapterList = new ArrayList<EthAdapter>();
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public void doInitialSchedules() {

        EthAdapter adapter_1 = new EthAdapter(this, "eth-adapter",true, "1");
        adapter_1.schedule(new TimeSpan(0));

        EthAdapter adapter_2 = new EthAdapter(this, "eth-adapter",true, "2");
        adapter_2.schedule(new TimeSpan(0));

        TCPMessageGenerator msgGenertor = new TCPMessageGenerator(this, "msg-generator",true, adapter_1, "2");
        msgGenertor.schedule(new TimeSpan(0));

        ethAdapterList.add(adapter_1);
        ethAdapterList.add(adapter_2);

        EthLinkRouter router = new EthLinkRouter(this, "ethlink-router",true, ethAdapterList);
        router.schedule(new TimeSpan(0));

    }

    @Override
    public void init() {
        ethLink = new Queue<EthFrame>(this,"ethLink",true,true);

        tcpMessageGeneratorTime= new ContDistUniform(this, "ServiceTimeStream", 5.0, 5.0, true, false);

    }
}
