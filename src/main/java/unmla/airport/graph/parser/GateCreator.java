package unmla.airport.graph.parser;

import org.xml.sax.SAXException;

import unmla.airport.Gate;
import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;
import unmla.airport.ui.UiComponent;

/**
 * 
 */
public class GateCreator implements TypeCreator<LuggageRouter, 
                                            LuggageRouter, LuggageTransporter> {

    @Override
    public LuggageRouter create(
            GraphMLParser<LuggageRouter, LuggageTransporter> parser)
            throws SAXException {
        if(parser == null) {
            throw new NullPointerException();
        }
        
        final UiComponent gateUi = new UiComponent();        
        final Gate gate = new Gate(parser.getObjName(),
                                    parser.getObjId(), 
                                    parser.getObjCapacity(), 
                                    parser.getObjTime(),
                                    Gate.DefaultArrivalRate);
        
        gateUi.setLevel(UiComponent.CoreLevel);
        gateUi.setX(parser.getX());
        gateUi.setY(parser.getY());
        
        gate.setUiComponent(gateUi);
        
        return gate;
    }
}
