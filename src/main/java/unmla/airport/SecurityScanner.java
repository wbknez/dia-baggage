package unmla.airport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

import unmla.airport.simul.Simulation;
import unmla.airport.simul.TimeCounter;


public class SecurityScanner extends AbstractLuggageRouter {
	
	//Current list of items that could flag a bag and allow it not to pass
	//To change this list, Airport would need to do a global push to all security scanners to reset
	private ArrayList<String> alertLuggageItems = new  ArrayList<String>(Arrays.asList("knife","gun","wire","handcuffs",
			"metal box","shank","large dense object","medium dense object","small dense object","nunchucks", "box cutter",
			"cattle prod"));
	
	//These items are NEVER allowed, bag should not pass security if it contains any items no matter the security level
	private ArrayList<String> neverPassLuggageItems = new  ArrayList<String>(Arrays.asList("bomb","gasoline","fireworks","gun powder","flare"));
			
	
	public SecurityScanner(final String id, final String uniqueId, 
                        final int defaultCapacity, final double defaultTime) {
	    super(id, uniqueId, defaultCapacity, defaultTime);
	}
	
	public boolean scanStuff(Luggage l, hlsColor color) {
		//check some flag in luggage, 
		//return true if no unsecure items are found
		//else return false and call alertTheAuthorities
		
		boolean check = true;
		
		//if bag was already scanned, don't do anything
		if(!(l.getScanned())) {
			l.setScanned(true);
		
			int alertCounter = 0;
		
			//	If the bag contains any of the items in the list that are NEVER allowed, alert the authorities right away
			//  bag SHALL NOT PASS
			for (String i: l.getLuggageContents()){
				for(String n: neverPassLuggageItems ){
					if (i.equals(n)){
						check = false;
						alertTheAuthorities(l);
						return check;
					}
				}
			}
		
			//Checking to see how many of the alert items are in the luggage
			for(String i: l.getLuggageContents()){
				for(String a: alertLuggageItems){
					if (i.equals(a)){
						alertCounter++;
					}
				}
			}
			
			check = didBagPass(color, alertCounter);
				
		}
		
		//Check will be set to false if it did not pass inspection, authorities need to be notified
		if (!check){
			alertTheAuthorities(l);
			return check;
		}
		
		return check;
	}
	

	private boolean didBagPass(hlsColor color, int alert){
		
		if ( (color == hlsColor.RED) & (alert > 0)){ //1
			return false;
		}
		else if( (color == hlsColor.ORANGE) & (alert > 1)){ //2
			return false;
		}
		else if( (color == hlsColor.YELLOW) & (alert > 2)){ //3
			return false;
		}
		else if ( (color == hlsColor.BLUE) & (alert > 3)){ //4
			return false;
		}
		else if ( (color ==hlsColor.GREEN) & (alert > 4)){ //5
			return false;
		}
		else{
			return true;
		}
		
	}
	
	protected void alertTheAuthorities(Luggage l) {
		//send luggage to Land of Lost Bags
		this.luggageDude.sendToIncinerator(l);
		this.getContents().remove(l);
	}
	
	

	//Used if this list needs to be updated by authorities, each security scanner instance in graph would need to be changed
		//This could be used if they would be scanning different types of pieces of luggage and needed to be on the look out for that
	protected void setAlertLuggageItems(ArrayList<String> alertLuggageItems) {
		this.alertLuggageItems = alertLuggageItems;
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
        
        for(Luggage m : contents) {
            m.incrementTimer(deltaTime);
        }
	    
	    final TimeCounter counter = this.getTimeCounter();	    
    	    
        if (!(contents.isEmpty())) {

            counter.advance(deltaTime);

            if (counter.canStep()) {
                counter.step();

                Luggage l = contents.peek();

                boolean check = this.scanStuff(l, this.getAirport()
                        .getCurrentHLSColor());

                if (check) {
                    // If bag is in system for more than 15 minutes it needs to
                    // be sent to next plane
                    if (l.bagexpired()) {
                        luggageDude.sendtoLandOfLostBags(l);
                        this.getContents().remove(l);
                        this.getAirport().getStatistics().addLateLuggage();
                    }

                    else if (l.isLost((LuggageTransporter) this)) {
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
                                    Luggage.MaximumTime));
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
                        // if next step is full, luggage is not removed from
                        // queue and
                        // will be tried again next timestep
                    }
                }
            }
        } else {
            counter.reset();
        }

        if (!contents.isEmpty()) {
            /*
             * Check the multiplexer.
             */
            final Multiplexer multiplexer = this.getMultiplexer();

            if (!multiplexer.isEmpty()) {
                final LuggageTransporter input = multiplexer.poll();
                final Luggage luggage = input.getContents().poll();

                input.requestAccepted();

                if (luggage != null) {
                    this.addLuggage(luggage);
                }
            }
        }
	}	
}
