package unmla.airport.ui.tabpanes;

import javax.swing.JTabbedPane;

import unmla.airport.simul.SimulationController;
import unmla.airport.ui.panels.OptionsPanel;
import unmla.airport.ui.panels.StatisticsPanel;

/**
 * 
 */
public class SettingsTabPane extends JTabbedPane {

    /**
     * For serialization; generated by Eclipse.
     */
    private static final long serialVersionUID = 2423259984088246228L;

    private final OptionsPanel      optionsPanel;
    
    private final StatisticsPanel   statisticsPanel;
    
    public SettingsTabPane(final SimulationController controller) {
        super();
        
        this.optionsPanel = new OptionsPanel(controller);
        this.statisticsPanel = new StatisticsPanel(controller);
        
        this.addTab("Options", this.optionsPanel);
        this.addTab("Statistics", this.statisticsPanel);
        
        this.setSelectedIndex(1);
    }
    
    public OptionsPanel getOptionsPanel() {
        return this.optionsPanel;
    }
    
    public StatisticsPanel getStatisticsPanel() {
        return this.statisticsPanel;
    }
}