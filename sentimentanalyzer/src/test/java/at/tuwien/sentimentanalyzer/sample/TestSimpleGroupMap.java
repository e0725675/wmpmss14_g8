package at.tuwien.sentimentanalyzer.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestSimpleGroupMap {
	Double gk01=0.1;
	Double gk02=0.2;
	Double gk03=0.3;
	Double gk04=0.4;
	Double gk05=0.5;
	
	Integer k01=1;
	Integer k02=2;
	Integer k03=3;
	Integer k04=4;
	Integer k05=5;
	
	String v01="v01";
	String v02="v02";
	String v03="v03";
	String v04="v04";
	String v05="v05";
	
	@Before
	public void before() {
		
	}
	@Test
	public void testOne() {

		SimpleGroupMap<Double,Integer,String> map = new SimpleGroupMap<Double,Integer,String>();
		
		assertFalse(map.containsGroup(gk01));
		assertFalse(map.containsKey(k01));
		assertFalse(map.contains(gk01, k01));
		
		assertTrue(map.put(gk01, k01, v01));
		
		assertTrue(map.containsGroup(gk01));
		assertTrue(map.containsKey(k01));
		assertTrue(map.contains(gk01, k01));
		
		assertEquals(v01,map.get(gk01, k01));
		
		assertTrue(map.put(gk01, k01, v02));
		
		assertEquals(v02,map.get(gk01, k01));
		
	}
	@Test
	public void testMultiple() {
		SimpleGroupMap<Double,Integer,String> map = new SimpleGroupMap<Double,Integer,String>();
		assertTrue(map.put(gk01, k01, v01));
		assertTrue(map.put(gk01, k02, v02));
		assertTrue(map.put(gk01, k03, v03));
		assertTrue(map.put(gk01, k04, v03));
		
		assertEquals(4,map.getByGroupKey(gk01).size());
		assertEquals(1,map.getByKey(k01).size());
		
		assertTrue(map.put(gk02, k01, v01));
		
		assertEquals(4,map.getByGroupKey(gk01).size());
		assertEquals(2,map.getByKey(k01).size());
		
		assertTrue(map.put(gk03, k05, v05));
		
		assertEquals(3,map.groupKeySet().size());
		assertEquals(5,map.keySet().size());
		
		assertEquals(6,map.values().size());
		
	}
}
