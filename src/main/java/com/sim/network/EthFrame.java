package main.java.com.sim.network;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;

public class EthFrame extends Entity {

    private String destAddress;
    public EthAdapter adapter;
    private TimeInstant startTransmission;
    private TimeInstant stopTransmission;
    public TimeInstant insertedTime;
    // number of retries. IEEE 802.3 norm allow for 16 tries only
    private int retriesCounter;
    private TCPMessage tcpMessage;


    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    public EthAdapter getAdapter() {
        return adapter;
    }

    public TimeInstant getTransmissionTime(){
        return new TimeInstant(stopTransmission.getTimeAsDouble() - getStartTransmission().getTimeAsDouble());
    }

    public TimeInstant getStartTransmission() {
        return startTransmission;
    }

    public void setStartTransmission(TimeInstant startTransmission) {
        this.startTransmission = startTransmission;
    }

    public TimeInstant getStopTransmission() {
        return stopTransmission;
    }

    public void setStopTransmission(TimeInstant stopTransmission) {
        this.stopTransmission = stopTransmission;
    }

    public TimeInstant getInsertedTime() {
        return insertedTime;
    }

    public int getRetriesCounter() {
        return retriesCounter;
    }

    public void setRetriesCounter(int retriesCounter) {
        this.retriesCounter = retriesCounter;
    }

    public TCPMessage getTcpMessage() {
        return tcpMessage;
    }

    public void setTcpMessage(TCPMessage tcpMessage) {
        this.tcpMessage = tcpMessage;
    }

    public EthFrame(Model owner, String name, boolean showInTrace){
        super(owner, name, showInTrace);
    }


    public EthFrame(Model owner, String name, boolean showInTrace, EthAdapter adapater){
        super(owner, name, showInTrace);
        this.adapter = adapater;
    }


    public EthFrame(Model owner, String name, boolean showInTrace, EthAdapter adapater, TimeInstant insertedTime){
        super(owner, name, showInTrace);
        this.insertedTime = insertedTime;
        this.adapter = adapater;
    }

}
