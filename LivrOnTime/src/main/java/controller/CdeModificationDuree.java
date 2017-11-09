package controller;

import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Tournee;

public class CdeModificationDuree implements Commande{
	private Plan plan;
	private int duree;
	private Tournee tournee;
	private Livraison livraison;
	int dureeA;
    
    
	public CdeModificationDuree(Plan plan, Tournee tournee, Livraison livraison, int duree) {
		super();
		this.plan = plan;
		this.duree = duree;
		this.tournee = tournee;
		this.livraison = livraison;
		this.dureeA=livraison.getDuree();
	}

	@Override
	public void redoCde() {
		tournee.ModifierLivraison(plan, livraison, duree);
		
	}

	@Override
	public void undoCde() {
	     System.out.println("HHHHHH"+dureeA);
		tournee.ModifierLivraison(plan,livraison, dureeA);
		
	}
	

}
