package unmla.airport.ui.transformers;

import edu.uci.ics.jung.visualization.RenderContext;
import org.apache.commons.collections15.Transformer;
import unmla.airport.LuggageRouter;

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 * 
 */
public class RouterStrokeTransformer 
                                implements Transformer<LuggageRouter, Stroke> {

    @Override
    public Stroke transform(LuggageRouter router) {
        if(!router.getState()) {
            return RenderContext.DASHED;
        }
        
        return new BasicStroke(1);
    }

}
