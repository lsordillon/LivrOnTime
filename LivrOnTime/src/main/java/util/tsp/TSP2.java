package util.tsp;

import java.util.ArrayList;


public class TSP2 extends TSP1 {


	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, long[][] cout, long[] duree) {
		int borne=0;
		long min=cout[sommetCourant][0]+duree[sommetCourant];
		for(int i=0;i<cout[sommetCourant].length;i++)
		{
			if((cout[sommetCourant][i])+duree[sommetCourant]<min&&sommetCourant!=i)
			{
				min=cout[sommetCourant][i]+duree[sommetCourant];
			}
		}
		borne+=min;
		
		for(Integer nonVu : nonVus)
		{
		
			min=cout[nonVu][0]+duree[nonVu];
			for(int i=0;i<cout[nonVu].length;i++)
			{
				if((cout[nonVu][i]+duree[nonVu])<min&&nonVu!=i)
				{
					min=cout[nonVu][i]+duree[nonVu];
				}
			}
			borne+=min;
		}		
		return borne;
	}
}
