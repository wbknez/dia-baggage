package unmla.airport;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

import unmla.airport.simul.Simulation;

public class Gate extends AbstractLuggageRouter {
    
    public static final int DefaultArrivalRate  = 5;

	public Gate(final String id, final String uniqueId, 
                        final int maxCapacity, final double defaultTime, int arrivalRate) {
		super(id, uniqueId, maxCapacity, defaultTime);
		
	    final LuggageComparator comp=new LuggageComparator();
		this.contentsIn = new PriorityQueue<Luggage>(maxCapacity, comp);

		this.arrivalRate = arrivalRate;
	}

	//to do: add two wait queues and a moveLuggage etc function for each
	private final PriorityQueue<Luggage> contentsIn;

	private int arrivalRate;
	
	private Random generator= new Random(System.currentTimeMillis());
	
	
	public void setArrivalRate(int r) {
		this.arrivalRate = r;
	}
	
	@Override
	//gates can hold as much luggage as needed
	public boolean isFull() {
		return false;
	}
	

	public void addLuggageIn(Luggage l) {
		this.contentsIn.offer(l);
	}
	
	@Override
	public void step(final double deltaTime, final Simulation simulation) {
		if(Double.compare(deltaTime, 0) <= 0) {
	        throw new IllegalArgumentException("The delta time should be > 0!");
	    }
	    
	    if(simulation == null) {
	        throw new NullPointerException();
	    }
	
    	//first deal with luggage coming from checkins
	    if(!(this.getContents().isEmpty())) {
	    	Luggage l = this.getContents().peek();
	    	//send luggage to plane
	    	if(l.getDestination() instanceof Gate) {
	    		this.getAirport().getStatistics().addMadeItToGate();
		    	this.getAirport().removeLuggage(l);	
	    		this.getContents().remove(l);
	    	}
	    	for (Luggage m : this.getContents()) {
	    		m.incrementTimer(deltaTime);
	    	}
	    }
	    
	    
	    //now deal with new luggage
	    	    
	    AbstractLuggageTransporter[] edges=new AbstractLuggageTransporter[1];
	    this.getAirport().getGraph().getOutEdges(this).toArray(edges);
	    AbstractLuggageTransporter cb = edges[0]; 
	    //if current CheckIn isn't full or disabled, and the next conveyer belt
	    // also isn't full or disabled, consider creating luggage
	    if(this.getState()&&(!this.isFull())&&cb.getState()&&(!cb.isFull())) {

	    	//use rng to decide if a new luggage is created
	    	if((generator.nextInt(10)+1)<arrivalRate) {
	    		Luggage l = new Luggage(this.getAirport().getIdPool().getID(),this.pickBagClaim(),this.pickFlightTime());
	    		this.checkInLuggage(l);
	    	}
	    }
	    	
	    if(!(contentsIn.isEmpty())) {

	    	//move top luggage in queue
	    	Luggage l = contentsIn.peek();
	    	
		//If bag is in system for more than 15 minutes it needs to be sent to next plane 
		if (l.bagexpired()){
			luggageDude.sendtoLandOfLostBags(l);
			this.getContents().remove(l);
			this.getAirport().getStatistics().addLateLuggage();
		}

		else if(l.isLost((LuggageTransporter) this)) {
			luggageDude.callSegWay(l);
			this.getContents().remove(l);
    		this.getAirport().getStatistics().addLostLuggage();
		} else {
	    
	    		//if current node is broken, then call luggage mover person
	    		if(!(this.getState())) {
	    			this.yellForLuggageMoverPerson(l);
	    			contentsIn.remove(l);
	    		} else {
				
	    			//get next step in path

	    			if(l.getPath().isEmpty()) {
	    				//if no further path is possible, call a person
	    				l.setPath(calcPath((LuggageRouter) this, (LuggageRouter) l.getDestination(), Luggage.MaximumTime));
	    				if(l.getPath()==null) {
	    					yellForLuggageMoverPerson(l);
	    				}
	    			} else {
	    			AbstractLuggageTransporter t = (AbstractLuggageTransporter) l.getNextStep();
	    			if(t==null||!(t.getState())) {
	    				//if next step is broken, then recalculate path
	    				//if no further path is possible, call a person
	    				l.setPath(calcPath((LuggageRouter) this, (LuggageRouter) l.getDestination(), Luggage.MaximumTime));
	    				if(l.getPath()==null) {
	    					yellForLuggageMoverPerson(l);
	    				}
	    			} else {
	    				//if next step is functional and available, move luggage
	    				if(!t.isFull()) {
	    					t.addLuggage(l);
	    					contentsIn.remove(l);
	    				}
	    				//if next step is full, luggage is not removed from queue and
	    				// will be tried again next timestep
	    			}
	    		}
	    		}
	    			for (Luggage m : contentsIn) {
	    				//change '1' to dt variable from update method
	    				m.incrementTimer(deltaTime);
	    			}
	    	}	
	    }
	}
	
	public void checkInLuggage(Luggage l) {
		l.setPath(calcPath((LuggageRouter) this, (LuggageRouter) l.getDestination(), Luggage.MaximumTime));
		l.setScanned(true);
		this.addLuggageIn(l);
		this.getAirport().getStatistics().addTotalLuggage();

	}

	private BaggageClaim pickBagClaim() {
		//pick baggage claim from those in graph
		ArrayList<BaggageClaim> b = this.getAirport().getBaggageClaims();
		int i = generator.nextInt(b.size());
		return b.get(i);
	}
	
	//select random flight time (time after arrival at gate)
	private double pickFlightTime() {
		return generator.nextInt(200*60);
	}

}
