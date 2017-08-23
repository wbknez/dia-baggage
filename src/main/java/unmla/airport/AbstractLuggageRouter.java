package unmla.airport;

import java.util.LinkedList;
import java.util.PriorityQueue;

import unmla.airport.simul.Simulation;
import unmla.airport.simul.TimeCounter;

public abstract class AbstractLuggageRouter extends AbstractLuggageTransporter implements LuggageRouter {

    private final Multiplexer   multiplexer;
    
	public AbstractLuggageRouter(final String id, final String uniqueId,
                                    final int defaultCapacity,                            
                                    final double defaultTime) {
	    super(id, uniqueId, defaultCapacity, defaultTime);
	    
	    this.multiplexer = new Multiplexer();
	}
	
	public LinkedList<LuggageTransporter> calcPath(LuggageRouter s, LuggageRouter d, final double maxDistance){
		LinkedList<LuggageTransporter> path= this.getAirport().findPath(s,d, maxDistance);
		//will use Dijkstra's algorith to find the quickest path from the current node to the given destination
		return path;
	}
	
	public double calcDistance(LuggageRouter s, LuggageRouter t) {
		return this.getAirport().findDistance(s, t);
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
	    
	    /*
	     * Update bags (always).
	     */
        for (Luggage m : contents) {
            m.incrementTimer(deltaTime);
        }
        
        if(!this.getState()) {
            return;
        }
        
        final TimeCounter counter = this.getTimeCounter();
        
        if (!(contents.isEmpty())) {
            counter.advance(deltaTime);

            if (counter.canStep()) {
                counter.step();
                Luggage l = contents.peek();

                // If bag is in system for more than 15 minutes it needs to be
                // sent to next plane
                if (l.bagexpired()) {
                    this.luggageDude.sendtoLandOfLostBags(l);
                    this.getContents().remove(l);
                    this.getAirport().getStatistics().addLateLuggage();
                } else if (l.isLost((LuggageTransporter) this)) {
                    luggageDude.callSegWay(l);
                    this.getContents().remove(l);
                    this.getAirport().getStatistics().addLostLuggage();
                } else {

                    // get next step in path
                    AbstractLuggageTransporter t = (AbstractLuggageTransporter) l
                            .getNextStep();
                    if (t==null||!(t.getState())) {
                        // if next step is broken, then recalculate path
                        // if no further path is possible, call a person
                        l.setPath(calcPath(this, l.getDestination(),
                                Luggage.MaximumTime - l.getTimer()));
                        if (l.getPath() == null) {
                            yellForLuggageMoverPerson(l);
                        }
                    } else {
                        // if next step is functional and available, move
                        // luggage
                        if (!t.isFull()) {
                            t.addLuggage(l);
                            contents.remove(l);
                        }
                    }
                    // if next step is full, luggage is not removed from queue
                    // and
                    // will be tried again next timestep
                }
            }
        } else {
            counter.reset();
        }
    	    
    	    if(!contents.isEmpty()) {
    	        /*
    	         * Check the multiplexer.
    	         */
    	        final Multiplexer multiplexer = this.getMultiplexer();
    	        
    	        if(!multiplexer.isEmpty()) {
    	            final LuggageTransporter input = multiplexer.poll();
    	            final Luggage luggage = input.getContents().poll();
    	            
                    input.requestAccepted();
    	            
                    if(luggage != null) {
                        this.addLuggage(luggage);
                    }
    	        }
    	    }
	}
	
	@Override
	public Multiplexer getMultiplexer() {
	    return this.multiplexer;
	}
}