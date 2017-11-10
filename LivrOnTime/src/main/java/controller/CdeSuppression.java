package controller;

import LivrOnTime.Main;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Tournee;

public class CdeSuppression implements Commande {
	
	private Plan plan;
	private Intersection intersection;
	private int index;
	private Tournee tournee;
	private Livraison livraison;
	
	
	

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
