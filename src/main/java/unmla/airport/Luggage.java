package unmla.airport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;



/**
 * Luggage Class stores information about 
 *
 * First constructor is for running simulation NOT in real time
 * 
 */
@SuppressWarnings("unused")
public class Luggage {
    
    public static final double MaximumTime  = 15d * 60d;
	
  //Variables:
	//To store luggage id
	private double id;	
	
	//store destination
	private LuggageRouter destination;
	
	//flight time
	private double flightTime;
	
	//Keeps track of how long time 
	private double  timer = 0;

	//store whether bag has been through a security scanner or not
	private boolean scanned;
	
	//Keeps track of the bag's contents, 
	private ArrayList<String> luggageContents = new ArrayList<String>();
	
	//Keep track of how may items we want to store in each bag
	private int luggageContentsSize = 10;
	
	// For tracking a luggage's progress on a conveyor.
	private double timeOnConveyor  = 0;

	
	

	//Will hold the future path of the bag
	private LinkedList<LuggageTransporter> path = new LinkedList<LuggageTransporter>();

	//Will hold the past path of the bag
	private LinkedList<LuggageTransporter> previousPath = new LinkedList<LuggageTransporter>();
	
	//To store time when bag instance was created -> if real time simulation wanted to be run
	//Need this in constructor: bagStartTime = dateformat.format(cal.getTime());
	DateFormat dateformat = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
	Calendar cal = Calendar.getInstance();
	String bagStartTime;
	
	
	
  //Constructors:	
	/*This constructor assumes that something else is keeping track of what the next valid id number is
	* This Constructor is to be used when running simulation in NOT in real time
	* Input:
	* 	id: unique id to identify the piece of luggage
	* 	d: Destination, either a gate or a baggage claim
	* 	flightTime: time flight is taking off
	*/
	public Luggage(double id, LuggageRouter d, double flightTime) {
		this.id = id;
		this.setDestination(d);
		this.flightTime=flightTime;
		scanBagSet(d);
		resetTimer();
		//fillLuggage();
		this.fillLuggage();
	}
	
	
	
  //Methods:
	//Use this to set variable scanned
	//  if a bag is checked in at a CheckIn it WILL need to be scanned at a security scanner
	//  if a bag is checked in at a Gate it WILL NOT need to scanned at a security scanner
	private void scanBagSet(LuggageRouter d){
		if(this.destination instanceof Gate) {
			this.scanned = false;
		}
		if (this.destination instanceof BaggageClaim) {
			this.scanned = true;
		}
	}
	
	//Takes first element from future path adds to the first element of previousPath, then removes it
	//Will be used to keep track of path as bag moves through airport
	//This function assumes path is stored correctly and will be called correctly, and not when path is empty
	public void updateStoredPath(){
		if(!(path.isEmpty())) {
			previousPath.addFirst(path.pop());
		}
	}
	
	public void updateStoredPath(LuggageTransporter t) {
		if(!(path.isEmpty())) {
			path.pop();
			previousPath.addFirst(t);
		}
	}
	
	public void updateStoredPathBackward(){
		if(!(path.isEmpty())){
			path.addFirst(previousPath.pop());
		}
	}
	
	//Randomly fill a piece of luggage with out a file reader
	private void fillLuggage(){
		Random r = new Random();
		for (int i = 0; i < luggageContentsSize; i++){
			String random = luggageStuff.get(r.nextInt(luggageStuff.size()));
			this.luggageContents.add(random);
		}
	}
	

	//Randomly fill a piece of luggage with items in a file
	private void fillLuggageWithFile() {
		//Set up Buffered Reader to read from txt file to a list of strings
		BufferedReader breader = null;
		try{
		    final InputStream is = Luggage.class.getResourceAsStream("/data/LuggagContents.txt");
			breader = new BufferedReader(new InputStreamReader(is));
			ArrayList<String> lines = new ArrayList<String>();
			String line;// = breader.readLine();

			while((line = breader.readLine()) != null ) {
				lines.add(line);
				line = breader.readLine();
			}

			// Randomly choose from the list of strings 
			Random r = new Random();
		
			for (int i = 0; i < luggageContentsSize; i++){
				String random = lines.get(r.nextInt(lines.size()));
				this.luggageContents.add(random);
			}
			}
		catch (IOException e){
			e.printStackTrace();
			fillLuggage(); //Use alternative method to fill bag instead
		}
		finally{
			try{
				
				if( breader != null)
					breader.close();
			}
			catch(IOException e2){
				e2.printStackTrace();
			}
		}
	}
	
	
	//Increments timer by the amount passed in by double variable t
	public void incrementTimer(double t){
		this.timer += t;
	}
	
	//Resets timer to 0
	public void resetTimer(){
		this.timer = 0;
	}
	

	public static LinkedList<LuggageTransporter> findNewPath (Luggage l){
		return l.getPath();
	}
	
	
	//Method will return true if bag has been in system for over 15 minutes, false if not expired (has been in system for less than 15 minutes)
	public boolean bagexpired (){
		if (this.timer > MaximumTime){
			return true;
		}
		else
			return false;
	}
	
