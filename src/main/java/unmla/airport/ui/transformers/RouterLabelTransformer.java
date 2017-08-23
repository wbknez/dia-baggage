package unmla.airport.ui.transformers;

import org.apache.commons.collections15.Transformer;

import unmla.airport.LuggageRouter;

/**
 * 
 */
public class RouterLabelTransformer implements Transformer<LuggageRouter, 
                                                            String> {

    @Override
    public String transform(LuggageRouter router) {
        return router.getID() + " " + Integer.toString(router.getCapacity()) + "/" + Integer.toString(router.getMaxCapacity());
    }
}
