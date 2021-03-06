package unmla.airport.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import unmla.airport.simul.SimulationController;
import unmla.airport.ui.UiComponent;
import unmla.airport.ui.UiController;
import unmla.airport.ui.pick.PickData;

/**
 * 
 */
public class ViewCarouselLevelAction extends AbstractSimulationAction {

    /**
     * For serialization; generated by Eclipse.
     */
    private static final long serialVersionUID = 3239759492946746730L;

    public ViewCarouselLevelAction(final SimulationController controller) {
        super("Carousel Level", controller);
        
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                KeyEvent.VK_B, 
                KeyEvent.CTRL_MASK));
        this.putValue(Action.LARGE_ICON_KEY, 
                this.loadIcon("/data/icons/baggage.png", null));
        this.putValue(Action.SHORT_DESCRIPTION, "View the carousel terminal.");
        this.putValue(Action.SMALL_ICON, 
                this.loadIcon("/data/icons/baggageSmall.png", null));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final UiController uiController = 
                this.getController().getUiController();
        final PickData pickData = uiController.getPickData();
        
        if(pickData.getUiLevel() != UiComponent.IncomingLevel) {
            pickData.setUiLevel(UiComponent.IncomingLevel);
            uiController.getGraphPanel().getViewer().repaint();
        }
    }
}
