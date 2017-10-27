package controller;

import java.util.ArrayList;
import java.util.List;

import LivrOnTime.Main;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.Chemin;
import model.DemandeLivraison;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Tournee;
import model.Troncon;
import vue.DessinerPlan;

public class DescriptifController {
	
    //private final ObservableList<Row> data = FXCollections.observableArrayList();
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
	public Plan plan;
	 ObservableList<Livraison> data = FXCollections.observableArrayList();
	   final ListView<Livraison> listView = new ListView<Livraison>(data);
	
	
	public class CustomListView {
	  

	    }
	   // ************ ListesLivraison *********************
	public ListView<Livraison> ListerLivraisons(DemandeLivraison dl, Plan plan){
		this.plan = plan;
		
		 
	      data.addAll(dl.getLivraisons());
	     
	        listView.setCellFactory(new Callback<ListView<Livraison>, ListCell<Livraison>>() {

	            public ListCell<Livraison> call(ListView<Livraison> arg0) {
	                return new ListCell<Livraison>() {

	                    @Override
	                    protected void updateItem(Livraison item, boolean bln) {
	                    	String plageHoraire;
	                    	  if (item != null) {
	                    	if (item.getDebutPlageHoraire()!= null && item.getFinPlageHoraire()!=null){
	                            String debut = String.format("%02d:%02d", item.getDebutPlageHoraire().getHours() , item.getDebutPlageHoraire().getMinutes());
	                            String fin = String.format("%02d:%02d", item.getFinPlageHoraire().getHours() , item.getFinPlageHoraire().getMinutes());
	                            plageHoraire=debut +" - "+fin;
	                                  }else {
	                                      plageHoraire="--:-- - --:--";
	                					}
	                        super.updateItem(item, bln);
	                      
	                            VBox vBox = new VBox(new Text(getAdresse(item.getDestination())), new Text(plageHoraire));
	                         
	                           
	                           
	                           // HBox hBox = new HBox(new ImageView, vBox);
	                           // hBox.setSpacing(10);
	                            setGraphic(vBox);
	                            
	                            
	                        }
	                        
	                  
	                    }

	                };
	            }

			

			
	        });
	       
	        interaction(listView);
	        return listView;
	}
	//********************** ListerTournee ***********************
	public ListView<Livraison> ListerTournee(Tournee tournee,DemandeLivraison dl, Plan plan){
		data.clear();
		
		boolean premireFois = true;
		ArrayList<Livraison> livraisons = new ArrayList<Livraison>();
		for (Chemin chemin : tournee.getItineraire()){
	    	for(Livraison l : dl.getLivraisons()){
	    		if(l.getDestination().getId() == chemin.getOrigine().getId() && premireFois){
	    			livraisons.add(0,l);
	    			System.out.println("origine");
	    		}else if(l.getDestination().getId() == chemin.getDestination().getId()){
	    			livraisons.add(l);
	    			System.out.println("destination");
	    		}
	    	}
	    premireFois=false;
	    }
	      data.addAll(livraisons);
	      
	        listView.setCellFactory(new Callback<ListView<Livraison>, ListCell<Livraison>>() {

	            public ListCell<Livraison> call(ListView<Livraison> arg0) {
	                return new ListCell<Livraison>() {

	                    @Override
	                    protected void updateItem(Livraison item, boolean bln) {
	                    	String plageHoraire;
	                    	  if (item != null) {
	                    	if (item.getDebutPlageHoraire()!= null && item.getFinPlageHoraire()!=null){
	                            String debut = String.format("%02d:%02d", item.getDebutPlageHoraire().getHours() , item.getDebutPlageHoraire().getMinutes());
	                            String fin = String.format("%02d:%02d", item.getFinPlageHoraire().getHours() , item.getFinPlageHoraire().getMinutes());
	                            plageHoraire=debut +" - "+fin;
	                                  }else {
	                                      plageHoraire="--:-- - --:--";
	                					}
	                        super.updateItem(item, bln);
	                      
	                            VBox vBox = new VBox(new Text(getAdresse(item.getDestination())), new Text(plageHoraire));
	                         
	                           
	                           
	                           // HBox hBox = new HBox(new ImageView, vBox);
	                           // hBox.setSpacing(10);
	                            setGraphic(vBox);
	                            
	                            
	                        }
	                        
	                  
	                    }

	                };
	            }

			

			
	        });
	       
	        interaction(listView);

	        return listView;

	}
	

 
	//mettre seconde en format heure minutes secondes
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
	
	
	public void interaction(final ListView<Livraison> listView) {
		  listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Livraison>() {
			  public void changed(ObservableValue<? extends Livraison> observable, Livraison oldValue,
						Livraison newValue) {
				       if (listView.getSelectionModel().getSelectedItem() != null) {
				    	   if (oldValue != null){
				    	   ((Circle) DessinerPlan.dessine.get(oldValue.getDestination().getId())).setFill(Color.BLUE);
				    	   ((Circle) DessinerPlan.dessine.get(oldValue.getDestination().getId())).setStroke(Color.BLUE);
				    	   }
				    	   long id = newValue.getDestination().getId();
				          ((Circle) DessinerPlan.dessine.get(id)).setFill(Color.YELLOW);
				          ((Circle) DessinerPlan.dessine.get(id)).setStroke(Color.YELLOW);
				           
				       }
				   }

				
			
			});
	}
  
}

