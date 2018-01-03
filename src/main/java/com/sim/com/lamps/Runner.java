package main.java.com.sim.com.lamps;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import desmoj.core.report.Reporter;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeFormatter;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeSpan;

public class Runner {

	//po uruchomieniu, odpalamy skrypt /Users/jwszol/Documents/workplace-dr/smolsim/sim_py_parser/python -u log_parser_lamps.py
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LampProcess model = new LampProcess(null,"LampProcess - script 1", true, true);
		Experiment exp = new Experiment("LampProcessExperiment",TimeUnit.SECONDS, TimeUnit.MILLISECONDS, null);
		model.connectToExperiment(exp);
		exp.setShowProgressBar(false); // display a progress bar (or not)
		exp.stop(new TimeInstant(90, TimeUnit.SECONDS)); // set end of
															// simulation at
															// 1500 minutes
		exp.tracePeriod(new TimeInstant(0), new TimeInstant(90,TimeUnit.SECONDS));
		// set the period of the trace
		exp.debugPeriod(new TimeInstant(0), new TimeInstant(90,TimeUnit.MILLISECONDS));

		  
		exp.start();
		exp.report();
		exp.finish();
	}
}