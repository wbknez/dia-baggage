package unmla.airport;

import java.util.Comparator;

public class LuggageComparator implements Comparator<Luggage> {
	public int compare(Luggage l1, Luggage l2) {
		double t1=l1.getTimer()-l1.getFlightTime();
		double t2=l2.getTimer()-l2.getFlightTime();

		if(t1<t2) { 
			return (int) -1; 
		} else {
			if(t1>t2) {
				return (int) 1;
			} else {
				return (int) 0;
			}
		}
	}
}
