package main.java.com.sim.network;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class TCPMessage extends Entity {

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String data;


    public TCPMessage(Model owner, String name, boolean showInTrace){
        super(owner, name, showInTrace);
    }

}
