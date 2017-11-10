package controleur;

import LivrOnTime.Main;
import modele.Intersection;
import modele.Livraison;
import modele.Plan;
import modele.Tournee;


/**
 * La classe CdeSuppression implemente l interface Commande
 * pour la suppression d une livraison dans la tournee.
 * Elle stocke la livraison supprimee et sa position.
 * @author Matthieu
 *
 */
public class CdeSuppression implements Commande {
	
	private Plan plan;
	private Intersection intersection;
	private int index;
	private Tournee tournee;
	private Livraison livraison;
	
	
	
	/**
	 * Constructeur de la classe CdeSuppression
	 * @param plan
	 * @param intersection
	 * @param tournee
	 * @param livraison
	 * @param index
	 */
	public CdeSuppression(Plan plan, Intersection intersection, Tournee tournee,Livraison livraison,int index) {
		super();
		this.plan = plan;
		this.intersection = intersection;
		this.tournee = tournee;
		this.index=index;
		this.livraison=livraison;
	}

	@Override
	public void undoCde() {
		Main.aController.getDemandeLiv().getLivraisons().add(livraison);
		try{
		tournee.AjouterLivraison(plan,intersection,livraison,index);
		}catch(Exception e){
			System.err.println("Le undo de la suppression bug!");	
		}
	}

	@Override
	public void redoCde() {
		Main.aController.getDemandeLiv().getLivraisons().remove(livraison);
		try{
		index =(tournee.SupprimerLivraison(plan,intersection,livraison)).getKey();
		}catch(Exception e){
			System.err.println("Le redo de la suppression bug!");
		}
	}

}
