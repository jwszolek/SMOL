package main.java.com.sim.network;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class EthFrame extends Entity {


    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    private String destAddress;



    public EthFrame(Model owner, String name, boolean showInTrace){
        super(owner, name, showInTrace);
    }




}
