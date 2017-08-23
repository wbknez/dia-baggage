package unmla.airport.graph.parser;

import org.xml.sax.SAXException;

import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;
import unmla.airport.SecurityScanner;
import unmla.airport.ui.UiComponent;

/**
 * 
 */
public class ScannerCreator implements TypeCreator<LuggageRouter, 
                                            LuggageRouter, LuggageTransporter> {

    @Override
    public LuggageRouter create(
            GraphMLParser<LuggageRouter, LuggageTransporter> parser) 
            throws SAXException {
        if(parser == null) {
            throw new NullPointerException();
        }
        
        final UiComponent scannerUi = new UiComponent();        
        final SecurityScanner scanner = 
                    new SecurityScanner(parser.getObjName(),
                                        parser.getObjId(), 
                                        parser.getObjCapacity(), 
                                        parser.getObjTime());
        
        scannerUi.setLevel(parser.getUiLevel());
        scannerUi.setX(parser.getX());
        scannerUi.setY(parser.getY());
        
        scanner.setUiComponent(scannerUi);
        
        return scanner;
    }
}
