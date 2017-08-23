package unmla.airport;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LuggageTest {

	Luggage luggage0;
	Luggage luggage1; //destination gate
	Luggage luggage2; //destination baggage claim
	double id1;
	double id2;
	double flightTime1;
	double flightTime2;
	Gate gate;
	BaggageClaim bagclaim;
	

	@Before
	public void setUp() throws Exception {
		//System.out.println("@Before - setUp");
		id1 = 1;
		id2 = 2;
		flightTime1 = 10;
		flightTime2 = 20;
		gate = new Gate("Test Gate", "Gate1", 20, 20, 5);
		bagclaim = new BaggageClaim("Test BC", "BC1", 50, 50);
		luggage1 = new Luggage(id1,gate, flightTime1);
		luggage2 = new Luggage(id2,bagclaim, flightTime2);
	}

	@After
	public void tearDown() throws Exception {
		//System.out.println("@After - tearDown");
	}

	@Test
	public void testLuggageIsNull() {
		assertTrue(luggage0 ==null);
	}

	@Test
	public void testLuggageIsNotNull() {
		assertFalse(luggage1 ==null);
	}
	
	@Test
	public void testLuggagetoBagClaimIsNotNul() {
		assertFalse(luggage2 ==null);
	}
	
	@Test
	public void testScanBagSet(){
		assertFalse(luggage1.getScanned());
	}
	
	@Test
	public void testScanBagSetBC(){
		assertTrue(luggage2.getScanned());
	}
	

	@Test
	public void testIncrementTimer() {
		luggage1.incrementTimer(2);
		assertTrue(luggage1.getTimer() == 2);
	}
	
	@Test
	public void testIncrementTimerBC() {
		luggage1.incrementTimer(4);
		assertTrue(luggage1.getTimer() == 4);
	}

	@Test
	public void testResetTimer() {
		luggage1.incrementTimer(20);
		luggage1.resetTimer();
		assertTrue(luggage1.getTimer() == 0);
	}
	
	@Test
	public void testResetTimerBC() {
		luggage2.incrementTimer(20);
		luggage2.resetTimer();
		assertTrue(luggage2.getTimer() == 0);
	}


	@Test
	public void testBagexpired() {
		luggage1.incrementTimer(10*60);
		assertFalse(luggage1.bagexpired());
		luggage1.incrementTimer(10*60);
		assertTrue(luggage1.bagexpired());
	}
	
	@Test
	public void testBagexpiredBC() {
		luggage2.incrementTimer(10*60);
		assertFalse(luggage2.bagexpired());
		luggage2.incrementTimer(10*60);
		assertTrue(luggage2.bagexpired());
	}

	@Test
	public void testgetTimer() {
		luggage1.incrementTimer(10);
		assertTrue(luggage1.getTimer() == 10);
	}
	
	@Test
	public void testgetTimerBC() {
		luggage2.incrementTimer(10);
		assertTrue(luggage2.getTimer() == 10);

	}
	

	@Test
	public void testGetId() {
		assertTrue(id1 == luggage1.getId());
	}
	
	@Test
	public void testGetIdBC() {
		assertTrue(id2 == luggage2.getId());
	}

	@Test
	public void testGetScanned() {
		assertFalse(luggage1.getScanned());
	}

	@Test
	public void testGetScannedBC() {
		assertTrue(luggage2.getScanned());
	}
	
	@Test
	public void testSetScanned() {
		luggage1.setScanned(true);
		assertTrue(luggage1.getScanned());
	}
	
	@Test
	public void testSetScannedBC() {
		luggage2.setScanned(false);
		assertFalse(luggage2.getScanned());
	}
	
	//Test get scanned after they have been set
	@Test
	public void testGetScannedAfter() {
		assertFalse(luggage1.getScanned());
	}

	@Test
	public void testGetScannedAfterBC() {
		assertTrue(luggage2.getScanned());
	}

	@Test
	public void testGetDestination() {
		assertTrue(luggage1.getDestination() instanceof Gate);
	}
	
	@Test
	public void testGetDestinationBC() {
		assertTrue(luggage2.getDestination() instanceof BaggageClaim);
	}

	@Test
	public void testSetDestination() {
		assertFalse(luggage1.getDestination() == null);
	}
	
	@Test
	public void testSetDestinationBC() {
		assertFalse(luggage2.getDestination() == null);
	}

	@Test
	public void testGetFlightTime() {
		assertTrue(flightTime1 == luggage1.getFlightTime() );
	}
	
	@Test
	public void testGetFlightTimeBC() {
		assertTrue(flightTime2 == luggage2.getFlightTime() );
	}

	
	@Test
	public void testSetFlightTime() {
		flightTime1 = 8;
		luggage1.setFlightTime(flightTime1);
		assertTrue(flightTime1 == luggage1.getFlightTime());
	}
	
	@Test
	public void testSetFlightTimeBC() {
		flightTime2 = 22;
		luggage2.setFlightTime(flightTime2);
		assertTrue(flightTime2 == luggage2.getFlightTime());
	}



}
