package model;
import java.util.ArrayList;


/**
 * Classe nous permettant de creer tous les elements necessaires au fonctionnement du TSP
 * @author lauriesordillon
 *
 */
public class GrapheComplet {
	
	private long [] duree;
	private long [][] couts;

	public GrapheComplet (ArrayList<Livraison> listeLivraisons,ArrayList<Intersection> listeIntersections,ArrayList<Chemin> chemins) {
		int taille = listeIntersections.size();
		duree= new long [taille];
		couts= new long [taille][taille];
	
		int i;
		//remplir les durees : on stocke le temps passe a chaque point de livraison
		for(i=1;i<taille;i++) {
			duree[i]=listeLivraisons.get(i-1).getDuree()*1000;
		}
		
		//remplir le tableau de cout : on stocke le cout du trajet entre chaque point de livraison 
		for (i=0; i<taille;i++) {
			Intersection origine = listeIntersections.get(i);
			for (int j=0; j<taille;j++) {
				Intersection arrivee = listeIntersections.get(j);
				if(j!=i) {
					Chemin courant = trouverChemin(origine,arrivee,chemins);
					double cout=0;
					// on calcule le cout du chemin en sommant le cout des troncons
					for(int k=0; k<courant.getTroncons().size(); k++) {
						cout=cout+courant.getTroncons().get(k).getLongueur();
					}
					//Conversion d'une distance en durée
					couts[i][j]=(long)(cout*1000/Tournee.VITESSE);
				}
			}
		}
	}
	
	/**
	 * La methode trouverChemin renvoie le chemin entre 2 intersections donnees
	 * dans une liste de chemins
	 * @param origine
	 * @param destination
	 * @param chemins
	 * @return
	 */
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
	
	
	public long[][] getCout () {
		return couts;
	}
	public long[] getDuree () {
		return duree;
	}

}
