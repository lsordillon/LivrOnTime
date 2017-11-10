package util.tsp;

import java.util.ArrayList;
import java.util.Iterator;

public class TSP3 extends TSP2 {

	@Override
	protected Iterator<Integer> iterator(Integer sommetCourant, ArrayList<Integer> nonVus, long[][] cout,
			long[] duree) {
		return new IteratorMinFirst(nonVus, sommetCourant, cout);
	}
}
