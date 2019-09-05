import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;

import objects.BrickMap;

public class Test {
	private BrickMap bm = new BrickMap(3, 5);
	
	@Before public void setup() {
		
	}
	
	@org.junit.Test public void test() {
		System.out.println(bm.map.length);
		bm.addLayer();
		assertEquals(6, bm.map.length);
	}
	
}
