package controller;



import java.net.URL;

import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import LivrOnTime.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SplitPane.Divider;
import javafx.stage.Stage;
import model.Chemin;
import model.DemandeLivraison;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Tournee;

public class LivraisonController implements Initializable {
	public TextField adresseField;
	public TextField dureeField;
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
	private ListeDeCdes listeDeCdes;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dureeField.setText("10");
		adresseField.setDisable(true);
		for(int i=0;i<24;i++){
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
					
					dureeField.setText( String.valueOf(livraison.getDuree() / 60));
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
		AccueilController aController = Main.aController;
		plan = AccueilController.getPlan();
		listeDeCdes=AccueilController.getListeDeCdes();
		Date debut = new Date();
		Date fin = new Date();
		
		if(!comboAHeur.getSelectionModel().isEmpty() && !comboAMinute.getSelectionModel().isEmpty() && !comboDeHeur.getSelectionModel().isEmpty() && !comboDeMinute.getSelectionModel().isEmpty()){			
			debut.setHours(comboDeHeur.getSelectionModel().getSelectedItem());
			debut.setMinutes(comboDeMinute.getSelectionModel().getSelectedItem());			
			fin.setHours(comboAHeur.getSelectionModel().getSelectedItem());
			fin.setMinutes(comboAMinute.getSelectionModel().getSelectedItem());			
			}else{
				debut = null;
				fin = null;
			}
		
			if (AccueilController.getTournee()==null){
				int idx = aController.getDl().getLivraisons().indexOf(livraison);
				livraison.setDebutPlageHoraire(debut);
				livraison.setFinPlageHoraire(fin);
				livraison.setDuree(Integer.parseInt(dureeField.getText()) * 60);
				aController.getDl().getLivraisons().set(idx, livraison);
				aController.update(null);
			}else{
				AccueilController.getTournee().ModifierLivraison(plan, livraison, debut, fin);
				AccueilController.getTournee().ModifierLivraison(plan, livraison, Integer.parseInt(dureeField.getText()) * 60);
				aController.update(AccueilController.getTournee());
			}
			Stage stage = (Stage) modifBtn.getScene().getWindow();
		    stage.close();
	}
	
	public void SupprimerLivraison(){
		AccueilController aController = Main.aController;
		plan = aController.getPlan();
		listeDeCdes=AccueilController.getListeDeCdes();
		
		aController.getDl().getLivraisons().remove(livraison);
		if (aController.getTournee()==null){
			aController.update(null);
		}else{
		int idx = aController.getdController().listView.getSelectionModel().getSelectedIndex();
		aController.getTournee().SupprimerLivraison(plan,intersection,  livraison);
		listeDeCdes.ajoute(new CdeSuppression(plan,intersection,AccueilController.getTournee(),livraison,idx));
		aController.setListeDeCdes(listeDeCdes);
		aController.update(AccueilController.getTournee());
		}
		Stage stage = (Stage) suppBtn.getScene().getWindow();
	    stage.close();
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
		plan = AccueilController.getPlan();
		listeDeCdes=AccueilController.getListeDeCdes();
		aController.getDl().getLivraisons().add(livraison);
		if (AccueilController.getTournee()==null){
			aController.update(null);
		}else{
			    	int idx = aController.getdController().listView.getSelectionModel().getSelectedIndex();
					AccueilController.getTournee().AjouterLivraison(plan,intersection,livraison, idx);
					listeDeCdes.ajoute(new CdeAjout(plan,intersection,AccueilController.getTournee(),livraison,idx));
					aController.setListeDeCdes(listeDeCdes);
					aController.update(AccueilController.getTournee());
		}
		Stage stage = (Stage) ajoutBtn.getScene().getWindow();
	    stage.close();
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
