package unmla.airport.graph.parser;

import org.xml.sax.SAXException;

import unmla.airport.ConveyerBelt;
import unmla.airport.IdPool;
import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;
import unmla.airport.ui.UiComponent;

/**
 * 
 */
public class ConveyorCreator implements TypeCreator<LuggageTransporter, 
                                            LuggageRouter, LuggageTransporter> {

    private final IdPool    idPool;
    
    public ConveyorCreator() {
        this.idPool = new IdPool();
    }
    
    @Override
    public LuggageTransporter create(
            GraphMLParser<LuggageRouter, LuggageTransporter> parser)
            throws SAXException {
        if(parser == null) {
            throw new NullPointerException();
        }
        
        final double id = this.idPool.getID();
        final UiComponent conveyorUi = new UiComponent();
        final ConveyerBelt conveyor = new ConveyerBelt(Double.toString(id),
                                                        parser.getObjId(), 
                                                        parser.getObjCapacity(), 
                                                        parser.getObjTime());
        
        conveyor.setUiComponent(conveyorUi);
        
        return conveyor;
    }
}
