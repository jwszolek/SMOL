package main.java.com.sim.network;

import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;
import main.groovy.smoh.ExpNode;
import main.groovy.smoh.HBase;
import main.groovy.smoh.SANode;
import main.groovy.smoh.TNode;
import main.java.com.sim.network.rs232.RS232Adapter;
import main.java.com.sim.network.rs232.RS232MessageGenerator;

import java.util.*;
import java.util.stream.Collectors;


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
    private HashMap<String, HBase> modelingObjects;


    public NetworkModel(Model owner, String modelName, boolean showInReport, boolean showInTrace) {
        super(owner, modelName, showInReport, showInTrace);
        this.ethAdapterList = new ArrayList<EthAdapter>();
    }


    public NetworkModel(Model owner, String modelName, boolean showInReport, boolean showInTrace, HashMap<String, HBase> modelingObjects) {
        super(owner, modelName, showInReport, showInTrace);
        this.ethAdapterList = new ArrayList<EthAdapter>();
        this.modelingObjects = modelingObjects;
    }

    @Override
    public String description() {
        return null;
    }


     private <T> Map<String, Map<T,String>> generateMap(HashMap<String, HBase> modelingObjects, Class<T> cls) {

        Map<String, Map<T, String>> outputMap = new HashMap<>();

        if(this.modelingObjects != null){
            Iterator it = modelingObjects.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();

                if(cls.isInstance(pair.getValue())){
                    Map<T, String> innerMap = new HashMap<>();
                    innerMap.put((T)pair.getValue(), ((HBase)pair.getValue()).getConnected());
                    outputMap.put(pair.getKey().toString(), innerMap);
                }
            }
        }
        return outputMap;
    }


    @Override
    public void doInitialSchedules() {


        Map<String, Map<TNode,String>> adapters = generateMap(this.modelingObjects, TNode.class);
        Map<String, Map<SANode,String>> sensors = generateMap(this.modelingObjects, SANode.class);
        Map<String, Map<ExpNode,String>> converters = generateMap(this.modelingObjects, ExpNode.class);

        Map<String, EthAdapter> adaptesLst = new HashMap<>();
        Map<String, RS232Adapter> convertersLst = new HashMap<>();

        if(adapters != null){
            Iterator it = adapters.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                String name = pair.getKey().toString();

                TNode adapter = ((Map<TNode,String>)pair.getValue()).keySet().stream().findFirst().get();
                EthAdapter ethAdapter = new EthAdapter(this, name, true, adapter.ip);
                ethAdapter.schedule(new TimeSpan(0));
                sendTraceNote("EthAdapter has been created " + ethAdapter.getName());

                adaptesLst.put(name, ethAdapter);
                ethAdapterList.add(ethAdapter);
            }
        }

        if(converters != null){
            Iterator it = converters.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                String name = pair.getKey().toString();

                ExpNode converterObj = ((Map<ExpNode,String>)pair.getValue()).keySet().stream().findFirst().get();
                String converterConnectedTo = ((Map<ExpNode,String>)pair.getValue()).values().stream().findFirst().get();

                Map<String, EthAdapter> adapterInfo = adaptesLst.entrySet().stream().filter(x -> converterConnectedTo.equals(x.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


                //TODO: it needs to be more genetic - we should support different types, not only RS232Adapter
                if(!adapterInfo.isEmpty()) {
                    RS232Adapter rs232Converter = new RS232Adapter(this, name,false, adapterInfo.values().stream().findFirst().get());
                    rs232Converter.schedule(new TimeSpan(0));
                    sendTraceNote("RS232Adapter has been created " + rs232Converter.getName());

                    convertersLst.put(name, rs232Converter);
                }
            }
        }



        if(sensors != null){
            Iterator it = sensors.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                String name = pair.getKey().toString();

                SANode sensorObj = ((Map<SANode,String>)pair.getValue()).keySet().stream().findFirst().get();
                String sensorConnectedTo = ((Map<SANode,String>)pair.getValue()).values().stream().findFirst().get();

                Map<String, EthAdapter> adapterInfo = adaptesLst.entrySet().stream().filter(x -> sensorConnectedTo.equals(x.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


                if(!adapterInfo.isEmpty()){
                    TCPMessageGenerator msgGenerator = new TCPMessageGenerator(this, name,false,
                            adapterInfo.values().stream().findFirst().get(), sensorObj.destAddress, sensorObj.freq);
                    msgGenerator.schedule(new TimeSpan(0));
                }

                Map<String, RS232Adapter> converterInfo = convertersLst.entrySet().stream().filter(x -> sensorConnectedTo.equals(x.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                //TODO: it needs to be more genetic - we should support different types, not only RS232Adapter
                if(!converterInfo.isEmpty()){
                    RS232MessageGenerator rs232MessageGenerator = new RS232MessageGenerator(this, name, true,
                            converterInfo.values().stream().findFirst().get());
                    rs232MessageGenerator.schedule(new TimeSpan(0));
                }
            }
        }



//        EthAdapter adapter_1 = new EthAdapter(this, "eth-adapter-1",false, "1");
//        adapter_1.schedule(new TimeSpan(0));
//
//        EthAdapter adapter_3 = new EthAdapter(this, "eth-adapter-3",false, "3");
//        adapter_3.schedule(new TimeSpan(0));
//
//        EthAdapter adapter_2 = new EthAdapter(this, "eth-adapter",true, "2");
//        adapter_2.schedule(new TimeSpan(0));



//        RS232Adapter rs232Conv1 = new RS232Adapter(this, "rs232-adapter",false, adapter_1);
//        rs232Conv1.schedule(new TimeSpan(0));
//
//        RS232Adapter rs232Conv2 = new RS232Adapter(this, "rs232-adapter",false, adapter_2);
//        rs232Conv2.schedule(new TimeSpan(0));

//        TCPMessageGenerator msgGenertor1 = new TCPMessageGenerator(this, "msg-generator",false, adapter_1, "2", 50);
//        msgGenertor1.schedule(new TimeSpan(0));
//
//
//        TCPMessageGenerator msgGenertor2 = new TCPMessageGenerator(this, "msg-generator",false, adapter_3, "2", 55);
//        msgGenertor2.schedule(new TimeSpan(0));






//        EthAdapter adapter_1 = new EthAdapter(this, "eth-adapter",false, "1");
//        adapter_1.schedule(new TimeSpan(0));
//
//        EthAdapter adapter_2 = new EthAdapter(this, "eth-adapter",true, "2");
//        adapter_2.schedule(new TimeSpan(0));
//
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


//        TCPMessageGenerator msgGenertor = new TCPMessageGenerator(this, "msg-generator",false, adapter_1, "2", 20);
//        msgGenertor.schedule(new TimeSpan(0));
//
//        TCPMessageGenerator msgGenertor3 = new TCPMessageGenerator(this, "msg-generator",false, adapter_3, "2", 50);
//        msgGenertor3.schedule(new TimeSpan(0));
//
//        TCPMessageGenerator msgGenertor4 = new TCPMessageGenerator(this, "msg-generator",false, adapter_4, "2", 50);
//        msgGenertor4.schedule(new TimeSpan(0));


//        RS232MessageGenerator rs232MessageGenerator = new RS232MessageGenerator(this, "rs232-msg-generator", true, rs232Conv1);
//        rs232MessageGenerator.schedule(new TimeSpan(0));


//        TCPMessageGenerator msgGenertor5 = new TCPMessageGenerator(this, "msg-generator",true, adapter_5, "2");
//        msgGenertor5.schedule(new TimeSpan(0));



//        ethAdapterList.add(adapter_1);
//        ethAdapterList.add(adapter_2);
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
