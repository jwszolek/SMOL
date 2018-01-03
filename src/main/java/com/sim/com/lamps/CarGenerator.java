package main.java.com.sim.com.lamps;

import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

public class CarGenerator extends SimProcess {

	public CarGenerator(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		
	}

	@Override
	public void lifeCycle() {
		LampProcess model = (LampProcess)getModel();
		
		
		while (true) {
			Car car = new Car(model, "Car", true);
			car.activateAfter(this);
			model.Lamp1Queue.insert(car);
			model.Lamp2Queue.insert(car);
			//pierwsze lampy wlaczaja sie 2 sek przed nadjezdzajacym autem
			//odleglosc miedzy lampami auto przebywa w czasie 1sek
			//wlaczenie lampy 3 i 4 powinno nastapic 2 sek od wykrycia auta przy lampie 1
			hold(new TimeSpan(2, TimeUnit.SECONDS));
			model.Lamp3Queue.insert(car);
			model.Lamp4Queue.insert(car);
			hold(new TimeSpan(2, TimeUnit.SECONDS));
			model.Lamp5Queue.insert(car);
			//model.Lamp6Queue.insert(car);			
			
			sendTraceNote("Car created.");
			
			hold(new TimeSpan(6, TimeUnit.SECONDS));
		}
	}
}
