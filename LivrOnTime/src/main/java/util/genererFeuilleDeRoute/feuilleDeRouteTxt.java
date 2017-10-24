package util.genererFeuilleDeRoute;
import java.util.ArrayList;

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

/*
public class feuilleDeRouteTxt {

	private Tournee tourneeCalculee;
	private String nomFichier;
	
	public feuilleDeRouteTxt(Tournee tournee, String nomFichier) {
	
	    int heureDep=tournee.getHeureDepart();
	    int heureArr=tournee.getHeureArrive();
	    Intersection entrepotAd= ...getAdresseEntrepot();
	}
	
	public String genererFeuilleDeRoute(){
	String contenu = "";
	contenu = contenu + "Depart de l'entrepot"+ entrepotAd+ "a" + "heureDep";
	
		for (...) {
		contenu = contenu + ....
		}
		
	contenu = contenu + "Arrivee a l'entrepot" + entrepotAd + "a" + "heureArr";
	
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


