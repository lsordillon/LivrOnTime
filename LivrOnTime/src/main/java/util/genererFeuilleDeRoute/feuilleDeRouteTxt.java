package util.genererFeuilleDeRoute;
import java.util.ArrayList;

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
importer la tourn�e calcul�e***
r�cup�rer heure d�part initial***
r�cup�rer heure d�part de chaque point***
r�cup�rer intersection de point de livraison 
r�cup�rer distance entre chaque point 
r�cup�rer heure arriv�e de chaque point
r�cup�rer heure d�arriv�e finale
r�cup�rer temps de livraison 
r�cup�rer attente s�il y a

}
*/


