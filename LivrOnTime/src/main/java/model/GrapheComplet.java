package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GrapheComplet {
	private int [] duree;
<<<<<<< HEAD
	private double [][] cout;
	private int[] equivalent;
=======
	private int [][] cout;

>>>>>>> branch 'master' of https://github.com/mtp24/LivrOnTime
	
	public GrapheComplet (ArrayList<Livraison> listeLivraisons,ArrayList <Chemin> analyse) {
		int taille = listeLivraisons.size();
		duree= new int [taille];
		cout= new int [taille][taille];
		
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
