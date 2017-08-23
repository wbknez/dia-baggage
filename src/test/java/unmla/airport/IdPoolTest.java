package unmla.airport;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IdPoolTest {

	private IdPool	idPool;
	
	@Before
	public void setUp() throws Exception {
		this.idPool = new IdPool();
	}

	@After
	public void tearDown() throws Exception {
		this.idPool = null;
	}

	@Test
	public void testGetIdPool() {
		final IdPool idPool0 = IdPool.getIdPool();
		final IdPool idPool1 = IdPool.getIdPool();
		
		assertSame(idPool0, idPool1);
	}

	@Test
	public void testGetID() {
	    // pump a couple of numbers to demonstrate linearity
		assertEquals(2.0, this.idPool.getID(), 0);
		assertEquals(3.0, this.idPool.getID(), 0);
		
		this.idPool.getID();
	    this.idPool.getID();
	    this.idPool.getID();
	      
	    assertEquals(7.0, this.idPool.getID(), 0);  
	}

	@Test
	public void testReturnID() {
		final double id0 = this.idPool.getID();
		final double id1 = this.idPool.getID();
		
		this.idPool.returnID(id0);
		this.idPool.returnID(id1);
		
		assertEquals(id0, this.idPool.getID(), 0);
		assertEquals(id1, this.idPool.getID(), 0);
	}
}
