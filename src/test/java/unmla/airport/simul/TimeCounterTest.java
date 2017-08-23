package unmla.airport.simul;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class TimeCounterTest {

    public static final double  StandardTimeStep        = 40;
    
    public static final double  StandardSimulationStep  = 120;
    
    private TimeCounter counter;
    
    @Before
    public void setUp() {
        this.counter = new TimeCounter(StandardTimeStep);
    }
    
    @Test
    public void testAdvance() {
        assertEquals(0, Double.compare(0, this.counter.getAccumulatedTime()));
        
        this.counter.advance(StandardSimulationStep);
        assertEquals(0, Double.compare(StandardSimulationStep, 
                        this.counter.getAccumulatedTime()));
        
        this.counter.advance(StandardSimulationStep);
        assertEquals(0, Double.compare(StandardSimulationStep * 2.0, 
                        this.counter.getAccumulatedTime()));
    }
    
    @Test
    public void testCanStep() {
        // each step is 120
        // timer should be able to step 3 times per advance
        this.counter.advance(StandardSimulationStep);
        
        assertTrue(this.counter.canStep());
        this.counter.step();
        assertTrue(this.counter.canStep());
        this.counter.step();
        assertTrue(this.counter.canStep());
        this.counter.step();
        assertFalse(this.counter.canStep());
    }
    
    @Test
    public void testStep() {
        // each step is 120
        // timer should be able to step 3 times per advance
        final double expected0 = 120.0;
        final double expected1 = 80.0;
        final double expected2 = 40.0;
        final double expected3 = 0.0;
        
        this.counter.advance(StandardSimulationStep);
        
        assertEquals(0, Double.compare(expected0, 
                        this.counter.getAccumulatedTime()));
        this.counter.step();
        assertEquals(0, Double.compare(expected1, 
                this.counter.getAccumulatedTime()));
        this.counter.step();
        assertEquals(0, Double.compare(expected2, 
                this.counter.getAccumulatedTime()));
        this.counter.step();
        assertEquals(0, Double.compare(expected3, 
                this.counter.getAccumulatedTime()));
    }
}
