package unmla.airport;

import unmla.airport.simul.Simulation;
import unmla.airport.simul.Steppable;
import unmla.airport.simul.TimeCounter;
import unmla.airport.ui.UiProvider;

import java.util.PriorityQueue;

/**
 * 
 * @author redacted
 *  interface to use to define vertices in graph
 *  extended by LuggageRouter
 *  implmented by ConveyerBelt, SecurityScanner, CheckIn, Switch, GateGroup and Gate
 */
public interface LuggageTransporter extends Steppable, UiProvider {

	int getCapacity();
	int getMaxCapacity();	
	boolean isFull();
	boolean equals(final Object obj);
	void addLuggage(Luggage l);
	void yellForLuggageMoverPerson(Luggage l);
	PriorityQueue<Luggage> getContents();
	double getDefaultTime();
	double getTime();
	void setTime(double t);
	boolean getState();
	void setState(boolean s);
	String getID();
	void setID(String i);
	String getUniqueID();
	void breakLink();
	void fixLink();
	TimeCounter getTimeCounter();
	void step(final double deltaTime, final Simulation simulation);
	void initialize(final Simulation simulation);
	void reset(final Simulation simulation);	
	void requestAccepted();	
	void requestSubmitted();
	double getCapacityRatio();
	boolean isWaitingOnRequest();
	
}
