package unmla.airport.simul;

import unmla.airport.simul.command.SimulationCommand;
import unmla.airport.ui.UiController;

/**
 * 
 */
public class SimulationController {

    /** */
    private boolean             isRunning;
    /** */
    private final Simulation    simulation;
    
    /** */
    // Unavoidable hack =/
    private UiController        uiController;
    
    public SimulationController(final Simulation simulation) {
        if(simulation == null) {
            throw new NullPointerException();
        }
        
        this.simulation = simulation;
    }
    
    public void setUiController(final UiController uiController) {
        if(uiController == null) {
            throw new NullPointerException();
        }
        
        this.uiController = uiController;
    }
    
    public UiController getUiController() {
        return this.uiController;
    }
    
    /**
     * 
     * @param command
     */
    public void submitCommand(final SimulationCommand command) {
        if(command == null) {
            throw new NullPointerException();
        }
        
        if(this.isRunning) {
            this.simulation.addCommand(command);
        }
        else {
            command.execute(this.simulation);
        }
    }
    
    /**
     * 
     * @return
     */
    public Simulation getSimulation() {
        return this.simulation;
    }
    
    /**
     * Returns whether or not the simulation is currently executing in it's 
     * own thread.
     * 
     * <p>
     * 
     * </p>
     * 
     * @return
     */
    public boolean isRunning() {
        return this.isRunning;
    }
    
    public void requestRepaint() {
        this.uiController.requestDisplayUpdate();
    }
    
    public void start() {
        if(!this.isRunning) {
            this.isRunning = true;
            this.simulation.setIsRunning(true);
        }
    }
    
    public void stop() {
        if(this.isRunning) {
            this.isRunning = false;
            this.simulation.setIsRunning(false);
        }
    }
}
