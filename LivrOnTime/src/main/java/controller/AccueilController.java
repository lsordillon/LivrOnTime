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

import org.w3c.dom.Element;

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
	
    private static ListeDeCdes listeDeCommandes;
	private static Plan plan;
	private static Tournee tournee;
	private DessinerPlan dessinerPlan;
	private DemandeLivraison demandeLiv;
	private Intersection intersectionSelectionnee;
    private static DescriptifController dController;
    
    public AccueilController() {
    	listeDeCommandes=new ListeDeCdes();
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
		    	 try{
					Group group = dessinerPlan.Dessiner(plan);
					VuePlan.getChildren().clear();
					VuePlan.getChildren().add(group);
					// VuePlan.setStyle("-fx-background-color: #f7f7d4");
					dessinerPlan.PannableScene(VuePlan.getScene(), this);
					ChargerLivraison.setDisable(false);
			    }catch(Exception e){
			    	 Alert alert = new Alert(AlertType.ERROR, "Plan corrompu : L'echec du chargement du plan a �t� provoqu� car certaines rues ne possedent pas d'intersection"+ "\n");
	                   alert.showAndWait();
		    	 }
		    	 
		    	
				    
				    
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
		boolean invalide=false;
		String fautive="";
		if (selectedFile != null) {
			InputStream xsd = new FileInputStream("src/main/resources/ValidationDL.xsd");
	    	InputStream xml = new FileInputStream(selectedFile.getAbsolutePath());
	    	XmlParserLivraison parserLivraison = new XmlParserLivraison();
	    	if(parserLivraison.validationXSD(xml, xsd))
	    	{
	    		parserLivraison.lecteur(selectedFile.getAbsolutePath());
				if(!plan.getIntersections().containsKey((Long.parseLong((String)((Element)XmlParserLivraison.entrepot.item(0)).getAttribute("adresse")))))
				{
					System.out.println("entrepot merde "+plan.getIntersections().size()+" "+Long.parseLong(((Element)XmlParserLivraison.entrepot.item(0)).getAttribute("adresse")));
					fautive=((Element)XmlParserLivraison.entrepot.item(0)).getAttribute("adresse");
					invalide=true;
				}
				for(int i = 0; i<XmlParserLivraison.livraisons.getLength(); i++) {
					if(!plan.getIntersections().containsKey(Long.parseLong((String)((Element)XmlParserLivraison.livraisons.item(i)).getAttribute("adresse")))){
						System.out.println("merde");
						fautive=((Element)XmlParserLivraison.livraisons.item(i)).getAttribute("adresse");
						invalide=true;
					}
				}
				if(!invalide){
	    		
					demandeLiv = new DemandeLivraison(XmlParserLivraison.livraisons,XmlParserLivraison.entrepot,plan);
					LivraisonController.setDL(demandeLiv);
					VuePlan.getChildren().add(dessinerPlan.Dessiner(demandeLiv,plan));
				    dessinerPlan.PannableScene(VuePlan.getScene(), this);			    
				    //ListerLivraisons(dl.getLivraisons());
				     
		
				    VBox vBox3 = new VBox(new Label ("Adresse Entrepot :     "+ getAdresse(demandeLiv.getAdresseEntrepot())),
				    					  new Label ("Heure de Depart :      "+ dureeHms.format(demandeLiv.getHeureDepart())),
				    					  new Label ("Heure de Retour :      "));
			   		vBox3.setSpacing(10);
			   		VBox vBox2 = new VBox(vBox3,dController.ListerLivraisons(demandeLiv.getLivraisons(), plan, null));
			   		
			   		vBox2.setSpacing(40);
			   		vBox2.setLayoutX(30);
			        vBox2.setLayoutY(50);
				    VueDescriptif.getChildren().add(vBox2);
				    CalculTournee.setDisable(false);
				    listeDeCommandes.reset();
				} else{
                    Alert alerte = new Alert(AlertType.ERROR, "Demande de livraison corrompue : L'adresse '"+fautive+"' n'existe pas "+ "\n");
                    alerte.showAndWait();
				}
	    	}else{
	    		Alert alerte = new Alert(AlertType.ERROR, "Format fichier non valide"+ "\n" + parserLivraison.getMessageErreur());
	    		alerte.showAndWait();
	    	}	
		}
		
	}

	
	public void CalculTournee(ActionEvent actionEvent) {
		tournee=plan.calculerLaTournee(demandeLiv);
		tournee.initTempsPassage();
	
		
		VuePlan.getChildren().add(dessinerPlan.afficherChemin(tournee));
	    dessinerPlan.PannableScene(VuePlan.getScene(), this);
	    
	    GenererFeuille.setDisable(false);
	    VueDescriptif.getChildren().clear();
	    
		VBox vBox3 = new VBox(new Label ("Adresse Entrepot :     "+ getAdresse(demandeLiv.getAdresseEntrepot())),
							  new Label ("Heure de Depart :      "+ dureeHms.format(demandeLiv.getHeureDepart())),
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
			Alert alerte = new Alert(AlertType.INFORMATION, "Feuille de route generee dans src/main/resources/ ");
    		alerte.showAndWait();
			
    		
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
	    demandeLiv=null;
	    intersectionSelectionnee=null;
	    listeDeCommandes = null;
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
	public static String getAdresse(Intersection intersec){
		  for(Troncon troncon : plan.getTroncons()){
          	if(troncon.getDestination().getId() == intersec.getId() || troncon.getOrigine().getId() == intersec.getId()){
          		if(troncon.getNomRue().equals("")){
          			return "Rue sans nom";
          		}else{
          			return troncon.getNomRue();
          		}
                  }
          }  
		  return "";
	}
	

	public void update(){
		ArrayList<Livraison> livraisons = new ArrayList<>();
		
		Group groupe = dessinerPlan.Dessiner(plan);
		Group groupe2 = dessinerPlan.Dessiner(demandeLiv,plan);
		VuePlan.getChildren().clear();
	    VuePlan.getChildren().add(groupe);
	    VuePlan.getChildren().add(groupe2);
	    VueDescriptif.getChildren().clear();
	    VBox vBox3;
		if(tournee != null){
			VuePlan.getChildren().add(dessinerPlan.afficherChemin(tournee));
			dessinerPlan.actualiserCouleurPoints(tournee);
			
		    livraisons = tournee.getListeLivraison();
		    
			vBox3 = new VBox(new Label ("Adresse Entrepot :     "+ getAdresse(demandeLiv.getAdresseEntrepot())),
							 new Label ("Heure de Depart :      "+ dureeHms.format(demandeLiv.getHeureDepart())),
							 new Label ("Heure de Retour :      "+ dureeHms.format(tournee.getHeureArrive())));
			
			vBox3.setSpacing(10);
		}
		else{
			livraisons = demandeLiv.getLivraisons();
			vBox3 = new VBox(new Label ("Adresse Entrepot :     "+ getAdresse(demandeLiv.getAdresseEntrepot())),
					  		 new Label ("Heure de Depart :      "+ dureeHms.format(demandeLiv.getHeureDepart())),
					  		 new Label ("Heure de Retour :      "));
		}
	
		VBox vBox2 = new VBox(vBox3,dController.ListerLivraisons(livraisons, plan, tournee));
		
		vBox2.setSpacing(40);
		vBox2.setLayoutX(30);
		vBox2.setLayoutY(50);
		VueDescriptif.getChildren().add(vBox2); 
		dessinerPlan.PannableScene(VuePlan.getScene(), this);
	}
	
	
	public Intersection getIntersectionSelectionnee() {
		return intersectionSelectionnee;
	}
	
	
	public void setIntersectionSelectionnee(Intersection intersectionSelectionnee) {
		this.intersectionSelectionnee = intersectionSelectionnee;
	}
	
	
	public static Plan getPlan() {
		return plan;
	}
	
	
	public static void setPlan(Plan plan) {
		AccueilController.plan = plan;
	}
	
	
	public Tournee getTournee() {
		return tournee;
	}
	
	
	public void setTournee(Tournee tournee) {
		AccueilController.tournee = tournee;
	}
	
	
	public static DescriptifController getdController() {
		return dController;
	}
	
	
	public static void setdController(DescriptifController dController) {
		AccueilController.dController = dController;
	}
	
	
	public DemandeLivraison getDemandeLiv() {
		return demandeLiv;
	}
	
	
	public void setDemandeLiv(DemandeLivraison demandeLiv) {
		this.demandeLiv = demandeLiv;
	}
	
	
	public static ListeDeCdes getListeDeCdes() {
		return listeDeCommandes;
	}
	
	
	public static void setListeDeCdes(ListeDeCdes listeDeCommandes) {
		AccueilController.listeDeCommandes = listeDeCommandes;
	}
	
	
	public void Undo(){
		listeDeCommandes.undo();
		System.out.println("Undo");
		update();
	}
	
	
	public void Redo(){
		listeDeCommandes.redo();
		update();
	}

}  


