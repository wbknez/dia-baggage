package unmla.airport.ui.transformers;

import java.awt.Color;
import java.awt.Paint;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections15.Transformer;

import unmla.airport.BaggageClaim;
import unmla.airport.CheckIn;
import unmla.airport.Gate;
import unmla.airport.LuggageRouter;
import unmla.airport.SecurityScanner;
import unmla.airport.Switch;
import unmla.airport.ui.UiComponent;

/**
 * 
 */
public class RouterDrawTransformer 
                                implements Transformer<LuggageRouter, Paint> {

    private final Map<Class<?>, Color>  colorMap;
    
    public RouterDrawTransformer() {
        this.colorMap = new HashMap<Class<?>, Color>();
        
        this.colorMap.put(BaggageClaim.class, 
                UiComponent.Colors.CarouselBorderColor);
        this.colorMap.put(CheckIn.class, UiComponent.Colors.CheckInBorderColor);
        this.colorMap.put(Gate.class, UiComponent.Colors.GateBorderColor);
        this.colorMap.put(SecurityScanner.class, 
                UiComponent.Colors.ScannerBorderColor);
        this.colorMap.put(Switch.class, UiComponent.Colors.SwitchBorderColor);
    }
    
    @Override
    public Paint transform(LuggageRouter router) {
        return this.colorMap.get(router.getClass());
    }

}
