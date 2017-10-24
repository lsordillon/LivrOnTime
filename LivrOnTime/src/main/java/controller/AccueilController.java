package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.scene.paint.Color;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import model.DemandeLivraison;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Tournee;
import model.Troncon;
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
	public Label AccueilLabel;
	
	private final TableView<Row> table = new TableView<Row>();
    private final ObservableList<Row> data = FXCollections.observableArrayList();
	private Plan plan;
	static DessinerPlan dessinerPlan;
	private DemandeLivraison dl;

	public void ChargerFichier (ActionEvent actionEvent) throws FileNotFoundException {
		

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
		File selectedFile = fileChooser.showOpenDialog(null);
		XmlParserPlan parserPlan = new XmlParserPlan();
		if (selectedFile != null) {
		  
		    
		    dessinerPlan = new DessinerPlan();
		
		    InputStream xsd = new FileInputStream("src/main/resources/ValidationPlan.xsd");
	    	InputStream xml = new FileInputStream(selectedFile.getAbsolutePath());
	        
		    if (parserPlan.validationXSD(xml, xsd)){
		    	 plan = CreerPlan(selectedFile.getAbsolutePath());
		    	 Group group = dessinerPlan.Dessiner(plan);
				    
				    VuePlan.getChildren().clear();
				    VuePlan.getChildren().add(group);
				    dessinerPlan.PannableScene(VuePlan.getScene());
				    ChargerLivraison.setDisable(false);
		    }else{
		    	Alert alert = new Alert(AlertType.ERROR, "Format fichier non valide");
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
	    		parserLivraison.Reader(selectedFile.getAbsolutePath());
				dl = new DemandeLivraison(XmlParserLivraison.livraisons,XmlParserLivraison.entrepot,plan);
				VuePlan.getChildren().add(dessinerPlan.Dessiner(dl));
			    dessinerPlan.PannableScene(VuePlan.getScene());			    
			    ListerLivraisons(dl.getLivraisons());
			    CalculTournee.setDisable(false);
	    	}else{
	    		Alert alert = new Alert(AlertType.ERROR, "Format fichier non valide");
	    		alert.showAndWait();
	    	}
			
			
		}
		
	}

	
	public void CalculTournee(ActionEvent actionEvent) {
		Tournee tournee=plan.calculerLaTournee(dl);

		VuePlan.getChildren().add(dessinerPlan.afficherChemin(tournee));
	    dessinerPlan.PannableScene(VuePlan.getScene());
			    
	}
	
	public Plan CreerPlan(String chemin) throws FileNotFoundException{
		XmlParserPlan parser = new XmlParserPlan();
		Plan plan = new Plan();
		
		parser.Reader(chemin);
		plan.CreerIntersections(XmlParserPlan.noeuds);
		plan.CreerTroncons(XmlParserPlan.troncons);
		plan.TronconsVoisins();
		return plan;

	}   

	
	public void ListerLivraisons(ArrayList<Livraison> livraisons){

		for(Livraison item : livraisons){
			String adresse = getAdresse(item.getDestination());
			String plageHoraire = "";
                              	  
            if (item.getDebutPlageHoraire()!= null && item.getFinPlageHoraire()!=null){
            String debut = String.format("%02d:%02d", item.getDebutPlageHoraire().getHours() , item.getDebutPlageHoraire().getMinutes());
            String fin = String.format("%02d:%02d", item.getFinPlageHoraire().getHours() , item.getFinPlageHoraire().getMinutes());
            plageHoraire=debut +" - "+fin;
                  }else {
                      plageHoraire="--:-- - --:--";
					}
             Row row = new Row(plageHoraire, adresse,convertSecondsToHMmSs(item.getDuree()));
             data.add(row);
             row.setId(item.getDestination().getId());
             }
		 TableColumn<Row, String> plageCol = new TableColumn<Row, String>("Plage Horaire");
		 plageCol.setMinWidth(60);
		 plageCol.setCellValueFactory(new PropertyValueFactory<Row,String>("plage"));
		 
	     TableColumn<Row, String> adresseCol = new TableColumn<Row, String>("Adresse");
	     adresseCol.setMinWidth(200);
	     adresseCol.setCellValueFactory(new PropertyValueFactory<Row,String>("adresse"));
	     
	     TableColumn<Row, String> dureeCol = new TableColumn<Row, String>("Duree");
	     dureeCol.setMinWidth(60);
	     dureeCol.setCellValueFactory(new PropertyValueFactory<Row,String>("duree"));
	     
	     
	     
	
			table.setItems(data);
			table.getColumns().addAll(plageCol, adresseCol, dureeCol);
            
            
           
           
           //interaction TableView-Plan
           table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Row>() {
			public void changed(ObservableValue<? extends Row> observableValue, Row oldValue, Row newValue) {
			       if (table.getSelectionModel().getSelectedItem() != null) {
			    	   if (oldValue != null){
			    	   ((Circle) dessinerPlan.dessine.get(oldValue.getId())).setFill(Color.BLUE);
			    	   ((Circle) dessinerPlan.dessine.get(oldValue.getId())).setStroke(Color.BLUE);
			    	   }
			    	   long id = newValue.getId();
			          ((Circle) dessinerPlan.dessine.get(id)).setFill(Color.YELLOW);
			          ((Circle) dessinerPlan.dessine.get(id)).setStroke(Color.YELLOW);
			           
			       }
			   }
		});
       VBox vBox = new VBox(new Label ("Adresse Entrepot: "+ getAdresse(dl.getAdresseEntrepot())),new Label ("Heure de Depart: "+ dl.getHeureDepart().getHours()+":"+dl.getHeureDepart().getMinutes()+":"+dl.getHeureDepart().getSeconds()));
   		vBox.setSpacing(10);
   		VBox vBox2 = new VBox(vBox,table);
   		vBox2.setSpacing(40);
   		vBox2.setLayoutX(30);
        vBox2.setLayoutY(100);
		VueDescriptif.getChildren().add(vBox2);
    

	}

	
	public static String convertSecondsToHMmSs(long seconds) {
	    long s = seconds % 60;
	    long m = (seconds / 60) % 60;
	    long h = (seconds / (60 * 60)) % 24;
	    return String.format("%d:%02d:%02d", h,m,s);
	}
	//Methode pour retourner l'adresse d'une intersection
	public String getAdresse(Intersection item){
		  for(Troncon troncon : plan.getTroncons()){
          	if(troncon.getDestination().getId() == item.getId() || troncon.getOrigine().getId() == item.getId()){
          		return troncon.getNomRue();
                  }
          }  
		  return "";
	}
	
	
	
	/** Récupère une intersection du plan a partir des coordonnee
	 * d un clic su le plan
	 * @param x, l abscisse du point
	 * @param y, l ordonnee du point
	 * @return selectionne, l intersection la plus proche des coordonnees
	 */
	public Intersection getIntersectionParCoordonnees (int x, int y) {
		
		Intersection selectionnee = null;
		
		ArrayList<Long> id_intersections= plan.getId_intersections();
		HashMap<Long,Intersection> intersections = plan.getIntersections();
		
		Iterator<Long> it = id_intersections.iterator();
		Long idCourant;
		Intersection courant;
		double distanceMin = Double.MAX_VALUE;
		double distance;
		
		while(it.hasNext()) {
			idCourant = it.next();
			courant = intersections.get(idCourant);
			
			
			distance = distancePoints(x, y, courant.getX(), courant.getY());
			if (distance < distanceMin) {
				distanceMin = distance;
				selectionnee = courant;
			}
			
		}
		return selectionnee;
		
	}
	
	/** Calcule la distance entre 2 points a partir de leurs
	 * coordonnees
	 * @param x1, l abscisse du point 1
	 * @param y1, l ordonnee du point 1
	 * @param x2, l abscisse du point 2
	 * @param y2, l abscisse du point 2
	 * @return distance, la distance entre les points
	 */
	public double distancePoints(int x1, int y1, int x2, int y2) {
		
		double distance = Math.sqrt(((x2-x1)*(x2-x1)) + ((y2-y1)*(y2-y1)));
		
		return distance;
	}
	
	
}  


