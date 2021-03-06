package main.java.com.sim.network;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeSpan;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EthCollisionMonitor extends ExternalEvent {

    public EthCollisionMonitor(Model owner, String name, boolean showInTrace, List<EthAdapter> adapters) {
        super(owner, name, showInTrace);
    }

    @Override
    public void eventRoutine() throws SuspendExecution {

        NetworkModel model = (NetworkModel) getModel();

        if (!model.ethPendingBuffer.isEmpty()) {

            EthFrame framePending = model.ethPendingBuffer.first();

            if (model.ethLink.isEmpty()) {
                model.ethPendingBuffer.remove(framePending);
                framePending.setStartTransmission(presentTime());
                model.ethLink.insert(framePending);
            }

            for (EthFrame frame : model.ethPendingBuffer) {

                //sendTraceNote("ethLink startTransmision = " + model.ethLink.first().getStartTransmission());
                TimeInstant ethLinkFrameTime = null;

                if (!model.ethLink.isEmpty()) {
                    if (model.ethLink.first().getStartTransmission() != null) {
                        ethLinkFrameTime = model.ethLink.first().getStartTransmission();
                    } else {
                        ethLinkFrameTime = model.ethLink.first().getInsertedTime();
                    }
                }

                //sendTraceNote("ethLink startTransmision = " + ethLinkFrame);

                if (!model.ethLink.isEmpty() && Objects.equals(ethLinkFrameTime, frame.getInsertedTime())
                        && !Objects.equals(model.ethLink.first().adapter.getName(), frame.adapter.getName())) {


                    sendTraceNote("ethLink inserted time = " + model.ethLink.first().getInsertedTime());
                    sendTraceNote("ethLink inserted time = " + model.ethLink.first());
                    sendTraceNote("frame inserted time = " + frame.getInsertedTime());
                    sendTraceNote("-----");

                    sendTraceNote("Collision detected= " + !model.ethLink.isEmpty());
                    sendTraceNote("Collision detected= " + model.ethLink.first().getInsertedTime());
                    sendTraceNote("Collision detected= " + frame.getInsertedTime());
                    sendTraceNote("Collision detected= " + frame.getName());
                    sendTraceNote("Collision detected= " + model.ethLink.first().getName());

                    sendTraceNote("Adapter name = " + frame.getName());
                    int collision = model.getAllColisionsCouter() + 1;
                    model.setAllColisionsCouter(collision);
                    sendTraceNote("Collision counter = " + collision);

                    //Collision detected
                    sendTraceNote("Collision detected= " + model.ethLink.first().getAdapter());
                    // ETH-Link item
                    EthFrame linkFrame = model.ethLink.first();
                    EthAdapter linkAdapter = linkFrame.getAdapter();
                    linkAdapter.outAdapterQueue.insert(linkFrame);
                    linkAdapter.setCollisionDetected(true);
                    model.ethLink.remove(linkFrame);

                    ReleaseEthAdapterEvent rAdapter = new ReleaseEthAdapterEvent(model, "collision waiting", true, linkAdapter);
                    double rAdapterWait = getCollisionWaitingTimeSlot();
                    rAdapter.schedule(linkFrame, new TimeSpan(rAdapterWait, TimeUnit.MICROSECONDS));
                    sendTraceNote("Collision waiting time slot = " + rAdapterWait + " adapter = " + linkAdapter.getName());

                    EthAdapter colAdapter = frame.getAdapter();
                    colAdapter.setCollisionDetected(true);
                    colAdapter.outAdapterQueue.insert(frame);
                    model.ethPendingBuffer.remove(frame);
                    double colAdapterWait = getCollisionWaitingTimeSlot();
                    ReleaseEthAdapterEvent rAdapterCol = new ReleaseEthAdapterEvent(model, "collision waiting", true, colAdapter);
                    rAdapterCol.schedule(frame, new TimeSpan(colAdapterWait, TimeUnit.MICROSECONDS));
                    sendTraceNote("Collision waiting time slot = " + colAdapterWait + " adapter = " + colAdapter.getName());

                }
            }
        }
        schedule(new TimeSpan(1, TimeUnit.MICROSECONDS));
    }


    public double getCollisionWaitingTimeSlot() {
        NetworkModel model = (NetworkModel) getModel();
        // SlotTime = 512 bits for 10/100 Mb/s networks, 51.2 micro sec
        double SlotTime = 51.2;
        int n = 1;
        double Ri = model.getRandCollisionValue();
        return Ri * SlotTime;
    }


}
