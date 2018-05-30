package main.java.com.sim.network.bacnet;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;


public class BacnetMessage extends Entity {
    private String tcpDstAddress;

    public BacnetMessage(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
    }

    public String getTcpDstAddress() {
        return tcpDstAddress;
    }

    public void setTcpDstAddress(String tcpDstAddress) {
        this.tcpDstAddress = tcpDstAddress;
    }
}
