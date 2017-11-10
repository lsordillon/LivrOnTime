import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import modele.Chemin;
import modele.GrapheComplet;
import modele.Intersection;

public class GrapheCompletTest {

	@Test
	public void testTrouverChemin1() {
		ArrayList<Chemin> chemins=new ArrayList();
		Chemin c=new Chemin();
		c.setOrigine(new Intersection(0,6,2));
		c.setDestination(new Intersection(1,8,4));
		chemins.add(c);
		GrapheComplet graphC=new GrapheComplet ( new ArrayList(), new ArrayList(),chemins);
		assertEquals(c,graphC.trouverChemin(new Intersection(0,6,2),new Intersection(1,8,4),chemins));
		 c=new Chemin();
		c.setOrigine(new Intersection(0,6,2));
		c.setDestination(new Intersection(3,9,0));
		chemins.add(c);
		assertEquals(c,graphC.trouverChemin(new Intersection(0,6,2),new Intersection(3,9,0),chemins));
		assertNull(graphC.trouverChemin(new Intersection(0,6,2),new Intersection(6,8,4),chemins));
	}
}