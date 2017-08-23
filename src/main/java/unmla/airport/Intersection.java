package unmla.airport;

public class Intersection<EdgeType> {
	public Intersection(EdgeType incoming, EdgeType outgoing) {
		
	}
	private EdgeType incoming;
	private EdgeType outgoing;
	private int id;
	
	public int getID() {
			return id;
	}
	public void setID(int i) {
		id=i;
	}
	public EdgeType getIncomingEdge() {
		return incoming;
	}
	public void setIncomingEdge(EdgeType e) {
		incoming=e;
	}
	public EdgeType getOutgoingEdge() {
		return outgoing;
	}
	public void setOutgoingEdge(EdgeType e) {
		outgoing=e;
	}

}
