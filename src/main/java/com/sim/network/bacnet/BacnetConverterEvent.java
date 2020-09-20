package main.java.com.sim.network.bacnet;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import co.paralleluniverse.fibers.SuspendExecution;
import main.java.com.sim.network.NetworkModel;
import main.java.com.sim.network.TCPMessage;

public class BacnetConverterEvent extends Event<BacnetMessage> {

    private BacnetMessage receivedMsg;
    private BacnetAdapter bacnetAdapter;

    public BacnetConverterEvent(Model owner, String name, boolean showInTrace, BacnetAdapter rs232Adapter) {
        super(owner, name, showInTrace);
        this.bacnetAdapter = rs232Adapter;
    }

    @Override
    public void eventRoutine(BacnetMessage bacnetMessage) throws SuspendExecution {
        NetworkModel model = (NetworkModel)getModel();
        this.receivedMsg = bacnetMessage;

        TCPMessage tcpMessage = new TCPMessage(model, "Bacnet To TCP", true);
        tcpMessage.setDstAddress(bacnetMessage.getTcpDstAddress());
        tcpMessage.setDstAddress(this.bacnetAdapter.getEthAdapter().getAdapterAddress());
        if(this.bacnetAdapter.inBacnetAdapterQueue != null) {
            this.bacnetAdapter.inBacnetAdapterQueue.insert(tcpMessage);
        }
    }
}
