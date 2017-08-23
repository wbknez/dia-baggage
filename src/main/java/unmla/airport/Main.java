package unmla.airport;

import javax.swing.SwingUtilities;

import unmla.airport.simul.Simulation;
import unmla.airport.simul.SimulationController;
import unmla.airport.ui.UiController;

/**
 * Main driver.
 */
public class Main {

    public static void main(String args[]) {
        //Thread.setDefaultUncaughtExceptionHandler(
        //                                    new DefaultExceptionHandleDialog());
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                final Simulation simulation = new Simulation();
                final SimulationController simulController = 
                                                    simulation.getController();
                final UiController uiController = 
                                            new UiController(simulController);
                
                uiController.setUpUi();
            }
        });
    }
}
