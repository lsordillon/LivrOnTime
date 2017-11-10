package controller;

import LivrOnTime.Main;
import javafx.util.Pair;
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
	public void redoCde()  {
		Main.aController.getDemandeLiv().getLivraisons().add(livraison);
		try{
		tournee.AjouterLivraison(plan,intersection,livraison,index);
		} catch(Exception e){
			System.err.println("Le redo de l'ajout bug!");
		}
	}

	@Override
	public void undoCde() {
		Main.aController.getDemandeLiv().getLivraisons().remove(livraison);
		try{
		index = (tournee.SupprimerLivraison(plan,intersection,livraison)).getKey();
		}catch(Exception e){
			System.err.println("Le undo de l'ajout bug!");
		}
		
		
	}

}
