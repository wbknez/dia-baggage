package unmla.airport.simul.command;

import unmla.airport.simul.Simulation;

/**
 * 
 */
public interface SimulationCommand {
    
    /**
     * 
     * @param simulation
     * @throws NullPointerException If <code>simulation</code> is 
     * <code>null</code>.
     */
    void execute(final Simulation simulation);
}
