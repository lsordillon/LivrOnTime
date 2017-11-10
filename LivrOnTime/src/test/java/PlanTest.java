import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import modele.Chemin;
import modele.Intersection;
import modele.Plan;
import modele.Troncon;

public class PlanTest {




	
	@Test
	public void testTrouverChemin() {
		ArrayList<Chemin> chemins=new ArrayList();
		Chemin c=new Chemin();
		c.setOrigine(new Intersection(0,6,2));
		c.setDestination(new Intersection(1,8,4));
		chemins.add(c);
		Plan plan=new Plan();
		plan.setChemins(chemins);
		assertEquals(c,plan.trouverChemin(new Intersection(0,6,2),new Intersection(1,8,4)));
		 c=new Chemin();
		 c.setOrigine(new Intersection(0,6,2));
		 c.setDestination(new Intersection(3,9,0));
		 chemins.add(c);
		 plan.setChemins(chemins);
		 assertEquals(c,plan.trouverChemin(new Intersection(0,6,2),new Intersection(3,9,0)));
		 assertNull(plan.trouverChemin(new Intersection(0,6,2),new Intersection(6,8,4)));
	}

	
	
	
	@Test
	public void testTrouverTroncon()  {
		
		
		ArrayList<Troncon> troncons=new ArrayList();
		Troncon t=new Troncon(new Intersection(0,6,2),3,"rue1",new Intersection(1,8,4));
		troncons.add(t);
		Plan plan=new Plan();
		plan.setTroncons(troncons);
		assertEquals(t,plan.trouverTroncon(new Intersection(1,8,4),new Intersection(0,6,2)));
		t=new Troncon(new Intersection(0,6,2),4,"rue2",new Intersection(3,9,0));
		troncons.add(t);
		plan.setTroncons(troncons);
		assertNull(plan.trouverChemin(new Intersection(0,6,2),new Intersection(6,8,4)));
		
	}
}
