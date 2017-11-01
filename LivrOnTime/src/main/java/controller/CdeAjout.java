package controller;

import model.DemandeLivraison;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Tournee;



public class CdeAjout implements Commande {
	private Plan plan;
	private Intersection intersection;
	private DemandeLivraison dl;
	private Tournee tournee;
	private Livraison livraison;
	
	
	
	

	public CdeAjout(Plan plan, Intersection intersection, DemandeLivraison dl, Tournee tournee,Livraison livraison) {
		super();
		this.plan = plan;
		this.intersection = intersection;
		this.dl = dl;
		this.tournee = tournee;
		this.livraison=livraison;
	}

	@Override
	public void redoCde() {
		plan.AjouterLivraison(intersection,dl,tournee);
	}

	@Override
	public void undoCde() {
		plan.SupprimerLivraison(intersection,dl,tournee,livraison);
	}

}
