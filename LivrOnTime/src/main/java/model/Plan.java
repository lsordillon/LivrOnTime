package model;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import util.tsp.Dijkstra;
import util.tsp.TSP;
import util.tsp.TSP2;

/**
 * Cette classe gère le chargement du plan et le lancement du calcul de la tournée
 * @author lauriesordillon
 *
 */
public class Plan {
	
	private ArrayList <Chemin> chemins=new ArrayList<Chemin>();
	private GrapheComplet graphe_complet;
	private HashMap<Long,Intersection> Intersections = new HashMap<Long, Intersection>();
	private ArrayList<Long> id_intersections=new ArrayList<Long>();
	
	private ArrayList<Troncon> Troncons = new ArrayList<Troncon>();
	

  public ArrayList<Long> getId_intersections() {
		return id_intersections;
	}
	public void setId_intersections(ArrayList<Long> id_intersections) {
		this.id_intersections = id_intersections;
	}
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

/**
 * Lance le dijkstra pour chaque point de Livraison + entrepot 
 * Trie et stocke les résultats
 * @param dl
 */
public void deroulerLesDijkstra (DemandeLivraison dl) {		
	Dijkstra d = new Dijkstra();
	
	//charge la liste des points de livraison + entrepot
	ArrayList <Intersection> origines=new ArrayList<Intersection>(dl.getIntersections());


	// Pour chaque point de livraison
	for (int j=0; j<origines.size();j++) {
		Intersection ptDepart = origines.get(j);
		
		//lance le calcul
		d.algoDijkstra(this, ptDepart);
		
		ArrayList <Intersection> destinations=new ArrayList<Intersection>(dl.getIntersections());
		destinations.remove(ptDepart);
		
		
		for (int i=0; i<destinations.size();i++) {
			// Pour chaque trajet reliant deux points de livraison, je cree un chemin
			Chemin chemin = this.creerChemin(ptDepart, destinations.get(i));
			chemins.add(chemin);
		}
	} 	
}

/**
 * Permet de renvoyer le troncon liant deux intersections
 * Parcours simplement la liste de troncon en comparant les origines et destinations
 * @param origine
 * @param destination
 * @return un troncon
 */
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

/**
 * Permet de renvoyer le chemin liant deux points
 * @param origine
 * @param destination
 * @return un chemin
 */
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

/**
 * Permet de créer un chemin suite à la réalisation d'un dijkstra (attention indispensable)
 * @param ptDepart
 * @param courante
 * @return le chemin correspondant
 */
public Chemin creerChemin (Intersection ptDepart,Intersection courante) {
	// Pour chaque trajet reliant deux points de livraison, je cree un chemin
	Chemin chemin = new Chemin();
	chemin.setOrigine(ptDepart);
	chemin.setDestination(courante);
				
	// Grace au predecesseur des intersections, je cree une liste de troncon qui constituent le chemin total
	ArrayList<Troncon> troncons=new ArrayList<Troncon>();
	while (courante!=ptDepart) {
		Troncon troncon=trouverTroncon(courante.getPredecesseur(),courante);
		if(troncon!=null) {
			troncons.add(0, troncon);
		}
		courante=courante.getPredecesseur();
	}
	chemin.setTroncons(troncons);
	return chemin;
}

/**
 * Calcul l'ensemble de la tournee a partir de la demande de livraison
 * realise tout d'abord des Dijkstra pour trouver les plus courts chemins entre chaque point
 * construit un graphe complet oriente
 * realise le TSP pour calculer l'itineraire le plus court
 * @param dl
 * @return
 */
public Tournee calculerLaTournee(DemandeLivraison dl) {
	
	//Mesure du temps n�cessaire � l'algorithme Dijkstra
	long debutDijkstra = System.currentTimeMillis();
	deroulerLesDijkstra(dl);
	long tpLimite = System.currentTimeMillis()-debutDijkstra;
	System.out.println("Algorithme Dijkstra : "+tpLimite+" ms");
	
	
	graphe_complet=new GrapheComplet(dl.getLivraisons(),dl.getIntersections() ,chemins);
	
	TSP etape2 = new TSP2 ();
	int nbSommet=dl.getLivraisons().size()+1;
	
	//Gestion des plages horaires dans un tableau 2d
	long[][] time = new long[dl.getLivraisons().size()+1][2];
	time[0][0]=-1;
	time[0][1]=Long.MAX_VALUE;
	for (int i= 0;i<dl.getLivraisons().size();i++) {
		time[i+1][0]=(dl.getLivraisons().get(i).getDebutPlageHoraire()==null? -1:dl.getLivraisons().get(i).getDebutPlageHoraire().getTime()-dl.getHeureDepart().getTime());
		time[i+1][1]=(dl.getLivraisons().get(i).getFinPlageHoraire()==null? Long.MAX_VALUE:dl.getLivraisons().get(i).getFinPlageHoraire().getTime()-dl.getHeureDepart().getTime());
	}
	
	etape2.chercheSolution(tpLimite,nbSommet, graphe_complet.getCout(),graphe_complet.getDuree(), time);
	
	ArrayList <Chemin> itineraire = new ArrayList<Chemin> ();
	for(int i=0; i<nbSommet-1;i++) {
		//on déroule le tableau MeilleureSolution du TSP pour creer l'itineraire
		//Pour chaque element du tableau, on cherche l'intersection correspondante. il s'agira a tour de role d'une origine et d'une destination
		//on cherche le chemin joignant ces deux intersections
		Intersection origine = dl.getIntersections().get(etape2.getMeilleureSolution(i));
		Intersection destination = dl.getIntersections().get(etape2.getMeilleureSolution(i+1));
		itineraire.add(trouverChemin(origine,destination));
	}

	//On ajoute le chemin entre le dernier point et le retour a l'entrepot
	Intersection destination = dl.getIntersections().get(etape2.getMeilleureSolution(0));
	Intersection origine = dl.getIntersections().get(etape2.getMeilleureSolution(nbSommet-1));
	itineraire.add(trouverChemin(origine,destination));

	System.out.println("LA TOURNEE");
	Tournee tournee = new Tournee(itineraire,dl);
	for(Chemin che:tournee.getItineraire()) {
		System.out.println(che);
	}

	
	return tournee;
}





}
