package main.java.com.sim.network.rs232;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import main.java.com.sim.network.EthFrame;
import main.java.com.sim.network.NetworkModel;
import main.java.com.sim.network.TCPMessage;

public class RS232ConverterEvent extends Event<RS232Message> {

    private RS232Message receivedMsg;
    private RS232Converter rs232Converter;


    public RS232ConverterEvent(Model owner, String name, boolean showInTrace, RS232Converter rs232Converter) {
        super(owner, name, showInTrace);
        this.rs232Converter = rs232Converter;
    }

    @Override
    public void eventRoutine(RS232Message rs232Message) throws SuspendExecution {
        NetworkModel model = (NetworkModel)getModel();
        this.receivedMsg = rs232Message;

        TCPMessage tcpMessage = new TCPMessage(model, "RS232 To TCP", true);
        this.rs232Converter.inRS232ConveterQueue.insert(tcpMessage);


    }


}
