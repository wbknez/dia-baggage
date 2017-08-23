package unmla.airport;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import unmla.airport.graph.UpdatePathLabeler;
import unmla.airport.graph.WeightedEdgeTransformer;
import unmla.airport.graph.parser.AirportParser;
import unmla.airport.simul.Simulation;
import unmla.airport.simul.Steppable;
import unmla.airport.simul.TimeCounter;
import unmla.airport.ui.AirportUiComponent;
import unmla.airport.ui.UiComponent;
import unmla.airport.ui.UiProvider;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class Airport implements Steppable, UiProvider {
    
	private DirectedSparseGraph<LuggageRouter, LuggageTransporter> airportGraph;
	
	private final AirportUiComponent   uiComponent;
	private WeightedEdgeTransformer edgeWeights;
	private DijkstraShortestPath<LuggageRouter, LuggageTransporter> shortestPath;
	
	private final Map<String, Gate>  gates;
	private final Map<String, CheckIn> checkIns;
	private final Map<String, BaggageClaim> baggageClaims;
	
	private final List<Steppable>    updateList;

	private Statistics statistics = null;

	private IdPool idPool = null;
	
	private LandOfLostBags landOfLostBags = null;
	
	//Security scanner to be used only by the LuggageMoverPerson instances when they are moving a bag in system and it needs
	//  to get scanned
	private SecurityScanner luggageMoverPersonScanner = null;
	
	public Airport() {
        this.uiComponent = new AirportUiComponent();
        this.baggageClaims = new HashMap<String, BaggageClaim>();
        this.checkIns = new HashMap<String, CheckIn>();
        this.gates = new HashMap<String, Gate>();
        this.updateList = new ArrayList<Steppable>();
        
        this.createGraph();
        
        this.edgeWeights = new WeightedEdgeTransformer(airportGraph);
        this.shortestPath = new DijkstraShortestPath<LuggageRouter, LuggageTransporter>(airportGraph, edgeWeights, false);

        // protect against invalid pre-caching
        this.shortestPath.reset();
        this.buildUpdateList();
    }
	
	public LinkedList<LuggageTransporter> findPath(LuggageRouter s, LuggageRouter d, final double maxDistance) {
	    shortestPath.reset();
	    shortestPath.setMaxDistance(maxDistance);
		//find shortest path from s to d
		LinkedList<LuggageTransporter> path = (LinkedList<LuggageTransporter>) shortestPath.getPath(s,d);

		if(path.isEmpty()) {
			return path;
		} else {
			LinkedList<LuggageTransporter> completePath = new LinkedList<LuggageTransporter>();
			//add first vertex; the source for the first edge in path
			completePath.addLast(airportGraph.getSource(path.getFirst()));
			//iterate through path, add in vertices between edges, save as completePath
			ListIterator<LuggageTransporter> it = path.listIterator();
			while(it.hasNext()) {
				LuggageTransporter nextNode = it.next();
				completePath.addLast(nextNode);
				completePath.addLast(airportGraph.getDest(nextNode));
			}
			return completePath;
		}
	}
	
	//find length of shortest path, without specifying a maximum
	public double findDistance(LuggageRouter s, LuggageRouter t) {
		return  (shortestPath.getDistance(s,t)).doubleValue();
	}
	
	//Current Homeland Security Advisory Security Color
	private hlsColor currentHLSColor = hlsColor.BLUE;
	

	
	@Override
	public UiComponent getUiComponent() {
	    return this.uiComponent;
	}
	
	@Override
	public void setUiComponent(final UiComponent uiComponent) {
	    throw new UnsupportedOperationException();
	}
	
	public hlsColor getCurrentHLSColor() {
		return currentHLSColor;
	}

	public void setCurrentHLSColor(hlsColor currentHLSColor) {
	    System.out.println("Airport HLS set to: " + currentHLSColor);
		this.currentHLSColor = currentHLSColor;
	}
	
	
	public Graph<LuggageRouter, LuggageTransporter> getGraph() {
	    return this.airportGraph;
	}
	
	private void createGraph() {
	    try {
	        final AirportParser parser = new AirportParser();
	        final InputStream graphStream = 
	          Airport.class.getResourceAsStream("/data/denver_airport.graphml");
	        
	        this.airportGraph = parser.parse(graphStream);
	        
	        // set up the "ui"
	        this.uiComponent.setMinX(parser.getMinX());
	        this.uiComponent.setMinY(parser.getMinY());
	        this.uiComponent.setMaxX(parser.getMaxX());
	        this.uiComponent.setMaxY(parser.getMaxY());         
	    }
	    catch(IOException ioEx) {
	        
	    }
	    catch (ParserConfigurationException pcEx) {
            
        }
	    catch(SAXException sEx) {
	        
	    }
	}
	
	private void buildUpdateList() {
	    final UpdatePathLabeler updateLabeler = 
	                                new UpdatePathLabeler(this.airportGraph);
	    
	    updateLabeler.labelPath();
	    
	    this.baggageClaims.putAll(updateLabeler.getCarousels());
	    this.checkIns.putAll(updateLabeler.getCheckIns());
	    this.gates.putAll(updateLabeler.getGates());
	    
	    this.updateList.addAll(updateLabeler.getTraversedPath());
	}

    @Override
    public void step(double deltaTime, Simulation simulation) {
        // perform any other updates first
        
        for(int i = 0; i < this.updateList.size(); i++) {
            final Steppable obj = this.updateList.get(i);
            obj.step(deltaTime, simulation);
        }
    }
    
    @Override
    public void initialize(final Simulation simulation) {
        for(int i = 0; i < this.updateList.size(); i++) {
            final Steppable obj = this.updateList.get(i);
            obj.initialize(simulation);
        }
        this.statistics = simulation.getStatistics();
        
        this.statistics.setTotalEdges(this.airportGraph.getEdgeCount());
        this.statistics.setTotalNodes(this.airportGraph.getVertexCount());

		this.landOfLostBags = new LandOfLostBags(this);
        
        this.idPool = simulation.getIdPool();
        
        this.luggageMoverPersonScanner = new SecurityScanner("extraScanner", "special", 
                50, 5);
        this.luggageMoverPersonScanner.initialize(simulation);
    }
    
    @Override
    public void reset(final Simulation simulation) {
        for(int i = 0; i < this.updateList.size(); i++) {
            final Steppable obj = this.updateList.get(i);
            obj.reset(simulation);
        }
    }
    
    @Override
    public TimeCounter getTimeCounter() {
        throw new UnsupportedOperationException();
    }

    public ArrayList<Gate> getGates() {
    	return new ArrayList<Gate>(this.gates.values());
    }
    
    public ArrayList<BaggageClaim> getBaggageClaims() {
    	return new ArrayList<BaggageClaim>(this.baggageClaims.values());
    }

	public Statistics getStatistics() {
	    return this.statistics;
	}

	public IdPool getIdPool() {
		return this.idPool;
	}
	
	public LandOfLostBags getLandOfLostBags() {
		return this.landOfLostBags;
	}
	
	public SecurityScanner getLuggageMoverPersonScanner() {
		return luggageMoverPersonScanner;
	}
	
	public void removeLuggage(Luggage l) {
		this.idPool.returnID(l.getId());
		l = null;
	//	this.getStatistics().removeTotalLuggage();
	}


	
}
