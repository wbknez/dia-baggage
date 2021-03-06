package unmla.airport.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import unmla.airport.simul.SimulationController;
import unmla.airport.ui.UiController;
import unmla.airport.ui.panels.GraphPanel;

/**
 * 
 */
public class ZoomInAction extends AbstractSimulationAction {

    /**
     * For serialization; generated by Eclipse.
     */
    private static final long serialVersionUID = -6589505530513224028L;
    
    public ZoomInAction(final SimulationController controller) {
        super("Zoom In", controller);
        
        if(controller == null) {
            throw new NullPointerException();
        }
        
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                KeyEvent.VK_MINUS, 
                KeyEvent.CTRL_MASK));
        this.putValue(Action.LARGE_ICON_KEY, 
                this.loadIcon("/data/icons/zoom_in.png", null));
        this.putValue(Action.SHORT_DESCRIPTION, "Zoom in.");
        this.putValue(Action.SMALL_ICON, 
                this.loadIcon("/data/icons/zoom_inSmall.png", null));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        final UiController uiController = 
                                this.getController().getUiController();
        final GraphPanel graphPanel = uiController.getGraphPanel();
        graphPanel.scaleView(2.0f);
    }

}
