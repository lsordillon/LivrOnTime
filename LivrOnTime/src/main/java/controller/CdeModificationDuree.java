package controller;

import java.util.Date;

import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Tournee;

public class CdeModificationDuree implements Commande{
	private Plan plan;
	private int duree;
	private Date debutPH; //debut plage horaire
	private Date finPH; //fin plage horaire
	private Tournee tournee;
	private Livraison livraison;
	private int dureeA;
	private Date DPH_A,FPH_A;
    
	public CdeModificationDuree(Plan plan, Tournee tournee, Livraison livraison, int dureeA,Date DPH_A,Date FPH_A) {
		this.plan = plan;
		this.duree = livraison.getDuree();
		this.debutPH = livraison.getDebutPlageHoraire();
		this.finPH = livraison.getFinPlageHoraire();
		this.tournee = tournee;
		this.livraison = livraison;
		this.dureeA=dureeA;
		this.DPH_A=DPH_A;
		this.FPH_A=FPH_A;
	}

	
	@Override
	public void redoCde() {
		tournee.ModifierLivraison(plan, livraison, duree);
		tournee.ModifierLivraison(plan, livraison, debutPH,finPH);
	}

	
	@Override
	public void undoCde() {
		tournee.ModifierLivraison(plan,livraison, DPH_A,FPH_A);
		tournee.ModifierLivraison(plan,livraison, dureeA);
		
	}
	
}
