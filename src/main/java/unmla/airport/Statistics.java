package unmla.airport;

import java.util.ArrayList;

import unmla.airport.ui.listeners.StatisticsListener;

public class Statistics {
	
	//incremented when luggage is added to incinerator in land of lost bags (when it fails security)
	private int luggagesIncinerated;
	//incremented when luggage is added to the lost and found in land of lost bags
	private int luggagesToLostAndFound;
	//incremented when luggage is added to next plane queue in land of lost bags
	private int luggagesSentToNextPlane;
	//incremented when luggage is rejected at check in because it doesn't have 15 minutes before its flight
	private int luggagesRejected;
	//incremented when luggage is created at check in or at gates,
	//decremented whenever luggage reaches its destination, gets lost or is incinerated
	private int totalLuggages;
	//incremented with Luggage.isLost is true
	private int luggagesLost;
	//incremented when a luggage has a path > 15 minutes at any point after creation
	private int lateLuggages;
	//incremented when a link is broken, decremented when a link is fixed
	private int brokenLinks;
	//incremented when a luggage reaches a gate
	private int madeItToGate;
	//incremented when a luggage reaches a baggage claim
	private int madeItToBagClaim;
	
	
	private int    edgesDisabled;
	private int    nodesDisabled;
	private int    totalEdges;
	private int    totalNodes;
	
	private boolean    updateNeeded;
	
	private final ArrayList<StatisticsListener>    listeners;
	
	public Statistics() {
	    this.listeners = new ArrayList<StatisticsListener>(1);
	    this.updateNeeded = false;
	}
	
	public void addListener(final StatisticsListener listener) {
	    if(listener != null) {
	        this.listeners.add(listener);
	    }
	}
	
	public void removeListener(final StatisticsListener listener) {
	    if(listener != null) {
	        this.listeners.remove(listener);
	    }
	}
	
	public int getLuggagesIncinerated() {
		return luggagesIncinerated;
	}
	public void addIncineratedLuggage() {
		this.luggagesIncinerated++;
		this.updateNeeded = true;
	}
	public int getLuggagesToLostAndFound() {
		return luggagesToLostAndFound;
	}
	public void addLostAndFoundLuggage() {
		this.luggagesToLostAndFound++;
		this.updateNeeded = true;
	}
	public int getLuggagesSentToNextPlane() {
		return luggagesSentToNextPlane;
	}
	public void addSentToNextPlaneLuggage() {
		this.luggagesSentToNextPlane++;
		this.updateNeeded = true;
	}
	public int getLuggagesRejected() {
		return luggagesRejected;
	}
	public void addRejectedLuggage() {
		this.luggagesRejected++;
		this.updateNeeded = true;
	}
	public int getTotalLuggages() {
		return totalLuggages;
	}
	public void addTotalLuggage() {
		this.totalLuggages++;
		this.updateNeeded = true;
	}
	public void removeTotalLuggage() {
		this.totalLuggages--;
		this.updateNeeded = true;
	}
	public int getLuggagesLost() {
		return luggagesLost;
	}
	public void addLostLuggage() {
		this.luggagesLost++;
	}
	public int getLateLuggages() {
		return lateLuggages;
	}
	public void addLateLuggage() {
		this.lateLuggages++;
		this.updateNeeded = true;
	}
	public int getBrokenLinks() {
		return brokenLinks;
	}
	public void addBrokenLink() {
		this.brokenLinks++;
	}
	public void removeBrokenLink() {
		this.brokenLinks--;
	}

	public int getEdgesDisabled() {
	    return this.edgesDisabled;
	}
	
	public void addEdgesDisabled(int amount) {
	    this.edgesDisabled += amount;
	    this.updateNeeded = true;
	}
	
	public int getNodesDisabled() {
	    return this.nodesDisabled;
	}
	
	public void addNodesDisabled(int amount) {
        this.nodesDisabled += amount;
        this.updateNeeded = true;
    }
    
    public void setTotalEdges(int total) {
        this.totalEdges = total;
        this.updateNeeded = true;
    }
    
    public int getTotalEdges() {
        return this.totalEdges;
    }
    
    public void setTotalNodes(int total) {
        this.totalNodes = total;
        this.updateNeeded = true;
    }
    
    public int getTotalNodes() {
        return this.totalNodes;
    }
    
    public boolean isUpdateNeeded() {
        return this.updateNeeded;
    }
    
    public void notifyListeners() {
        if(this.updateNeeded) {
            this.updateNeeded = false;
            
            for(int i = 0; i < this.listeners.size(); i++) {
                final StatisticsListener listener = this.listeners.get(i);
                listener.statisticsUpdated(this);
            }
        }
    }

	public int getMadeItToGate() {
		return madeItToGate;
	}

	public void addMadeItToGate() {
		this.madeItToGate++;
        this.updateNeeded = true;
	}

	public int getMadeItToBagClaim() {
		return madeItToBagClaim;
	}

	public void addMadeItToBagClaim() {
		this.madeItToBagClaim++;
        this.updateNeeded = true;
	}
}
