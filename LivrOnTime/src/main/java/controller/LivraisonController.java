package controller;



import java.net.URL;

import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import LivrOnTime.Main;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Chemin;
import model.DemandeLivraison;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Tournee;

public class LivraisonController implements Initializable {
	public TextField adresseField;
	public ComboBox<Integer> comboDeHeur;
	public ComboBox<Integer> comboDeMinute;
	public ComboBox<Integer> comboAHeur;
	public ComboBox<Integer> comboAMinute;
	
	public Button modifBtn;
	public Button suppBtn;
	public Button ajoutBtn;
	private static Intersection intersection;
	private static DemandeLivraison demandeL;
	private Livraison livraison;
	private Plan plan;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		adresseField.setDisable(true);
		for(int i=0;i<13;i++){
			comboDeHeur.getItems().add(i);
			comboAHeur.getItems().add(i);
		}
		for(int i=0;i<60;i++){
			comboDeMinute.getItems().add(i);
			comboAMinute.getItems().add(i);
		}
		boolean exist = false;
		if(intersection!=null){
			adresseField.setText(AccueilController.getAdresse(intersection));
			for(Livraison l : demandeL.getLivraisons()){
				if(l.getDestination().getId() == intersection.getId()){
					livraison = l;
					exist = true;
					if(l.getDebutPlageHoraire()!=null){
					comboDeHeur.getSelectionModel().select(l.getDebutPlageHoraire().getHours());
					comboDeMinute.getSelectionModel().select(l.getDebutPlageHoraire().getMinutes());
					comboAHeur.getSelectionModel().select(l.getFinPlageHoraire().getHours());
					comboAMinute.getSelectionModel().select(l.getFinPlageHoraire().getMinutes());
					}
				}
			}
			if(!exist){
				modifBtn.setDisable(true);
				suppBtn.setDisable(true);
			}
			
		}
	}
	
	public void ModifierLivraison(){
		
	}
	public void SupprimerLivraison(){
		AccueilController aController = Main.aController;
		plan = aController.getPlan();
		aController.getDl().getLivraisons().remove(livraison);
		if (aController.getTournee()==null){
			aController.update(null);
		}else{
		aController.getTournee().SupprimerLivraison(plan,intersection,  livraison);
		aController.update(AccueilController.getTournee());
		}
	
	}
	public void AjouterLivraison(){
		
		if(!comboAHeur.getSelectionModel().isEmpty() && !comboAMinute.getSelectionModel().isEmpty() && !comboDeHeur.getSelectionModel().isEmpty() && !comboDeMinute.getSelectionModel().isEmpty()){
		Date debut = new java.util.Date();
		debut.setHours(comboDeHeur.getSelectionModel().getSelectedItem());
		debut.setMinutes(comboDeMinute.getSelectionModel().getSelectedItem());
		Date fin = new java.util.Date();
		fin.setHours(comboAHeur.getSelectionModel().getSelectedItem());
		fin.setMinutes(comboAMinute.getSelectionModel().getSelectedItem());
		livraison = new Livraison(0, intersection, debut, fin);
		}else{
			livraison = new Livraison(0,intersection);
		}
		
		AccueilController aController = Main.aController;
		plan = aController.getPlan();
		aController.getDl().getLivraisons().add(livraison);
		if (aController.getTournee()==null){
			aController.update(null);
		}else{
		aController.getTournee().SupprimerLivraison(plan,intersection,  livraison);
		aController.update(AccueilController.getTournee());
		}
	}
	public static void setIntersection(Intersection intersect){
		intersection = intersect;
	}
	public static void setDL(DemandeLivraison dl){
		demandeL = dl;
	}


	public static DemandeLivraison getDemandeL() {
		return demandeL;
	}





	
}
