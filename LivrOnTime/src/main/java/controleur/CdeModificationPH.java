package controleur;

import java.util.Date;

import modele.Livraison;
import modele.Plan;
import modele.Tournee;

/**
 * La classe CdeModificationPH implemente l interface Commande pour la
 * modification de la plage horaire d une livraison dans la tournee. Elle stocke
 * la livraison modifie et sa plage horaire.
 * 
 * @author Matthieu
 *
 */
public class CdeModificationPH implements Commande {

	private Plan plan;
	private Date debutPH; // debut plage horaire
	private Date finPH; // fin plage horaire
	private Tournee tournee;
	private Livraison livraison;

	private Date DPH_A, FPH_A;

	/**
	 * Constructeur de la classe CdeModificationPH
	 * 
	 * @param plan
	 * @param tournee
	 * @param livraison
	 * @param DPH_A
	 * @param FPH_A
	 */
	public CdeModificationPH(Plan plan, Tournee tournee, Livraison livraison, Date DPH_A, Date FPH_A) {
		super();
		this.plan = plan;
		this.debutPH = livraison.getDebutPlageHoraire();
		this.finPH = livraison.getFinPlageHoraire();
		this.tournee = tournee;
		this.livraison = livraison;
		this.DPH_A = DPH_A;
		this.FPH_A = FPH_A;
	}

	@Override
	public void redoCde() {
		tournee.ModifierLivraison(plan, livraison, debutPH, finPH);

	}

	@Override
	public void undoCde() {
		tournee.ModifierLivraison(plan, livraison, DPH_A, FPH_A);
	}

}
