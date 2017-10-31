package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import model.Troncon;
import vue.DessinerPlan;

public class DescriptifController {
	
    //private final ObservableList<Row> data = FXCollections.observableArrayList();
    //private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
	public Plan plan;
	 ObservableList<Livraison> data = FXCollections.observableArrayList();
	   final ListView<Livraison> listView = new ListView<Livraison>(data);
	
	
	public class CustomListView {
	  

	    }
	   // ************ ListesLivraison *********************
	public ListView<Livraison> ListerLivraisons(ArrayList<Livraison> livr, Plan plan){
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

