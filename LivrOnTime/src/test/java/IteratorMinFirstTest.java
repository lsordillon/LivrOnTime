import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import util.tsp.IteratorMinFirst;

public class IteratorMinFirstTest {

	
	IteratorMinFirst itr;
	

	@Test
	public void testIterator() {
		
		
		Collection<Integer> nonVu=new ArrayList<Integer>();
		nonVu.add(1);
		nonVu.add(2);
		nonVu.add(3);
		nonVu.add(4);
		
		Collection<Integer> resultat=new ArrayList<Integer>();
		resultat.add(3);
		resultat.add(1);
		resultat.add(2);
		resultat.add(4);
		
		
		long[][] cout=new long[1][5];
		
		cout[0][1]=6;
		cout[0][2]= 8;
		cout[0][3]= 2;
		cout[0][4]= 10;
		
		
		 itr=new IteratorMinFirst(nonVu, 0, cout) ;
		 int crt;
		 for(Integer res:resultat)
		 {
			 crt=itr.next().intValue();
			 assertEquals(res.intValue(),crt);
		 }
		
	}

}
