package util.tsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import model.*;

/**
 * Cette classe est une classe utilitaire impl�mentant
 * l'algorithme de Dijkstra.
 * 
 * @author Matthieu
 *
 */
public class Dijkstra {
	
		/**
		 * M�thode principale de la classe, elle impl�mente l'algorithme de
		 * Dijkstra en calculant les plus cours chemins vers tous les noeuds d'un
		 * graphe � partir d'un noeud de d�part.
		 * 
		 * @param plan, le graphe utilis� pour l'algorithme rep�sentant la ville de Lyon
		 * @param ptDepart, l'intersection de d�part de l'algorithme
		 * 
		 */
		public void algoDijkstra(Plan plan, Intersection ptDepart) {
			
			//Creation des diff�rentes structures de donnees necessaires
			HashMap<Long,Intersection> intersections = plan.getIntersections();
			
			Collection <Intersection> inter = intersections.values();
			ArrayList <Intersection> sommetsBlancs = new ArrayList<Intersection> (inter);
			ArrayList <Intersection> sommetsGris = new ArrayList<Intersection> ();
			
			//Pres remplissage des predecesseurs a NULL et des couts a infini
			for (int i =0; i<sommetsBlancs.size();i++) {
				sommetsBlancs.get(i).setPredecesseur(null);
				sommetsBlancs.get(i).setDistance(Double.MAX_VALUE);
			}
			
			//Recuperation des voisins du premier point
			sommetsBlancs.remove(ptDepart);
			sommetsGris.add(ptDepart);
			ptDepart.setDistance(0.0);
			
			ArrayList <Troncon> versVoisins = ptDepart.getTronconsVersVoisins();
			Iterator<Troncon> it = versVoisins.iterator();
			Troncon nextTroncon;
			Intersection nextIntersection;
			
			//Relachement des arcs pour le premier point
			while(it.hasNext()) {
				nextTroncon = it.next();
				nextIntersection = nextTroncon.getDestination();
				sommetsGris.add(nextIntersection);
				sommetsBlancs.remove(nextIntersection);
				nextIntersection.setPredecesseur(ptDepart);
				nextIntersection.setDistance(nextTroncon.getLongueur());
			}
			
			sommetsGris.remove(ptDepart);
			
			
			while (!sommetsBlancs.isEmpty()||!sommetsGris.isEmpty()) {
				
				Intersection courant;
				//Recherche du noeud a examiner pour cette
				//iteration de l'algorithme
				if(sommetsGris.isEmpty()) {
					courant=sommetsBlancs.get(0);
					sommetsBlancs.remove(0);
					sommetsGris.add(courant);
				}
				else {
					courant = chercherMin(sommetsGris);
				}

				versVoisins = courant.getTronconsVersVoisins();
				
				it = versVoisins.iterator();
				
				//Relachement des arcs pour le noeud courrant
				while(it.hasNext()) {
					nextTroncon = it.next();
					nextIntersection = nextTroncon.getDestination();
					if (sommetsBlancs.contains(nextIntersection)) {
						sommetsGris.add(nextIntersection);
						sommetsBlancs.remove(nextIntersection);
					}
					if (sommetsGris.contains(nextIntersection)) {
						double distance = courant.getDistance() + nextTroncon.getLongueur();
						if(distance < nextIntersection.getDistance()) {
							nextIntersection.setPredecesseur(courant);
							nextIntersection.setDistance(distance);
						}
					}
				}
				
				sommetsGris.remove(courant);

			}
			
			//Tests d'affichage
			/*HashMap<Long,Intersection> inters = plan.getIntersections();
			Collection <Intersection> inters2 = inters.values();
			ArrayList <Intersection> inters3 = new ArrayList<Intersection> (inters2);
			
			Iterator<Intersection> it2 = inters3.iterator();
			Intersection next2;
			
			while(it2.hasNext()){
				next2 = it2.next();
				//System.out.println(next2.getDistance());	
				//System.out.println(next2.getPredecesseur());
			}*/
			
			sommetsGris.clear();
			sommetsBlancs.clear();
		}
		
		
		/**
		 * Cette m�thode est utilis�e pour l'algorithme de Dijkstra
		 * elle permet, � partir d'une liste de noeuds du graphe de
		 * trouver celui qui a la plus petite distance � partir
		 * de l'origine.
		 * 
		 * @param sommetsGris, la liste de noeuds du graphe qu'on consid�re
		 * @return suivant, le noeud de la liste d'entree dont la distance par
		 * rapport au point de depart est la plus petite
		 */
		public Intersection chercherMin (ArrayList<Intersection> sommetsGris) {
			
			Iterator<Intersection> it = sommetsGris.iterator();
			Intersection suivant = it.next();
			double coutMin = suivant.getDistance();
			
			Intersection next;
			while(it.hasNext()) {
				next = it.next();
				if (next.getDistance()<coutMin){
					coutMin = next.getDistance();
					suivant = next;
				}
			}
			
			return suivant;
		}
}
