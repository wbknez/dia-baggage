package unmla.airport.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import unmla.airport.BaggageClaim;
import unmla.airport.CheckIn;
import unmla.airport.Gate;
import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;
import unmla.airport.simul.Steppable;
import unmla.airport.ui.UiComponent;
import edu.uci.ics.jung.algorithms.shortestpath.BFSDistanceLabeler;
import edu.uci.ics.jung.graph.Graph;

/**
 * 
 */
public class UpdatePathLabeler {

    private final Map<String, BaggageClaim>                 carousels;
    
    private final Map<String, CheckIn>                      checkIns;
    
    private final Map<String, Gate>                         gates;
    
    private final Graph<LuggageRouter, LuggageTransporter>  graph;
    
    private final List<Steppable>                           traversed;
    
    public UpdatePathLabeler(
                        final Graph<LuggageRouter, LuggageTransporter> graph) {
        if(graph == null) {
            throw new NullPointerException();
        }
        
        this.carousels = new HashMap<String, BaggageClaim>();
        this.checkIns = new HashMap<String, CheckIn>();
        this.gates = new HashMap<String, Gate>();
        
        this.graph = graph;
        this.traversed = new ArrayList<Steppable>();
    }
    
    public List<Steppable> getTraversedPath() {
        return this.traversed;
    }
    
    public Map<String, BaggageClaim> getCarousels() {
        return this.carousels;
    }
    
    public Map<String, CheckIn> getCheckIns() {
        return this.checkIns;
    }
    
    public Map<String, Gate> getGates() {
        return this.gates;
    }
    
