package main.java.com.sim.com.lamps;

import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

public class LampQueueChecker extends SimProcess{

	public boolean zapalana = false;
	public boolean swieci = false;
	public boolean gasnie = false;

	
	public LampQueueChecker(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}

	//jeden proces per lampa, ktory sprawdza status lamp (zapalona, swieci, gasnie)
	//Lampa zapala sie w 2s
	//Gasnie w 3s
	//Minimalny czas dzialania w trybie swieci = 10sek
	
	
	@Override
	public void lifeCycle() {
		LampProcess model = (LampProcess)getModel();
		Car car = null;
		
		while(true){
			
			if(!model.Lamp1Queue.isEmpty()){
				car = model.Lamp1Queue.first();
				
				if(!swieci && !gasnie){
					zapalana = true;
				}else{
					sendTraceNote("jeszcze sie swieci, wiec nie zapalamy ponownie");
					zapalana = false;
				}
				
				if (zapalana && car != null){
					sendTraceNote("zapalana");
					
					//car.activateAfter(this);
					sendTraceNote(car.getName() + " $Z_START");
					hold(new TimeSpan(2, TimeUnit.SECONDS));
					sendTraceNote(car.getName() + " $Z_END");
					zapalana = false;
					swieci = true;
				}
				
				if (swieci && !car.isTerminated()){
					sendTraceNote("swieci " + car.getName());
					//model.Lamp1Queue.remove(car);
					//car.activateAfter(this);
				
					if(model.Lamp1Queue.isEmpty()){
						swieci = false;
						gasnie = true;
						continue;
					}
					sendTraceNote(car.getName() + " $S_START");
					hold(new TimeSpan(10, TimeUnit.SECONDS));
					sendTraceNote(car.getName() + " $S_END");
				}
				
//				if (gasnie && car != null){
//					sendTraceNote("gasnie");
//					hold(new TimeSpan(3, TimeUnit.SECONDS));
//					gasnie = false;
//				}
				
				if(car != null){
					model.Lamp1Queue.remove(car);
					//car.activateAfter(this);
				}

			}else{
				if(model.Lamp1Queue.isEmpty() && swieci){
					swieci = false;
					gasnie = true;
					sendTraceNote("brak aut - swiatlo gasnie");
					sendTraceNote(car.getName() + " $G_START");
					hold(new TimeSpan(3, TimeUnit.SECONDS));
					sendTraceNote(car.getName() + " $G_END");
					gasnie = false;
				}else{
					hold(new TimeSpan(0.1, TimeUnit.SECONDS));
				}
			}
			
			
//			if(!model.Lamp1ZQueue.isEmpty()){
//				Car car = model.Lamp1ZQueue.first();
//				sendTraceNote("$Z_START");
//				hold(new TimeSpan(2, TimeUnit.SECONDS));
//				
//				model.Lamp1ZQueue.remove(car);
//				sendTraceNote("$Z_END");
//				car.activateAfter(this);
//			}
			//hold(new TimeSpan(100, TimeUnit.MILLISECONDS));
			//passivate();
		}
	}
}
