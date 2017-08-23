package unmla.airport.simul;

/**
 * 
 */
public class SimulationClock extends TimeCounter {

    /**
     * The amount of seconds in a twenty four hour day, given that sixty 
     * seconds constitutes a single minute and sixty single minutes constitute 
     * a day.
     */
    public static double    SecondsPerDay = 60 * 60 * 24;
    
    /**
     * The amount of time that has passed in the "world".
     * 
     * <p>
     * This value is constrained to the interval 
     * <code>[0.0, SecondsPerDay]</code>, which represents a standard Earth day 
     * of 24 hours in standard U.S. time using seconds.
     * </p>
     */
    private double          worldTime;
    
    /**
     * Constructor.
     * 
     * @param timeStep The time step to use.
     * @throws IllegalArgumentException If <code>timeStep</code> is less than or 
     * equal to zero.
     */
    public SimulationClock(final double timeStep) {
        super(timeStep);
    }
    
    /**
     * Performs the same logical step as defined in {@link TimeCounter}, but in 
     * addition also increments the world time and checks for roll over 
     * (the world time is greater than 24 hours).
     * 
     * @see unmla.airport.simul.TimeCounter#step()
     */
    @Override
    public void step() {
        super.step();
        
        this.worldTime += this.getTimeStep();
        this.checkRollover();
    }
    
    /**
     * Returns the simulation time as a function of the world at large; i.e. 
     * converts the current simulation time to a human-readable time value 
     * between <code>0.0</code> and <code>SecondsPerDay</code>.
     * 
     * @return
     */
    public double getWorldTime() {
        return this.worldTime;
    }
    
    /**
     * 
     */
    private void checkRollover() {
        if(this.worldTime >= SecondsPerDay) {
            this.worldTime = this.worldTime - SecondsPerDay;
        }
    }
    
    /**
     * 
     */
    @Override
    public void reset() {
        super.reset();
        
        this.worldTime = 0;
    }
    
    @Override
    public String toString() {
        //return "";
        return TimeStamp.convertTimeToString(this.worldTime, ':');
    }
}
