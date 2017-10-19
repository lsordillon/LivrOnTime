package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GrapheComplet {
	private int [] duree;
	private double [][] cout;

	
	public GrapheComplet (ArrayList<Livraison> listeLivraisons,double [][] coutI) {
		int taille = listeLivraisons.size();
		duree= new int [taille];
		cout= new double [taille][taille];
		
		for(int i=0;i<taille;i++) {
			for(int j=0;j<taille; j++) {
				cout[i][j]=coutI[i][j];
			}
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
