package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
	public static DataFormat dataFormat =  new DataFormat("model.Livraison");

	public Plan plan;
	 ObservableList<Livraison> data = FXCollections.observableArrayList();
	   final ListView<Livraison> listView = new ListView<Livraison>(data);
	
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
								plageHoraire=debut +" - "+fin + "        ";
							}else {
								plageHoraire="";
							}
							
							plageHoraire+= "Duree livraison : "+item.getDuree()/60+" min";
							
							
							
							super.updateItem(item, bln);
							 //setItem(item);
			                    

							VBox vBox = new VBox(new Text(getAdresse(item.getDestination())), new Text(plageHoraire));
							
							//Affichage des temps de passage
							if (tournee!=null) {
								for(Livraison l : tournee.getListeLivraison()){
									if(l.toString().equals(item.toString())){
										item = l;
									}
								}
								String heureArrivee="";
								String attente="";
								Date[] tmp=tournee.getTempsPassage()[tournee.getListeLivraison().indexOf(item)];
						    	heureArrivee="Arrivee a "+dureeHms.format(tmp[0]);
						    	attente=(tmp[1]==null?"    Pas d'attente":("  Attente "+dureeHms.format(new Date(tmp[1].getTime()-tmp[0].getTime()-3600000))+ " min"));

						    	Text txtHeureArrivee;
						    	
						    	//COULEUR
						    	if (item.getFinPlageHoraire()==null) {
						    		txtHeureArrivee = new Text(heureArrivee);
						    		txtHeureArrivee.setFill(Color.BLACK);
						    	}
						    	else {
						    		txtHeureArrivee = new Text(heureArrivee+ attente);
							    	if (tmp[1] == null) { //s'il n'y a pas d'attente
							    		txtHeureArrivee.setFill(Color.GREEN);
							    	}
							    	else { //sinon on teste si c'est une plage horaire tendue 
							    		
							    		long plageTendueMin = item.getFinPlageHoraire().getTime() - 3600000 - tmp[0].getTime();
							    		if (plageTendueMin <= 15*60000){
							    			txtHeureArrivee.setFill(Color.RED);//Plage tendue !!
							    		}
							    		else if (plageTendueMin <= 45*60000) {
							    			txtHeureArrivee.setFill(Color.ORANGE);//Plage un peu tendue mais pas trop
							    		}
							    		else {
							    			txtHeureArrivee.setFill(Color.GREEN);//Détendue mais attente quand meme..
							    		}
							    	}
						    	}
						    	
						    	//2e vBox qui affiche la dur�e et l'heure d'arriv�e
						    	Text txtDureeTrajet;
						    	int index = tournee.getListeLivraison().indexOf(item);
						    	if (index ==0) {
						    		txtDureeTrajet = new Text("Duree du trajet : "+dureeHms.format(new Date(tmp[0].getTime()-tournee.getHeureDepart().getTime()-3600000)));
						    	}
						    	else {
						    		long dureeTrajet = tmp[0].getTime()-(tournee.getTempsPassage()[index-1][1]==null?tournee.getTempsPassage()[index-1][0].getTime():tournee.getTempsPassage()[index-1][1].getTime())-3600000;
						    		txtDureeTrajet = new Text("Duree du trajet : "+dureeHms.format(new Date(dureeTrajet)));
							    	
						    	}
						    	VBox vBox2 = new VBox(vBox,new VBox(txtDureeTrajet,txtHeureArrivee));
						    	//vBox2.setSpacing(10); (????)
						    	setGraphic(vBox2);
							}

							// HBox hBox = new HBox(new ImageView, vBox);
							// hBox.setSpacing(10);
							else setGraphic(vBox);
							setOnDragDetected(new EventHandler<MouseEvent>(){
		                        @Override
		                        public void handle(MouseEvent event) {
					            	 if (getItem() == null) {
					                     return;
					                 }
					                System.out.println( "listcell setOnDragDetected" );
					                Dragboard db = startDragAndDrop( TransferMode.MOVE );
					                ClipboardContent content = new ClipboardContent();
					                content.put(dataFormat, getItem());
					                
					                db.setContent( content );
					                event.consume();
					                System.out.println( "listcell setOnDragDetected" );
					            }} );
					            setOnDragEntered(new EventHandler<DragEvent>(){
			                        @Override
			                        public void handle(DragEvent event) {
					                setStyle( "-fx-background-color: PaleGreen;" );
					            }} );

					            setOnDragExited( ( DragEvent event ) ->
					            {
					                setStyle( "" );
					            } );

					           setOnDragOver(new EventHandler<DragEvent>(){
			                        @Override
			                        public void handle(DragEvent event) {
					    
					                Dragboard db = event.getDragboard();
					                if ( db.hasContent(dataFormat) )
					                {
					                    event.acceptTransferModes( TransferMode.MOVE );
					                }
					                event.consume();
					            } });
					            setOnDragDropped(new EventHandler<DragEvent>(){
			                        @Override
			                        public void handle(DragEvent event) {
					            	 if (getItem() == null) {
					                     return;
					                 }
					                System.out.println( "listCell.setOnDragDropped" );
					                Dragboard db = event.getDragboard();
					                boolean success = false;
					                if ( db.hasContent(dataFormat))
					                {
					                	System.out.println( "listCell.setOnDragDropped TRUE" );
					                    ObservableList<Livraison> items = listView.getItems();					               
					                    //int draggedIdx = items.indexOf(db.getContent(dataFormat));
					                    int draggedIdx = getListView().getSelectionModel().getSelectedIndex();
					                    int thisIdx = items.indexOf(getItem());
					                    System.out.println(draggedIdx+"**"+thisIdx);
					                    System.out.println(((Livraison) db.getContent(dataFormat)).toString());
					                    items.remove(draggedIdx);
					                    items.add(thisIdx,(Livraison) db.getContent(dataFormat) );
					                    
					                    //items.set(thisIdx, (Livraison) db.getContent(dataFormat));

					                    List<Livraison> itemscopy = new ArrayList<>(items);
					                    listView.getItems().setAll(itemscopy);

					                    success = true;
					                }
					                event.setDropCompleted( success );
					                event.consume();
					            }} );

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
	public void setDraggable(ListView<String> list){
		
	}
  
}

