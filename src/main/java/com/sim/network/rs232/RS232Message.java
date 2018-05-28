package main.java.com.sim.network.rs232;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class RS232Message extends Entity {

    private String tcpDstAddress;


    public RS232Message(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
    }

    public String getTcpDstAddress() {
        return tcpDstAddress;
    }

    public void setTcpDstAddress(String tcpDstAddress) {
        this.tcpDstAddress = tcpDstAddress;
    }
}
