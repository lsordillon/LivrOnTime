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
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Callback;

import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Tournee;
import model.Troncon;
import model.Chemin;

import vue.DessinerPlan;


public class DescriptifController {
	
	public static DataFormat dataFormat =  new DataFormat("model.Livraison");
	private DessinerPlan dessinerPlan;
	private Chemin cheminSelectionne;
	private AccueilController accueilController;
	 
	ObservableList<Livraison> data = FXCollections.observableArrayList();
	final ListView<Livraison> listView;
	   
	   
	public DescriptifController(DessinerPlan dessinerPlan, AccueilController accueilController) {
		   this.dessinerPlan = dessinerPlan;
		   listView = new ListView<Livraison>(data);
		   this.accueilController = accueilController;
	}
	
	
	   // ************ ListesLivraison *********************
	public ListView<Livraison> ListerLivraisons(ArrayList<Livraison> livr, Plan plan, Tournee tournee){
		
		data.clear();
		
		data.addAll(livr);

		listView.setCellFactory(new Callback<ListView<Livraison>, ListCell<Livraison>>() {

			public ListCell<Livraison> call(ListView<Livraison> arg0) {
				return new ListCell<Livraison>() {

			@Override
					protected void updateItem(Livraison livr, boolean bln) {
						String plageHoraire;
						

						SimpleDateFormat dureeHm = new SimpleDateFormat("HH:mm");
						
						if (livr != null) {
							if (livr.getDebutPlageHoraire()!= null && livr.getFinPlageHoraire()!=null){
								String debut = dureeHm.format(livr.getDebutPlageHoraire());
								String fin = dureeHm.format(livr.getFinPlageHoraire());
								plageHoraire=debut +" - "+fin + "        ";
							}else {
								plageHoraire="";
							}
							
							plageHoraire+= "Duree livraison : "+livr.getDuree()/60+" min";
							
							
							
							super.updateItem(livr, bln);
							 //setItem(item);
			                    

							VBox vBox = new VBox(new Text(getAdresse(livr.getDestination())), new Text(plageHoraire));
							
							//Affichage des temps de passage
							if (tournee!=null) {
								for(Livraison l : tournee.getListeLivraison()){
									if(l.toString().equals(livr.toString())){
										livr = l;
									}
								}
								String heureArrivee="";
								String attente="";
								Date[] tmp=tournee.getTempsPassage()[tournee.getListeLivraison().indexOf(livr)];
						    	heureArrivee="Arrivee a "+dureeHm.format(tmp[0]);
						    	attente=(tmp[1]==null?"    Pas d'attente":("  Attente "+dureeHm.format(new Date(tmp[1].getTime()-tmp[0].getTime()-3600000))+ " min"));

						    	Text txtHeureArrivee;
						    	
						    	int valeurPH = tournee.VerifierPlagesHorairesUneLiv(livr);
						    	
						    	if (livr.getFinPlageHoraire()==null) {
						    		txtHeureArrivee = new Text(heureArrivee);
						    		txtHeureArrivee.setFill(Color.BLACK);
						    	}
						    	else {			
						    		txtHeureArrivee = new Text(heureArrivee+ attente);
						    		txtHeureArrivee.setFill(Color.GREEN);
						    		
							    	if (valeurPH == 0) { // pas dattente et pas tendu 
							    		txtHeureArrivee.setFill(Color.GREEN);
							    	}
									if (valeurPH == 1) { //pas d'attente et tendu
										txtHeureArrivee.setFill(Color.ORANGE);		    		
									}
									if (valeurPH == 2) { //attente
										txtHeureArrivee.setFill(Color.PURPLE);
									}
									if (valeurPH == 3) { // plage horaire violee
										txtHeureArrivee.setFill(Color.RED);
									}
									if (valeurPH == 4) { //erreur
										txtHeureArrivee.setFill(Color.BLUEVIOLET);
									}
						    	}
						    	

						    	
						    	//2e vBox qui affiche la dur�e et l'heure d'arriv�e

						    	Text txtDureeTrajet;
						    	Text spaceTxt = new Text("|");
						    	Text spaceTxt2 = new Text("|");
						    	
						    	int index = tournee.getListeLivraison().indexOf(livr);
						    	if (index ==0) {
						    		txtDureeTrajet = new Text("|    Trajet de "+dureeHm.format(new Date(tmp[0].getTime()-tournee.getHeureDepart().getTime()-3600000)));
						    	}
						    	else {
						    		long dureeTrajet = tmp[0].getTime()-(tournee.getTempsPassage()[index-1][1]==null?tournee.getTempsPassage()[index-1][0].getTime():tournee.getTempsPassage()[index-1][1].getTime())-3600000;
						    		txtDureeTrajet = new Text("|    Trajet de "+dureeHm.format(new Date(dureeTrajet))+" min");
							    	
						    	}
						    	txtDureeTrajet.setFill(Color.BLUE);
						    	spaceTxt.setFill(Color.BLUE);
						    	spaceTxt2.setFill(Color.BLUE);
						    	VBox vBox2 = new VBox(new VBox(spaceTxt,txtDureeTrajet,spaceTxt2),vBox,new VBox(txtHeureArrivee));
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
		for(Troncon troncon : accueilController.getPlan().getTroncons()){
        	if(troncon.getDestination().getId() == item.getId() || troncon.getOrigine().getId() == item.getId()){
          		return troncon.getNomRue();
           	}
        }  
		  return "";
	}
	
	
	public void interaction(final ListView<Livraison> listView) {
		
		if (accueilController.getTournee()!=null) {
			listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Livraison>() {
				
				public void changed(ObservableValue<? extends Livraison> observable, Livraison oldValue,Livraison newValue) {				
					if (listView.getSelectionModel().getSelectedItem() != null) {
						if (oldValue != null){
							dessinerPlan.actualiserCouleurPoints(accueilController.getTournee());					
							
							for(Troncon t: cheminSelectionne.getTroncons()){
								dessinerPlan.surlignerTroncon(t,Color.GREEN);
							}
							
						}
						
						
						long id = newValue.getDestination().getId();
						for ( Chemin c : accueilController.getTournee().getItineraire()) {
							if (c.getDestination().getId()==id){
								cheminSelectionne = c;
								
								for(Troncon t: c.getTroncons()){
					    			dessinerPlan.surlignerTroncon(t,Color.YELLOW);
					    		}
							}
						}
						
						((Circle) accueilController.getDessinerPlan().getDessine().get(id)).setFill(Color.YELLOW);
						((Circle) accueilController.getDessinerPlan().getDessine().get(id)).setStroke(Color.YELLOW);
					}
				
					dessinerPlan.passerChiffresDevant();
					
				}
			});
		}
	}
	
	
	public void setDraggable(ListView<String> list){
		
	}
  
}

