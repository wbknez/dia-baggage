package unmla.airport.ui.panels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import unmla.airport.Statistics;
import unmla.airport.simul.SimulationController;
import unmla.airport.ui.listeners.StatisticsListener;

import net.java.dev.designgridlayout.DesignGridLayout;

/**
 * 
 */
public class StatisticsPanel extends JPanel implements StatisticsListener {
    
    private static class StatisticsTextField extends JTextField {
        
        /**
         * For serialization; generated by Eclipse.
         */
        private static final long serialVersionUID = 2726522940841962423L;

        public StatisticsTextField(final String text) {
            super(text);
            
            this.setDisabledTextColor(Color.black);
            this.setEnabled(false);
            this.setHorizontalAlignment(JTextField.RIGHT);
        }
    }

    /**
     * For serialization; generated by Eclipse.
     */
    private static final long serialVersionUID = 5150685065833659704L;

    private final StatisticsTextField   incinerated;
    private final StatisticsTextField   late;
    private final StatisticsTextField   lost;
    private final StatisticsTextField   lostAndFound;
    private final StatisticsTextField   madeItToCarousel;
    private final StatisticsTextField   madeItToGate;
    private final StatisticsTextField   sentToPlane;
    private final StatisticsTextField   rejected;
    private final StatisticsTextField   total;
    
    private final StatisticsTextField   disabledEdges;
    private final StatisticsTextField   disabledNodes;
    private final StatisticsTextField   totalEdges;
    private final StatisticsTextField   totalNodes;
    
    /**
     * Constructor.
     */
    public StatisticsPanel(final SimulationController controller) {
        if(controller == null) {
            throw new NullPointerException();
        }
        
        controller.getSimulation().getStatistics().addListener(this);
        
        this.incinerated = new StatisticsTextField("0");
        this.late = new StatisticsTextField("0");
        this.lost = new StatisticsTextField("0");
        this.lostAndFound = new StatisticsTextField("0");
        this.madeItToCarousel = new StatisticsTextField("0");
        this.madeItToGate =new StatisticsTextField("0");
        this.sentToPlane = new StatisticsTextField("0");
        this.rejected = new StatisticsTextField("0");
        this.total = new StatisticsTextField("0");
        
        this.disabledEdges = new StatisticsTextField("0");
        this.disabledNodes = new StatisticsTextField("0");
        this.totalEdges = new StatisticsTextField("0");
        this.totalNodes = new StatisticsTextField("0");
        
        this.setUpUi(controller);
    }
    
    private void setUpUi(final SimulationController controller) {
        final JLabel luggageHeader = new JLabel("Luggage");
        final JLabel simulationHeader = new JLabel("Simulation");
        
        luggageHeader.setFont(luggageHeader.getFont().deriveFont(Font.BOLD));
        simulationHeader.setFont(
                            simulationHeader.getFont().deriveFont(Font.BOLD));
        
        final DesignGridLayout designGrid = new DesignGridLayout(this);
        

        designGrid.row().center().add(luggageHeader);
        designGrid.row().grid().add(new JLabel("Arrived at Carousel"))
                                .add(this.madeItToCarousel);
        designGrid.row().grid().add(new JLabel("Arrived at Gate"))
                                .add(this.madeItToGate);
        designGrid.row().grid().add(new JLabel("Incinerated"))
                                .add(this.incinerated);
        designGrid.row().grid().add(new JLabel("Late"))
                                .add(this.late);
        designGrid.row().grid().add(new JLabel("Lost"))
                                .add(this.lost);
        designGrid.row().grid().add(new JLabel("Lost and Found"))
                                .add(this.lostAndFound);
        designGrid.row().grid().add(new JLabel("Next Plane"))
                                .add(this.sentToPlane);
        designGrid.row().grid().add(new JLabel("Rejected"))
                                .add(this.rejected);
        designGrid.row().grid().add(new JLabel("Total"))
                                .add(this.total);
        
        designGrid.emptyRow();
        
        designGrid.row().center().add(simulationHeader);
        designGrid.row().grid().add(new JLabel("Disabled Edges"))
                                .add(this.disabledEdges);
        designGrid.row().grid().add(new JLabel("Disabled Nodes"))
                                .add(this.disabledNodes);
        designGrid.row().grid().add(new JLabel("Total Edges"))
                                .add(this.totalEdges);
        designGrid.row().grid().add(new JLabel("Total Nodes"))
                                .add(this.totalNodes);
        
        final Statistics statistics = controller.getSimulation()
                                            .getStatistics();
        this.statisticsUpdated(statistics);
    }

    @Override
    public void statisticsUpdated(Statistics stats) {        
        this.incinerated.setText(
                            Integer.toString(stats.getLuggagesIncinerated()));
        this.late.setText(Integer.toString(stats.getLateLuggages()));
        this.lost.setText(Integer.toString(stats.getLuggagesLost()));
        this.lostAndFound.setText(
                        Integer.toString(stats.getLuggagesToLostAndFound()));
        this.madeItToCarousel.setText(
                        Integer.toString(stats.getMadeItToBagClaim()));
        this.madeItToGate.setText(
                        Integer.toString(stats.getMadeItToGate()));
        this.sentToPlane.setText(
                        Integer.toString(stats.getLuggagesSentToNextPlane()));
        this.rejected.setText(Integer.toString(stats.getLuggagesRejected()));
        this.total.setText(Integer.toString(stats.getTotalLuggages()));
        
        this.disabledEdges.setText(Integer.toString(stats.getEdgesDisabled()));
        this.disabledNodes.setText(Integer.toString(stats.getNodesDisabled()));
        this.totalEdges.setText(Integer.toString(stats.getTotalEdges()));
        this.totalNodes.setText(Integer.toString(stats.getTotalNodes()));
        
        this.repaint();
    }
}