	//if top entry in previousPath doesn't match the given object, luggage is lost
	public boolean isLost(LuggageTransporter r) {
		if(previousPath.isEmpty()) {
			if(((r instanceof CheckIn)&&(this.destination instanceof Gate))||((r instanceof Gate)&&(this.destination instanceof BaggageClaim))) {
				return false;
				} else {
					return true;
				}
		} else {
			return !((r.getID()).equals(this.previousPath.peek().getID()));
		}
	}
	
  //Getters & Setters:
	public double getId() {
		return id;
	}

	//returns true if bag has been scanned
	public boolean getScanned() {
		return this.scanned;
	}
	
	public void setScanned(boolean b) {
		this.scanned = b;
	}
	
	//Returns the gate or baggage claim the bag is heading to
	public LuggageRouter getDestination() {
	    return this.destination;
	}
	
	//set destination for bag
	public void setDestination(LuggageRouter d) {
		this.destination = d;
	}
	
	public double getFlightTime() {
		return this.flightTime;
	}
	
	public void setFlightTime(double time) {
		this.flightTime=time;
	}
	
	public LinkedList<LuggageTransporter> getPath() {
		return path;
	}
	
	public void setPath(LinkedList<LuggageTransporter> path) {
		this.path = path;
	}


	public LuggageTransporter getNextStep() {
		if(path.isEmpty()||path==null) {
			return null;
		} else {
			//return next step in path (will be first element in linked list)
			return path.getFirst();
		}
	}

	public LinkedList<LuggageTransporter> getPreviousPath() {
		return previousPath;
	}

	public void setPreviousPath(LinkedList<LuggageTransporter> previousPath) {
		this.previousPath = previousPath;
	}
	
	public LuggageTransporter getPreviousStep() {
		if(previousPath.isEmpty()||path==null) {
			return null;
		} else {
			//return previous step in path (will be first element in this linked list)
			return previousPath.getFirst();
		}
	}

	//Returns current value of how long a piece of luggage has been in the airport's system
	public double getTimer(){
		return timer;
	}
	
	//Returns the ArrayList containing the contents of the luggage
	public ArrayList<String> getLuggageContents() {
		return luggageContents;
	}
	
	public void setLuggageContents(ArrayList<String> contents) {
		this.luggageContents = contents;
	}

	public double getTimeOnConveyor() {
	    return this.timeOnConveyor;
	}
	
	public void clearTimeOnConveyor() {
	    this.timeOnConveyor = 0;
	}
	
	public void incrementTimeOnConveyor(final double deltaTime) {
	    this.timeOnConveyor += deltaTime;
	}
	
	//Collection of items that can go in a piece of luggage --> to use of buffered reader method way isn't working
		private List<String> luggageStuff = new  ArrayList<String>(Arrays.asList("pen","headphones","laptop","notebook","knife","gun","wire",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens","handcuffs",
				"present","metal box", "hair brush","makeup","shank","board game","high heal","dress","socks","underwear","french horn",
				"cell phone charger","lap top charger","map","teddy bear","pillow","large dense object","medium dense object","small dense object",
				"scarf","gloves","flip flops","sandals","book","eReader","electronic tablet","cell phone","toiletries","lamp shade",
				"dog toy","cat toy","blanket","pj's","jeans","slacks","suit pants","skirt","stockings","dress shoes","shoe polish",
				"padfolio","nail clippers","bomb","gasoline","fireworks","gun powder","flare","screwdriver", "wrench", "cattle prod",
				"cards","towel","mints","postits","glasses","socks","yarn","wooden fan","paper clip","highlighter","hand sanatizer",
				"resume","buisnes card","dictionary","green tea bags","instant coffee grounds","CD","tape","DVD","guide book",
				"translation book","study guide","homework","demo guideline","papers to grade","final exams","sneakers",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"cards","towel","mints","postits","glasses","socks","yarn","wooden fan","paper clip","highlighter","hand sanatizer",
				"resume","buisnes card","dictionary","green tea bags","instant coffee grounds","CD","tape","DVD","guide book",
				"translation book","study guide","homework","demo guideline","papers to grade","final exams","sneakers",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"cards","towel","mints","postits","glasses","socks","yarn","wooden fan","paper clip","highlighter","hand sanatizer",
				"resume","buisnes card","dictionary","green tea bags","instant coffee grounds","CD","tape","DVD","guide book",
				"translation book","study guide","homework","demo guideline","papers to grade","final exams","sneakers",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"cards","towel","mints","postits","glasses","socks","yarn","wooden fan","paper clip","highlighter","hand sanatizer",
				"resume","buisnes card","dictionary","green tea bags","instant coffee grounds","CD","tape","DVD","guide book",
				"translation book","study guide","homework","demo guideline","papers to grade","final exams","sneakers",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens"));

}
