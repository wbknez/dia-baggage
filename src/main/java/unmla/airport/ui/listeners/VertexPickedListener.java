package unmla.airport.ui.listeners;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import unmla.airport.LuggageTransporter;
import unmla.airport.simul.SimulationController;
import unmla.airport.ui.UiController;
import unmla.airport.ui.pick.PickData;

/**
 * 
 */
public class VertexPickedListener implements ItemListener {

    private final UiController  uiController;
    
    public VertexPickedListener(final SimulationController controller) {
        if(controller == null) {
            throw new NullPointerException();
        }
        
        this.uiController = controller.getUiController();
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) {
        final PickData pickData = this.uiController.getPickData();
        pickData.setPicked((LuggageTransporter)e.getItem());
    }

}
