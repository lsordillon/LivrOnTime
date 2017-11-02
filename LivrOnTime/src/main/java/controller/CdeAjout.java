package controller;

import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Tournee;



public class CdeAjout implements Commande {
	private Plan plan;
	private Intersection intersection;
	private Tournee tournee;
	private Livraison livraison;
	private int index;
	
	
	
	

	public CdeAjout(Plan plan, Intersection intersection, Tournee tournee,Livraison livraison, int index) {
		super();
		this.plan = plan;
		this.intersection = intersection;
		this.tournee = tournee;
		this.livraison=livraison;
		this.index = index;
	}

	@Override
	public void redoCde() {
		tournee.AjouterLivraison(plan,intersection,livraison,index);
	}

	@Override
	public void undoCde() {
		index = tournee.SupprimerLivraison(plan,intersection,livraison);
	}

}