    public void labelPath() {
        /*
         * First of all, we need to extract the relevant information for the 
         * graph to build our maps.  What we're really doing is establishing 
         * our traverse boundaries, which is why the function is called thus.
         */
        this.establishTraverseBoundaries();
        
        /*
         * Next we need to find the update path.
         * 
         * We're going to do this in two passes and leverage the UI level ID to 
         * keep them distinct.  The first pass is for the "top" or outgoing 
         * network, while the second is the "bottom" or incoming.  The top is 
         * denoted as all paths from the checkins to the gates, and the second 
         * from the gates to the carousels.  
         * 
         * We can also exploit the fact that edges on each path are on the same 
         * "level" as their destination vertex and thus keep them separate 
         * without any extra effort.  Unfortunately, we have to do this on an 
         * edge-by-edge basis because we cannot guarantee the order returned by 
         * the BFSDistanceLabeler.
         * 
         * To actually generate the path, we have to iterate over all of a 
         * node's outgoing and incoming edges so that those edges are updated 
         * before the next vertex.  To ensure we don't add the same vertex or 
         * edge twice we utilize two caches - one for each type, respectively.
         * 
         * Lastly, it is true that we iterate over the entire graph 1.5 times.  
         * Realistically, it would be a lot better if we did this at the BFS 
         * level, but with current time constraints and modern hardware it 
         * should not be a huge issue.
         */
        final BFSDistanceLabeler<LuggageRouter, LuggageTransporter> bfsLabeler = 
                new BFSDistanceLabeler<LuggageRouter, LuggageTransporter>();
        final HashSet<LuggageTransporter> edgeCache = 
                                            new HashSet<LuggageTransporter>();
        final HashSet<LuggageRouter> vertexCache = new HashSet<LuggageRouter>();
        
        final HashSet<LuggageRouter> carouselSet = 
                        new HashSet<LuggageRouter>(this.carousels.values());
        final HashSet<LuggageRouter> checkInSet = 
                        new HashSet<LuggageRouter>(this.checkIns.values());
        final HashSet<LuggageRouter> gateSet = 
                        new HashSet<LuggageRouter>(this.gates.values());
        
        bfsLabeler.labelDistances(this.graph, checkInSet);
        
        final List<LuggageRouter> firstPass = 
                                        bfsLabeler.getVerticesInOrderVisited();
        
        /*
         * We loop from the start to the end - 1, exploiting the fact that it 
         * doesn't matter if the iteration is accurate since the later half of 
         * the list won't be counted here.
         */
        /*
         * Here we exploit the fact that the list returned from the 
         * BFSDistanceLabeler is an ArrayList and so sequential iteration will 
         * be lightning fast.
         */
        /*
         * Lastly, we start off the iteration with the Check-Ins and end with 
         * the Gates to ensure a smooth iteration.
         */
        this.traversed.addAll(checkInSet);
        
        for(int i = 0; i < firstPass.size(); i++) {
            final LuggageRouter v0 = firstPass.get(i);
             
            if(!this.isOnCorrectLevel(v0, 1) || vertexCache.contains(v0)) {
                continue;
            }
            
            /*
             * 1. Add vertex to update path.
             *  a. Add vertex to cache.
             * 2. Find all incoming edges.
             * 3. Find all outgoing edges.
             * 4. Iterate over all the incoming edges.
             *  a. Determine if the edge has already been seen.
             *  b. Determine if their destination vertex is on the correct 
             *     level.
             *  c. 
             * 5. Iterate over all the outgoing edges.
             */

            vertexCache.add(v0);
            
            if(!(v0 instanceof CheckIn) && !(v0 instanceof Gate)) {
                this.traversed.add((Steppable)v0);
            }
            
            final Collection<LuggageTransporter> incoming = 
                                                    this.graph.getInEdges(v0);
            final Collection<LuggageTransporter> outgoing = 
                                                    this.graph.getOutEdges(v0);
            
            for(final LuggageTransporter edge : incoming) {
                if(!edgeCache.contains(edge)) {
                    final LuggageRouter source = 
                                this.graph.getSource(edge);
                    
                    if(this.isOnCorrectLevel(source, 1)) {
                        edgeCache.add(edge);
                        this.traversed.add((Steppable)edge);
                    }
                }
            }
            
            for(final LuggageTransporter edge : outgoing) {
                if(!edgeCache.contains(edge)) {
                    final LuggageRouter destination = this.graph.getDest(edge);
                    
                    if(this.isOnCorrectLevel(destination, 1)) {
                        edgeCache.add(edge);
                        this.traversed.add((Steppable)edge);
                    }
                }
            }
        }
        
        this.traversed.addAll(gateSet);
        
        // phase two
        bfsLabeler.labelDistances(this.graph, gateSet);
        
        final List<LuggageRouter> secondPass = 
                                        bfsLabeler.getVerticesInOrderVisited();
        
        // there is no need to perform any level checks here
        // all level 1 items will trip the cache checks
        for(int j = 0; j < secondPass.size(); j++) {
            final LuggageRouter v0 = secondPass.get(j);
                        
            if(vertexCache.contains(v0)) {
                continue;
            }
            
            vertexCache.add(v0);
            
            if(!(v0 instanceof BaggageClaim)) {
                this.traversed.add((Steppable)v0);
            }
            
            final Collection<LuggageTransporter> incoming = 
                                                    this.graph.getInEdges(v0);
            final Collection<LuggageTransporter> outgoing = 
                                                    this.graph.getOutEdges(v0);
            
            for(final LuggageTransporter edge : incoming) {
                if(!edgeCache.contains(edge)) {
                    final LuggageRouter source = 
                            this.graph.getSource(edge);
                    
                    if(this.isOnCorrectLevel(source, 2) || 
                            (source instanceof Gate)) {
                        edgeCache.add(edge);
                        this.traversed.add((Steppable)edge);
                    }
                }
            }
            
            for(final LuggageTransporter edge : outgoing) {
                if(!edgeCache.contains(edge)) {
                    final LuggageRouter destination = this.graph.getDest(edge);
                    
                    if(this.isOnCorrectLevel(destination, 2)) {
                        edgeCache.add(edge);
                        this.traversed.add((Steppable)edge);
                    }
                }
            }
        }
        
        this.traversed.addAll(carouselSet);
    }
    
    private boolean isOnCorrectLevel(final LuggageRouter router, 
                                            final int level) {
        if(router == null) {
            throw new NullPointerException();
        }
        
        final UiComponent uiComponent = router.getUiComponent();
        return ((uiComponent.getLevel() == level)||uiComponent.getLevel() == UiComponent.CoreLevel);
    }
    
    private void establishTraverseBoundaries() {
        /*
         * Technically, this could be generalized using generics on a per-map 
         * basis, but that isn't efficient in this case as we don't want to 
         * iterate over all the vertices [n] times.  Instead, we'll just do it 
         * once and use if/elses to determine the type.
         */
        for(final LuggageRouter router : this.graph.getVertices()) {
            if(router instanceof BaggageClaim) {
                this.carousels.put(router.getID(), (BaggageClaim)router);
            }
            else if(router instanceof CheckIn) {
                this.checkIns.put(router.getID(), (CheckIn)router);
            }
            else if(router instanceof Gate) {
                this.gates.put(router.getID(), (Gate)router);
            }
        }
    }
}
