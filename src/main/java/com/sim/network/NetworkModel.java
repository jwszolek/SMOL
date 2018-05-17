package main.java.com.sim.network;

import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;
import main.java.com.sim.network.rs232.RS232Adapter;
import main.java.com.sim.network.rs232.RS232MessageGenerator;

import java.util.ArrayList;
import java.util.List;


public class NetworkModel extends Model{


    private ContDistUniform randCollisionValue;

    public double getRandGeneratorValue() {
        return randGeneratorValue.sample();
    }

    private ContDistUniform randGeneratorValue;

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

        EthAdapter adapter_1 = new EthAdapter(this, "eth-adapter",false, "1");
        adapter_1.schedule(new TimeSpan(0));

        EthAdapter adapter_2 = new EthAdapter(this, "eth-adapter",true, "2");
        adapter_2.schedule(new TimeSpan(0));

//        EthAdapter adapter_3 = new EthAdapter(this, "eth-adapter",false, "3");
//        adapter_3.schedule(new TimeSpan(0));
////
//        EthAdapter adapter_4 = new EthAdapter(this, "eth-adapter",false, "4");
//        adapter_4.schedule(new TimeSpan(0));
//
//        EthAdapter adapter_5 = new EthAdapter(this, "eth-adapter",true, "4");
//        adapter_5.schedule(new TimeSpan(0));


//        RS232Adapter rs232Conv1 = new RS232Adapter(this, "rs232-adapter",false, adapter_1);
//        rs232Conv1.schedule(new TimeSpan(0));


        TCPMessageGenerator msgGenertor = new TCPMessageGenerator(this, "msg-generator",false, adapter_1, "2", 10);
        msgGenertor.schedule(new TimeSpan(0));

//        TCPMessageGenerator msgGenertor3 = new TCPMessageGenerator(this, "msg-generator",false, adapter_3, "2", 50);
//        msgGenertor3.schedule(new TimeSpan(0));
//
//        TCPMessageGenerator msgGenertor4 = new TCPMessageGenerator(this, "msg-generator",false, adapter_4, "2", 50);
//        msgGenertor4.schedule(new TimeSpan(0));


//        RS232MessageGenerator rs232MessageGenerator = new RS232MessageGenerator(this, "rs232-msg-generator", true, rs232Conv1);
//        rs232MessageGenerator.schedule(new TimeSpan(0));


//        TCPMessageGenerator msgGenertor5 = new TCPMessageGenerator(this, "msg-generator",true, adapter_5, "2");
//        msgGenertor5.schedule(new TimeSpan(0));



        ethAdapterList.add(adapter_1);
        ethAdapterList.add(adapter_2);
//        ethAdapterList.add(adapter_3);
//        ethAdapterList.add(adapter_4);
//        ethAdapterList.add(adapter_5);

        EthLinkRouter router = new EthLinkRouter(this, "ethlink-router",false, ethAdapterList);
        router.schedule(new TimeSpan(0));

        EthCollisionMonitor collisionMonitor = new EthCollisionMonitor(this, "collision-monitor",false, ethAdapterList);
        collisionMonitor.schedule(new TimeSpan(0));

    }

    @Override
    public void init() {
        ethLink = new Queue<EthFrame>(this,"ethLink",true,true);
        ethPendingBuffer = new Queue<EthFrame>(this,"ethLinkPending",true,true);

        randCollisionValue= new ContDistUniform(this, "RandCollisionValue", 0, 1, true, false);
        randGeneratorValue= new ContDistUniform(this, "TCPMessageGeneratorValue", 800, 1100, true, false);


    }


    public double getRandCollisionValue(){
        return  randCollisionValue.sample();
    }
}
