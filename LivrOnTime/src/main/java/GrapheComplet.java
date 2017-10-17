import java.util.HashMap;
import java.util.Map;

import Plan.Intersection;

public class GrapheComplet {
	public int [] duree;
	public int [][] cout;
	public int []equivalent;
	
	public GrapheComplet (HashMap<Long,Intersection> intersections) {
		
		equivalent=new int[intersections.size()];// Etape de création d'équivalent. cera présent dans Plan, a supprimer
		int iterator=0;
		for (Map.Entry key : intersections.entrySet()) {
			equivalent[iterator]=(Integer) key.getKey();
			iterator++;
		}
		
		//construction de durée
		
	}
	
	//On peut renvoyer des tableaux ?
	public int[][] getCout () {
		return cout;
	}
	public int[] getDuree () {
		return duree;
	}

}
