package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GrapheComplet {
	private int [] duree;
	private double [][] cout;

	
	public GrapheComplet (ArrayList<Livraison> listeLivraisons,ArrayList <Chemin> analyse) {
		int taille = listeLivraisons.size();
		duree= new int [taille];
		cout= new double [taille][taille];
		
		for(int i=0;i<taille;i++) {
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
