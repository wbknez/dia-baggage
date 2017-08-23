package unmla.airport.ui.pick;

import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;

import java.awt.event.MouseEvent;

/**
 * 
 */
public class FixedTranslatingGraphMousePlugin 
                    extends TranslatingGraphMousePlugin {

    public FixedTranslatingGraphMousePlugin(final int modifiers) {
        super(modifiers);
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if(this.down != null) {
            super.mouseDragged(e);
        }
    }
}
