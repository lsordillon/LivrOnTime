package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.sun.javafx.runtime.VersionInfo;

import javafx.event.ActionEvent;

import javafx.scene.Group;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.scene.control.Label;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import model.DemandeLivraison;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Tournee;
import model.Troncon;
import util.genererFeuilleDeRoute.feuilleDeRouteTxt;
import util.parserXML.XmlParserLivraison;
import util.parserXML.XmlParserPlan;
import vue.DessinerPlan;



public class AccueilController{
	
	//Etape 1 : Redefinir tout les composants existants dans le fichier fxml comme des attributs public du controller
	public AnchorPane VuePlan;
	public AnchorPane VueControl;
	public AnchorPane VueDescriptif; 
	public Button ChargerButoon;
	public Button ChargerLivraison;
	public Button CalculTournee;
	public Button AccueilBouton;
	public Button GenererFeuille; 
	public Button undoButton;
	public Button redoButton;

	private SimpleDateFormat dureeHms = new SimpleDateFormat("HH:mm:ss");
	
    private static ListeDeCdes listeDeCdes;
	private static Plan plan;
	private static Tournee tournee;
	private DessinerPlan dessinerPlan;
	private DemandeLivraison dl;
	private Intersection intersectionSelectionne;
    private static DescriptifController dController;
    
