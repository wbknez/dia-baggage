package unmla.airport.ui.transformers;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

import unmla.airport.LuggageTransporter;
import unmla.airport.simul.SimulationController;
import unmla.airport.ui.UiComponent;
import unmla.airport.ui.UiController;
import unmla.airport.ui.pick.PickData;

/**
 * 
 */
public class TransporterDrawTransformer 
                            implements Transformer<LuggageTransporter, Paint> {

    private final UiController  uiController;
    
    public TransporterDrawTransformer(final SimulationController controller) {
        if(controller == null) {
            throw new NullPointerException();
        }
        
        this.uiController = controller.getUiController();
    }
    
    @Override
    public Paint transform(LuggageTransporter trans) {
        
        final PickData pickData = this.uiController.getPickData();
        
        if(pickData.isPicked(trans)) {
            return UiComponent.Colors.PickedColor;
        }
        
        if(pickData.isMarked(trans)) {
            return UiComponent.Colors.MarkedColor;
        }
        
        if(trans.getState() == false) {
            return UiComponent.Colors.DisabledColor;
        }
        
        if(trans.getCapacity() == 1) {
            return UiComponent.Colors.Capacity01;
        }
        
        final double capacityRatio = trans.getCapacityRatio();
        
        if(Double.compare(capacityRatio, 25.0) <= 0 && 
                Double.compare(capacityRatio, 0.0) > 0) {
            return UiComponent.Colors.Capacity25;
        }
        else if(Double.compare(capacityRatio, 50.0) <= 0 && 
                Double.compare(capacityRatio, 25.0) > 0) {
            return UiComponent.Colors.Capacity50;
        }
        else if(Double.compare(capacityRatio, 75.0) <= 0 && 
                Double.compare(capacityRatio, 50.0) > 0) {
            return UiComponent.Colors.Capacity75;
        }
        else if(Double.compare(capacityRatio, 99.9) <= 0 && 
                Double.compare(capacityRatio, 50.0) > 0) {
            return UiComponent.Colors.Capacity99;
        }
        
        if(trans.isFull()) {
            return UiComponent.Colors.FullColor;
        }
        
        return Color.black;
    }
}
