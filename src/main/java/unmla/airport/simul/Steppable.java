package unmla.airport.simul;

/**
 * 
 */
public interface Steppable {
    
    /**
     * 
     * @return
     */
    public TimeCounter getTimeCounter();
    
    /**
     * 
     * @param simulation
     */
    void initialize(final Simulation simulation);

    /**
     * 
     * @param simulation
     */
    void reset(final Simulation simulation);
    
    /**
     * 
     * 
     * @param deltaTime The amount of time that has passed since the last step.
     * @param simulation The simulation object that is controlling the step.
     * @throws IllegalArgumentException If <code>deltaTime</code> is less than 
     * or equal to zero.
     * @throws NullPointerException If <code>simulation</code> is 
     * <code>null</code>.
     */
    void step(final double deltaTime, final Simulation simulation);
}
