package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GrapheComplet {
	private int [] duree;
	private double [][] cout;
	private int[] equivalent;
	
	public GrapheComplet (ArrayList<Livraison> listeLivraisons) {
		equivalent= new int [listeLivraisons.size()];
		for(int i=0;i<listeLivraisons.size();i++) {
			duree[i]=listeLivraisons.get(i).getDuree();
		}
		
		
	}
	
	//On peut renvoyer des tableaux ?
	public double[][] getCout () {
		return cout;
	}
	public int[] getDuree () {
		return duree;
	}

}
