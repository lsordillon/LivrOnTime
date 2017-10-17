package model;
import java.util.HashMap;
import java.util.Map;


public class GrapheComplet {
	public int [] duree;
	public int [][] cout;
	public int []equivalent;
	
	public GrapheComplet () {
		
	}
	
	//On peut renvoyer des tableaux ?
	public int[][] getCout () {
		return cout;
	}
	public int[] getDuree () {
		return duree;
	}

}
