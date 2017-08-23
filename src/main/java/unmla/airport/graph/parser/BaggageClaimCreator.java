package unmla.airport.graph.parser;

import org.xml.sax.SAXException;

import unmla.airport.BaggageClaim;
import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;
import unmla.airport.ui.UiComponent;

/**
 * 
 */
public class BaggageClaimCreator implements TypeCreator<LuggageRouter, 
                                            LuggageRouter, LuggageTransporter> {
    
    @Override
    public LuggageRouter create(
            GraphMLParser<LuggageRouter, LuggageTransporter> parser) 
            throws SAXException {
        if(parser == null) {
                throw new NullPointerException();
        }
        
        final UiComponent baggageClaimUi = new UiComponent();        
        final BaggageClaim baggageClaim = new BaggageClaim(parser.getObjName(),
                                                        parser.getObjId(), 
                                                        parser.getObjCapacity(), 
                                                        parser.getObjTime());
        
        baggageClaimUi.setLevel(parser.getUiLevel());
        baggageClaimUi.setX(parser.getX());
        baggageClaimUi.setY(parser.getY());
        
        baggageClaim.setUiComponent(baggageClaimUi);
        
        return baggageClaim;
    }
}
