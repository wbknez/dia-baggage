package unmla.airport.ui;

import java.awt.event.InputEvent;

import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;
import unmla.airport.simul.SimulationController;
import unmla.airport.ui.pick.AlterGraphStatePlugin;
import unmla.airport.ui.pick.FixedTranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;

/**
 * 
 */
public class UiMouse extends AbstractModalGraphMouse {

    private final SimulationController  controller;
    
    private  AlterGraphStatePlugin      setStatePlugin;
    
    public UiMouse(final SimulationController controller) {
        this(controller, 1.1f, 1/1.1f);
    }
    
    public UiMouse(final SimulationController controller, 
                    final float in, final float out) {
        super(in, out);
        
        if(controller == null) {
            throw new NullPointerException();
        }
        
        this.controller = controller;
        
        this.loadPlugins();
    }

    @Override
    protected void loadPlugins() {
        this.pickingPlugin = 
               new PickingGraphMousePlugin<LuggageRouter, LuggageTransporter>(
                       InputEvent.CTRL_MASK | InputEvent.BUTTON1_MASK, 
                       InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK | 
                           InputEvent.BUTTON1_MASK);
        this.scalingPlugin = new ScalingGraphMousePlugin(
                        new CrossoverScalingControl(), 
                        0, 
                        this.in, 
                        this.out);
        this.setStatePlugin = new AlterGraphStatePlugin(this.controller);
        this.translatingPlugin = new FixedTranslatingGraphMousePlugin(
                        InputEvent.BUTTON1_MASK);
        
        this.add(this.pickingPlugin);
        this.add(this.scalingPlugin);
        this.add(this.setStatePlugin);
        this.add(this.translatingPlugin);
    }
}
