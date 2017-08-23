package unmla.airport.graph.parser;

import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;

/**
 * 
 */
public class AirportParser extends GraphMLParser<LuggageRouter, 
                                                LuggageTransporter> {

    public AirportParser() {
        this.addEdgeCreator("ConveyorBelt", new ConveyorCreator());
        
        this.addVertexCreator("BaggageClaim", new BaggageClaimCreator());
        this.addVertexCreator("CheckIn", new CheckInCreator());
        this.addVertexCreator("Gate",new GateCreator());
        this.addVertexCreator("Scanner", new ScannerCreator());
        this.addVertexCreator("Switch", new SwitchCreator());
    }
}
