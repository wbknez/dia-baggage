package unmla.airport.simul;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for {@link unmla.airport.simul.SimulationClock}
 */
public class SimulationClockTest {
public static final double  StandardTimeStep        = 40;
    
    public static final double  StandardSimulationStep  = 120;
    
    private SimulationClock clock;
    
    @Before
    public void setUp() {
        this.clock = new SimulationClock(StandardTimeStep);
    }
    
    @Test
    public void testAdvance() {
        assertEquals(0, Double.compare(0, this.clock.getAccumulatedTime()));
        
        this.clock.advance(StandardSimulationStep);
        assertEquals(0, Double.compare(StandardSimulationStep, 
                        this.clock.getAccumulatedTime()));
        
        this.clock.advance(StandardSimulationStep);
        assertEquals(0, Double.compare(StandardSimulationStep * 2.0, 
                        this.clock.getAccumulatedTime()));
    }
    
    @Test
    public void testCanStep() {
        // each step is 120
        // timer should be able to step 3 times per advance
        this.clock.advance(StandardSimulationStep);
        
        assertTrue(this.clock.canStep());
        this.clock.step();
        assertTrue(this.clock.canStep());
        this.clock.step();
        assertTrue(this.clock.canStep());
        this.clock.step();
        assertFalse(this.clock.canStep());
    }
    
    @Test
    public void testStep() {
        // each step is 120
        // timer should be able to step 3 times per advance
        final double expected0 = 120.0;
        final double expected1 = 80.0;
        final double expected2 = 40.0;
        final double expected3 = 0.0;
        
        this.clock.advance(StandardSimulationStep);
        
        assertEquals(0, Double.compare(expected0, 
                        this.clock.getAccumulatedTime()));
        this.clock.step();
        assertEquals(0, Double.compare(expected1, 
                this.clock.getAccumulatedTime()));
        this.clock.step();
        assertEquals(0, Double.compare(expected2, 
                this.clock.getAccumulatedTime()));
        this.clock.step();
        assertEquals(0, Double.compare(expected3, 
                this.clock.getAccumulatedTime()));
    }
    
    public void testRollOverDirectlyAtMidnight() {
        final double expected0 = 0.0;
        
        this.clock.advance(SimulationClock.SecondsPerDay);
        
        assertEquals(0, Double.compare(expected0, this.clock.getWorldTime()));
    }
}
