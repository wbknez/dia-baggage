package unmla.airport.simul.command;

import unmla.airport.simul.Simulation;

/**
 * 
 */
public class ResetSimulationCommand implements SimulationCommand {

    @Override
    public void execute(Simulation simulation) {
        simulation.reset();
        simulation.getController().requestRepaint();
    }

}
