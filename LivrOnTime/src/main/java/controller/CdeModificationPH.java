package controller;

import java.util.Date;

import model.Livraison;
import model.Plan;
import model.Tournee;

public class CdeModificationPH implements Commande{
	
	private Plan plan;
	private Date debutPH; //debut plage horaire
	private Date finPH; //fin plage horaire
	private Tournee tournee;
	private Livraison livraison;
    private Date debutPH_A=livraison.getDebutPlageHoraire();
    private Date finPH_A=livraison.getFinPlageHoraire();
    
    
    
	public CdeModificationPH(Plan plan, Tournee tournee, Livraison livraison, Date dPH, Date fPH) {
		super();
		this.plan = plan;
		debutPH = dPH;
		finPH = fPH;
		this.tournee = tournee;
		this.livraison = livraison;
	}

	@Override
	public void redoCde() {
		tournee.ModifierLivraison(plan, livraison, debutPH,finPH);
		
	}

	@Override
	public void undoCde() {
		tournee.ModifierLivraison(plan,livraison, debutPH_A,finPH_A);
	}

}
