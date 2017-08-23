package unmla.airport.ui.popup;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;

import unmla.airport.simul.SimulationController;
import unmla.airport.ui.actions.PauseSimulationAction;
import unmla.airport.ui.actions.ResetSimulationAction;
import unmla.airport.ui.actions.StartSimulationAction;
import unmla.airport.ui.actions.ViewCarouselLevelAction;
import unmla.airport.ui.actions.ViewCheckInLevelAction;

/**
 * 
 */
public class CommandPopUp extends JPopupMenu {
    
    /**
     * 
     */
    private static final long serialVersionUID = -5018936132416872141L;

    public CommandPopUp(final SimulationController controller) {
        super();
        
        final AbstractAction carouselView = 
                                    new ViewCarouselLevelAction(controller);
        final AbstractAction checkInView = 
                                    new ViewCheckInLevelAction(controller);
        
        carouselView.putValue(Action.NAME, "View Carousels");
        checkInView.putValue(Action.NAME, "View Check-Ins");
        
        this.add(new StartSimulationAction(controller));
        this.add(new PauseSimulationAction(controller));
        this.add(new ResetSimulationAction(controller));
        
        this.addSeparator();
        
        this.add(carouselView);
        this.add(checkInView);
    }
}
