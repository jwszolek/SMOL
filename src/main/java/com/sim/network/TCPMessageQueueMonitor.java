package main.java.com.sim.network;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import main.java.com.sim.network.EthFrame;
import main.java.com.sim.network.NetworkModel;
import main.java.com.sim.network.TCPMessage;

import java.util.concurrent.TimeUnit;

public class TCPMessageQueueMonitor extends Event<TCPMessage> {

    public TCPMessageQueueMonitor(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
    }

    @Override
    public void eventRoutine(TCPMessage tcpMessage) {
        NetworkModel model = (NetworkModel) getModel();

        if (!model.msgQueue.isEmpty()) {
            model.msgQueue.remove(tcpMessage);
            EthFrame frame = new EthFrame(model, "ETH-Frame", true);
            model.ethQueue.insert(frame);
        }

    }
}
