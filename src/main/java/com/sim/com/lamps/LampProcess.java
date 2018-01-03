package main.java.com.sim.com.lamps;

import desmoj.core.dist.ContDistExponential;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.ProcessQueue;
import desmoj.core.simulator.TimeSpan;

public class LampProcess extends Model {

	//Lamp process per Lampa
	//sprawdzanie statusu {zapa/swiec/gasz}
	
	private desmoj.core.dist.ContDistExponential carArrivalTime;
	protected ProcessQueue<Car> Lamp1Queue;
	protected ProcessQueue<Car> Lamp2Queue;
	protected ProcessQueue<Car> Lamp3Queue;
	protected ProcessQueue<Car> Lamp4Queue;
	protected ProcessQueue<Car> Lamp5Queue;
	protected ProcessQueue<Car> Lamp6Queue;
	protected ProcessQueue<Car> Lamp7Queue;
	protected ProcessQueue<Car> Lamp8Queue;
	protected ProcessQueue<Car> Lamp9Queue;
		
	public LampProcess(Model owner, String modelName, boolean showInReport, boolean showInTrace) {
		super(owner, modelName, showInReport, showInTrace);
	}

	@Override
	public String description() {
		return "Lamps script 1";	
	}

	@Override
	public void doInitialSchedules() {
	   CarGenerator generator = new CarGenerator(this,"CarArrival",true);
	   generator.activate(new TimeSpan(0));
	   
	   LampQueueChecker lchecker = new LampQueueChecker(this, "LampQueueChecker",true);
	   lchecker.activate(new TimeSpan(0));
	   
	   LampQueueChecker2 lchecker2 = new LampQueueChecker2(this, "LampQueueChecker2",true);
	   lchecker2.activate(new TimeSpan(0));
	   
	   LampQueueChecker3 lchecker3 = new LampQueueChecker3(this, "LampQueueChecker3",true);
	   lchecker3.activate(new TimeSpan(0));

	   LampQueueChecker4 lchecker4 = new LampQueueChecker4(this, "LampQueueChecker4",true);
	   lchecker4.activate(new TimeSpan(0));

	   LampQueueChecker5 lchecker5 = new LampQueueChecker5(this, "LampQueueChecker5",true);
	   lchecker5.activate(new TimeSpan(0));
//
//	   LampQueueChecker6 lchecker6 = new LampQueueChecker6(this, "LampQueueChecker6",true);
//	   lchecker6.activate(new TimeSpan(0));
	}

	public double getCarArrivalTime() {
		return carArrivalTime.sample();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		carArrivalTime = new ContDistExponential(this,"CarArrivalTimeStream", 3, true, false);
		carArrivalTime.setNonNegative(true);
		
		Lamp1Queue = new ProcessQueue<Car>(this, "Lamp1 Queue", true, true);
		Lamp2Queue = new ProcessQueue<Car>(this, "Lamp2 Queue", true, true);
		Lamp3Queue = new ProcessQueue<Car>(this, "Lamp3 Queue", true, true);
		Lamp4Queue = new ProcessQueue<Car>(this, "Lamp4 Queue", true, true);
		Lamp5Queue = new ProcessQueue<Car>(this, "Lamp5 Queue", true, true);
		Lamp6Queue = new ProcessQueue<Car>(this, "Lamp6 Queue", true, true);
		
	}
}
