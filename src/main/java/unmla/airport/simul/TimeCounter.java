package unmla.airport.simul;

/**
 * 
 */
public class TimeCounter {

    /**
     * The total amount of time that has accumulated.
     * 
     * <p>
     * It is important to realize that this value actually represents the total 
     * amount of time that has accumulated minus the number of steps that have 
     * been taken, as this value is reduced by every step that occurs.
     * </p>
     */
    private double  accumulatedTime;
    
    /** 
     * The amount of time that must pass before a step notification may 
     * be given.
     */
    private double  timeStep;
    
    /**
     * Constructor.
     * 
     * @param timeStep The time step to use.
     * @throws IllegalArgumentException If <code>timeStep</code> is less than or 
     * equal to zero.
     */
    public TimeCounter(final double timeStep) {
        if(Double.compare(timeStep, 0) <= 0) {
            throw new IllegalArgumentException("Time steps must be positive!");
        }
        
        this.timeStep = timeStep;
    }
    
    /**
     * Adds the specified amount of time to the total accumulated time.
     * 
     * @param deltaTime The amount of time that has "passed".
     */
    public void advance(final double deltaTime) {
        this.accumulatedTime += deltaTime;
    }
    
    /**
     * Returns whether or not the amount of accumulated time is sufficiently 
     * greater than or equal to the time step.
     * 
     * @return
     */
    public boolean canStep() {
        return this.accumulatedTime - this.timeStep >= 0;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if(obj == this) {
            return true;
        }
        
        if(!(obj instanceof TimeCounter)) {
            return false;
        }
        
        final TimeCounter counter = (TimeCounter)obj;
        
        if(Double.compare(this.accumulatedTime, counter.accumulatedTime) != 0) {
            return false;
        }
        if(Double.compare(this.timeStep, counter.timeStep) != 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Returns the total amount of accumulated time in this counter.
     * 
     * @return
     */
    public double getAccumulatedTime() {
        return this.accumulatedTime;
    }
    
    /**
     * Returns the time step used by this counter.
     * 
     * @return
     */
    public double getTimeStep() {
        return this.timeStep;
    }
    
    /**
     * Sets the total accumulated time to zero, effectively resetting the 
     * counter.
     */
    public void reset() {
        this.accumulatedTime = 0;
    }
    
    /**
     * Sets the time step to the specified amount.
     * 
     * @param timeStep The time step to use.
     * @throws IllegalArgumentException If <code>timeStep</code> is less than or 
     * equal to zero.
     */
    public void setTimeStep(final double timeStep) {
        if(Double.compare(timeStep, 0) <= 0) {
            throw new IllegalArgumentException("Time steps must be positive!");
        }
        
        this.timeStep = timeStep;
    }
    
    /**
     * Performs a "step"; that is, reduces the total accumulated time by the 
     * time step to simulate an iteration.
     */
    public void step() {
        this.accumulatedTime -= this.timeStep;
    }
    
    @Override
    public String toString() {
        return this.accumulatedTime + "/" + this.timeStep;
    }
}
