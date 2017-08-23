package unmla.airport.ui.transformers;

import java.awt.geom.Point2D;

import org.apache.commons.collections15.Transformer;

import unmla.airport.LuggageRouter;
import unmla.airport.ui.UiComponent;

/**
 * 
 */
public class PositionTransformer 
                            implements Transformer<LuggageRouter, Point2D> {
    
    /**
     * Constructor.
     */
    public PositionTransformer() {
    }
    
    @Override
    public Point2D transform(LuggageRouter router) {
        if(router == null) {
            throw new NullPointerException();
        }
        
        final UiComponent uiComponent = router.getUiComponent();
        final Point2D position = new Point2D.Double(uiComponent.getX(), 
                                                    uiComponent.getY());
        
        return position;
    }

}
