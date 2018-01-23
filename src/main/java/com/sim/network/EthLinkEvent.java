package main.java.com.sim.network;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;


public class EthLinkEvent extends Event<EthFrame> {

    public EthLinkEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
    }

    @Override
    public void eventRoutine(EthFrame ethFrame) {
        NetworkModel model = (NetworkModel) getModel();



        if(model.ethLink.isEmpty()){
            model.ethLink.insert(ethFrame);
            sendTraceNote("DODAJE  = " + ethFrame.getName());
            sendTraceNote("CZAS  = " + ethFrame.presentTime());

        }else{
            sendTraceNote("LINIA ZAJETA = " + ethFrame.getName());
            sendTraceNote("CZAS  = " + ethFrame.presentTime());

            //kolizja
        }




//        if (!model.msgQueue.isEmpty()) {
//            model.msgQueue.remove(tcpMessage);
//            EthFrame frame = new EthFrame(model, "ETH-Frame", true);
//            model.ethQueue.insert(frame);
//        }

    }
}
