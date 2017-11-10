package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.w3c.dom.Element;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
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
import vue.MouseGestures;
import vue.SceneGestures;


/**
 * Controleur principal de l application.
 * @author Matthieu
 *
 */
public class AccueilControleur{
	
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
	public VBox legendeText;

	private SimpleDateFormat dureeHms = new SimpleDateFormat("HH:mm:ss");
	
    private ListeDeCdes listeDeCommandes = new ListeDeCdes();
	private Plan plan;
	private Tournee tournee;
	private DessinerPlan dessinerPlan;
	private DemandeLivraison demandeLiv;
	private Intersection intersectionSelectionnee;
    private DescriptifController dController;
    private MouseGestures mouseGestures;
    private SceneGestures sceneGestures;
    
    /**
     * Constructeur de la classe AccueilControleur
     */
    public AccueilControleur() {
    	listeDeCommandes=new ListeDeCdes();
    	mouseGestures = new MouseGestures(this);
    	sceneGestures = new SceneGestures(this);	
    	dessinerPlan = new DessinerPlan(mouseGestures, sceneGestures);
    	dController = new DescriptifController(dessinerPlan,this);
    	
    }
    
    /**
     * La methode ChargerFichier permet de charger dans l application
     * un fichier de plan.
     * @param actionEvent
     * @throws FileNotFoundException
     */
	public void ChargerFichier (ActionEvent actionEvent) throws FileNotFoundException {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
		File fichierSelectionne = fileChooser.showOpenDialog(null);
		XmlParserPlan parserPlan = new XmlParserPlan();
		if (fichierSelectionne != null) {
		
		    InputStream xsd = new FileInputStream("src/main/resources/ValidationPlan.xsd");
	    	InputStream xml = new FileInputStream(fichierSelectionne.getAbsolutePath());
	        
		    if (parserPlan.validationXSD(xml, xsd)){
		    	 plan = CreerPlan(fichierSelectionne.getAbsolutePath());
		    	 try{
					Group group = dessinerPlan.Dessiner(plan);
					VuePlan.getChildren().clear();
					VuePlan.getChildren().add(group);
					sceneGestures.rendreCanvasZoomable(this);
					ChargerLivraison.setDisable(false);
					
			    }catch(Exception e){
			    	 Alert alert = new Alert(AlertType.ERROR, "Plan corrompu : certaines rues  ou intersections sont invalides"+ "\n");
	                 alert.showAndWait();
		    	 }
	    
		    }
		    
		    else{
		    		Alert alert = new Alert(AlertType.ERROR, "Format fichier non valide" +"\n" + parserPlan.getMessageErreur());
		    		alert.showAndWait();
		    }   
		}
	}
	
