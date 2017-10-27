package util.genererFeuilleDeRoute;
import java.util.ArrayList;
import java.util.Date;

import model.*;



/*
Départ de l’entrepot rue st mort - rue sainte cathy à 8h00
Prendre rue sainte cathy sur 800 metres
Prendre rue des maches sur 1200 metres
Arrivée au point de livraison rue des maches - rue caroline à 8h15
Livraison pendant 10 minutes
Départ du point de livraison rue des maches - rue Caroline à 8h25
Prendre rue Caroline sur 800 metres
Prendre avenue des Arts sur 800 metres
Prendre rue des sports sur 200 metres
Prendre chemin des Coquelicots sur 100 metres
Arrivée au point de livraison avenue jean jaures - chemin de coquelicot à 8h45
Attente pendant 15 minutes 
Livraison pendant 10 minutes
Départ du point de livraison avenue jean jaures - chemin de coquelicot à 9h10
Prendre avenue Jean Jaures sur 30 metres
Prendre rue st mort sur 200 metres
Arrivée à l’entrepot rue st mort - rue sainte cathy à 9h25
*/


/** Cette classe est une classe utilitaire permettant
 * la creation de la feuille de route de la tournee
 * calculee en format txt
 * 
 * @author Perrine
 *
 */


public class feuilleDeRouteTxt {

	
	public feuilleDeRouteTxt(){
	// Constructeur vide
	}
	
	
	public static String genererFeuilleDeRoute(Tournee tournee){
		String contenu = "";
		Date heureDep = tournee.getHeureDepart(); //a quoi ressemble cette date de merde?
		Date heureArr = tournee.getHeureArrive();
		Intersection entrepotAd = (tournee.getItineraire()).get(0).getOrigine(); //getOrigine donne une coordonnee?
		
		//Premiere ligne de depart 
		contenu = contenu + "Depart de l'entrepot "+ entrepotAd+ " a " + heureDep+ "\r\n";
		
		int it = 0;
		
		//Tant que la destination du chemin n'est pas l'adresse de l'entrepot
		//On parcout l'itineraire entier (= Liste de chemins) 
		while ((tournee.getItineraire()).get(it).getDestination() != entrepotAd) {
		
			//Pour tous les chemins, on recup adress dep adress arr
			//ainsi que heure arrivee au depart et heure de depart apres livraison
			for (int numChemin = 0; numChemin < 10000000; numChemin++) {
				Intersection origineCheminSuivant = null ;
				Intersection destinationCheminActuel = null ;
				Date heureFinCheminActuel = null ; 
				Date heureDepartCheminSuivant = null;
				int tempsAttente = 0;
	
				//Puis on recupere chaque troncon (nom de rue et longueur du troncon)
				//et le temps de la livraison d'arrivee de l'it
				for (int numTroncon = 0; numTroncon < 10000000; numTroncon++) {
					String rueAPrendre = "";
					double distanceTroncon = 10000; // A CONVERTIR EN METRES 
					contenu = contenu + "Prendre "+ rueAPrendre + " sur " + distanceTroncon+ " metres \r\n";
				}
				
				contenu = contenu + "Arrivee au point de livraison "+ destinationCheminActuel+ " a " + heureFinCheminActuel+ "\r\n";
				if (tempsAttente >0) {
					contenu = contenu + "Attente pendant "+ tempsAttente+ " minutes \r\n";
				}
				contenu = contenu + "Livraison pendant "+ " TEMPS DE LIVRAISON";
				//heureDepartPoint = heureFinCheminActuel + TEMPS DE LIVRAISON;
				contenu = contenu + "Depart du point de livraison "+ destinationCheminActuel + " a " + heureDepartCheminSuivant+ "\r\n";
			}
			
			it++;
		}
		
		
		//decomposer le dernier itineraire de retour jusque dans l'entrepot
		for (int numTroncon = 0; numTroncon < 10000000; numTroncon++) {
			String rueAPrendre = "";
			double distanceTroncon = 10000; // A CONVERTIR EN METRES 
			contenu = contenu + "Prendre "+ rueAPrendre + " sur " + distanceTroncon+ " metres \r\n";
		}
	
		//Derniere ligne d'arrivee
		contenu = contenu + "Arrivee a l'entrepot " + entrepotAd + " a " + heureArr +"\r\n";
	
	    return contenu;
	}
	/*
ouvrir systeme de gestion de fichier pour enregistrer  
importer la tournée calculée***
récupérer heure départ initial***
récupérer heure départ de chaque point***
récupérer intersection de point de livraison 
récupérer distance entre chaque point 
récupérer heure arrivée de chaque point
récupérer heure d’arrivée finale
récupérer temps de livraison 
récupérer attente s’il y a

}
*/
	

}