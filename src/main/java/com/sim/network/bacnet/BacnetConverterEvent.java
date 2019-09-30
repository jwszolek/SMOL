package main.java.com.sim.network.bacnet;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import main.java.com.sim.network.NetworkModel;
import main.java.com.sim.network.TCPMessage;

public class BacnetConverterEvent extends Event<BacnetMessage> {
    private BacnetAdapter bacnetAdapter;

    BacnetConverterEvent(Model owner, String name, boolean showInTrace, BacnetAdapter rs232Adapter) {
        super(owner, name, showInTrace);
        this.bacnetAdapter = rs232Adapter;
    }

    @Override
    public void eventRoutine(BacnetMessage bacnetMessage) {
        NetworkModel model = (NetworkModel) getModel();

        TCPMessage tcpMessage = new TCPMessage(model, "Bacnet To TCP", true);

        tcpMessage.setSrcAddress(this.bacnetAdapter.getEthAdapter().getAdapterAddress());
        tcpMessage.setDstAddress(bacnetMessage.getTcpDstAddress());

        if (this.bacnetAdapter.getInBacnetAdapterQueue() != null) {
            this.bacnetAdapter.getInBacnetAdapterQueue().insert(tcpMessage);
        }
    }
}
