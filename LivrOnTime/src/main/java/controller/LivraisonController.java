package controller;



import java.net.URL;
import java.util.ArrayList;
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
		plan = AccueilController.getPlan();
		Tournee nouvelleTrounee = plan.SupprimerLivraison(intersection, demandeL, AccueilController.getTournee(), livraison);
		DescriptifController dController = AccueilController.getdController();
		AccueilController aController = Main.aController;
		//dController.ListerLivraisons(demandeL.getLivraisons(), plan, nouvelleTrounee);
		aController.update(nouvelleTrounee);
		
	
	}
	public void AjouterLivraison(){
		
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
