package unmla.airport;

public class LandOfLostBags {
	
	public LandOfLostBags(Airport airport){
		this.residentCount=0;
		this.airport = airport;
	}

	private int residentCount;
	
	private int lostAndFoundBags;
	
	private int waitingForPlaneBags;

	private Airport airport;	
	
	public void sendToPlane(){
		this.residentCount=this.residentCount-this.waitingForPlaneBags;
		this.waitingForPlaneBags = 0;
	}
	
	public void pickUpLuggage() {
		this.residentCount = this.residentCount-this.lostAndFoundBags;
		this.lostAndFoundBags = 0;
	}
	
	public void incinerateBag(Luggage l) {
		this.airport.removeLuggage(l);
		// this adds to incinerator
		this.airport.getStatistics().addIncineratedLuggage();
	}
	
	public void addBag(Luggage l) {
	
		if(l.getDestination() instanceof Gate) {
			// this adds to next-plane bags (for luggage going to a gate)
			this.airport.getStatistics().addSentToNextPlaneLuggage();
		}
		if(l.getDestination() instanceof BaggageClaim) {
			// this adds to lost and found (for luggage that's going to a baggage claim)
			this.airport.getStatistics().addLostAndFoundLuggage();			
		}
		this.airport.removeLuggage(l);
		
		residentCount++;
	}
}
