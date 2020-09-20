package main.java.com.sim.network;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class TCPMessage extends Entity {
    private String srcAddress;
    private String dstAddress;
    private TimeInstant startTransmission;
    private TimeInstant stopTransmission;
    private String data;

    public TCPMessage(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        startTransmission = presentTime();
    }

    TimeInstant getTransmissionTime() {
        if (startTransmission == null || stopTransmission == null) return null;
        return new TimeInstant(stopTransmission.getTimeAsDouble() - startTransmission.getTimeAsDouble());
    }

    @Override
    public String toString() {
        TimeInstant transmissionTime = getTransmissionTime();

        return "TCPMessage{" +
                "srcAddress='" + srcAddress + '\'' +
                ", dstAddress='" + dstAddress + '\'' +
                ((transmissionTime == null)
                        ? ""
                        : ", transmissionTime='" + transmissionTime + '\'') +
                ", data='" + data + '\'' +
                '}';
    }
}