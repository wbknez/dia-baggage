package unmla.airport;

import java.util.PriorityQueue;

import unmla.airport.simul.Simulation;

public class BaggageClaim extends AbstractLuggageRouter implements
		LuggageRouter {	

	final IdPool ids = IdPool.getIdPool();

	public BaggageClaim(final String id, final String uniqueId, 
	                    final int defaultCapacity, final double defaultTime) {
	    super(id, uniqueId, defaultCapacity, defaultTime);
	}

	@Override
	//baggage claims can hold as much luggage as needed
	public boolean isFull() {
		return false;
	}
	
	@Override
	public void step(final double deltaTime, final Simulation simulation) {
	    if(Double.compare(deltaTime, 0) <= 0) {
	        throw new IllegalArgumentException("The delta time should be > 0!");
	    }
	    
	    if(simulation == null) {
	        throw new NullPointerException();
	    }
	    
	    final PriorityQueue<Luggage> contents = this.getContents();
	    
	    if(!(contents.isEmpty())) {
	    	Luggage l = contents.peek();
	    	//luggage is picked up, so return bag ID to pool
	    	contents.remove(l);
	    	this.getAirport().getStatistics().addMadeItToBagClaim();
	    	this.getAirport().removeLuggage(l);
	    	for (Luggage m : contents) {
	    		m.incrementTimer(deltaTime);
	    	}
	    }
	}
	
}
