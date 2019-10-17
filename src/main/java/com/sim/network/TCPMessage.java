package main.java.com.sim.network;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TCPMessage extends Entity {
    private String dstAddress;
    private String srcAddress;
    private TimeInstant startTransmission;
    private TimeInstant stopTransmission;
    private String data;

    public TCPMessage(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        startTransmission = presentTime();
    }

    TimeInstant getTransmissionTime() {
        return new TimeInstant(stopTransmission.getTimeAsDouble() - startTransmission.getTimeAsDouble());
    }
}