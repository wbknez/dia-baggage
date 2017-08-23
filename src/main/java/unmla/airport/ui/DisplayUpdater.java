package unmla.airport.ui;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;
import unmla.airport.Statistics;
import unmla.airport.simul.SimulationController;
import unmla.airport.ui.panels.GraphPanel;

/**
 * 
 */
public class DisplayUpdater implements Runnable {

    private final SimulationController  controller;
    
    private final UiController          uiController;

    public DisplayUpdater(final SimulationController controller) {
        if (controller == null) {
            throw new NullPointerException();
        }
        
        this.controller = controller;
        this.uiController = controller.getUiController();
    }

    @Override
    public void run() {
        final GraphPanel graphPanel = this.uiController.getGraphPanel();
        final Statistics statistics = this.controller
                                        .getSimulation()
                                        .getStatistics();
        final VisualizationViewer<LuggageRouter, LuggageTransporter> viewer 
                                                    = graphPanel.getViewer();
        
        // pump any stat changes first
        if(statistics.isUpdateNeeded()) {
            statistics.notifyListeners();
        }
                
        // update the luggage thing
        this.uiController.getConsoleTabPane().getLuggagePanel().update();
        // prepare the pick cache
        this.uiController.getPickData().prepareCache();
        // repaint the graph
        viewer.repaint();
    }
}
