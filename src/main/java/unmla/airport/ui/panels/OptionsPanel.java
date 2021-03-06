package unmla.airport.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.java.dev.designgridlayout.DesignGridLayout;

import unmla.airport.hlsColor;
import unmla.airport.simul.Simulation;
import unmla.airport.simul.SimulationController;

/**
 * 
 */
public class OptionsPanel extends JPanel implements ActionListener, 
                                                        ChangeListener {

    /**
     * For serialization; generated by Eclipse.
     */
    private static final long serialVersionUID = 3366646123791625639L;

    @SuppressWarnings("rawtypes")
    private final JComboBox             hlsLevel;
    
    private final JTextField            minTickSpacing;
    
    private final JSlider               simulationSpeed;
    
    private final JTextField            simulationTimeStep;
    
    private final SimulationController  controller;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public OptionsPanel(final SimulationController controller) {
        if(controller == null) {
            throw new NullPointerException();
        }        
        
        this.hlsLevel = new JComboBox(hlsColor.values());
        this.minTickSpacing = new JTextField("300");
        this.simulationSpeed = new JSlider(JSlider.HORIZONTAL, 1, 6, 2);
        this.simulationTimeStep = new JTextField
         (Double.toString(controller.getSimulation().getClock().getTimeStep()));
        
        /*
         * Label table to make the JSlider look good.
         */
        final Dictionary<Integer, JLabel> labelTable = 
                                new Hashtable<Integer, JLabel>();
        
        labelTable.put(new Integer(1), new JLabel("0.5"));
        labelTable.put(new Integer(2), new JLabel("1.0"));
        labelTable.put(new Integer(3), new JLabel("1.5"));
        labelTable.put(new Integer(4), new JLabel("2.0"));
        labelTable.put(new Integer(5), new JLabel("2.5"));
        labelTable.put(new Integer(6), new JLabel("3.0"));
        //labelTable.put(new Integer(7), new JLabel("3.5"));
        //labelTable.put(new Integer(8), new JLabel("4.0"));
        
        this.simulationSpeed.setLabelTable(labelTable);
        
        this.controller = controller;
        this.setUpUi();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        this.hlsLevel.setEnabled(enabled);
        this.minTickSpacing.setEnabled(enabled);
        this.simulationSpeed.setEnabled(enabled);
    }
    
    private void setUpUi() {
        this.hlsLevel.addActionListener(this);
        this.hlsLevel.setSelectedIndex(1);
        
        this.minTickSpacing.addActionListener(this);
        this.minTickSpacing.setHorizontalAlignment(JTextField.RIGHT);
        
        this.simulationSpeed.addChangeListener(this);
        this.simulationSpeed.setPaintLabels(true);
        this.simulationSpeed.setPaintTicks(true);
        
        this.simulationTimeStep.addActionListener(this);
        this.simulationTimeStep.setHorizontalAlignment(JTextField.RIGHT);
        
        final DesignGridLayout designGrid = new DesignGridLayout(this);
        
        designGrid.row().center().add(new JLabel("Speed"));
        designGrid.row().grid().add(this.simulationSpeed);
        designGrid.row().grid().add(new JLabel("Security Level"))
                                .add(this.hlsLevel);
        designGrid.row().grid().add(new JLabel("Tick Spacing (ms)"))
                                .add(this.minTickSpacing);
        designGrid.row().grid().add(new JLabel("Time Step (s)"))
                                .add(this.simulationTimeStep);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() == this.simulationSpeed) {
            if(!this.simulationSpeed.getValueIsAdjusting()) {
                final int base = this.simulationSpeed.getValue();
                
                final Simulation simulation = this.controller.getSimulation();
                simulation.setModifier(base / 2.0);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.hlsLevel) {
            final hlsColor chosen = (hlsColor)this.hlsLevel.getSelectedItem();
            
            if(chosen != this.controller.getSimulation()
                            .getAirport().getCurrentHLSColor()) {
                this.controller.getSimulation().getAirport()
                            .setCurrentHLSColor(chosen);
            }
        }
        else if(e.getSource() == this.minTickSpacing) {
            final Simulation simulation = this.controller.getSimulation();
            final int previousTick = simulation.getTickSpacing();
            
            int parsedValue = -1;
            
            try {
                parsedValue = Integer.parseInt(this.minTickSpacing.getText());
                
                if(parsedValue > 0) {
                    simulation.setTickSpacing(parsedValue);
                }
                else {
                    this.minTickSpacing.setText(Integer.toString(previousTick));                    
                }
            }
            catch(NumberFormatException nfEx) {
                this.minTickSpacing.setText(Integer.toString(previousTick));
            }
        }
        else if(e.getSource() == this.simulationTimeStep) {
            final Simulation simulation = this.controller.getSimulation();
            final double previousStep = simulation.getClock().getTimeStep();
            
            double parsedValue = -1.0d;
            
            try {
                parsedValue = Double.parseDouble(
                        this.simulationTimeStep.getText());
                
                if(Double.compare(parsedValue, 0) > 0) {
                    simulation.getClock().setTimeStep(parsedValue);
                }
                else {
                    this.simulationTimeStep.setText(
                            Double.toString(previousStep));
                }
            }
            catch(NumberFormatException nfEx) {
                this.simulationTimeStep.setText(Double.toString(previousStep));
            }
        }
    }
}
