package main.java.com.sim.com.lamps;


import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;

public class Lamp extends SimProcess {
	
	private LampProcess myModel;
	
	public boolean zaplana = false;
	public boolean swieci = false;
	public boolean gasnie = false;
	
	public Lamp(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		myModel = (LampProcess) owner;
	}
	
	@Override
	public void lifeCycle() {
		sendTraceNote("Lamp 1 process started");
		
	}

}
