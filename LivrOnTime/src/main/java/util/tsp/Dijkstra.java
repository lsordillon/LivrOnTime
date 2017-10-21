package util.tsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import model.*;

public class Dijkstra {
	
		public void algoDijkstra(Plan plan, Intersection ptDepart) {
			
			//INIT des éléments
			HashMap<Long,Intersection> intersections = plan.getIntersections();
			
			Collection <Intersection> inter = intersections.values();
			ArrayList <Intersection> sommetsBlancs = new ArrayList<Intersection> (inter);
			ArrayList <Intersection> sommetsGris = new ArrayList<Intersection> ();
			
			//Pré remplissage des predecesseurs à NULL et des couts à infini
			for (int i =0; i<sommetsBlancs.size();i++) {
				sommetsBlancs.get(i).setPredecesseur(null);
				sommetsBlancs.get(i).setDistance(Double.MAX_VALUE);
			}
			
			//Réalisation du processus sur le premier point 
			sommetsBlancs.remove(ptDepart);
			sommetsGris.add(ptDepart);
			ptDepart.setDistance(0.0);
			
			ArrayList <Troncon> versVoisins = ptDepart.getTronconsVersVoisins();
			Iterator<Troncon> it = versVoisins.iterator();
			Troncon nextTroncon;
			Intersection nextIntersection;
			
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
				if(sommetsGris.isEmpty()) {
					courant=sommetsBlancs.get(0);
					sommetsBlancs.remove(0);
					sommetsGris.add(courant);
				}
				else {
					courant = chercherMin(sommetsGris);
				}
				
				//System.out.println(courant);

				versVoisins = courant.getTronconsVersVoisins();
				
				it = versVoisins.iterator();
				
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
			
			HashMap<Long,Intersection> inters = plan.getIntersections();
			Collection <Intersection> inters2 = inters.values();
			ArrayList <Intersection> inters3 = new ArrayList<Intersection> (inters2);
			
			Iterator<Intersection> it2 = inters3.iterator();
			Intersection next2;
			
			while(it2.hasNext()){
				next2 = it2.next();
				//System.out.println(next2.getDistance());	
				//System.out.println(next2.getPredecesseur());
			}
			
			sommetsGris.clear();
			sommetsBlancs.clear();
		}
		
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
