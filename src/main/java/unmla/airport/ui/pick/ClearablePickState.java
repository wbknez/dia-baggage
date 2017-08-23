package unmla.airport.ui.pick;

import java.awt.event.ItemEvent;

import edu.uci.ics.jung.visualization.picking.MultiPickedState;

/**
 * 
 */
public class ClearablePickState<T> extends MultiPickedState<T> {

    /**
     * Constructor.
     */
    public ClearablePickState() {
        super();
    }
    
    @Override
    public void clear() {
        super.clear();
        
        this.fireItemStateChanged(new ItemEvent(this, 
                        ItemEvent.ITEM_STATE_CHANGED,
                        null, ItemEvent.DESELECTED));
    }
}
