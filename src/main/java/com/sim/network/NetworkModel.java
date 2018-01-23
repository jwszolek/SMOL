package main.java.com.sim.network;

import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;

import java.util.ArrayList;
import java.util.List;


public class NetworkModel extends Model{


    private ContDistUniform randCollisionValue;
    public int getAllColisionsCouter() {
        return allColisionsCouter;
    }

    public void setAllColisionsCouter(int allColisionsCouter) {
        this.allColisionsCouter = allColisionsCouter;
    }
    private int allColisionsCouter;


    protected Queue<EthFrame> ethLink;
    protected Queue<EthFrame> ethPendingBuffer;
    private List<EthAdapter> ethAdapterList;

    public List<EthAdapter> getEthAdapterList() {
        return ethAdapterList;
    }



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

        EthAdapter adapter_3 = new EthAdapter(this, "eth-adapter",true, "3");
        adapter_3.schedule(new TimeSpan(0));

        EthAdapter adapter_4 = new EthAdapter(this, "eth-adapter",true, "3");
        adapter_4.schedule(new TimeSpan(0));



        TCPMessageGenerator msgGenertor = new TCPMessageGenerator(this, "msg-generator",true, adapter_1, "2");
        msgGenertor.schedule(new TimeSpan(0));

        TCPMessageGenerator msgGenertor3 = new TCPMessageGenerator(this, "msg-generator",true, adapter_3, "2");
        msgGenertor3.schedule(new TimeSpan(0));

        TCPMessageGenerator msgGenertor4 = new TCPMessageGenerator(this, "msg-generator",true, adapter_4, "2");
        msgGenertor4.schedule(new TimeSpan(0));


        ethAdapterList.add(adapter_1);
        ethAdapterList.add(adapter_2);
        ethAdapterList.add(adapter_3);
        ethAdapterList.add(adapter_4);

        EthLinkRouter router = new EthLinkRouter(this, "ethlink-router",true, ethAdapterList);
        router.schedule(new TimeSpan(0));

        EthCollisionMonitor collisionMonitor = new EthCollisionMonitor(this, "collision-monitor",true, ethAdapterList);
        collisionMonitor.schedule(new TimeSpan(0));

    }

    @Override
    public void init() {
        ethLink = new Queue<EthFrame>(this,"ethLink",true,true);
        ethPendingBuffer = new Queue<EthFrame>(this,"ethLinkPending",true,true);

        randCollisionValue= new ContDistUniform(this, "RandCollisionValue", 0, 1, true, false);


    }


    public double getRandCollisionValue(){
        return  randCollisionValue.sample();
    }
}
