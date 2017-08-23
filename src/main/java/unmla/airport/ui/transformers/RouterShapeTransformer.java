package unmla.airport.ui.transformers;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import org.apache.commons.collections15.Transformer;

import unmla.airport.BaggageClaim;
import unmla.airport.CheckIn;
import unmla.airport.Gate;
import unmla.airport.LuggageRouter;
import unmla.airport.SecurityScanner;

/**
 * 
 */
public class RouterShapeTransformer implements Transformer<LuggageRouter, Shape> {
    
    @Override
    public Shape transform(LuggageRouter router) {
        if(router instanceof BaggageClaim || 
                router instanceof CheckIn || 
                router instanceof Gate) {
            return new Rectangle2D.Float(-10, -10, 20, 20);
        }
        else if(router instanceof SecurityScanner) {
            return this.createTriangle();
        }
        
        return new Ellipse2D.Float(-10, -10, 20, 20);
    }
    
    private Shape createTriangle() {
        final Polygon triangle = new Polygon();
        
        triangle.addPoint(-10, -10);
        triangle.addPoint(5, 20);
        triangle.addPoint(20, -10);
        
        return triangle;
    }
}
