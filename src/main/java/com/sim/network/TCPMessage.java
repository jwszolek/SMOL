package main.java.com.sim.network;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;

public class TCPMessage extends Entity {
    private String dstAddress;
    private String srcAddress;
    private TimeInstant startTransmission;
    private TimeInstant stopTransmission;

    public TCPMessage(Model owner, String name, boolean showInTrace){
        super(owner, name, showInTrace);
        startTransmission = presentTime();
    }

    public TimeInstant getTransmissionTime(){
        return new TimeInstant(stopTransmission.getTimeAsDouble() - startTransmission.getTimeAsDouble());
    }

    public void setStopTransmission(TimeInstant stopTransmission) {
        this.stopTransmission = stopTransmission;
    }

    public String getDstAddress() {
        return dstAddress;
    }

    public void setDstAddress(String dstAddress) {
        this.dstAddress = dstAddress;
    }

    public String getSrcAddress() {
        return srcAddress;
    }

    public void setSrcAddress(String srcAddress) {
        this.srcAddress = srcAddress;
    }
}
