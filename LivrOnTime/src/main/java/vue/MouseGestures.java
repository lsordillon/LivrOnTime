package vue;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import controleur.AccueilControleur;
import controleur.LivraisonControleur;

import java.util.Objects;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import modele.Intersection;
import modele.Plan;
import modele.Troncon;

/**
 * La classe MouseGestures gere toutes les interactions entre la souris
 * et l interface, elle implemente tous les Listeners.
 * @author Matthieu
 *
 */
public class MouseGestures {
	
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private Plan plan;
    private long cle;
    private Paint couleurSelectionne;
	private Circle cercleSelectionne;
	private PannableCanvas canvas;
	private AccueilControleur accueilControleur;
	
	final static Paint COULEURPOINTSELECTIONNE = Color.CYAN;

	/**
	 * Constructeur de la classe MouseGestures
	 * @param AccueilControleur
	 */
    public MouseGestures (AccueilControleur accueilControleur) {
    	this.accueilControleur = accueilControleur;
    }

    /**
     * La methode rendreDoubleCliquable gere l interaction des points
     * au double clic : ouvre la pop-up de modifications.
     * @param node
     */
	public void rendreDoubleCliquable(Node node) {
        node.setOnMousePressed(circleOnMousePressedEventHandler);
    }
	
	/**
     * La methode rendreCliquable gere l interaction des points
     * au clic : selectionne le point et le trajet qui y mene.
     * @param node
     */
	public void rendreCliquable(Node node) {
		node.setOnMouseClicked(circleOnMouseClickedEventHandler);
	}
	
	/**
     * La methode rendreLigneSurvolable gere l interaction des troncons
     * au passage de la souris : affiche le nom de la rue.
     * @param node
     */
	public void rendreLigneSurvolable(Node noeud, Intersection D, Intersection O) {
		if (noeud instanceof Line) {
			String adresse = "";
	        for(Troncon troncon : accueilControleur.getPlan().getTroncons()){
	        		if(troncon.getDestination().getId()== D.getId() && troncon.getOrigine().getId()==O.getId()){
	        			adresse = troncon.getNomRue();
	        		} 		
	        }
	        Tooltip tronconSurvole = new Tooltip(adresse);
	       
	        noeud.setOnMouseEntered(new EventHandler<MouseEvent>() {
	             @Override
	             public void handle(MouseEvent t) {
	                  Node  node =(Node)t.getSource();

	                  tronconSurvole.show(node, noeud.getLayoutX()+400, 300+noeud.getLayoutY());

	                }
	            });
	        noeud.setOnMouseExited(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent t) {             
	                 tronconSurvole.hide();
	               }
	           });

		}	
	}
	
	EventHandler<MouseEvent> circleOnMouseClickedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
       		double orgSceneX = t.getSceneX();
            double orgSceneY = t.getSceneY();
            if (t.getSource() instanceof Circle) {
            		Circle p = ((Circle) (t.getSource()));
                double orgTranslateX = p.getCenterX();
                double orgTranslateY = p.getCenterY();
                long key=0;
                for(Circle circle : accueilControleur.getDessinerPlan().getDessine().values()){	
            			if(circle.equals(p)){
            				key = getKeyByValue(accueilControleur.getDessinerPlan().getDessine(), circle);
            			}
                }
                Intersection intersectionClicked = plan.getIntersections().get(key);
                accueilControleur.getdControleur().setSelection(intersectionClicked);
            }
          }
      };
      
     
    EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
        	 if (t.getClickCount() == 2) {

	            orgSceneX = t.getSceneX();
	            orgSceneY = t.getSceneY();
	
	            if (t.getSource() instanceof Circle) {
	
	                Circle p = ((Circle) (t.getSource()));
	
	                orgTranslateX = p.getCenterX();
	                orgTranslateY = p.getCenterY();
	                
	                if (cercleSelectionne != null) {
	                	cercleSelectionne.setFill(couleurSelectionne);
	                	cercleSelectionne.setStroke(couleurSelectionne);
	                }
	                
	                cercleSelectionne = p;
	                couleurSelectionne = p.getFill();
	                
	                p.setFill(COULEURPOINTSELECTIONNE);
	                p.setStroke(COULEURPOINTSELECTIONNE);
	                
	                cle = getKeyByValue(accueilControleur.getDessinerPlan().getDessine(), p );
	                
	            	Intersection intersectionClicked = plan.getIntersections().get(cle);

	            	LivraisonControleur.setIntersection(intersectionClicked);
	            	if(LivraisonControleur.getDemandeL()!=null && accueilControleur.getTournee()!=null){
		            	Parent root;
				        try {
				        	root = FXMLLoader.load(getClass().getResource("../fxml/Livraison.fxml"));
				            Stage stage = new Stage();
				            stage.setTitle("Modifier Livraison");
				            stage.setAlwaysOnTop(true);
				            stage.setScene(new Scene(root));
				            
				            stage.show();
				            
				        }
				        catch (IOException e) {
				            e.printStackTrace();
				        }
	                	
	            	} 
	            }
	        }
        }
    };
    
    public static <T, E> T getKeyByValue(HashMap<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public PannableCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(PannableCanvas canvas) {
		this.canvas = canvas;
	}
     
}

