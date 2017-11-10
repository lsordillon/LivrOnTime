

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;



public class PlanTest {
	Plan plan;
	HashMap<Long,Intersection> intersections = new HashMap<Long,Intersection>();
	ArrayList<Troncon> troncons = new ArrayList<>();
	
	@Before
	public void setUp(){
		Intersection int1 = new Intersection(1,0,0);
		Intersection int2 = new Intersection(2,0,1);
		Intersection int3 = new Intersection(3,0,3);
		intersections.put(new Long(1),int1);
		intersections.put(new Long(2),int2);
		intersections.put(new Long(3),int3);
		Troncon tr1 = new Troncon(int1, 10, "nomRue1", int2);
		Troncon tr2 = new Troncon(int1, 10, "nomRue2", int3);
		Troncon tr3 = new Troncon(int2, 10, "nomRue3", int3);
		troncons.add(tr1);
		troncons.add(tr2);
		troncons.add(tr3);
		plan = new Plan();
		plan.setIntersections(intersections);
	}
	
	@Test
	public void trouverCheminTest() {
		Troncon tronconCherché = new Troncon(intersections.get(new Long(1)), 10, "nomRue1", intersections.get(new Long(2)));		
		assertEquals(plan.trouverTroncon(intersections.get(new Long(1)), intersections.get(new Long(2))), tronconCherché );
		
	}

}
