package unmla.airport.ui.transformers;

import java.awt.BasicStroke;
import java.awt.Stroke;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.RenderContext;

import unmla.airport.LuggageTransporter;

/**
 * 
 */
public class TransporterStrokeTransformer 
                            implements Transformer<LuggageTransporter, Stroke> {

    @Override
    public Stroke transform(LuggageTransporter trans) {
        if(!trans.getState()) {
            return RenderContext.DASHED;
        }
        
        return new BasicStroke(1);
    }

}
