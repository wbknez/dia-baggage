package unmla.airport.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections15.Transformer;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import unmla.airport.AbstractLuggageRouter;
import unmla.airport.AbstractLuggageTransporter;
import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;

/**
 * Test suite for {@link unmla.airport.graph.WeightedEdgeTransformer}.
 */
public class WeightedEdgeTransformerTest {

    private static class TestRouter extends AbstractLuggageRouter {
        private final String identifier;
        
        public TestRouter(final String identifier, final double weight) {
            super(identifier, "N/A", 1, weight);
            
            if(identifier == null) {
                throw new NullPointerException();
            }
            
            this.identifier = identifier;
        }
        
        public String getIdentifier() {
            return this.identifier;
        }
        
        @Override
        public String toString() {
            return this.identifier;
        }
    }
    
    private static class TestTransporter extends AbstractLuggageTransporter {
        private final String identifier;
        
        public TestTransporter(String identifier, final double weight) {
            super(identifier, "N/A", 1, weight);
            
            if(identifier == null) {
                throw new NullPointerException();
            }
            
            this.identifier = identifier;
            this.setTime(weight);
        }
        
        public String getIdentifier() {
            return this.identifier;
        }
        
        @Override
        public String toString() {
            return this.identifier;
        }
    }
    
    private static class DefaultTransformer 
                        implements Transformer<LuggageTransporter, Double> {

        @Override
        public Double transform(LuggageTransporter trans) {
            return trans.getTime();
        }
        
    }
    
    /** */
    private DefaultTransformer                          defaultTransformer;
    
    private DijkstraShortestPath<LuggageRouter, LuggageTransporter> 
                                                        defaultShortestPath;
    private DijkstraShortestPath<LuggageRouter, LuggageTransporter> 
                                                        weightedShortestPath;
    /** */
    private Graph<LuggageRouter, LuggageTransporter>    graph;
    /** */
    private WeightedEdgeTransformer                     weightedTransformer;
    
    @Before
    public void setUp() {
        this.graph = new DirectedSparseGraph<LuggageRouter, 
                                                LuggageTransporter>();
        
        this.createGraph(this.graph);
        
        this.defaultTransformer = new DefaultTransformer();
        this.weightedTransformer = new WeightedEdgeTransformer(this.graph);
        
        this.defaultShortestPath = 
              new DijkstraShortestPath<LuggageRouter, LuggageTransporter>(graph, 
                                                      this.defaultTransformer);
        this.weightedShortestPath = 
              new DijkstraShortestPath<LuggageRouter, LuggageTransporter>(graph, 
                                                      this.weightedTransformer);
        
        this.defaultShortestPath.enableCaching(false);
        this.weightedShortestPath.enableCaching(false);
    }
    
    private void createGraph(
                        final Graph<LuggageRouter, LuggageTransporter> graph) {
        if(graph == null) {
            throw new NullPointerException();
        }
        
        /*
         * The graph we are aiming to create is a simple one.  There will be 
         * 4 nodes with 5 edges, just enough to make sure the algorithm works 
         * correctly without needing to setup a massive graph.  The graph will 
         * look like this:
         * 
         *  V0 --> V1
         *  | \    |
         *  |  \   |
         *  |   \  |
         *  V    > V
         *  V2 --> V3
         *  
         * where:
         *      [V0->V2]: 2   : e0
         *      [V0->V1]: 3   : e1
         *      [V2->V3]: 4   : e2
         *      [V1->V3]: 5   : e3
         *      [V0->V3]: 30  : e4
         *      
         * and:
         *      v0: 1
         *      v1: 2
         *      v2: 5
         *      v3: 6
         */
        final TestRouter v0 = new TestRouter("v0", 1);
        final TestRouter v1 = new TestRouter("v1", 2);
        final TestRouter v2 = new TestRouter("v2", 5);
        final TestRouter v3 = new TestRouter("v3", 6);
        
        final TestTransporter e0 = new TestTransporter("e0", 2);
        final TestTransporter e1 = new TestTransporter("e1", 3);
        final TestTransporter e2 = new TestTransporter("e2", 4);
        final TestTransporter e3 = new TestTransporter("e3", 5);
        final TestTransporter e4 = new TestTransporter("e4", 30);
        
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        
        graph.addEdge(e0, v0, v2);
        graph.addEdge(e1, v0, v1);
        graph.addEdge(e2, v2, v3);
        graph.addEdge(e3, v1, v3);
        graph.addEdge(e4, v0, v3);
    }
    
