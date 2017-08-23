package unmla.airport.graph;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;

import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;

/**
 * Used by JUNG to extract a {@link LuggageTransporter}'s time value for use 
 * as an edge weight.
 * 
 * @see {org.apache.commons.collections15.Transformer}
 */
public class WeightedEdgeTransformer 
                        implements Transformer<LuggageTransporter, Double> {
    
    /** The graph to use for extracting a destination vertex. */
    private final Graph<LuggageRouter, LuggageTransporter>  graph;
    
    /**
     * 
     * @param graph
     * @throws NullPointerException 
     */
    public WeightedEdgeTransformer(final Graph<LuggageRouter, 
                                LuggageTransporter> graph) {
        if(graph == null) {
            throw new NullPointerException();
        }
        
        this.graph = graph;
    }
    
    @Override
    public Double transform(LuggageTransporter trans) {
        final LuggageRouter router = this.graph.getDest(trans);
        
        
        if(trans.isFull() || router.isFull()) {
            return Double.POSITIVE_INFINITY;
        }
        
        
        return trans.getTime() + (router.getTime() * router.getCapacity());
        
        //return trans.getTime() + router.getTime();
    }

}
