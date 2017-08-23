package unmla.airport;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unused")
public class SecurityScannerTest {
    
    private static final class TestScanner extends SecurityScanner {

        private boolean alerted;
        
        /**
         * @param id
         * @param uniqueId
         * @param defaultCapacity
         * @param defaultTime
         */
        public TestScanner(String id, String uniqueId, int defaultCapacity,
                double defaultTime) {
            super(id, uniqueId, defaultCapacity, defaultTime);
            
            this.alerted = false;
        }
        
        @Override
        public void alertTheAuthorities(final Luggage l) {
            this.alerted = true;
        }
    }

	SecurityScanner scanner0;
	SecurityScanner scanner1;
	private ArrayList<String> alertLuggageItems;
	private ArrayList<String> neverPassLuggageItems;
	
	//Collection of items that can go in a piece of luggage --> to use of buffered reader method way isn't working
    private List<String> luggageStuff;
	
	private List<String> bagcontent1; //fill with only good stuff
	private List<String> bagcontent2; //fill with a few bad stuff (3 items)
	private List<String> bagcontent3; //fill with an item on the never allowed list (1 item, fireworks)
	
	private hlsColor low_color;
	private hlsColor med_color;
	private hlsColor high_color;
	
	//stuff for luggage
	Luggage luggage1; //normal contents
	Luggage luggage2; //3 watched items
	Luggage luggage3; //1 not allowed item
	double id1;
	double id2;
	double flightTime1;
	double flightTime2;
	Gate gate;

	Airport DIA;
	LuggageMoverPerson dude;
	
	
	@Before
	public void setUp() throws Exception {
		scanner1 = new TestScanner("Scanner","S1",20,20);
		alertLuggageItems = new  ArrayList<String>(Arrays.asList("knife","gun","wire","tape","handcuffs",
					"metal box","shank","large dense object","medium dense object","small dense object","nunchucks", "box cutter","nail clippers",
					"screwdriver", "wrench", "cattle prod"));
		neverPassLuggageItems = new  ArrayList<String>(Arrays.asList("bomb","shoe bomb","aerosol","fuel","gasoline",
				"gas torche","lighter fluid","fireworks","gun powder","flare"));
		
		luggageStuff = new  ArrayList<String>(Arrays.asList("pen","headphones","laptop","notebook","knife","gun","wire",
				"tape","spoon","fork","souvenir","sunblock","shoes","pants","shirt","tie","suit","jacket","hat","mittens","handcuffs",
				"present","metal box", "hair brush","makeup","shank","board game","high heal","dress","socks","underwear","french horn",
				"cell phone charger","lap top charger","map","teddy bear","pillow","large dense object","medium dense object","small dense object",
				"scarf","gloves","flip flops","sandals","book","eReader","electronic tablet","cell phone","toiletries","lamp shade",
				"dog toy","cat toy","blanket","pj's","jeans","slacks","suit pants","skirt","stockings","dress shoes","shoe polish",
				"padfolio","nail clippers","bomb","shoe bomb","aerosol","fuel","gasoline","gas torche","lighter fluid","fireworks",
				"gun powder","flare","screwdriver", "wrench", "cattle prod"));
		
		bagcontent1 = new  ArrayList<String>(Arrays.asList("pen","headphones","laptop","notebook","teddy bear","pillow","scarf"));
		
		bagcontent2 = new  ArrayList<String>(Arrays.asList("knife","gun","wire","pen","headphones","laptop","notebook","teddy bear"));
		
		bagcontent3 = new  ArrayList<String>(Arrays.asList("pen","headphones","laptop","notebook","teddy bear","pillow","scarf",
				"gloves","flip flops","fireworks"));//,"large dense object","medium dense object","small dense object"));
		
		low_color = hlsColor.BLUE;
		med_color = hlsColor.YELLOW;
		high_color = hlsColor.RED;
		
		id1 = 1;
		id2 = 2;
		flightTime1 = 10;
		flightTime2 = 12;
		gate = new Gate("Test Gate", "Gate1", 20, 20, 5);
		luggage1 = new Luggage(id1, gate, flightTime1);
		luggage1.setLuggageContents((ArrayList<String>) bagcontent1);
		luggage2 = new Luggage(id2,gate, flightTime2);
		luggage2.setLuggageContents((ArrayList<String>) bagcontent2);
		luggage3 = new Luggage(id1, gate, flightTime1);
		luggage3.setLuggageContents((ArrayList<String>) bagcontent3);
		
		DIA = new Airport();
		dude = new LuggageMoverPerson(DIA);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSecurityScannerIsNull() {
		assertTrue(scanner0 == null);
	}
	
	@Test
	public void testSecurityScannerIsNotNull() {
		assertFalse(scanner1 == null);
	}
	

	//@Test
	public void testScanStuffOkBags() {
		//Test in low security
		assertTrue(scanner1.scanStuff(luggage1, low_color));
		//Test in med security 
		assertTrue(scanner1.scanStuff(luggage1, med_color));
		//Test in high security
		assertTrue(scanner1.scanStuff(luggage1, high_color));
		
	}
	
	@Test
	public void testScanStuffMaybeBags() {
		//Test in low security
		assertTrue(scanner1.scanStuff(luggage2, low_color));
	}
	@Test
	public void testScanStuffMaybeBags2() {
		//Test in med security 
		assertFalse(scanner1.scanStuff(luggage2, med_color));
	}
	@Test
	public void testScanStuffMaybeBags3() {
		//Test in high security
		assertFalse(scanner1.scanStuff(luggage2, high_color));
	}


		
	
	
	@Test
	public void testScanStuffNeverBags() {
		//Test in low security
		assertFalse(scanner1.scanStuff(luggage3, low_color));
	}
	@Test
	public void testScanStuffNeverBags2() {
		//Test in med security 
		assertFalse(scanner1.scanStuff(luggage3, med_color));
	}
	@Test
	public void testScanStuffNeverBags3() {
		//Test in high security
		assertFalse(scanner1.scanStuff(luggage3, high_color));
	}
	
	

	//@Test
	public void testDidBagPassRed() {
		
	}
	
	//@Test
	public void testDidBagPassOrange() {
		
	}
	
	//@Test
	public void testDidBagPassYellow() {
		
	}
	
	//@Test
	public void testDidBagPassGreen() {
		
	}

	//@Test
	public void testDidBagPassBlue() {
		
	}
}
