package unmla.airport;

import java.util.PriorityQueue;



public class LuggageMoverPerson {
		
	//Time cost of Luggage Mover Person moving bags around
	private final double transporttime_small = 3;
	private final double transporttime_med = 5;
	private final double transporttime_large = 10;
	
	
	Airport airport = null;

	//TODO: (maybe) have a moveBag(PriorityQueue<Luggage>), which will move all luggages in this queue
	
	LuggageMoverPerson(Airport a){
		this.airport=a;
	}
	
	//Grab bag and find new path
	public void moveBag(Luggage l){
		if(l.getDestination() instanceof BaggageClaim)
			moveBagForward(l);
		else
			moveBagBack(l);
	}
	
	//move all bags in the given queue
	public void moveBag(PriorityQueue<Luggage> q) {
		while(!q.isEmpty()) {
		    moveBag(q.poll());
		}
	}
	
	//Use this when a piece of luggage is going to a gate
	//This will move the piece of luggage back in the path when trying to find a new path
	public void moveBagBack(Luggage l){
		l.incrementTimer(transporttime_small);


		//If bag is in system for more than 15 minutes it needs to be sent to next plane 
		if (l.bagexpired()){
			sendtoLandOfLostBags(l);
		}
		
		//  if bag has moved back in path to first element and can't find a path call luggage mover on segway
		else if(l.getPreviousPath().size() < 1){
			callSegWay(l);
		}		
		//Move bag back to next (non full) LuggageRouter along its path so new path can be assigned to bag there
		else{
			AbstractLuggageTransporter t = (AbstractLuggageTransporter) l.getPreviousStep();
			if(t==null) {
				//Check to see if previous step in path is LuggageRouter, if not go back one more step and it will be
				if(!(t instanceof AbstractLuggageRouter)) {
					t = (AbstractLuggageTransporter) l.getPreviousPath().get(1);
					l.updateStoredPathBackward();
				}
			
				//go back in path until a non-full luggage router object is found
				while(t.isFull() && (l.getPreviousPath().size() > 0) ) {
					//Check to see if previous step in path is LuggageRouter, if not go back one more step and it will be
					if(!(t instanceof AbstractLuggageRouter) && l.getPreviousPath().size() > 1) {
						t = (AbstractLuggageTransporter) l.getPreviousPath().get(1);
						l.updateStoredPathBackward();
					}
					else{
						t = (AbstractLuggageTransporter) l.getPreviousPath().getFirst();
					}
					//update the luggage's path
					l.updateStoredPathBackward();
				}
				
				if (l.getPreviousPath().size() < 1){
					sendtoLandOfLostBags(l);
				} else {
//					l.updateStoredPath();
					t.addLuggage(l);
				}
			}
		}	
	}
	
	//Use this when a piece of luggage is going to a baggage claim
	//This will move the piece of luggage forward in the path instead of backward
	public void moveBagForward(Luggage l){
		l.incrementTimer(transporttime_small);
		boolean passed = false;
		
		//if bag hasn't already been scanned, do so
		if(!(l.getScanned())) {
			//security scan bag
			passed = this.airport.getLuggageMoverPersonScanner().scanStuff(l, airport.getCurrentHLSColor());
		}
		else if(l.getScanned()){
			passed = true; //bag has been previously scanned and passed security scan
		}
		
		//Bag has been scanned and passed security scan
		if(passed){
			//If bag is in system for more than 15 minutes it needs to be sent to lost and found OR
			//  if bag has moved forward in path to 2nd to first element and can't get to bag claim
			if (l.bagexpired() || (l.getPath().size() < 1)){
				sendtoLandOfLostBags(l);
			}
			//Move bag forward to next (non full) LuggageRouter along its path so new path can be assigned to bag there
			else{
				AbstractLuggageTransporter t = (AbstractLuggageTransporter) l.getNextStep();
				if(t==null) {
					this.callSegWay(l);
				} else {
					//Check to see if next step in path is LuggageRouter, if not go forward one more step and it will be
					if(!(t instanceof AbstractLuggageRouter)) {
						t = (AbstractLuggageTransporter) l.getPath().get(1);
						l.updateStoredPath();
					}
					//go back in path until a non-full luggage router object is found		
					while(t.isFull() && (l.getPath().size() > 0) ) {
						//Check to see if previous step in path is LuggageRouter, if not go back one more step and it will be
						if(!(t instanceof AbstractLuggageRouter) && l.getPreviousPath().size() > 1) {
							t = (AbstractLuggageTransporter) l.getPreviousPath().get(1);
							l.updateStoredPath();
						}
						else{
							t = (AbstractLuggageTransporter) l.getPreviousPath().getFirst();
						}
						//update the luggage's path
						l.updateStoredPath();
					}
					if (l.getPath().size() < 1){
						sendtoLandOfLostBags(l);
					} else {
//						l.updateStoredPathBackward();
						t.addLuggage(l);
					}
				}
			}
		}
	}

	
	//Grab bag and send directly to land of lost bags
	public void sendtoLandOfLostBags(Luggage l){
		l.incrementTimer(transporttime_large); //Don't really need this, but it *would* take time to get there
		if(!(l.getScanned())) {
			//security scan bag
			boolean passed = this.airport.getLuggageMoverPersonScanner().scanStuff(l, airport.getCurrentHLSColor());
			//send to destination
			if (passed) {
				airport.getLandOfLostBags().addBag(l);
			}
		}
		//bag has already been scanned
		else{
			airport.getLandOfLostBags().addBag(l);
		}
	}

	//Grab bag and send directly to incinerator in Land of Lost Bags (at light speed, so it doesn't explode)
	public void sendToIncinerator(Luggage l) {	    
		airport.getLandOfLostBags().incinerateBag(l);
	}
	
	//Grab luggage and send directly to destination
	private void sendToEnd(Luggage l){
		l.incrementTimer(transporttime_med);
		AbstractLuggageTransporter t = (AbstractLuggageTransporter) l.getDestination();
		//If destination is full or disabled send to land of lost bags (state: true=node works, false=node is broken)
		if(t.isFull() || !t.getState()){
			sendtoLandOfLostBags(l);
		}
		//Destination is available and not full
		t.addLuggage(l);
	}
	
	//Grab luggage and send to next node in path 
	public void callSegWay(Luggage l){
		l.incrementTimer(transporttime_small);
		if(l.getDestination() instanceof BaggageClaim){
			sendToEnd(l);
		}
		else{
			//if bag hasn't already been scanned, do so
			if(!(l.getScanned())) {
				//security scan bag
				boolean passed = this.airport.getLuggageMoverPersonScanner().scanStuff(l, airport.getCurrentHLSColor());
				//send to destination
				if (passed)
					sendToEnd(l);
			}
			
		}
	}
}
