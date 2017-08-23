package unmla.airport.ui.pick;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import unmla.airport.Luggage;
import unmla.airport.LuggageTransporter;
import unmla.airport.ui.UiComponent;

/**
 * 
 */
public class PickData {

    private final ArrayList<PickListener>       listeners;
    
    private Luggage                             luggage;
    
    private final Set<LuggageTransporter>       markCache;
    
    private LuggageTransporter                  picked;
    
    private int                                 uiLevel;
    
    /**
     * Constructor.
     */
    public PickData() {
        this.listeners = new ArrayList<PickListener>();
        this.luggage = null;
        this.markCache = new HashSet<LuggageTransporter>();
        this.picked = null;
        this.uiLevel = UiComponent.OutgoingLevel;
    }
    
    public void addListener(final PickListener listener) {
        if(listener == null) {
            throw new NullPointerException();
        }
        
        if(!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }
    
    public LuggageTransporter getPicked() {
        return this.picked;
    }
    
    public int getUiLevel() {
        return this.uiLevel;
    }
    
    public void prepareCache() {
        this.markCache.clear();
        
        if(this.luggage != null) {
            this.markCache.addAll(this.luggage.getPreviousPath());
            this.markCache.addAll(this.luggage.getPath());
        }
    }
    
    public boolean isMarked(final LuggageTransporter obj) {
        if(this.luggage == null || this.markCache.isEmpty()) {
            return false;
        }
        
        return this.markCache.contains(obj);        
    }
    
    public boolean isPicked(final LuggageTransporter obj) {
        if(this.picked == null) {
            return false;
        }

        return picked.equals(obj);
    }
    
    public void setFollowedLuggage(final Luggage luggage) {
        this.luggage = luggage;
    }
    
    public void setPicked(final LuggageTransporter picked) {        
        // use reference equality to avoid false updates
        if(picked != this.picked) {
            this.picked = picked;
            
            if(picked == null) {
                if(this.luggage != null) {
                    this.luggage = null;
                }
            }
            
            for(int i = 0; i < this.listeners.size(); i++) {
                final PickListener listener = this.listeners.get(i);
                listener.onPick(this);
            }
        }
    }
    
    public void setUiLevel(final int uiLevel) {
        this.uiLevel = uiLevel;
    }
}
