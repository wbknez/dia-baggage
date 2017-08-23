package unmla.airport;

import java.util.Comparator;
import java.util.PriorityQueue;

import unmla.airport.simul.Simulation;
import unmla.airport.simul.TimeCounter;
import unmla.airport.ui.UiComponent;

public abstract class AbstractLuggageTransporter implements LuggageTransporter {

	private Airport                airport = null;
	
	private UiComponent            uiComponent;
	private String id;  // id for specific instance
	private TimeCounter            counter;
	private boolean                state; // true=node works, false=node is broken
	
	private final String           uniqueId;
	private final int              maxCapacity;
	private final double           defaultTime;
	
	private PriorityQueue<Luggage> contents;
	
	public LuggageMoverPerson luggageDude;

	private boolean                waitingOnRequest;
	
    public AbstractLuggageTransporter(final String id, final String uniqueId,
            final int maxCapacity, final double defaultTime) {
        this(id, uniqueId, maxCapacity, defaultTime, new LuggageComparator());
    }
    
    public AbstractLuggageTransporter(final String id, final String uniqueId,
            final int maxCapacity, final double defaultTime, 
            final Comparator<Luggage> comp) {
        if (id == null) {
            throw new NullPointerException();
        }

        if (uniqueId == null) {
            throw new NullPointerException();
        }

        if (Double.compare(maxCapacity, 0d) <= 0) {
            throw new IllegalArgumentException("Maximum size of Priority "
                    + "Queue must be greater than 0!");
        }

        if (Double.compare(defaultTime, 0d) <= 0) {
            throw new IllegalArgumentException("A time-step of less than zero "
                    + "doesn't even make sense!");
        }
        
        if(comp == null) {
            throw new NullPointerException();
        }
        
        this.maxCapacity = maxCapacity;
        this.defaultTime = defaultTime;
        this.id = id;
        this.uniqueId = uniqueId;

        this.counter = new TimeCounter(this.defaultTime);

        this.contents = new PriorityQueue<Luggage>(maxCapacity, comp);

        this.setState(true);
        this.waitingOnRequest = false;
    }
	
	public int getCapacity() {
		return this.contents.size();
	}
	
	public int getMaxCapacity() {
		return maxCapacity;
	}
	
	public boolean isFull() {
		return (maxCapacity <= this.contents.size());
	}
	
	@Override
	public boolean equals(final Object obj) {
	    if(obj == this) {
	        return true;
	    }
	    
	    if(!(obj instanceof LuggageTransporter)) {
	        return false;
	    }
	    
	    final LuggageTransporter trans = (LuggageTransporter)obj;
	    
	    if(!this.id.equals(trans.getID())) {
	        return false;
	    }
	    if(!this.uniqueId.equals(trans.getUniqueID())) {
	        return false;
	    }
	    if(!this.counter.equals(trans.getTimeCounter())) {
	        return false;
	    }
	    if(this.state == trans.getState()) {
	        return false;
	    }
	    if(Double.compare(this.defaultTime, trans.getDefaultTime()) != 0) {
	        return false;
	    }
	    if(this.maxCapacity != trans.getMaxCapacity()) {
	        return false;
	    }
	    if(!this.uiComponent.equals(trans.getUiComponent())) {
	        return false;
	    }
	    if(this.contents.equals(trans.getContents())) {
	        return false;
	    }
	    
	    return true;
	}
	
	public void addLuggage(Luggage l) {
	    l.clearTimeOnConveyor();
		if(this.isFull()) {
			this.yellForLuggageMoverPerson(l);
			//this case shouldn't happen
		} else {
			l.updateStoredPath(this);
			if((this instanceof Gate)&&(l.getDestination() instanceof BaggageClaim)) {
				Gate g = (Gate) this;
				g.addLuggageIn(l);
			} else {
			this.contents.offer(l);
			}
		}
	}
			
	public void yellForLuggageMoverPerson(Luggage l) {
		luggageDude.moveBag(l);
		this.contents.remove(l);
	}

	public PriorityQueue<Luggage> getContents() {
	    return this.contents;
	}
	
	public double getDefaultTime() {
	    return this.defaultTime;
	}
	
	public double getTime() {
		return this.counter.getTimeStep();
	}

	public void setTime(double t) {
		this.counter.setTimeStep(t);
	}
	
