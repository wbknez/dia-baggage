package unmla.airport.ui.transformers;

import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

import unmla.airport.LuggageRouter;
import unmla.airport.simul.SimulationController;
import unmla.airport.ui.UiComponent;
import unmla.airport.ui.UiController;
import unmla.airport.ui.pick.PickData;

/**
 * 
 */
public class RouterFillTransformer implements Transformer<LuggageRouter, 
                                                            Paint> {

    private final UiController  uiController;
    
    public RouterFillTransformer(final SimulationController controller) {
        if(controller == null) {
            throw new NullPointerException();
        }
        
        this.uiController = controller.getUiController();
    }
    
    @Override
    public Paint transform(LuggageRouter router) {        
        final PickData pickData = this.uiController.getPickData();
        
        // then picking
        if(pickData.isPicked(router)) {
            return UiComponent.Colors.PickedColor;
        }        
        // then marked along a luggage's route
        if(pickData.isMarked(router)) {
            return UiComponent.Colors.MarkedColor;
        }
        if(router.getState() == false) {
            return UiComponent.Colors.DisabledColor;
        }
        
        if(router.getCapacity() == 1) {
            return UiComponent.Colors.Capacity01;
        }
        
        final double capacityRatio = router.getCapacityRatio();
        
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
        
        // then whether or not it is full
        if(router.isFull()) {
            return UiComponent.Colors.FullColor;
        }
        
        return UiComponent.Colors.WorkingColor;
    }
}