    private LuggageRouter findVertex(
            final Graph<LuggageRouter, LuggageTransporter> graph,
            String identifier) {
        if(graph == null) {
            throw new NullPointerException();
        }
        
        if(identifier == null) {
            throw new NullPointerException();
        }
        
        for(final LuggageRouter router : graph.getVertices()) {
            if(((TestRouter)router).getIdentifier().equals(identifier)) {
                return router;
            }
        }
        
        throw new AssertionError();
    }
    
    private LuggageTransporter findEdge(
            final Graph<LuggageRouter, LuggageTransporter> graph,
            String identifier) {
        if(graph == null) {
            throw new NullPointerException();
        }
        
        if(identifier == null) {
            throw new NullPointerException();
        }
        
        for(final LuggageTransporter transporter : graph.getEdges()) {
            if(((TestTransporter)transporter).getIdentifier()
                    .equals(identifier)) {
                return transporter;
            }
        }
        
        throw new AssertionError();
    }
    
    /*
     * Just for safety.
     */
    @Test
    public void testGraphCreation() {
        assertEquals(4, graph.getVertexCount());
        assertEquals(5, graph.getEdgeCount());
    }
    
    /*
     * Just for safety.
     * 
     * The default transformer will ignore the vertices.  Using the above 
     * graph, we will test the path from [v0->v3], which is 6.
     */
    @Test
    public void testDefaultTransformerDistance() {
        final LuggageRouter source = this.findVertex(this.graph, "v0");
        final LuggageRouter target = this.findVertex(this.graph, "v3");
        final double expected = 6.0;
        
        final Number result = 
                        this.defaultShortestPath.getDistance(source, target);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDefaultTransformerPath() {
        final LuggageRouter source = this.findVertex(this.graph, "v0");
        final LuggageRouter target = this.findVertex(this.graph, "v3");
        
        final List<LuggageTransporter> expectedPath = 
                            this.defaultShortestPath.getPath(source, target);

        assertEquals(2, expectedPath.size());
        
        final Iterator<LuggageTransporter> iterator = expectedPath.iterator();
        
        assertThat(((TestTransporter)iterator.next()).getIdentifier(), 
                        is("e0"));
        assertThat(((TestTransporter)iterator.next()).getIdentifier(), 
                        is("e2"));
    }
    
    /*
     * The weighted transformer will take the vertices into account.  Using the 
     * same path as with the previous test, the path from [v0->v3] will now be 
     * 16 and instead of passing from [v0->v2->v3] will not be [v0->v1->v3].
     */
    @Test
    public void testWeightedTransformerDistance() {
        final LuggageRouter source = this.findVertex(this.graph, "v0");
        final LuggageRouter target = this.findVertex(this.graph, "v3");
        final double expected = 6.0;
        
        final Number result = 
                        this.weightedShortestPath.getDistance(source, target);

        assertEquals(expected, result);
    }
    
    @Test
    public void testWeightedTransformerPath() {
        final LuggageRouter source = this.findVertex(this.graph, "v0");
        final LuggageRouter target = this.findVertex(this.graph, "v3");
        
        final List<LuggageTransporter> expectedPath = 
                            this.weightedShortestPath.getPath(source, target);

        assertEquals(2, expectedPath.size());
        
        final Iterator<LuggageTransporter> iterator = expectedPath.iterator();
        
        assertThat(((TestTransporter)iterator.next()).getIdentifier(), 
                        is("e0"));
        assertThat(((TestTransporter)iterator.next()).getIdentifier(), 
                        is("e2"));
    }
    
    /*
     * The path is unaffected.
     */
    @Test
    public void testDefaultTransformerDistanceWithInfiniteVertices() {
        final LuggageRouter v1 = this.findVertex(this.graph, "v1");
        final LuggageRouter v2 = this.findVertex(this.graph, "v2");
        
        v1.setTime(Double.POSITIVE_INFINITY);
        v2.setTime(Double.POSITIVE_INFINITY);
     
        final LuggageRouter source = this.findVertex(this.graph, "v0");
        final LuggageRouter target = this.findVertex(this.graph, "v3");
        final double expected = 6.0;
        
        final Number result = 
                this.defaultShortestPath.getDistance(source, target);

        assertEquals(expected, result);
    }
    
    /*
     * The path is unaffected.
     */
    @Test
    public void testDefaultTransformerPathWithInfiniteVertices() {
        final LuggageRouter v1 = this.findVertex(this.graph, "v1");
        final LuggageRouter v2 = this.findVertex(this.graph, "v2");
        
        v1.setTime(Double.POSITIVE_INFINITY);
        v2.setTime(Double.POSITIVE_INFINITY);
     
        final LuggageRouter source = this.findVertex(this.graph, "v0");
        final LuggageRouter target = this.findVertex(this.graph, "v3");
        
        final List<LuggageTransporter> expectedPath = 
                this.defaultShortestPath.getPath(source, target);

        assertEquals(2, expectedPath.size());
        
        final Iterator<LuggageTransporter> iterator = expectedPath.iterator();
        
        assertThat(((TestTransporter)iterator.next()).getIdentifier(), 
                    is("e0"));
        assertThat(((TestTransporter)iterator.next()).getIdentifier(), 
                    is("e2"));
    }
    
    /*
     * The only valid path is e1->e3
     */
    @Test
    public void testDefaultTransformerDistanceWithInfiniteEdges() {
        final LuggageTransporter e0 = this.findEdge(this.graph, "e0");
        final LuggageTransporter e2 = this.findEdge(this.graph, "e2");
        final LuggageTransporter e4 = this.findEdge(this.graph, "e4");
        
        e0.setTime(Double.POSITIVE_INFINITY);
        e2.setTime(Double.POSITIVE_INFINITY);
        e4.setTime(Double.POSITIVE_INFINITY);
     
        final LuggageRouter source = this.findVertex(this.graph, "v0");
        final LuggageRouter target = this.findVertex(this.graph, "v3");
        final double expected = 8.0;
        
        final Number result = 
                this.defaultShortestPath.getDistance(source, target);

        assertEquals(expected, result);
    }
    
    /*
     * The only valid path is e1->e3
     */
    @Test
    public void testDefaultTransformerPathWithInfiniteEdges() {
        final LuggageTransporter e0 = this.findEdge(this.graph, "e0");
        final LuggageTransporter e2 = this.findEdge(this.graph, "e2");
        final LuggageTransporter e4 = this.findEdge(this.graph, "e4");
        
        e0.setTime(Double.POSITIVE_INFINITY);
        e2.setTime(Double.POSITIVE_INFINITY);
        e4.setTime(Double.POSITIVE_INFINITY);
        
        final LuggageRouter source = this.findVertex(this.graph, "v0");
        final LuggageRouter target = this.findVertex(this.graph, "v3");
        
        final List<LuggageTransporter> expectedPath = 
                            this.defaultShortestPath.getPath(source, target);

        assertEquals(2, expectedPath.size());
        
        final Iterator<LuggageTransporter> iterator = expectedPath.iterator();
        
        assertThat(((TestTransporter)iterator.next()).getIdentifier(), 
                        is("e1"));
        assertThat(((TestTransporter)iterator.next()).getIdentifier(), 
                        is("e3"));
    }
    
    /*
     * The only valid path is to take the diagonal.
     */
    @Test
    public void testWeightedTransformerDistanceWithInfiniteVertices() {
        final LuggageRouter v1 = this.findVertex(this.graph, "v1");
        final LuggageRouter v2 = this.findVertex(this.graph, "v2");
        
        v1.setTime(Double.POSITIVE_INFINITY);
        v2.setTime(Double.POSITIVE_INFINITY);
     
        final LuggageRouter source = this.findVertex(this.graph, "v0");
        final LuggageRouter target = this.findVertex(this.graph, "v3");
        final double expected = 30.0;
        
        final Number result = 
                this.weightedShortestPath.getDistance(source, target);

        assertEquals(expected, result);
    }
    
    /*
     * The only valid path is to take the diagonal.
     */
    @Test
    public void testWeightedTransformerPathWithInfiniteVertices() {
        final LuggageRouter v1 = this.findVertex(this.graph, "v1");
        final LuggageRouter v2 = this.findVertex(this.graph, "v2");
        
        v1.setTime(Double.POSITIVE_INFINITY);
        v2.setTime(Double.POSITIVE_INFINITY);
     
        final LuggageRouter source = this.findVertex(this.graph, "v0");
        final LuggageRouter target = this.findVertex(this.graph, "v3");
        
        final List<LuggageTransporter> expectedPath = 
                this.weightedShortestPath.getPath(source, target);

        assertEquals(1, expectedPath.size());
        
        final Iterator<LuggageTransporter> iterator = expectedPath.iterator();
        
        assertThat(((TestTransporter)iterator.next()).getIdentifier(), 
                    is("e4"));
    }
    
    /*
     * The only valid path is e1->e3
     */
    @Test
    public void testWeightedTransformerDistanceWithInfiniteEdges() {
        final LuggageTransporter e0 = this.findEdge(this.graph, "e0");
        final LuggageTransporter e2 = this.findEdge(this.graph, "e2");
        final LuggageTransporter e4 = this.findEdge(this.graph, "e4");
        
        e0.setTime(Double.POSITIVE_INFINITY);
        e2.setTime(Double.POSITIVE_INFINITY);
        e4.setTime(Double.POSITIVE_INFINITY);
     
        final LuggageRouter source = this.findVertex(this.graph, "v0");
        final LuggageRouter target = this.findVertex(this.graph, "v3");
        final double expected = 8.0;
        
        final Number result = 
                this.weightedShortestPath.getDistance(source, target);

        assertEquals(expected, result);
    }
    
    /*
     * The only valid path is e1->e3
     */
    @Test
    public void testWeightedTransformerPathWithInfiniteEdges() {
        final LuggageTransporter e0 = this.findEdge(this.graph, "e0");
        final LuggageTransporter e2 = this.findEdge(this.graph, "e2");
        final LuggageTransporter e4 = this.findEdge(this.graph, "e4");
        
        e0.setTime(Double.POSITIVE_INFINITY);
        e2.setTime(Double.POSITIVE_INFINITY);
        e4.setTime(Double.POSITIVE_INFINITY);
        
        final LuggageRouter source = this.findVertex(this.graph, "v0");
        final LuggageRouter target = this.findVertex(this.graph, "v3");
        
        final List<LuggageTransporter> expectedPath = 
                            this.weightedShortestPath.getPath(source, target);

        assertEquals(2, expectedPath.size());
        
        final Iterator<LuggageTransporter> iterator = expectedPath.iterator();
        
        assertThat(((TestTransporter)iterator.next()).getIdentifier(), 
                        is("e1"));
        assertThat(((TestTransporter)iterator.next()).getIdentifier(), 
                        is("e3"));
    }
    
    /*
     * There are two paths: e1->e3 and e4 (diagonal), but the algorithm should 
     * only choose e1-e3 because of the constraint that it should not accept 
     * paths that are longer than 9.
     */
    @Test
    public void testDefaultTransformerDistanceWithRestraintAndInfiniteBoundary() {
        final LuggageTransporter e0 = this.findEdge(this.graph, "e0");
        final LuggageTransporter e2 = this.findEdge(this.graph, "e2");
        
        e0.setTime(Double.POSITIVE_INFINITY);
        e2.setTime(Double.POSITIVE_INFINITY);
     
        final LuggageRouter source = this.findVertex(this.graph, "v0");
        final LuggageRouter target = this.findVertex(this.graph, "v3");
        final double expected = 8.0;
        
        this.defaultShortestPath.setMaxDistance(9.0);
        
        final Number result = 
                this.defaultShortestPath.getDistance(source, target);

        assertEquals(expected, result);
    }
    
    /*
     * There are two paths: e1->e3 and e4 (diagonal), but the algorithm should 
     * only choose e1-e3 because of the constraint that it should not accept 
     * paths that are longer than 17.
     */
    @Test
    public void testWeightedTransformerDistanceWithRestraintAndInfiniteBoundary() {
        final LuggageTransporter e0 = this.findEdge(this.graph, "e0");
        final LuggageTransporter e2 = this.findEdge(this.graph, "e2");
        
        e0.setTime(Double.POSITIVE_INFINITY);
        e2.setTime(Double.POSITIVE_INFINITY);
     
        final LuggageRouter source = this.findVertex(this.graph, "v0");
        final LuggageRouter target = this.findVertex(this.graph, "v3");
        final double expected = 8.0;
        
        this.weightedShortestPath.setMaxDistance(17.0);
        
        final Number result = 
                this.weightedShortestPath.getDistance(source, target);

        assertEquals(expected, result);
    }
}
