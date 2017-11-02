package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Tournee;
import model.Troncon;
import vue.DessinerPlan;

public class DescriptifController {

	public Plan plan;
	 ObservableList<Livraison> data = FXCollections.observableArrayList();
	   final ListView<Livraison> listView = new ListView<Livraison>(data);
	
	
	public class CustomListView {
	  

	    }
	   // ************ ListesLivraison *********************
	public ListView<Livraison> ListerLivraisons(ArrayList<Livraison> livr, Plan plan, Tournee tournee){
		this.plan = plan;
		
		data.clear();


		data.addAll(livr);

		listView.setCellFactory(new Callback<ListView<Livraison>, ListCell<Livraison>>() {

			public ListCell<Livraison> call(ListView<Livraison> arg0) {
				return new ListCell<Livraison>() {

			@Override
					protected void updateItem(Livraison item, boolean bln) {
						String plageHoraire;
						

						SimpleDateFormat dureeHms = new SimpleDateFormat("HH:mm");
						
						if (item != null) {
							if (item.getDebutPlageHoraire()!= null && item.getFinPlageHoraire()!=null){
								String debut = dureeHms.format(item.getDebutPlageHoraire());
								String fin = dureeHms.format(item.getFinPlageHoraire());
								plageHoraire=debut +" - "+fin;
							}else {
								plageHoraire="--:-- - --:--";
							}
							
							plageHoraire+= "        Duree : "+item.getDuree()/60+" min";
							
							
							
							super.updateItem(item, bln);
							

							VBox vBox = new VBox(new Text(getAdresse(item.getDestination())), new Text(plageHoraire));
							
							//Affichage des temps de passage
							if (tournee!=null) {
								String heureArrivee="";
								String attente="";
								Date[] tmp=tournee.getTempsPassage()[tournee.getListeLivraison().indexOf(item)];
						    	heureArrivee="Arrivee a "+dureeHms.format(tmp[0]);
						    	attente=(tmp[1]==null?"    Pas d'attente":("  Attente "+dureeHms.format(new Date(tmp[1].getTime()-tmp[0].getTime()-3600000))+ " min"));
						    	VBox vBox2 = new VBox();
						    	Text txt = new Text(heureArrivee+ attente);
						    	
						    	//COULEUR
						    	if (item.getFinPlageHoraire()==null) {
						    		txt.setFill(Color.BLACK);
						    	}
						    	else {
							    	if (tmp[1] != null) { //s'il n'y a pas d'attente
							    		txt.setFill(Color.GREEN);
							    	}
							    	else { //sinon on teste si c'est une plage horaire tendue 
							    		
							    		long plageTendueMin = item.getFinPlageHoraire().getTime() - 3600000 - tmp[0].getTime();
							    		if (plageTendueMin <= 15*60000){
							    			txt.setFill(Color.RED);//Plage tendue !!
							    		}
							    		else if (plageTendueMin <= 45*60000) {
							    			txt.setFill(Color.ORANGE);//Plage un peu tendue mais pas trop
							    		}
							    		else {
							    			txt.setFill(Color.GREEN);//Détendue
							    		}
							    	}
						    	}
						    	
						    	//Creation d'une nouvelle vBox
						    	vBox2.getChildren().addAll(vBox,new VBox(txt));
						    	vBox2.setSpacing(10);
						    	setGraphic(vBox2);
							}

							// HBox hBox = new HBox(new ImageView, vBox);
							// hBox.setSpacing(10);
							else setGraphic(vBox);


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

