package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import model.DemandeLivraison;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Tournee;
import util.parserXML.XmlParserLivraison;
import util.parserXML.XmlParserPlan;
import vue.DessinerPlan;



public class AccueilController implements Initializable {
	
	//Etape 1 : Redefinir tout les composants existants dans le fichier fxml comme des attributs public du controller
	public AnchorPane VuePlan;
	public AnchorPane VueControl;
	public AnchorPane VueDescriptif; 
	public Button ChargerButoon;
	public Button ChargerLivraison;
	public Button CalculTournee;
	public Label AccueilLabel;

	private Plan plan;
	static DessinerPlan dessinerPlan;
	private DemandeLivraison dl;
	
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	
	public void ChargerFichier (ActionEvent actionEvent) throws FileNotFoundException {
		

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
		File selectedFile = fileChooser.showOpenDialog(null);
		XmlParserPlan parserPlan = new XmlParserPlan();
		if (selectedFile != null) {
		   // System.out.println(selectedFile.getAbsolutePath());
		    
		    dessinerPlan = new DessinerPlan();
		
		    InputStream xsd = new FileInputStream("src/main/java/resources/ValidationPlan.xsd");
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
			InputStream xsd = new FileInputStream("src/main/java/resources/ValidationDL.xsd");
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

	
	/*public void ListerLivraisons(ArrayList<Livraison> livraisons){
		ChargerButoon.setVisible(false);
		ChargerLivraison.setVisible(false);
		AccueilLabel.setVisible(false);
		
	ListView<HBox> listeLivraison = new ListView<>();
	HBox HBox  = new HBox(new Label("Plages Horaires"),new Label("Adresse"),new Label("Duree"));
	HBox.setSpacing(10);
	HBox.setAlignment(Pos.CENTER);
	listeLivraison.getChildren().add(HBox);
	
		for(Livraison item : livraisons){
	                        if (item != null) {
	                            HBox = new HBox(new Text("adresse"), new Text(convertSecondsToHMmSs(item.getDuree())));
	                           
	                            HBox hBox;
	                            if (item.getDebutPlageHoraire()!= null && item.getFinPlageHoraire()!=null){
	                            	String Debut = String.format("%02d:%02d", item.getDebutPlageHoraire().getHours() , item.getDebutPlageHoraire().getMinutes());
	                            	String Fin = String.format("%02d:%02d", item.getFinPlageHoraire().getHours() , item.getFinPlageHoraire().getMinutes());
	                            	hBox = new HBox(new Label(Debut +" - "+ Fin  ), HBox);
	                            	hBox.setAlignment(Pos.CENTER);
	                            }else {
	                            	hBox = new HBox(new Label("--:-- - --:--"), HBox);
	                            	hBox.setAlignment(Pos.CENTER);
								}
	                            
	                            hBox.setSpacing(10);
	                            listeLivraison.getChildren().add(hBox);
	                        }
	                    }

	    	listeLivraison.setAlignment(Pos.CENTER);
			listeLivraison.setPrefSize(418, 600);
			Highlight(listeLivraison);
	        VueControl.getChildren().add(listeLivraison);
	       
	}*/
	public void ListerLivraisons(ArrayList<Livraison> livraisons){
		
		
	
	  ObservableList<Livraison> data = FXCollections.observableArrayList();
      data.addAll(livraisons);

      final ListView<Livraison> listView = new ListView<Livraison>(data);
      listView.setCellFactory(new Callback<ListView<Livraison>, ListCell<Livraison>>() {

          public ListCell<Livraison> call(ListView<Livraison> arg0) {
              return new ListCell<Livraison>() {

                  @Override
                  protected void updateItem(Livraison item, boolean bln) {
                      super.updateItem(item, bln);
                      if (item != null) {
                    	  HBox HBox = new HBox(new Text("adresse"), new Text(convertSecondsToHMmSs(item.getDuree())));
                    	  HBox.setSpacing(10);
                    	  HBox hBox;
                    	  if (item.getDebutPlageHoraire()!= null && item.getFinPlageHoraire()!=null){
                          	String Debut = String.format("%02d:%02d", item.getDebutPlageHoraire().getHours() , item.getDebutPlageHoraire().getMinutes());
                          	String Fin = String.format("%02d:%02d", item.getFinPlageHoraire().getHours() , item.getFinPlageHoraire().getMinutes());
                          	hBox = new HBox(new Label(Debut +" - "+ Fin  ), HBox);
                          	hBox.setAlignment(Pos.CENTER);
                          }else {
                          	hBox = new HBox(new Label("--:-- - --:--"), HBox);
                          	hBox.setAlignment(Pos.CENTER);
							}
                          
                          hBox.setSpacing(10);
                          setGraphic(hBox);
                      }
                  }

              };
          }

      });
	listView.setLayoutX(41);
	listView.setLayoutY(140);
    listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {
            Livraison clicked = listView.getSelectionModel().getSelectedItem();
            Circle circle = DessinerPlan.getDessine().get(clicked.getDestination().getId());
            circle.setStroke(Color.GREEN);
            circle.getScene();
           
            System.out.println("cliick");
        }
    });
	
    VueDescriptif.getChildren().add(listView);
    

	}
	
	public static String convertSecondsToHMmSs(long seconds) {
	    long s = seconds % 60;
	    long m = (seconds / 60) % 60;
	    long h = (seconds / (60 * 60)) % 24;
	    return String.format("%d:%02d:%02d", h,m,s);
	}
	
	
	
	}  

