package util.tsp;

import java.util.ArrayList;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {

	@Override
	protected Iterator<Integer> iterator(Integer sommetCourant, ArrayList<Integer> nonVus, long[][] cout,
			long[] duree) {
		return new IteratorSeq(nonVus, sommetCourant);
	}

	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, long[][] cout, long[] duree) {
		return 0;
	}
}
