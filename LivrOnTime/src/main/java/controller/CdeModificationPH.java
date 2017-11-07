package controller;

import java.util.Date;

import model.Livraison;
import model.Plan;
import model.Tournee;

public class CdeModificationPH implements Commande{
	
	private Plan plan;
	private Date DPH;
	private Date FPH;
	private Tournee tournee;
	private Livraison livraison;
    private Date DPH_A=livraison.getDebutPlageHoraire();
    private Date FPH_A=livraison.getFinPlageHoraire();
	@Override
	public void redoCde() {
		tournee.ModifierLivraison(plan, livraison, DPH,FPH);
		
	}

	@Override
	public void undoCde() {
		tournee.ModifierLivraison(plan,livraison, DPH_A,FPH_A);
	}

}