	/**
     * La methode ChargerFichier permet de charger dans l application
     * un fichier de demande de livraison.
     * @param actionEvent
     * @throws FileNotFoundException
     */
	public void ChargerLivraison(ActionEvent actionEvent) throws FileNotFoundException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
		File fichierSelectionne = fileChooser.showOpenDialog(null);
		boolean invalide=false;
		String fautive="";
		if (fichierSelectionne != null) {
			InputStream xsd = new FileInputStream("src/main/resources/ValidationDL.xsd");
	    	InputStream xml = new FileInputStream(fichierSelectionne.getAbsolutePath());
	    	XmlParserLivraison parserLivraison = new XmlParserLivraison();
	    	if(parserLivraison.validationXSD(xml, xsd))
	    	{
	    		parserLivraison.lecteur(fichierSelectionne.getAbsolutePath());
				if(!plan.getIntersections().containsKey((Long.parseLong((String)((Element)XmlParserLivraison.entrepot.item(0)).getAttribute("adresse")))))
				{
					fautive=((Element)XmlParserLivraison.entrepot.item(0)).getAttribute("adresse");
					invalide=true;
				}
				for(int i = 0; i<XmlParserLivraison.livraisons.getLength(); i++) {
					if(!plan.getIntersections().containsKey(Long.parseLong((String)((Element)XmlParserLivraison.livraisons.item(i)).getAttribute("adresse")))){
						fautive=((Element)XmlParserLivraison.livraisons.item(i)).getAttribute("adresse");
						invalide=true;
					}
				}
				if(!invalide){
	    		
					demandeLiv = new DemandeLivraison(XmlParserLivraison.livraisons,XmlParserLivraison.entrepot,plan);
					LivraisonController.setDL(demandeLiv);
					VuePlan.getChildren().add(dessinerPlan.Dessiner(demandeLiv,plan));
				    sceneGestures.rendreCanvasZoomable(this);			    
	
				    VBox vBox3 = new VBox(new Label ("Adresse Entrepot :     "+ getAdresse(demandeLiv.getAdresseEntrepot())),
				    					  new Label ("Heure de Depart :      "+ dureeHms.format(demandeLiv.getHeureDepart())),
				    					  new Label ("Heure de Retour :      "));
			   		vBox3.setSpacing(10);
			   		VBox vBox2 = new VBox(vBox3,dController.ListerLivraisons(demandeLiv.getLivraisons(), plan, null));
			   		
			   		vBox2.setSpacing(40);
			   		vBox2.setLayoutX(70);
			        vBox2.setLayoutY(50);
				    VueDescriptif.getChildren().add(vBox2);
				    CalculTournee.setDisable(false);
				    ChargerButoon.setDisable(true);
				    ChargerLivraison.setDisable(true);
				    listeDeCommandes.reset();
				} 
				
				else{
                    Alert alerte = new Alert(AlertType.ERROR, "Demande de livraison corrompue : L'adresse '"+fautive+"' n'existe pas "+ "\n");
                    alerte.showAndWait();
				}
	    	}
	    	
	    	else{
	    		Alert alerte = new Alert(AlertType.ERROR, "Format fichier non valide"+ "\n" + parserLivraison.getMessageErreur());
	    		alerte.showAndWait();
	    	}	
		}
		
	}

	/**
	 * La methode CalculTournee calcule la tournee
	 * et l affiche sur le plan.
	 * @param actionEvent
	 */
	public void CalculTournee(ActionEvent actionEvent) {
		
		try {
			tournee=plan.calculerLaTournee(demandeLiv);
		
			tournee.initTempsPassage();
			VBox vBoxPlan = new VBox(dessinerPlan.afficherChemin(tournee), legendeText);
			VuePlan.getChildren().add(vBoxPlan);
	
			sceneGestures.rendreCanvasZoomable(this);
		    
		    GenererFeuille.setDisable(false);
		    VueDescriptif.getChildren().clear();
		    
			VBox vBox3 = new VBox(new Label ("Adresse Entrepot :     "+ getAdresse(demandeLiv.getAdresseEntrepot())),
								  new Label ("Heure de Depart :      "+ dureeHms.format(demandeLiv.getHeureDepart())),
								  new Label ("Heure de Retour :      "+ dureeHms.format(tournee.getHeureArrive())));
			vBox3.setSpacing(10);
			
			ArrayList <Livraison> listeDestinations=tournee.getListeLivraison();
			Livraison retourEntrepot=new Livraison(0,tournee.getItineraire().get(0).getOrigine());
			listeDestinations.add(retourEntrepot);
			VBox vBox2 = new VBox(vBox3,dController.ListerLivraisons(listeDestinations, plan, tournee));
		
			vBox2.setSpacing(40);
			vBox2.setLayoutX(30);
			vBox2.setLayoutY(50);
			VueDescriptif.getChildren().add(vBox2); 
			CalculTournee.setDisable(true);
			legendeText.setVisible(true);
			
		} 
		
		catch (Exception e) {
			 Alert alert = new Alert(AlertType.ERROR, "Une livraison inaccessible sur ce plan ! ");
             alert.showAndWait();
		}
	}
	
	/**
	 * La methode GenererFeuille genere la feuille de route
	 * une fois la tournee calculee et l ouvre.
	 * @param actionEvent
	 */
	public void GenererFeuille(ActionEvent actionEvent) {
		FileWriter fichierGenere;
		
		try {
			fichierGenere = new FileWriter("src/main/resources/FeuilleDeRoute.txt");
			fichierGenere.write(feuilleDeRouteTxt.genererFeuilleDeRoute(tournee));
			fichierGenere.close();	
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
	
	/**
	 * La methode retourAccueil permet de remettre l application dans
	 * l etat d origine pour recharger un nouveau plan et une nouvelle 
	 * demande de livraison.
	 * @param actionEvent
	 */
	public void retourAccueil(ActionEvent actionEvent) {
		VuePlan.getChildren().clear();
		VueDescriptif.getChildren().clear();
		
		ChargerButoon.setDisable(false);
	    ChargerLivraison.setDisable(true);
	    CalculTournee.setDisable(true);
	    GenererFeuille.setDisable(true);
	    legendeText.setVisible(false);
	    undoButton.setDisable(true);
	    redoButton.setDisable(true);
	    
	    plan=null;
	    tournee=null;
	    demandeLiv=null;
	    intersectionSelectionnee=null;
	    listeDeCommandes = new ListeDeCdes();
	}
	
	/**
	 * La methode CreerPlan parse le fichier xml du plan
	 * et genere les objets correspondants.
	 * @param chemin
	 * @return
	 * @throws FileNotFoundException
	 */
	public Plan CreerPlan(String chemin) throws FileNotFoundException{
		XmlParserPlan parser = new XmlParserPlan();
		Plan plan = new Plan();
		
		parser.lecteur(chemin);
		plan.CreerIntersections(XmlParserPlan.noeuds);
		plan.CreerTroncons(XmlParserPlan.troncons);
		plan.TronconsVoisins();
		return plan;
	}   
	
	/**
	 * La methode mettreAJour synchronise la vue textuelle et le plan
	 * apres une modificatio, quelconque de la tournee.
	 */
	public void mettreAJour(){
		ArrayList<Livraison> livraisons = new ArrayList<>();
		
		Group groupe = dessinerPlan.Dessiner(plan);
		Group groupe2 = dessinerPlan.Dessiner(demandeLiv,plan);
		VuePlan.getChildren().clear();
	    VuePlan.getChildren().add(groupe);
	    VuePlan.getChildren().add(groupe2);
	    VueDescriptif.getChildren().clear();
	    VBox vBox3;
		if(tournee != null){
			VBox vBoxPlan = new VBox(dessinerPlan.afficherChemin(tournee), legendeText);
			legendeText.setLayoutY(0);
			VuePlan.getChildren().add(vBoxPlan);
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
		sceneGestures.rendreCanvasZoomable(this);
	}
	
	/**
	 * Implemente le design pattern undo
	 */
	public void Undo(){
		listeDeCommandes.undo();
		System.out.println("Undo");
		mettreAJour();
	}
	
	/**
	 * Implemente le design pattern redo
	 */
	public void Redo(){
		listeDeCommandes.redo();
		mettreAJour();
	}
	
	public String getAdresse(Intersection intersec){
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
	
	public static String convertSecondsToHMmSs(long seconds) {
	    long s = seconds % 60;
	    long m = (seconds / 60) % 60;
	    long h = (seconds / (60 * 60)) % 24;
	    return String.format("%d:%02d:%02d", h,m,s);
	}
	
	public Intersection getIntersectionSelectionnee() {
		return intersectionSelectionnee;
	}
	
	
	public void setIntersectionSelectionnee(Intersection intersectionSelectionnee) {
		this.intersectionSelectionnee = intersectionSelectionnee;
	}
	
	
	public Plan getPlan() {
		return plan;
	}
	
	
	public void setPlan(Plan plan) {
		this.plan = plan;
	}
	
	
	public Tournee getTournee() {
		return tournee;
	}
	
	
	public void setTournee(Tournee tournee) {
		this.tournee = tournee;
	}
	
	
	public DescriptifController getdController() {
		return dController;
	}
	
	
	public void setdController(DescriptifController dController) {
		this.dController = dController;
	}
	
	
	public DemandeLivraison getDemandeLiv() {
		return demandeLiv;
	}
	
	
	public void setDemandeLiv(DemandeLivraison demandeLiv) {
		this.demandeLiv = demandeLiv;
	}
	
	
	public ListeDeCdes getListeDeCdes() {
		undoButton.setDisable(false);
		redoButton.setDisable(false);
		return listeDeCommandes;
	}
	
	
	public void setListeDeCdes(ListeDeCdes listeDeCommandes) {
		this.listeDeCommandes = listeDeCommandes;
	}

	public DessinerPlan getDessinerPlan() {
		return dessinerPlan;
	}


	public void setDessinerPlan(DessinerPlan dessinerPlan) {
		this.dessinerPlan = dessinerPlan;
	}

}  


