package unmla.airport.graph.parser;

import org.xml.sax.SAXException;

import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;
import unmla.airport.Switch;
import unmla.airport.ui.UiComponent;

/**
 * 
 */
public class SwitchCreator implements TypeCreator<LuggageRouter, 
                                            LuggageRouter, LuggageTransporter> {

    @Override
    public LuggageRouter create(
            GraphMLParser<LuggageRouter, LuggageTransporter> parser) 
            throws SAXException {
        if(parser == null) {
            throw new NullPointerException();
        }
        
        final UiComponent switchUi = new UiComponent();        
        final Switch switchNode = new Switch(parser.getObjName(),
                                                parser.getObjId(), 
                                                parser.getObjCapacity(), 
                                                parser.getObjTime());
        
        switchUi.setLevel(parser.getUiLevel());
        switchUi.setX(parser.getX());
        switchUi.setY(parser.getY());
        
        switchNode.setUiComponent(switchUi);
        
        return switchNode;
    }
}
