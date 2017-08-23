package unmla.airport.simul.command;

import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;
import unmla.airport.simul.Simulation;
import unmla.airport.simul.SimulationController;

/**
 * 
 */
public class SetStateCommand implements SimulationCommand {

    private final LuggageTransporter    target;

    private final SimulationController  controller;
    
    public SetStateCommand(final SimulationController controller, 
                            final LuggageTransporter target) {
        if(controller == null) {
            throw new NullPointerException();
        }
        
        if(target == null) {
            throw new NullPointerException();
        }
        
        this.controller = controller;
        this.target = target;
    }
    
    @Override
    public void execute(Simulation simulation) {
        final int modifier = (this.target.getState() ? 1 : -1);
        
        if(target instanceof LuggageRouter) {
            controller.getSimulation().getStatistics()
                .addNodesDisabled(modifier);
        }
        else {
            controller.getSimulation().getStatistics()
                        .addEdgesDisabled(modifier);
        }
        
        this.target.setState(!this.target.getState());
    }

}
