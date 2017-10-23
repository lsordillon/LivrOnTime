package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import util.tsp.Dijkstra;
import util.tsp.TSP;
import util.tsp.TSP1;

public class Plan {
	
	private ArrayList <Chemin> chemins=new ArrayList<Chemin>();
	private GrapheComplet graphe_complet;
	private HashMap<Long,Intersection> Intersections = new HashMap<Long, Intersection>();
	private ArrayList<Long> id_intersections=new ArrayList<Long>();
	
	private ArrayList<Troncon> Troncons = new ArrayList<Troncon>();
	

  public void CreerIntersections(NodeList noeuds) {
	  final int nbNoeuds = noeuds.getLength();
      for(int j = 0; j<nbNoeuds; j++) {
          final Element noeud = (Element) noeuds.item(j);
         long k = Long.parseLong((String) noeud.getAttribute("id"));
          Intersection intersection = new Intersection( k , Integer.parseInt(noeud.getAttribute("x")) , Integer.parseInt(noeud.getAttribute("y")));
          Intersections.put(k,intersection);
          id_intersections.add(k);
      } 
}
public void CreerTroncons(NodeList troncons) {
  final int nbTroncons = troncons.getLength();
  for(int j = 0; j<nbTroncons; j++) {
      final Element troncon = (Element) troncons.item(j);
      Long k = Long.parseLong(troncon.getAttribute("destination"));
      Intersection intersectionD = Intersections.get(k);
      k = Long.parseLong(troncon.getAttribute("origine"));
      Intersection intersectionO = Intersections.get(k); 
      Troncons.add(new Troncon(intersectionD, Double.parseDouble(troncon.getAttribute("longueur")), troncon.getAttribute("nomRue") , intersectionO));
  
  }

}

public void TronconsVoisins() {
	
	for(int i =0; i< Troncons.size();i++) {
		Troncons.get(i).getOrigine().setTronconsVersVoisins(Troncons.get(i));
	}
}


// ------------- Calcul Itinéraire

// L'intersection est lié à son numéro de placement dans la liste


public HashMap<Long, Intersection> getIntersections() {
	return Intersections;
}
public void setIntersections(HashMap<Long, Intersection> intersections) {
	Intersections = intersections;
}
public ArrayList<Troncon> getTroncons() {
	return Troncons;
}
public void setTroncons(ArrayList<Troncon> troncons) {
	Troncons = troncons;
}

public void deroulerLesDijkstra (DemandeLivraison dl) {		
	Dijkstra d = new Dijkstra();
	ArrayList <Intersection> origines=new ArrayList<Intersection>(dl.getIntersections());
	Date hDepart=dl.getHeureDepart();
	for (int j=0; j<origines.size();j++) {
		Intersection ptDepart = origines.get(j);
		
		d.algoDijkstra(this, ptDepart);
		ArrayList <Intersection> destinations=new ArrayList<Intersection>(dl.getIntersections());
		
		destinations.remove(ptDepart);
		
		
		for (int i=0; i<destinations.size();i++) {
			Chemin chemin = new Chemin();
			chemin.setOrigine(ptDepart);
			chemin.setDestination(destinations.get(i));
			
			Intersection courante = destinations.get(i);
			ArrayList<Troncon> troncons=new ArrayList<Troncon>();
			while (courante!=ptDepart) {
				Troncon troncon=trouverTroncon(courante.getPredecesseur(),courante);
				if(troncon!=null) {
					troncons.add(0, troncon);
				}
				courante=courante.getPredecesseur();
			}
			chemin.setTroncons(troncons);
			chemins.add(chemin);
		}
	} 	
}

public Troncon trouverTroncon (Intersection origine, Intersection destination) {
	Troncon result=null;
	for(int i=0; i<Troncons.size(); i++) {
		if(Troncons.get(i).getDestination()==destination && Troncons.get(i).getOrigine()==origine ) {
			result = Troncons.get(i);
			break;
		}
	}
	return result;
}

public Chemin trouverChemin (Intersection origine, Intersection destination) {
	Chemin result=null;
	for(int i=0; i<chemins.size(); i++) {
		if(chemins.get(i).getDestination()==destination && chemins.get(i).getOrigine()==origine ) {
			result = chemins.get(i);
			break;
		}
	}
	return result;
}

public Tournee calculerLaTournee(DemandeLivraison dl) {
	
	deroulerLesDijkstra(dl);
	
	graphe_complet=new GrapheComplet(dl.getLivraisons(),dl.getIntersections() ,chemins);
	int tpLimite = 10;
	TSP etape2 = new TSP1 ();
	int nbSommet=dl.getLivraisons().size()+1;
	ArrayList <Chemin> itineraire = new ArrayList<Chemin> ();
	
	etape2.chercheSolution(tpLimite,nbSommet, graphe_complet.getCout(),graphe_complet.getDuree());
	
	for(int i=0; i<nbSommet-1;i++) {
		Intersection origine = dl.getIntersections().get(etape2.getMeilleureSolution(i));
		Intersection destination = dl.getIntersections().get(etape2.getMeilleureSolution(i+1));
		itineraire.add(trouverChemin(origine,destination));
	}


	Intersection destination = dl.getIntersections().get(etape2.getMeilleureSolution(0));
	Intersection origine = dl.getIntersections().get(etape2.getMeilleureSolution(nbSommet-1));
	itineraire.add(trouverChemin(origine,destination));

	System.out.println("LA TOURNEE");
	Tournee tournee = new Tournee(itineraire,dl);
	for(int j=0;j<tournee.getItineraire().size();j++) {
		System.out.println(tournee.getItineraire().get(j).toString());
	}

	
	return tournee;
}

}
