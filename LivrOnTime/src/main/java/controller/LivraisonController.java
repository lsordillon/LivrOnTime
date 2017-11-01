package controller;



import java.net.URL;
import java.util.ResourceBundle;



import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.DemandeLivraison;
import model.Intersection;
import model.Livraison;

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
					ajoutBtn.setDisable(true);
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