	public boolean getState() {
		return state;
	}

	public void setState(boolean s) {	    
		state=s;
		if(s) {
			this.fixLink(); 
		} else {
			this.breakLink();
		}
	}

	public String getID() {
		return id;
	}

	public void setID(String i) {
		id=i;
	}
	
	public String getUniqueID() {
	    return this.uniqueId;
	}
	
	//break current link
	public void breakLink() {
	    this.counter.setTimeStep(Double.POSITIVE_INFINITY);
		this.state=false;
		this.luggageDude.moveBag(contents);
		this.contents.removeAll(contents);
	}

	//fix current link
	public void fixLink() {
		this.counter.setTimeStep(defaultTime);
		this.state=true;
		}
	
	@Override
	public TimeCounter getTimeCounter() {
	    return this.counter;
	}
	
	@Override
	public void step(final double deltaTime, final Simulation simulation) {
	    if(Double.compare(deltaTime, 0) <= 0) {
	        throw new IllegalArgumentException("The delta time should be > 0!");
	    }
	    
	    if(simulation == null) {
	        throw new NullPointerException();
	    }
	    
        for (Luggage m : contents) {
            m.incrementTimer(deltaTime);
            m.incrementTimeOnConveyor(deltaTime);
        }
	    
        if(this.isWaitingOnRequest()) {
            return;
        }
        
	    if(!(contents.isEmpty())) {
	    	Luggage l = contents.peek();

	    	if(Double.compare(l.getTimeOnConveyor(), this.getTime()) < 0) {
	    	    return;
	    	}
	    	
			//If bag is in system for more than 15 minutes it needs to be sent to next plane 
			if (l.bagexpired()){
				luggageDude.sendtoLandOfLostBags(l);
				this.contents.remove(l);
				this.getAirport().getStatistics().addLateLuggage();
			}
	    	
			else if(l.isLost((LuggageTransporter) this)) {
	    		luggageDude.callSegWay(l);
	    		this.contents.remove(l);
	    		this.getAirport().getStatistics().addLostLuggage();
	    	} else {
		
	    		//if current node is broken, then call luggage mover person
	    		if(!(this.getState())) {
	    			this.yellForLuggageMoverPerson(l);
	    			contents.remove(l);
	    		} else {
	    
	    			//get next step in path
	    			AbstractLuggageTransporter t = (AbstractLuggageTransporter) l.getNextStep();
	    			if(t==null||!(t.getState())) {
	    				//if next step is broken, then move luggage elsewhere
	    				yellForLuggageMoverPerson(l);
	    		
	    			} else {
	    				//if next step is functional and available, move luggage
	    				if(!t.isFull()) {
	    					t.addLuggage(l);
	    					contents.remove(l);
	    				}
	    				else {
	    				    final LuggageRouter router = (LuggageRouter)t;
	    				    final Multiplexer multiplexer = router.getMultiplexer();
	    				    
	    				    if(!multiplexer.contains(this)) {
	    				        multiplexer.offer(this);
	    				    }
	    				    this.requestSubmitted();
	    				}
	    			}
	    		}
	    	}
	    }
	}
	
	@Override
	public void initialize(final Simulation simulation) {
	    this.airport = simulation.getAirport();
	    this.luggageDude = new LuggageMoverPerson(this.airport);
	}
	
	@Override
	public void reset(final Simulation simulation) {
	    this.getContents().clear();
	    this.setState(true);
	    this.setTime(this.defaultTime);
	}
	
	@Override
	public UiComponent getUiComponent() {
	    return this.uiComponent;
	}
	
	@Override
	public void setUiComponent(final UiComponent uiComponent) {
	    if(uiComponent == null) {
	        throw new NullPointerException();
	    }
	    
	    this.uiComponent = uiComponent;
	}
	
	public Airport getAirport() {
	    return this.airport;
	}
	
	@Override
	public double getCapacityRatio() {
	    final double ratio = 
	            ((double)this.getCapacity() / (double)this.getMaxCapacity()) * 100.0;
	    return ratio;
	}
	
	@Override
	public boolean isWaitingOnRequest() {
	    return this.waitingOnRequest;
	}
	
	@Override
	public void requestAccepted() {
	    this.waitingOnRequest = false;
	}
	
	@Override
	public void requestSubmitted() {
	    this.waitingOnRequest = true;
	}
}
