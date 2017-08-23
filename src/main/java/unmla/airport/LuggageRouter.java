package unmla.airport;

import java.util.LinkedList;

/**
 * 
 * @author redacted
 * all graph vertices except ConveyerBelts will be LuggageRouters
 */
public interface LuggageRouter extends LuggageTransporter {
	public LinkedList<LuggageTransporter> calcPath(LuggageRouter s, LuggageRouter d, final double maxDistance);
	double calcDistance(LuggageRouter s, LuggageRouter t);
	public Multiplexer getMultiplexer();	
	
}
