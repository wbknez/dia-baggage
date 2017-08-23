package unmla.airport.ui.predicate;

import org.apache.commons.collections15.Predicate;

import unmla.airport.LuggageRouter;
import unmla.airport.LuggageTransporter;
import unmla.airport.simul.SimulationController;
import unmla.airport.ui.UiComponent;
import unmla.airport.ui.UiController;
import unmla.airport.ui.pick.PickData;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;

/**
 * 
 */
public class ShowRouterPredicate 
        implements Predicate<Context<Graph<LuggageRouter, LuggageTransporter>, 
                                            LuggageRouter>> {

    private final UiController  uiController;
    
    /**
     * Constructor.
     * 
     * @param controller
     * @throws NullPointerException
     */
    public ShowRouterPredicate(final SimulationController controller) {
        if(controller == null) {
            throw new NullPointerException();
        }
        
        this.uiController = controller.getUiController();
    }
    
    @Override
    public boolean evaluate(
     Context<Graph<LuggageRouter, LuggageTransporter>, LuggageRouter> context) {
        final PickData pickData = this.uiController.getPickData();
        
        final int uiLevel = pickData.getUiLevel();
        final UiComponent uiComponent = context.element.getUiComponent();
        
        return (uiLevel == uiComponent.getLevel() || 
                uiComponent.getLevel() == UiComponent.CoreLevel);
    }

}
