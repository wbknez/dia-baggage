package unmla.airport.ui.pick;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;
import unmla.airport.simul.SimulationController;
import unmla.airport.ui.popup.SetStatePopUp;
import unmla.airport.ui.popup.CommandPopUp;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractGraphMousePlugin;

/**
 * 
 */
public class AlterGraphStatePlugin extends AbstractGraphMousePlugin 
                                                implements MouseListener {

    private final SimulationController controller;
    
    public AlterGraphStatePlugin(final SimulationController controller) {
        this(controller, MouseEvent.BUTTON3_MASK);
    }
    
    /**
     * Constructor.
     * 
     * @param modifiers
     */
    public AlterGraphStatePlugin( final SimulationController controller, 
                                    int modifiers) {
        super(modifiers);
        
        if(controller == null) {
            throw new NullPointerException();
        }
        
        this.controller = controller;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() != MouseEvent.BUTTON3) {
            return;
        }
        
        final VisualizationViewer<LuggageRouter, LuggageTransporter> viewer = 
          (VisualizationViewer<LuggageRouter, LuggageTransporter>)e.getSource();
        final Point2D position = e.getPoint();
        
        final GraphElementAccessor<LuggageRouter, LuggageTransporter> 
                pickSupport = viewer.getPickSupport();
        
        if(pickSupport != null) {
            final LuggageRouter pickedVertex = pickSupport.getVertex(
                                                        viewer.getGraphLayout(), 
                                                        position.getX(), 
                                                        position.getY());
            final LuggageTransporter pickedEdge = pickSupport.getEdge(
                                                        viewer.getGraphLayout(), 
                                                        position.getX(), 
                                                        position.getY());
            
            final LuggageTransporter target = (pickedVertex != null ? 
                                                pickedVertex : 
                                                    (pickedEdge != null ? 
                                                            pickedEdge : null));
            
            if(target == null) {
                // show a command switcher instead
                final CommandPopUp command = 
                                    new CommandPopUp(controller);
                
                if(command.getComponentCount() > 0) {
                    command.show(viewer, e.getX(), e.getY());
                }
            }
            else {

                final SetStatePopUp statePopup = new SetStatePopUp(controller, 
                                                                        target);
                
                if(statePopup.getComponentCount() > 0) {
                    statePopup.show(viewer, e.getX(), e.getY());
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}
