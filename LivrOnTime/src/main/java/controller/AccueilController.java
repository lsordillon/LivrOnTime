package controller;



import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Plan;
import util.parserXML.XmlParserPlan;
import vue.DessinerPlan;

public class AccueilController implements Initializable {
	
	//Etape 1 : Redéfinir tout les composants existants dans le fichier fxml comme des attributs public du controller
	public AnchorPane VuePlan;
	public AnchorPane VueControl;
	public Button ChargerButoon;
	public Label AccueilLabel;
	
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		// cette méthode sert à initialiser la vue (vide pour le moment)
	}

	// L'implémentation méthode executer par le button ChargerButton ==> voir Accueil.fxml (onAction="#ChargerFichier"):
	
	public void ChargerFichier (ActionEvent actionEvent) {
		
		//FileChooser ouvre une fenetre pour parcourir les repertoires et choisir un fichier XML
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
		   // System.out.println(selectedFile.getAbsolutePath());
		    
		    DessinerPlan dessinerPlan = new DessinerPlan();
		    Group group = dessinerPlan.Dessiner(CreerPlan(selectedFile.getAbsolutePath()));
		    // On déssine le plan (on le met dans un Group) et puis on l'ajoute à notre anchorpane "VuePlan"
		    VuePlan.getChildren().clear();
		    VuePlan.getChildren().add(group);
		    dessinerPlan.PannableScene(VuePlan.getScene());
		}
		else {
		    System.err.println("Error");
		}	

	}
	
	public Plan CreerPlan(String chemin){
		XmlParserPlan parser = new XmlParserPlan();
		Plan plan = new Plan();
		
		parser.Reader(chemin);
		plan.CreerIntersections(XmlParserPlan.noeuds);
		plan.CreerTroncons(XmlParserPlan.troncons);
		return plan;
	}

}
