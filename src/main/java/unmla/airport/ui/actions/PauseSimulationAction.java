package unmla.airport.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import unmla.airport.simul.SimulationController;

/**
 * 
 */
public class PauseSimulationAction extends AbstractSimulationAction {
    
    /**
     * 
     */
    private static final long serialVersionUID = -2079415372962829280L;

    /**
     * @param controller
     */
    public PauseSimulationAction(SimulationController controller) {
        super("Pause", controller);
        
        /*
        this.setEnabled(true);
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                        KeyEvent.VK_W, 
                        KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
        this.putValue(Action.LARGE_ICON_KEY, 
                        this.loadIcon("/icons/closedoc.png", null));
        //this.putValue(Action.MNEMONIC_KEY, "o");
        this.putValue(Action.SHORT_DESCRIPTION, "Close all documents.");
        */
        this.putValue(Action.LARGE_ICON_KEY, 
                this.loadIcon("/data/icons/pause.png", null));
        this.putValue(Action.SHORT_DESCRIPTION, "Pause the simulation.");
        this.putValue(Action.SMALL_ICON, 
                this.loadIcon("/data/icons/pauseSmall.png", null));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final SimulationController controller = this.getController();
        
        if(controller.isRunning()) {
            controller.stop();
            
            controller.getUiController()
                    .getSettingsTabPane()
                    .getOptionsPanel()
                    .setEnabled(true);
        }
    }
}
