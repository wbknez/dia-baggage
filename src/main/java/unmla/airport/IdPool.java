package unmla.airport;

import java.util.PriorityQueue;

public class IdPool {
	
	private static IdPool ids;
	public static IdPool getIdPool() {
		if( ids == null) ids = new IdPool();
		return ids;
	}

	private PriorityQueue<Double> idStore;
	private double nextID;
	public IdPool() {
		idStore = new PriorityQueue<Double>();
		nextID=1;
	}

	public double getID() {
		if (idStore.peek()==null) {
			nextID++;
			return nextID;
		} else {
			double i=idStore.peek();
			idStore.remove(i);
			return i;
		}
	}
	
	public void returnID(double i) {
		idStore.offer(i);
	}
}
