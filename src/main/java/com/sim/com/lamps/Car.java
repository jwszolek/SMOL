package main.java.com.sim.com.lamps;


import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

public class Car extends SimProcess{

	private LampProcess myModel;
	
	public Car(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		// store a reference to the model this truck is associated with
		myModel = (LampProcess) owner;
	}

	@Override
	public void lifeCycle() {
		// TODO Auto-generated method stub
		//sendTraceNote("before");
		//hold(new TimeSpan(0.5, TimeUnit.SECONDS));
		
		passivate();
		
		//myModel.Lamp1Queue.remove(this);
		sendTraceNote("Aktywacja obiektu CAR");
	}
}
