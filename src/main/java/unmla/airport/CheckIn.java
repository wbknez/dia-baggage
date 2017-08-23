package unmla.airport;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

import unmla.airport.simul.Simulation;

public class CheckIn extends AbstractLuggageRouter {
    
    public static final int DefaultCheckInRate  = 5;

	public CheckIn(final String id, final String uniqueId, 
                        final int maxCapacity, final double defaultTime, int checkinRate) {
		super(id, uniqueId, maxCapacity, defaultTime);
//checkinRate = % of time to create luggage *10
		this.checkinRate=checkinRate;
	}
	
	private int checkinRate;
	
	private Random generator= new Random(System.currentTimeMillis());
		
	public void setCheckinRate(int r) {
		this.checkinRate = r;
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

	    AbstractLuggageTransporter[] edges=new AbstractLuggageTransporter[1];
	    this.getAirport().getGraph().getOutEdges(this).toArray(edges);
	    AbstractLuggageTransporter cb = edges[0]; 
	    //if current CheckIn isn't full or disabled, and the next conveyer belt
	    // also isn't full or disabled, consider creating luggage
	    if(this.getState()&&(!this.isFull())&&cb.getState()&&(!cb.isFull())) {
	    	//use rng to decide if a new luggage is created
	    	if((generator.nextInt(10)+1)<checkinRate) {
	    		Luggage l = new Luggage(this.getAirport().getIdPool().getID(),this.pickGate(),this.pickFlightTime());
	    		this.checkInLuggage(l,l.getDestination());
	    	}
	    }
	    
	    if(!(contents.isEmpty())) {
	    
	    	//move top luggage in queue
	    	Luggage l = contents.peek();

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
	    			contents.remove(l);
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
	    					contents.remove(l);
	    				}
	    				//if next step is full, luggage is not removed from queue and
	    				// will be tried again next timestep
	    			}
	    		}
	    		for (Luggage m : contents) {
	    			//change '1' to dt variable from update method
	    			m.incrementTimer(deltaTime);
	    		}
	    	}	
	    }
		
	}
	
	public void checkInLuggage(Luggage l, LuggageRouter t) {
		if((l.getFlightTime()-l.getTimer())>Luggage.MaximumTime) {
			l.setPath(calcPath((LuggageRouter) this, (LuggageRouter) l.getDestination(), Luggage.MaximumTime));
	         this.addLuggage(l);
			this.getAirport().getStatistics().addTotalLuggage();
		} else {
			//if flight is <15 minutes from now, reject luggage
			rejectPassenger(l);
		}
	}

	private Gate pickGate() {
		//pick gate from those in graph
		ArrayList<Gate> g = this.getAirport().getGates();
		int i = generator.nextInt(g.size());
		return g.get(i);
	}
		
	//select random flight time (time after checkin)
	private double pickFlightTime() {
		return generator.nextInt(200*60);
	}
	
	private void rejectPassenger(Luggage l) {
		//reject luggage
		this.getAirport().getStatistics().addRejectedLuggage();
		this.getAirport().removeLuggage(l);

	}
}
