package util.genererFeuilleDeRoute;
import java.util.ArrayList;
import java.util.Date;

import model.*;



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
		//TODO utilise plutôt SimpleDateFormat
		int heureDep = tournee.getHeureDepart().getHours();
		int minDep = tournee.getHeureDepart().getMinutes();
		int heureArr = tournee.getHeureArrive().getHours();
		int minArr = tournee.getHeureArrive().getMinutes();
		Intersection entrepotAd = (tournee.getItineraire()).get(0).getOrigine(); // rend ID =25303798NbTroncons2
		
		//Premiere ligne de depart 
		contenu = contenu + "Depart de l'entrepot "+ ruesDelIntersection(entrepotAd) +" a " + heureDep+ ":"+ minDep +"\r\n\r\n";
		

		//Pour tous les chemins, on recup adress dep adress arr
		//ainsi que heure arrivee au depart et heure de depart apres livraison
		for (int index = 0; index < nbChemin; index++) {
			contenu = contenu + retranscriptionDunChemin(tournee, index);
			//TODO Tu peux même fusionner les 2 méthodes maintenant ! Comme tu veux
			contenu +=retranscriptionLivraison(tournee, index);
		}
		
	    return contenu;
	}
	
	private static boolean testIdentique(Troncon premierT, Troncon deuxiemeT) {
		boolean identique = false;
		if (premierT.getNomRue().equals(deuxiemeT.getNomRue())) {
			identique = true;
		}
		return identique;
	}

	private static String retranscriptionDunChemin (Tournee tourneeEnCours, int index) {
		Chemin cheminActuel = tourneeEnCours.getItineraire().get(index);
		String contenu = "";
		long idEntrepot = (tourneeEnCours.getItineraire()).get(0).getOrigine().getId();
		int nbTroncons = (cheminActuel.getTroncons().size());
		Intersection destinationCheminActuel = cheminActuel.getDestination() ;
		Date heureFinCheminActuel = cheminActuel.getHeureArrivee() ; 
		int tempsAttente = 0;
		
		//On recupere chaque troncon (nom de rue et longueur du troncon) SAUF LE DERNIER
		//et le temps de la livraison d'arrivee de l'it
		
		
		//si 1 troncon tout va bien 
		if (nbTroncons ==1) {
			Troncon tronconActuel = cheminActuel.getTroncons().get(0);
			String rueAPrendre = tronconActuel.getNomRue();
			double distanceTroncon = tronconActuel.getLongueur();
			contenu = contenu + "Prendre "+ rueAPrendre + " sur " + (int)distanceTroncon+ " metres \r\n";
		}
		
		//si 2 troncons petit test
		if (nbTroncons ==2) {
			Troncon tronconActuel = cheminActuel.getTroncons().get(0);
			Troncon tronconSuivant = cheminActuel.getTroncons().get(1);
			if (testIdentique(tronconActuel, tronconSuivant)) {
				String rueAPrendre = tronconActuel.getNomRue();
				double distanceTroncon = tronconActuel.getLongueur() + tronconSuivant.getLongueur();;
				contenu = contenu + "Prendre "+ rueAPrendre + " sur " + (int)distanceTroncon+ " metres \r\n";
			}
			else {
				String rueAPrendre1 = tronconActuel.getNomRue();
				String rueAPrendre2 = tronconSuivant.getNomRue();
				double distanceTroncon1 = tronconActuel.getLongueur();
				double distanceTroncon2 = tronconSuivant.getLongueur();
				
				contenu = contenu + "Prendre "+ rueAPrendre1 + " sur " + (int)distanceTroncon1+ " metres \r\n";
				contenu = contenu + "Prendre "+ rueAPrendre2 + " sur " + (int)distanceTroncon2+ " metres \r\n";
				

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
				contenu = contenu + "Prendre "+ rueAPrendre + " sur " + (int)distanceTroncon+ " metres \r\n";
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
		
		}
		else {
			double LastDistanceTroncon = lastTroncon.getLongueur();
			String LastRueAPrendre = lastTroncon.getNomRue();
			contenu = contenu + "Prendre "+ LastRueAPrendre + " sur " + LastDistanceTroncon+ " metres \r\n";
		}
		}
		
		if (idEntrepot == destinationCheminActuel.getId()) {
			//Derniere ligne d'arrivee
			contenu = contenu + "\r"+"Arrivee a l'entrepot a " + tourneeEnCours.getHeureArrive().getHours() + ":"+ tourneeEnCours.getHeureArrive().getMinutes() +"\r\n";;
		}
		
		else {
		Livraison livraisonActuelle = combinaisonPointDeLivraison(tourneeEnCours.getListeLivraison(), destinationCheminActuel);
		String adresse = ruesDelIntersection(destinationCheminActuel);
		contenu = contenu + "\r"+"Arrivee au point de livraison "+ adresse + " a " + heureFinCheminActuel+ "\r\n";
		
		if (tempsAttente >0) {
			contenu = contenu + "Attente pendant "+ tempsAttente/60 + " minutes \r\n";
		}
		
			contenu = contenu + "Livraison pendant "+ livraisonActuelle.getDuree()/60 + " minutes \r\n";
			//heureDepartPoint = heureFinCheminActuel + livraisonActuelle.getDuree();
			//contenu = contenu + "Depart du point de livraison "+ destinationCheminActuel + " a " + heureDepartPoint+ "\r\n";
			contenu = contenu + "Depart du point de livraison "+ adresse + "\r\n\r\n";	
			}
		
		return contenu;
	}
	
	//TODO Bon courage Perrine ! :)
	private static String retranscriptionLivraison(Tournee tournee, int index) {
		Date[] tempsPassage = tournee.getTempsPassage()[index];
		return "";
	}

	private static Livraison combinaisonPointDeLivraison (ArrayList <Livraison> livraisons, Intersection intersection) {

		long idInter = intersection.getId();
		int nLiv = 0;
		Livraison livraisonGagnante = null;

		 while (idInter != livraisons.get(nLiv).getDestination().getId()) {			 
			 nLiv++;
		 }
		
		livraisonGagnante = livraisons.get(nLiv);
		return livraisonGagnante;
	}
	
	private static String ruesDelIntersection (Intersection intersection){
		ArrayList <Troncon> troncons = intersection.getTronconsVersVoisins();
		String rues = null;
		String rue2 = null;
		
		String rue1 = troncons.get(0).getNomRue();
		
		if (troncons.size()>1) {
			rue2 = troncons.get(1).getNomRue();
			rues = rue1 +" "+ rue2;
		}
		else
		rues = rue1;

		return rues;
	}


}