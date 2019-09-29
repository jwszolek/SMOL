package main.java.com.sim.network;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;
import lombok.Getter;
import lombok.Setter;

public class TCPMessage extends Entity {
    @Getter
    @Setter
    private String dstAddress;

    @Getter
    @Setter
    private String srcAddress;

    private TimeInstant startTransmission;

    @Setter
    private TimeInstant stopTransmission;

    public TCPMessage(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        startTransmission = presentTime();
    }

    TimeInstant getTransmissionTime() {
        return new TimeInstant(stopTransmission.getTimeAsDouble() - startTransmission.getTimeAsDouble());
    }
}
