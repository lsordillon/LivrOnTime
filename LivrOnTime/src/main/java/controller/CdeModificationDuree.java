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
    
    
	public CdeModificationDuree(Plan plan, Tournee tournee, Livraison livraison, int dureeA) {
		this.plan = plan;
		this.duree = livraison.getDuree();
		this.tournee = tournee;
		this.livraison = livraison;
		this.dureeA=dureeA;
	}

	
	@Override
	public void redoCde() {
		tournee.ModifierLivraison(plan, livraison, duree);
		
	}

	
	@Override
	public void undoCde() {
		
	   
		tournee.ModifierLivraison(plan,livraison, dureeA);
		
	}
	
}
