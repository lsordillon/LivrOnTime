package model;


//import Plan.Intersection;

public class GrapheComplet {
	public int [] duree;
	public int [][] cout;
	public long []equivalent;
	
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
