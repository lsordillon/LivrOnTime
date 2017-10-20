package util.tsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import model.*;

public class Dijkstra {
	
		public void algoDijkstra(Plan plan, Intersection ptDepart) {
			System.out.println("Dijkstra");
			HashMap<Long,Intersection> intersections = plan.getIntersections();
			
			Collection <Intersection> inter = intersections.values();
			ArrayList <Intersection> sommetsBlancs = new ArrayList<Intersection> (inter);
			ArrayList <Intersection> sommetsGris = new ArrayList<Intersection> ();
					
			for (int i =0; i<sommetsBlancs.size();i++) {
				sommetsBlancs.get(i).setPredecesseur(null);
				sommetsBlancs.get(i).setDistance(Double.MAX_VALUE);
			}
			
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
				Intersection courant = chercherMin(sommetsGris);
				System.out.println(courant);

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
			
			System.out.println("Fin");
			HashMap<Long,Intersection> inters = plan.getIntersections();
			Collection <Intersection> inters2 = inters.values();
			ArrayList <Intersection> inters3 = new ArrayList<Intersection> (inters2);
			
			Iterator<Intersection> it2 = inters3.iterator();
			Intersection next2;
			
			while(it2.hasNext()){
				next2 = it2.next();
				System.out.println(next2.getDistance());	
				System.out.println(next2.getPredecesseur());
			}
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
