package controleur;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Label;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import modele.Chemin;
import modele.Intersection;
import modele.Livraison;
import modele.Plan;
import modele.Tournee;
import modele.Troncon;
import vue.DessinerPlan;
import LivrOnTime.Main;

/**
 * Controleur de la vue textuelle de l application.
 * 
 * @author Matthieu
 *
 */
public class DescriptifControleur {

	public static DataFormat dataFormat = new DataFormat("model.Livraison");
	private DessinerPlan dessinerPlan;
	private Chemin cheminSelectionne;
	private AccueilControleur accueilControleur;

	ObservableList<Livraison> data = FXCollections.observableArrayList();
	final ListView<Livraison> listView;

	/**
	 * Constructeur de la classe DescriptifControleur
	 * 
	 * @param dessinerPlan
	 * @param AccueilControleur
	 */
	public DescriptifControleur(DessinerPlan dessinerPlan, AccueilControleur AccueilControleur) {
		this.dessinerPlan = dessinerPlan;
		listView = new ListView<Livraison>(data);
		this.accueilControleur = AccueilControleur;
	}

	// ************ ListesLivraison *********************
	public ListView<Livraison> ListerLivraisons(ArrayList<Livraison> livr, Plan plan, Tournee tournee) {

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
							if (livr.getDebutPlageHoraire() != null && livr.getFinPlageHoraire() != null) {
								String debut = dureeHm.format(livr.getDebutPlageHoraire());
								String fin = dureeHm.format(livr.getFinPlageHoraire());
								plageHoraire = debut + " - " + fin + "        ";
							} else {
								plageHoraire = "";
							}
							if (livr.getDuree() != 0) {
								plageHoraire += "Duree livraison : " + livr.getDuree() / 60 + " min";
							} else {
								plageHoraire += "Retour entrepot";
							}

							super.updateItem(livr, bln);
							VBox vBox = new VBox(new Text(getAdresse(livr.getDestination())), new Text(plageHoraire));

							// Affichage des temps de passage
							if (tournee != null) {
								for (Livraison l : tournee.getListeLivraison()) {
									if (l.toString().equals(livr.toString())) {
										livr = l;
									}
								}
								String heureArrivee = "";
								String attente = "";
								Date[] tmp = tournee.getTempsPassage()[tournee.getListeLivraison().indexOf(livr)];
								heureArrivee = "Arrivee a " + dureeHm.format(tmp[0]);
								attente = (tmp[1] == null ? "    Pas d'attente"
										: ("  Attente "
												+ dureeHm
														.format(new Date(tmp[1].getTime() - tmp[0].getTime() - 3600000))
												+ " min"));
								if (tournee.getListeLivraison().get(tournee.getListeLivraison().indexOf(livr))
										.getFinPlageHoraire() != null) { // plage
																			// tendue

									Date[] tempsPoint = tournee.getTempsPassage()[tournee.getListeLivraison()
											.indexOf(livr)];
									Date horaireArr = tempsPoint[0];

									Date finPH = livr.getFinPlageHoraire();
									Date tempsRestantAvantFinPHdate = new Date(finPH.getTime() - horaireArr.getTime());
									long tempsRestantAvantFinPH = tempsRestantAvantFinPHdate.getTime();
									if (tempsRestantAvantFinPH < livr.getDuree() * 1000) {
										attente = "    Livraison impossible";
									}
								}

								Text txtHeureArrivee;

								int valeurPH = tournee.VerifierPlagesHorairesUneLiv(livr);

								if (livr.getFinPlageHoraire() == null) {
									txtHeureArrivee = new Text(heureArrivee);
									txtHeureArrivee.setFill(Color.BLACK);
								} else {
									txtHeureArrivee = new Text(heureArrivee + attente);
									txtHeureArrivee.setFill(Color.GREEN);

									if (valeurPH == 0) { // pas dattente et pas
															// tendu
										txtHeureArrivee.setFill(Color.GREEN);
									}
									if (valeurPH == 1) { // pas d'attente et
															// tendu
										txtHeureArrivee.setFill(Color.ORANGE);
									}
									if (valeurPH == 2) { // attente
										txtHeureArrivee.setFill(Color.PURPLE);
									}
									if (valeurPH == 3) { // plage horaire violee
										txtHeureArrivee.setFill(Color.RED);
									}
									if (valeurPH == 4) { // erreur
										txtHeureArrivee.setFill(Color.BLUEVIOLET);
									}
								}

								// 2e vBox qui affiche la duree et l'heure
								// d'arrivee

								Text txtDureeTrajet;
								int index = tournee.getListeLivraison().indexOf(livr);
								if (index == 0) {
									txtDureeTrajet = new Text("Trajet de "
											+ dureeHm.format(new Date(
													tmp[0].getTime() - tournee.getHeureDepart().getTime() - 3600000))
											+ " min");
								} else {
									long dureeTrajet = tmp[0].getTime()
											- (tournee.getTempsPassage()[index - 1][1] == null
													? tournee.getTempsPassage()[index - 1][0].getTime()
													: tournee.getTempsPassage()[index - 1][1].getTime())
											- 3600000;
									txtDureeTrajet = new Text(
											"Trajet de " + dureeHm.format(new Date(dureeTrajet)) + " min");

								}
								txtDureeTrajet.setFill(Color.BLUE);

								// Affichage de la duree du trajet avec la
								// fleche

								Image imageFleche = null;
								ImageView vueFleche = null;

								try {
									imageFleche = new Image(
											new FileInputStream("src/main/resources/img/down_arrow.png"));
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}

								if (imageFleche != null) {
									vueFleche = new ImageView(imageFleche);
									vueFleche.setFitWidth(25);
									vueFleche.setPreserveRatio(true);
								}

								HBox hBoxTrajet = new HBox(vueFleche, txtDureeTrajet);
								hBoxTrajet.setAlignment(Pos.CENTER_LEFT);
								VBox vBox2 = new VBox(hBoxTrajet, vBox, new VBox(txtHeureArrivee));
								setGraphic(vBox2);
							}

							else {
								setGraphic(vBox);
							}

							setOnMouseClicked(new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent event) {
									if (event.getClickCount() == 2) {
										Livraison livraison = listView.getSelectionModel().getSelectedItem();
										LivraisonControleur.setIntersection(livraison.getDestination());
										if (livraison != null && accueilControleur.getTournee() != null) {
											Parent root;
											try {
												root = FXMLLoader
														.load(getClass().getResource("../fxml/Livraison.fxml"));
												Stage stage = new Stage();
												stage.setTitle("Modifier Livraison");
												stage.setAlwaysOnTop(true);
												stage.setScene(new Scene(root));

												stage.show();

											} catch (IOException e) {
												e.printStackTrace();
											}

										}
									}
								}
							});
						}

					}

				};
			}

		});

		interaction(listView);
		return listView;
	}

	/**
	 * La classe interaction permet de synchroniser la selection dans la liste
	 * textuelle et sur le plan.
	 * 
	 * @param listView
	 */
	public void interaction(final ListView<Livraison> listView) {

		if (accueilControleur.getTournee() != null) {
			listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Livraison>() {

				public void changed(ObservableValue<? extends Livraison> observable, Livraison oldValue,
						Livraison newValue) {
					if (listView.getSelectionModel().getSelectedItem() != null) {
						if (oldValue != null) {
							dessinerPlan.actualiserCouleurPoints(accueilControleur.getTournee());

							for (Troncon t : cheminSelectionne.getTroncons()) {
								dessinerPlan.surlignerTroncon(t, Color.GREEN);
							}

						}

						long id = newValue.getDestination().getId();
						for (Chemin c : accueilControleur.getTournee().getItineraire()) {
							if (c.getDestination().getId() == id) {
								cheminSelectionne = c;

								for (Troncon t : c.getTroncons()) {
									dessinerPlan.surlignerTroncon(t, Color.YELLOW);
								}
							}
						}

						((Circle) accueilControleur.getDessinerPlan().getDessine().get(id)).setFill(Color.YELLOW);
						((Circle) accueilControleur.getDessinerPlan().getDessine().get(id)).setStroke(Color.YELLOW);
					}

					dessinerPlan.passerChiffresDevant();

				}
			});
		}
	}

	public static String convertSecondsToHMmSs(long seconds) {
		long s = seconds % 60;
		long m = (seconds / 60) % 60;
		long h = (seconds / (60 * 60)) % 24;
		return String.format("%d:%02d:%02d", h, m, s);
	}

	public String getAdresse(Intersection item) {
		for (Troncon troncon : accueilControleur.getPlan().getTroncons()) {
			if (troncon.getDestination().getId() == item.getId() || troncon.getOrigine().getId() == item.getId()) {
				return troncon.getNomRue();
			}
		}
		return "";
	}

	public void setSelection(Intersection inter) {

		Iterator<Livraison> it = data.iterator();
		while (it.hasNext()) {
			Livraison courante = it.next();
			if (courante.getDestination() == inter) {

				listView.getSelectionModel().select(data.indexOf(courante));

			}
		}

	}

}
