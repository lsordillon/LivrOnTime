package util.genererFeuilleDeRoute;
import java.util.ArrayList;
import java.util.Date;

import model.*;



/*
D�part de l�entrepot rue st mort - rue sainte cathy � 8h00
Prendre rue sainte cathy sur 800 metres
Prendre rue des maches sur 1200 metres
Arriv�e au point de livraison rue des maches - rue caroline � 8h15
Livraison pendant 10 minutes
D�part du point de livraison rue des maches - rue Caroline � 8h25
Prendre rue Caroline sur 800 metres
Prendre avenue des Arts sur 800 metres
Prendre rue des sports sur 200 metres
Prendre chemin des Coquelicots sur 100 metres
Arriv�e au point de livraison avenue jean jaures - chemin de coquelicot � 8h45
Attente pendant 15 minutes 
Livraison pendant 10 minutes
D�part du point de livraison avenue jean jaures - chemin de coquelicot � 9h10
Prendre avenue Jean Jaures sur 30 metres
Prendre rue st mort sur 200 metres
Arriv�e � l�entrepot rue st mort - rue sainte cathy � 9h25
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
		int nbChemin = tournee.getItineraire().size();
		Date heureDep = tournee.getHeureDepart(); //a quoi ressemble cette date de merde? Fri Oct 27 08:00:00 CEST 2017
		Date heureArr = tournee.getHeureArrive(); // Fri Oct 27 09:12:08 CEST 2017
		Intersection entrepotAd = (tournee.getItineraire()).get(0).getOrigine(); // rend ID =25303798NbTroncons2
		
		//Premiere ligne de depart 
		contenu = contenu + "Depart de l'entrepot "+ entrepotAd+ " a " + heureDep+ "\r\n";
		
		

		//Pour tous les chemins, on recup adress dep adress arr
		//ainsi que heure arrivee au depart et heure de depart apres livraison
		for (int numChemin = 0; numChemin < nbChemin; numChemin++) {
			Chemin cheminActuel = ((tournee.getItineraire()).get(numChemin));
			contenu = contenu + retranscriptionDunChemin(cheminActuel);
		}
		

	
		//Derniere ligne d'arrivee
		contenu = contenu + "Arrivee a l'entrepot " + entrepotAd + " a " + heureArr +"\r\n";
	
	    return contenu;
	}
	
	private static boolean testIdentique(Troncon premierT, Troncon deuxiemeT) {
		boolean identique = false;
		if (premierT.getNomRue().equals(deuxiemeT.getNomRue())) {
			identique = true;
		}
		return identique;
	}

	private static String retranscriptionDunChemin (Chemin cheminActuel) {
		String contenu = "";
		int nbTroncons = (cheminActuel.getTroncons().size());
		Intersection destinationCheminActuel = cheminActuel.getDestination() ;
		Date heureFinCheminActuel = cheminActuel.getHeureArrivee() ; 
		int tempsAttente = 0;
		System.out.println("Test4 : test chemin actuel et heure fin chemin actuel"+destinationCheminActuel+ "heure"+ heureFinCheminActuel);
		
		
		//On recupere chaque troncon (nom de rue et longueur du troncon) SAUF LE DERNIER
		//et le temps de la livraison d'arrivee de l'it
		
		
		//si 1 troncon tout va bien 
		if (nbTroncons ==1) {
			Troncon tronconActuel = cheminActuel.getTroncons().get(0);
			String rueAPrendre = tronconActuel.getNomRue();
			double distanceTroncon = tronconActuel.getLongueur();
			contenu = contenu + "Prendre "+ rueAPrendre + " sur " + distanceTroncon+ " metres \r\n";
			System.out.println("Test5 troncon unique : test rue troncon actuel et taille" + rueAPrendre + distanceTroncon);
		}
		
		//si 2 troncons petit test
		if (nbTroncons ==2) {
			Troncon tronconActuel = cheminActuel.getTroncons().get(0);
			Troncon tronconSuivant = cheminActuel.getTroncons().get(1);
			if (testIdentique(tronconActuel, tronconSuivant)) {
				String rueAPrendre = tronconActuel.getNomRue();
				double distanceTroncon = tronconActuel.getLongueur() + tronconSuivant.getLongueur();;
				contenu = contenu + "Prendre "+ rueAPrendre + " sur " + distanceTroncon+ " metres \r\n";
				System.out.println("Test5 troncon double1 : test rue troncon actuel et taille" + rueAPrendre + distanceTroncon);
			}
			else {
				String rueAPrendre1 = tronconActuel.getNomRue();
				String rueAPrendre2 = tronconSuivant.getNomRue();
				double distanceTroncon1 = tronconActuel.getLongueur();
				double distanceTroncon2 = tronconSuivant.getLongueur();
				
				contenu = contenu + "Prendre "+ rueAPrendre1 + " sur " + distanceTroncon1+ " metres \r\n";
				contenu = contenu + "Prendre "+ rueAPrendre2 + " sur " + distanceTroncon2+ " metres \r\n";
				
				System.out.println("Test5 troncon double2: test rue troncon actuel et taille" + rueAPrendre1 + " 2  " + rueAPrendre2 + distanceTroncon1);
			
			}
		}

		
		//pour le grand test il faut au moins 3 troncons 
		if (nbTroncons > 2) {
		for (int numTroncon = 0; numTroncon < nbTroncons-1; numTroncon++) {
			Troncon tronconActuel = cheminActuel.getTroncons().get(numTroncon);
			Troncon tronconSuivant = cheminActuel.getTroncons().get(numTroncon+1);
			
			//si c'est deux troncons de la meme rue, on les assemble en 1 troncon
			if (testIdentique(tronconActuel, tronconSuivant) == true) {
				tronconSuivant.setLongueur(tronconActuel.getLongueur() + tronconSuivant.getLongueur());
			}
			else {
				String rueAPrendre = tronconActuel.getNomRue();
				double distanceTroncon = tronconActuel.getLongueur();
				contenu = contenu + "Prendre "+ rueAPrendre + " sur " + distanceTroncon+ " metres \r\n";
				System.out.println("Test5 : test rue troncon actuel et taille" + rueAPrendre + distanceTroncon);
			}
		
		}
		
		// Pour le dernier troncon, test avec le troncon PRECEDENT
		Troncon lastTroncon = (cheminActuel.getTroncons().get(nbTroncons-1));
		Troncon preLastTroncon = (cheminActuel.getTroncons().get(nbTroncons-2));
		if (testIdentique(lastTroncon, preLastTroncon) == true) {
			lastTroncon.setLongueur(preLastTroncon.getLongueur() + lastTroncon.getLongueur());
			double LastDistanceTroncon = lastTroncon.getLongueur();
			String LastRueAPrendre = lastTroncon.getNomRue();
			contenu = contenu + "Prendre "+ LastRueAPrendre + " sur " + LastDistanceTroncon+ " metres \r\n";
			System.out.println("Test5 : test rue troncon actuel et taille" + LastRueAPrendre + LastDistanceTroncon);
		
		}
		else {
			double LastDistanceTroncon = lastTroncon.getLongueur();
			String LastRueAPrendre = lastTroncon.getNomRue();
			contenu = contenu + "Prendre "+ LastRueAPrendre + " sur " + LastDistanceTroncon+ " metres \r\n";
			System.out.println("Test5 : test rue troncon actuel et taille" + LastRueAPrendre + LastDistanceTroncon);
		}
		}
		
		contenu = contenu + "Arrivee au point de livraison "+ destinationCheminActuel+ " a " + heureFinCheminActuel+ "\r\n";
		System.out.println("Test6 : test arrivee au pdl " + destinationCheminActuel + heureFinCheminActuel);
		
		if (tempsAttente >0) {
			contenu = contenu + "Attente pendant "+ tempsAttente+ " minutes \r\n";
		}
		
		//relier mon la destination de mon chemin actuel avec la destination de la livraison
		
		
		contenu = contenu + "Livraison pendant "+ " TEMPS DE LIVRAISON" + " minutes \r\n";
		//heureDepartPoint = heureFinCheminActuel + TEMPS DE LIVRAISON;
		//contenu = contenu + "Depart du point de livraison "+ destinationCheminActuel + " a " + heureDepartPoint+ "\r\n";
		contenu = contenu + "Depart du point de livraison "+ destinationCheminActuel + "\r\n";	
		System.out.println("Test7 : test depart du pdl " + destinationCheminActuel);
		
		return contenu;
	}
	
	

}