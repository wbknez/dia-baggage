package unmla.airport;

import java.util.Comparator;

/**
 * 
 */
public class ConveyorComparator implements Comparator<Luggage> {

    @Override
    public int compare(Luggage l0, Luggage l1) {
        final double time0 = l0.getTimeOnConveyor();
        final double time1 = l1.getTimeOnConveyor();
        
        final int compared = Double.compare(time0, time1);
        
        if(compared < 0) {
            return -1;
        }
        
        if(compared == 0) {
            return 0;
        }
        
        return 1;
    }

}
