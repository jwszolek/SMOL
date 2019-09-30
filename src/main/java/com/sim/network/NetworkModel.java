package main.java.com.sim.network;

import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;
import lombok.Getter;
import lombok.Setter;
import main.groovy.smoh.ExpNode;
import main.groovy.smoh.HBase;
import main.groovy.smoh.SANode;
import main.groovy.smoh.TNode;
import main.java.com.sim.network.bacnet.BacnetAdapter;
import main.java.com.sim.network.bacnet.BacnetMessageGenerator;
import main.java.com.sim.network.mqtt.MqttAdapter;
import main.java.com.sim.network.mqtt.MqttMessageGenerator;
import main.java.com.sim.network.rs232.RS232Adapter;
import main.java.com.sim.network.rs232.RS232MessageGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class NetworkModel extends Model {
    private ContDistUniform randCollisionValue;

    @Getter
    @Setter
    private int allColisionsCouter;

    @Getter
    private Queue<EthFrame> ethLink;

    @Getter
    private Queue<EthFrame> ethPendingBuffer;

    @Getter
    private List<EthAdapter> ethAdapterList;

    private HashMap<String, HBase> modelingObjects;

    public NetworkModel(Model owner, String modelName, boolean showInReport, boolean showInTrace) {
        super(owner, modelName, showInReport, showInTrace);
        this.ethAdapterList = new ArrayList<>();
    }

    public NetworkModel(Model owner, String modelName, boolean showInReport, boolean showInTrace, HashMap<String, HBase> modelingObjects) {
        super(owner, modelName, showInReport, showInTrace);
        this.ethAdapterList = new ArrayList<>();
        this.modelingObjects = modelingObjects;
    }

    @Override
    public void init() {
        randCollisionValue = new ContDistUniform(this, "RandCollisionValue", 0, 1, true, false);

        ethLink = new Queue<>(this, "ethLink", true, true);
        ethPendingBuffer = new Queue<>(this, "ethLinkPending", true, true);
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public void doInitialSchedules() {
        Map<String, Map<TNode, String>> adapters = generateMap(this.modelingObjects, TNode.class);
        Map<String, Map<SANode, String>> sensors = generateMap(this.modelingObjects, SANode.class);
        Map<String, Map<ExpNode, String>> converters = generateMap(this.modelingObjects, ExpNode.class);

        Map<String, EthAdapter> adaptesLst = new HashMap<>();
        Map<String, Object> convertersLst = new HashMap<>();

        if (adapters != null) {
            adapters.forEach((key, value) -> value.keySet().stream().findFirst().ifPresent(adapter -> {
                EthAdapter ethAdapter = new EthAdapter(this, key, true, adapter.ip);
                ethAdapter.schedule(new TimeSpan(0));
                sendTraceNote("EthAdapter has been created " + ethAdapter.getName());

                adaptesLst.put(key, ethAdapter);
                ethAdapterList.add(ethAdapter);
            }));
        }

        converters(converters, adaptesLst, convertersLst);
        sensors(sensors, adaptesLst, convertersLst);

        EthLinkRouter router = new EthLinkRouter(this, "ethlink-router", false, ethAdapterList);
        router.schedule(new TimeSpan(0));

        EthCollisionMonitor collisionMonitor = new EthCollisionMonitor(this, "collision-monitor", false);
        collisionMonitor.schedule(new TimeSpan(0));
    }

    double getRandCollisionValue() {
        return randCollisionValue.sample();
    }

    private void converters(
            Map<String, Map<ExpNode, String>> converters,
            Map<String, EthAdapter> adaptesLst,
            Map<String, Object> convertersLst
    ) {
        if (converters != null) {
            for (Map.Entry pair : converters.entrySet()) {
                String name = pair.getKey().toString();

                ExpNode converterObj = ((Map<ExpNode, String>) pair.getValue()).keySet().stream().findFirst().get();
                String converterConnectedTo = ((Map<ExpNode, String>) pair.getValue()).values().stream().findFirst().get();

                Map<String, EthAdapter> adapterInfo = adaptesLst.entrySet().stream().filter(x -> converterConnectedTo.equals(x.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                if (!adapterInfo.isEmpty()) {
                    switch (converterObj.model) {
                        case "rs232":
                            RS232Adapter rs232Converter = new RS232Adapter(this, name, false, adapterInfo.values().stream().findFirst().get());
                            rs232Converter.schedule(new TimeSpan(0));
                            convertersLst.put(name, rs232Converter);
                            sendTraceNote("RS232Adapter has been created " + rs232Converter.getName());

                            break;
                        case "bacnet":
                            BacnetAdapter bacnetConverter = new BacnetAdapter(this, name, false, adapterInfo.values().stream().findFirst().get());
                            bacnetConverter.schedule(new TimeSpan(0));
                            convertersLst.put(name, bacnetConverter);
                            sendTraceNote("BacnetAdapter has been created " + bacnetConverter.getName());
                            break;
                        case "mqtt":
                            MqttAdapter mqttAdapter = new MqttAdapter(this, name, false, adapterInfo.values().stream().findFirst().get());
                            mqttAdapter.schedule(new TimeSpan(0));
                            convertersLst.put(name, mqttAdapter);

                            sendTraceNote("MqttAdapter has been created " + mqttAdapter.getName());
                            break;
                    }
                }
            }
        }
    }

    private void sensors(
            Map<String, Map<SANode, String>> sensors,
            Map<String, EthAdapter> adaptesLst,
            Map<String, Object> convertersLst
    ) {
        if (sensors != null)
            for (Object o : sensors.entrySet()) {
                Map.Entry pair = (Map.Entry) o;
                String name = pair.getKey().toString();

                SANode sensorObj = ((Map<SANode, String>) pair.getValue()).keySet().stream().findFirst().get();
                String sensorConnectedTo = ((Map<SANode, String>) pair.getValue()).values().stream().findFirst().get();

                Map<String, EthAdapter> adapterInfo = adaptesLst.entrySet().stream().filter(x -> sensorConnectedTo.equals(x.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


                if (!adapterInfo.isEmpty()) {
                    TCPMessageGenerator msgGenerator = new TCPMessageGenerator(this, name, false,
                            adapterInfo.values().stream().findFirst().get(), sensorObj.destAddress, sensorObj.freq);
                    msgGenerator.schedule(new TimeSpan(0));
                }

                Map<String, Object> converterInfo = convertersLst.entrySet().stream().filter(x -> sensorConnectedTo.equals(x.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                if (!converterInfo.isEmpty()) {

                    if (converterInfo.entrySet().stream().findFirst().get().getValue() instanceof RS232Adapter) {
                        RS232MessageGenerator rs232MessageGenerator = new RS232MessageGenerator(this, name, true,
                                (RS232Adapter) converterInfo.values().stream().findFirst().get(), sensorObj.destAddress, sensorObj.freq);
                        rs232MessageGenerator.schedule(new TimeSpan(0));
                        sendTraceNote("RS232 sensor has been created " + rs232MessageGenerator.getName());
                    } else if (converterInfo.entrySet().stream().findFirst().get().getValue() instanceof BacnetAdapter) {
                        BacnetMessageGenerator bacnetMsgGenerator = new BacnetMessageGenerator(this, name, true,
                                (BacnetAdapter) converterInfo.values().stream().findFirst().get(), sensorObj.destAddress, sensorObj.freq);
                        bacnetMsgGenerator.schedule(new TimeSpan(0));
                        sendTraceNote("Bacnet sensor has been created " + bacnetMsgGenerator.getName());
                    } else if (converterInfo.entrySet().stream().findFirst().get().getValue() instanceof MqttAdapter) {
                        MqttAdapter adapter = (MqttAdapter) converterInfo.values().stream().findFirst().orElse(null);
                        if (adapter != null) {
                            MqttMessageGenerator mqttMsgGenerator = new MqttMessageGenerator(this, name, true,
                                    adapter, sensorObj.destAddress, sensorObj.freq);
                            mqttMsgGenerator.schedule(new TimeSpan(0));
                            sendTraceNote("Mqtt sensor has been created " + mqttMsgGenerator.getName());
                        } else {
                            sendTraceNote("Mqtt sensor has not been created adapter is NULL");
                        }
                    }
                }
            }
    }

    private <T> Map<String, Map<T, String>> generateMap(HashMap<String, HBase> modelingObjects, Class<T> cls) {
        Map<String, Map<T, String>> outputMap = new HashMap<>();

        if (this.modelingObjects != null) {
            modelingObjects.forEach((key, value) -> {
                if (cls.isInstance(value)) {
                    Map<T, String> innerMap = new HashMap<>();
                    innerMap.put((T) value, (value).getConnected());
                    outputMap.put(key, innerMap);
                }
            });
        }
        return outputMap;
    }
}
