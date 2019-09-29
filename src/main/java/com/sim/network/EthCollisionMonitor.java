package main.java.com.sim.network;

import desmoj.core.simulator.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class EthCollisionMonitor extends ExternalEvent {

    EthCollisionMonitor(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
    }

    @Override
    public void eventRoutine() {
        NetworkModel model = (NetworkModel) getModel();

        if (!model.getEthPendingBuffer().isEmpty()) {

            EthFrame framePending = model.getEthPendingBuffer().first();

            if (model.getEthLink().isEmpty()) {
                model.getEthPendingBuffer().remove(framePending);
                framePending.setStartTransmission(presentTime());
                model.getEthLink().insert(framePending);
            }

            for (EthFrame frame : model.getEthPendingBuffer()) {
                //sendTraceNote("ethLink startTransmision = " + model.getEthLink().first().getStartTransmission());
                TimeInstant ethLinkFrame = null;

                if (!model.getEthLink().isEmpty()) {
                    if (model.getEthLink().first().getStartTransmission() != null) {
                        ethLinkFrame = model.getEthLink().first().getStartTransmission();
                    } else {
                        ethLinkFrame = model.getEthLink().first().getInsertedTime();
                    }
                }

                //sendTraceNote("ethLink startTransmision = " + ethLinkFrame);

                if (!model.getEthLink().isEmpty() && Objects.equals(ethLinkFrame, frame.getInsertedTime())
                        && !Objects.equals(model.getEthLink().first().adapter.getName(), frame.adapter.getName())) {

                    sendTraceNote("ethLink inserted time = " + model.getEthLink().first().getInsertedTime());
                    sendTraceNote("ethLink inserted time = " + model.getEthLink().first());
                    sendTraceNote("frame inserted time = " + frame.getInsertedTime());
                    sendTraceNote("-----");

                    sendTraceNote("Collision detected= " + !model.getEthLink().isEmpty());
                    sendTraceNote("Collision detected= " + model.getEthLink().first().getInsertedTime());
                    sendTraceNote("Collision detected= " + frame.getInsertedTime());
                    sendTraceNote("Collision detected= " + frame.getName());
                    sendTraceNote("Collision detected= " + model.getEthLink().first().getName());

                    sendTraceNote("Adapter name = " + frame.getName());
                    int collision = model.getAllColisionsCouter();
                    collision = collision + 1;
                    model.setAllColisionsCouter(collision);
                    sendTraceNote("Collision counter = " + collision);

                    //Collision detected
                    sendTraceNote("Collision detected= " + model.getEthLink().first().getAdapter());
                    // ETH-Link item
                    EthFrame linkFrame = model.getEthLink().first();
                    EthAdapter linkAdapter = linkFrame.getAdapter();
                    linkAdapter.getOutAdapterQueue().insert(linkFrame);
                    linkAdapter.setCollisionDetected(true);
                    model.getEthLink().remove(linkFrame);

                    ReleaseEthAdapterEvent rAdapter = new ReleaseEthAdapterEvent(model, "collision waiting", true, linkAdapter);
                    double rAdapterWait = getCollisionWaitingTimeSlot();
                    rAdapter.schedule(linkFrame, new TimeSpan(rAdapterWait, TimeUnit.MICROSECONDS));
                    sendTraceNote("Collision waiting time slot = " + rAdapterWait + " adapter = " + linkAdapter.getName());

                    EthAdapter colAdapter = frame.getAdapter();
                    colAdapter.setCollisionDetected(true);
                    colAdapter.getOutAdapterQueue().insert(frame);
                    model.getEthPendingBuffer().remove(frame);
                    double colAdapterWait = getCollisionWaitingTimeSlot();
                    ReleaseEthAdapterEvent rAdapterCol = new ReleaseEthAdapterEvent(model, "collision waiting", true, colAdapter);
                    rAdapterCol.schedule(frame, new TimeSpan(colAdapterWait, TimeUnit.MICROSECONDS));
                    sendTraceNote("Collision waiting time slot = " + colAdapterWait + " adapter = " + colAdapter.getName());
                }
            }
        }
        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }


    private double getCollisionWaitingTimeSlot() {
        NetworkModel model = (NetworkModel) getModel();
        // SlotTime = 512 bits for 10/100 Mb/s networks, 51.2 micro sec
        double SlotTime = 51.2;
        double Ri = model.getRandCollisionValue();
        return Ri * SlotTime;
    }
}
