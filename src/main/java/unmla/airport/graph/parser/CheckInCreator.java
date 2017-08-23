package unmla.airport.graph.parser;

import org.xml.sax.SAXException;

import unmla.airport.CheckIn;
import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;
import unmla.airport.ui.UiComponent;

/**
 * 
 */
public class CheckInCreator implements TypeCreator<LuggageRouter, 
                                            LuggageRouter, LuggageTransporter> {

    @Override
    public LuggageRouter create(
            GraphMLParser<LuggageRouter, LuggageTransporter> parser) 
            throws SAXException {
        if(parser == null) {
                throw new NullPointerException();
        }
        
        final UiComponent checkInUi = new UiComponent();        
        final CheckIn checkIn = new CheckIn(parser.getObjName(),
                                            parser.getObjId(), 
                                            parser.getObjCapacity(), 
                                            parser.getObjTime(),
                                            CheckIn.DefaultCheckInRate);
        
        checkInUi.setLevel(parser.getUiLevel());
        checkInUi.setX(parser.getX());
        checkInUi.setY(parser.getY());
        
        checkIn.setUiComponent(checkInUi);
        
        return checkIn;
    }
}
