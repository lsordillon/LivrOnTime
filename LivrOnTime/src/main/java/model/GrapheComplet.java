package model;
import java.util.ArrayList;


public class GrapheComplet {
	private int [] duree;
	private double [][] couts;

	
	public GrapheComplet (ArrayList<Livraison> listeLivraisons,ArrayList<Intersection> listeIntersections,ArrayList<Chemin> chemins) {
		int taille = listeIntersections.size();
		duree= new int [taille];
		couts= new double [taille][taille];
	
		int i;
		//remplir les durees
		for(i=1;i<taille;i++) {
			duree[i]=listeLivraisons.get(i-1).getDuree();
		}
		
		//remplir le tableau de cout 
		for (i=0; i<taille;i++) {
			Intersection origine = listeIntersections.get(i);
			for (int j=0; j<taille;j++) {
				Intersection arrivee = listeIntersections.get(j);
				if(j!=i) {
					Chemin courant = trouverChemin(origine,arrivee,chemins);
					double cout=0;
					for(int k=0; k<courant.getTroncons().size(); k++) {
						cout=cout+courant.getTroncons().get(k).getLongueur();
					}
					couts[i][j]=cout;
				}
			}
		}
		
		
		
		
		
		
		
	}
	
	public Chemin trouverChemin (Intersection origine, Intersection destination, ArrayList<Chemin> chemins) {
		Chemin result=null;
		for(int i=0; i<chemins.size(); i++) {
			if(chemins.get(i).getDestination()==destination && chemins.get(i).getOrigine()==origine ) {
				result = chemins.get(i);
				break;
			}
		}
		return result;
	}
	
	//On peut renvoyer des tableaux ?
	public double[][] getCout () {
		return couts;
	}
	public int[] getDuree () {
		return duree;
	}

}
