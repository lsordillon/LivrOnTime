package util.tsp;

import java.util.ArrayList;

public class TSP2 extends TSP1 {

	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, long[][] cout, long[] duree) {
		int borne = 0;
		long min = Long.MAX_VALUE;
		for (int i = 0; i < cout[sommetCourant].length; i++) {
			if ((cout[sommetCourant][i]) < min && sommetCourant != i) {
				min = cout[sommetCourant][i];
			}
		}
		borne += min;

		for (Integer nonVu : nonVus) {

			min = Long.MAX_VALUE;
			for (int i = 0; i < cout[nonVu].length; i++) {
				if ((cout[nonVu][i]) < min && nonVu != i) {
					min = cout[nonVu][i];
				}
			}
			borne += min + duree[nonVu];
		}
		return borne;
	}
}
