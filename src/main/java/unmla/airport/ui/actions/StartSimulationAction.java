package unmla.airport.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import unmla.airport.simul.SimulationController;

/**
 * 
 */
public class StartSimulationAction extends AbstractSimulationAction {

    /**
     * For serialization; generated by Eclipse.
     */
    private static final long serialVersionUID = 7392810236767925228L;

    /**
     * @param controller
     */
    public StartSimulationAction(SimulationController controller) {
        super("Start", controller);
        
        this.putValue(Action.LARGE_ICON_KEY, 
                this.loadIcon("/data/icons/start.png", null));
        this.putValue(Action.SHORT_DESCRIPTION, "Start the simulation.");
        this.putValue(Action.SMALL_ICON, 
                this.loadIcon("/data/icons/startSmall.png", null));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final SimulationController controller = this.getController();
        
        if(!controller.isRunning()) {
            controller.getUiController()
                        .getSettingsTabPane()
                        .getOptionsPanel()
                        .setEnabled(false);
            
            controller.start();
            
            synchronized(controller) {
                controller.notify();
            }
        }
    }

}