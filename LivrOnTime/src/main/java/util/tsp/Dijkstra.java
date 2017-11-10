package util.tsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import modele.*;

/**
 * Cette classe est une classe utilitaire implementant
 * l'algorithme de Dijkstra.
 * 
 * @author Matthieu
 *
 */
public class Dijkstra {
	
		/**
		 * Methode principale de la classe, elle implemente l'algorithme de
		 * Dijkstra en calculant les plus cours chemins vers tous les noeuds d'un
		 * graphe a partir d'un noeud de depart.
		 * 
		 * @param plan, le graphe utilise pour l'algorithme representant la ville de Lyon
		 * @param ptDepart, l'intersection de depart de l'algorithme
		 * 
		 */
		public void algoDijkstra(Plan plan, Intersection ptDepart) {
			
			//Creation des differentes structures de donnees necessaires
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
			Troncon tronconSuivant;
			Intersection intersectionSuivante;
			
			//Relachement des arcs pour le premier point
			while(it.hasNext()) {
				tronconSuivant = it.next();
				intersectionSuivante = tronconSuivant.getDestination();
				sommetsGris.add(intersectionSuivante);
				sommetsBlancs.remove(intersectionSuivante);
				intersectionSuivante.setPredecesseur(ptDepart);
				intersectionSuivante.setDistance(tronconSuivant.getLongueur());
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
					tronconSuivant = it.next();
					intersectionSuivante = tronconSuivant.getDestination();
					if (sommetsBlancs.contains(intersectionSuivante)) {
						sommetsGris.add(intersectionSuivante);
						sommetsBlancs.remove(intersectionSuivante);
					}
					if (sommetsGris.contains(intersectionSuivante)) {
						double distance = courant.getDistance() + tronconSuivant.getLongueur();
						if(distance < intersectionSuivante.getDistance()) {
							intersectionSuivante.setPredecesseur(courant);
							intersectionSuivante.setDistance(distance);
						}
					}
				}
				
				sommetsGris.remove(courant);

			}
			
			sommetsGris.clear();
			sommetsBlancs.clear();
		}
		
		
		/**
		 * Cette methode est utilisee pour l'algorithme de Dijkstra
		 * elle permet, a partir d'une liste de noeuds du graphe de
		 * trouver celui qui a la plus petite distance a partir
		 * de l'origine.
		 * 
		 * @param sommetsGris, la liste de noeuds du graphe qu'on considere
		 * @return suivant, le noeud de la liste d'entree dont la distance par
		 * rapport au point de depart est la plus petite
		 */
		public Intersection chercherMin (ArrayList<Intersection> sommetsGris) {
			
			Iterator<Intersection> it = sommetsGris.iterator();
			Intersection suivant = it.next();
			double coutMin = suivant.getDistance();
			
			Intersection intersectionSuivante;
			while(it.hasNext()) {
				intersectionSuivante = it.next();
				if (intersectionSuivante.getDistance()<coutMin){
					coutMin = intersectionSuivante.getDistance();
					suivant = intersectionSuivante;
				}
			}
			
			return suivant;
		}
}
