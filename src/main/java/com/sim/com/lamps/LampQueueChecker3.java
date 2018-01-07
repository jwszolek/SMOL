package main.java.com.sim.com.lamps;

import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

public class LampQueueChecker3 extends SimProcess{

	public boolean zapalana = false;
	public boolean swieci = false;
	public boolean gasnie = false;
	
	
	public LampQueueChecker3(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}

	@Override
	public void lifeCycle() {
		LampProcess model = (LampProcess)getModel();
		Car car = null;

		while(true){
			if(!model.Lamp3Queue.isEmpty()){
				car = model.Lamp3Queue.first();
				
				if(!swieci && !gasnie){
					zapalana = true;
				}else{
					sendTraceNote("jeszcze sie swieci, wiec nie zapalamy ponownie");
					zapalana = false;
				}
				
				if (zapalana && car != null){
					sendTraceNote("zapalana");
					
					sendTraceNote(car.getName() +"3"+ " $Z_START");
					hold(new TimeSpan(2, TimeUnit.SECONDS));
					sendTraceNote(car.getName() +"3"+ " $Z_END");
					zapalana = false;
					swieci = true;
				}
				
				if (swieci){
					sendTraceNote("swieci " + car.getName());
				
					if(model.Lamp3Queue.isEmpty()){
						swieci = false;
						gasnie = true;
						continue;
					}
					sendTraceNote(car.getName() +"3"+ " $S_START");
					hold(new TimeSpan(10, TimeUnit.SECONDS));
					sendTraceNote(car.getName() +"3"+ " $S_END");
				}
				
				
				if(car != null){
					model.Lamp3Queue.remove(car);
//					car.activateAfter(this);
				}

			}else{
				if(car != null && model.Lamp3Queue.isEmpty() && swieci){
						swieci = false;
						gasnie = true;
						sendTraceNote("brak aut - swiatlo gasnie");
						sendTraceNote(car.getName() +"3"+ " $G_START");
						hold(new TimeSpan(3, TimeUnit.SECONDS));
						sendTraceNote(car.getName() +"3"+ " $G_END");
						gasnie = false;
				}else{
					hold(new TimeSpan(0.1, TimeUnit.SECONDS));
				}
			}
		}
	}

}