    public AccueilController() {
    	listeDeCdes=new ListeDeCdes();
    	dessinerPlan = new DessinerPlan();
    	dController = new DescriptifController(dessinerPlan);
    }
    
    
	public void ChargerFichier (ActionEvent actionEvent) throws FileNotFoundException {
		

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
		File selectedFile = fileChooser.showOpenDialog(null);
		XmlParserPlan parserPlan = new XmlParserPlan();
		if (selectedFile != null) {
		
		    InputStream xsd = new FileInputStream("src/main/resources/ValidationPlan.xsd");
	    	InputStream xml = new FileInputStream(selectedFile.getAbsolutePath());
	        
		    if (parserPlan.validationXSD(xml, xsd)){
		    	 plan = CreerPlan(selectedFile.getAbsolutePath());
	
		    	 Group group = dessinerPlan.Dessiner(plan);
				    
				    VuePlan.getChildren().clear();
				    VuePlan.getChildren().add(group);
				   // VuePlan.setStyle("-fx-background-color: #f7f7d4");
				    dessinerPlan.PannableScene(VuePlan.getScene(), this);
				    ChargerLivraison.setDisable(false);
		    }else{
		    		Alert alert = new Alert(AlertType.ERROR, "Format fichier non valide" +"\n" + parserPlan.getMessageErreur());
		    		alert.showAndWait();
		    }
		   
		 
		   
		}
		else {
		    System.err.println("Error");
		}	

	}
	public void ChargerLivraison(ActionEvent actionEvent) throws FileNotFoundException {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			InputStream xsd = new FileInputStream("src/main/resources/ValidationDL.xsd");
	    	InputStream xml = new FileInputStream(selectedFile.getAbsolutePath());
	    	XmlParserLivraison parserLivraison = new XmlParserLivraison();
	    	if(parserLivraison.validationXSD(xml, xsd)){
	    		parserLivraison.lecteur(selectedFile.getAbsolutePath());
				dl = new DemandeLivraison(XmlParserLivraison.livraisons,XmlParserLivraison.entrepot,plan);
				LivraisonController.setDL(dl);
				VuePlan.getChildren().add(dessinerPlan.Dessiner(dl));
			    dessinerPlan.PannableScene(VuePlan.getScene(), this);			    
			    //ListerLivraisons(dl.getLivraisons());
			     
	
			    VBox vBox3 = new VBox(new Label ("Adresse Entrepot :     "+ getAdresse(dl.getAdresseEntrepot())),
			    					  new Label ("Heure de Depart :      "+ dureeHms.format(dl.getHeureDepart())),
			    					  new Label ("Heure de Retour :      "));
		   		vBox3.setSpacing(10);
		   		VBox vBox2 = new VBox(vBox3,dController.ListerLivraisons(dl.getLivraisons(), plan, null));
		   		
		   		vBox2.setSpacing(40);
		   		vBox2.setLayoutX(30);
		        vBox2.setLayoutY(50);
			    VueDescriptif.getChildren().add(vBox2);
			    CalculTournee.setDisable(false);
			    
			   listeDeCdes.reset();
	    	}else{
	    		Alert alert = new Alert(AlertType.ERROR, "Format fichier non valide"+ "\n" + parserLivraison.getMessageErreur());
	    		alert.showAndWait();
	    	}	
		}
		
	}

	
	public void CalculTournee(ActionEvent actionEvent) {
		tournee=plan.calculerLaTournee(dl);
		tournee.initTempsPassage();
	

		
		VuePlan.getChildren().add(dessinerPlan.afficherChemin(tournee));
	    dessinerPlan.PannableScene(VuePlan.getScene(), this);
	    
	    GenererFeuille.setDisable(false);
	    VueDescriptif.getChildren().clear();
		VBox vBox3 = new VBox(new Label ("Adresse Entrepot :     "+ getAdresse(dl.getAdresseEntrepot())),
							  new Label ("Heure de Depart :      "+ dureeHms.format(dl.getHeureDepart())),
							  new Label ("Heure de Retour :      "+ dureeHms.format(tournee.getHeureArrive())));
		vBox3.setSpacing(10);
		
		VBox vBox2 = new VBox(vBox3,dController.ListerLivraisons(tournee.getListeLivraison(), plan, tournee));
	
		vBox2.setSpacing(40);
		vBox2.setLayoutX(30);
		vBox2.setLayoutY(50);
		VueDescriptif.getChildren().add(vBox2); 

	}
	
	public void GenererFeuille(ActionEvent actionEvent) {
		FileWriter fichierGenere;
		
		try {
			fichierGenere = new FileWriter("src/main/resources/FeuilleDeRoute.txt");
			fichierGenere.write(feuilleDeRouteTxt.genererFeuilleDeRoute(tournee));
			fichierGenere.close();	
			//System.out.println("Chemin absolu de la feuille de route generee : src/main/resources/FeuilleDeRoute.txt ");
			feuilleDeRouteTxt.Open();
			Alert alert = new Alert(AlertType.INFORMATION, "Feuille de route generee dans src/main/resources/ ");
    		alert.showAndWait();
			
    		
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
		e.printStackTrace();
		}     	

	}
	
	
	public void retourAccueil(ActionEvent actionEvent) {
		VuePlan.getChildren().clear();
		VueDescriptif.getChildren().clear();
	    ChargerLivraison.setDisable(true);
	    CalculTournee.setDisable(true);
	    GenererFeuille.setDisable(true);
	    plan=null;
	    tournee=null;
	    dl=null;
	    dessinerPlan=null;
	    intersectionSelectionne=null;
	}
	
	
	public Plan CreerPlan(String chemin) throws FileNotFoundException{
		XmlParserPlan parser = new XmlParserPlan();
		Plan plan = new Plan();
		
		parser.lecteur(chemin);
		plan.CreerIntersections(XmlParserPlan.noeuds);
		plan.CreerTroncons(XmlParserPlan.troncons);
		plan.TronconsVoisins();
		return plan;

	}   


	//mettre seconde en format heure minutes secondes
	public static String convertSecondsToHMmSs(long seconds) {
	    long s = seconds % 60;
	    long m = (seconds / 60) % 60;
	    long h = (seconds / (60 * 60)) % 24;
	    return String.format("%d:%02d:%02d", h,m,s);
	}
	
	
	//Methode pour retourner l'adresse d'une intersection
	public static String getAdresse(Intersection item){
		  for(Troncon troncon : plan.getTroncons()){
          	if(troncon.getDestination().getId() == item.getId() || troncon.getOrigine().getId() == item.getId()){
          		if(troncon.getNomRue().equals("")){
          			return "Rue sans nom";
          		}else{
          			return troncon.getNomRue();
          		}
                  }
          }  
		  return "";
	}
	

	public void update(Tournee tournee){
		ArrayList<Livraison> livraisons = new ArrayList<>();
		
		Group group = dessinerPlan.Dessiner(plan);
		Group group2 = dessinerPlan.Dessiner(dl);
		VuePlan.getChildren().clear();
	    VuePlan.getChildren().add(group);
	    VuePlan.getChildren().add(group2);
	    VueDescriptif.getChildren().clear();
	    VBox vBox3;
		if(tournee != null){
		VuePlan.getChildren().add(dessinerPlan.afficherChemin(tournee));
		dessinerPlan.actualiserCouleurPoints(tournee);
		
	    livraisons = tournee.getListeLivraison();
		vBox3 = new VBox(new Label ("Adresse Entrepot :     "+ getAdresse(dl.getAdresseEntrepot())),
				  new Label ("Heure de Depart :      "+ dureeHms.format(dl.getHeureDepart())),
				  new Label ("Heure de Retour :      "+ dureeHms.format(tournee.getHeureArrive())));
		vBox3.setSpacing(10);
		}else{
			livraisons = dl.getLivraisons();
			vBox3 = new VBox(new Label ("Adresse Entrepot :     "+ getAdresse(dl.getAdresseEntrepot())),
					  new Label ("Heure de Depart :      "+ dureeHms.format(dl.getHeureDepart())),
					  new Label ("Heure de Retour :      "));
		}
	

		VBox vBox2 = new VBox(vBox3,dController.ListerLivraisons(livraisons, plan, tournee));

		vBox2.setSpacing(40);
		vBox2.setLayoutX(30);
		vBox2.setLayoutY(50);
		VueDescriptif.getChildren().add(vBox2); 
		dessinerPlan.PannableScene(VuePlan.getScene(), this);
	}
	
	public Intersection getIntersectionSelectionne() {
		return intersectionSelectionne;
	}
	public void setIntersectionSelectionne(Intersection intersectionSelectionne) {
		this.intersectionSelectionne = intersectionSelectionne;
	}
	public static Plan getPlan() {
		return plan;
	}
	public static void setPlan(Plan plan) {
		AccueilController.plan = plan;
	}
	public static Tournee getTournee() {
		return tournee;
	}
	public static void setTournee(Tournee tournee) {
		AccueilController.tournee = tournee;
	}
	public static DescriptifController getdController() {
		return dController;
	}
	public static void setdController(DescriptifController dController) {
		AccueilController.dController = dController;
	}
	public DemandeLivraison getDl() {
		return dl;
	}
	public void setDl(DemandeLivraison dl) {
		this.dl = dl;
	}
	public static ListeDeCdes getListeDeCdes() {
		return listeDeCdes;
	}
	public static void setListeDeCdes(ListeDeCdes listeDeCdes) {
		AccueilController.listeDeCdes = listeDeCdes;
	}
	
	public void Undo(){
		listeDeCdes.undo();
		System.out.println("Undo");
		update(tournee);
	}
	public void Redo(){
		listeDeCdes.redo();
		update(tournee);
	}

}  


