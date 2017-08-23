package unmla.airport.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;

import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;
import unmla.airport.simul.SimulationController;
import unmla.airport.simul.command.SetStateCommand;
import unmla.airport.ui.UiController;
import unmla.airport.ui.popup.SetStatePopUp;

/**
 * 
 */
public class SetStateAction extends AbstractSimulationAction {

    /**
     * For serialization; generated by Eclipse.
     */
    private static final long serialVersionUID = 1299734915087365179L;
    
    private static final ImageIcon  disableIcon;
    
    private static final ImageIcon  enableIcon;
 
    static {
        disableIcon = new ImageIcon(
                SetStatePopUp.class.getResource("/data/icons/disableSmall.png"), 
                null);
        
        enableIcon = new ImageIcon(
                SetStatePopUp.class.getResource("/data/icons/enableSmall.png"), 
                null);
    }
    
    private final LuggageTransporter    target;
    
    /**
     * @param name
     * @param controller
     */
    public SetStateAction(String name, SimulationController controller,
                            final LuggageTransporter target) {
        super(name, controller);
        
        if(target == null) {
            throw new NullPointerException();
        }
        
        this.target = target;
        
        if(target.getState()) {
            this.putValue(Action.SMALL_ICON, disableIcon);
        }
        else {
            this.putValue(Action.SMALL_ICON, enableIcon);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final SimulationController controller = this.getController();
        final UiController uiController = controller.getUiController();
        final int modifier = (int)(target.getState() ? 1 : -1);
        
        if(controller.isRunning()) {
            controller.submitCommand(new SetStateCommand(controller, target));
        }
        else {
            if(target instanceof LuggageRouter) {
                controller.getSimulation().getStatistics()
                    .addNodesDisabled(modifier);
            }
            else {
                controller.getSimulation().getStatistics()
                            .addEdgesDisabled(modifier);
            }
            
            this.target.setState(!this.target.getState());
            uiController.requestDisplayUpdate();
        }
    }
}
