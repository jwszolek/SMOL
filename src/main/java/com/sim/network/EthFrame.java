package main.java.com.sim.network;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;

public class EthFrame extends Entity {


    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    private String destAddress;

    public EthAdapter getAdapter() {
        return adapter;
    }

    public EthAdapter adapter;

    public TimeInstant getStartTransmission() {
        return startTransmission;
    }

    public void setStartTransmission(TimeInstant startTransmission) {
        this.startTransmission = startTransmission;
    }

    private TimeInstant startTransmission;

    public TimeInstant getInsertedTime() {
        return insertedTime;
    }
    public TimeInstant insertedTime;


    public int getRetriesCounter() {
        return retriesCounter;
    }
    public void setRetriesCounter(int retriesCounter) {
        this.retriesCounter = retriesCounter;
    }

    // number of retries. IEEE 802.3 norm allow for 16 tries only
    private int retriesCounter;

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
