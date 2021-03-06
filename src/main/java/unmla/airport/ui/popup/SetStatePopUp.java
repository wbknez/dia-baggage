package unmla.airport.ui.popup;

import javax.swing.JPopupMenu;

import unmla.airport.LuggageTransporter;
import unmla.airport.simul.SimulationController;
import unmla.airport.ui.actions.SetStateAction;

/**
 * 
 */
public class SetStatePopUp extends JPopupMenu {

    /**
     * For serialization; generated by Eclipse.
     */
    private static final long serialVersionUID = 2455783762525242655L;
    
    public SetStatePopUp(final SimulationController controller, 
                            final LuggageTransporter target) {
        if(target == null) {
            throw new NullPointerException();
        }
        
        String name = target.getState() ? "Disable" : "Enable";
        name += " " + target.getClass().getSimpleName() + " " + target.getID();
        
        this.add(new SetStateAction(name, controller, target));
    }
}
