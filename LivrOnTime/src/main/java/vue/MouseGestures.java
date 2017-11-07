package vue;


import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

import controller.LivraisonController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Intersection;
import model.Plan;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;


	public class MouseGestures {
		
	    double orgSceneX, orgSceneY;
	    double orgTranslateX, orgTranslateY;
	    private Plan plan;
	    long key;
	    Paint couleurSelectionne;
		Circle cercleSelectionne;
		PannableCanvas canvas;
	  
	    public MouseGestures(Plan plan, PannableCanvas canvas) {
			this.plan=plan;
			this.canvas = canvas;
		}


		public void makeClickable(Node node) {
	        node.setOnMousePressed(circleOnMousePressedEventHandler);
	    }
		
	    //Event handler du clique sur un cercle
	    EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent t) {
	        	

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
	                
	                p.setFill(Color.CYAN);
	                p.setStroke(Color.CYAN);
	                
	                
	                	/*Est ce qu'on pourrait pas remplacer toute la boucle par : 
	                	 * key = getKeyByValue(DessinerPlan.dessine, p );
	                	 */
	                
	                	for(Circle circle : DessinerPlan.dessine.values()){
	                		if(circle.equals(p)){
	                			key = getKeyByValue(DessinerPlan.dessine, circle);
	                		}
	                	}
	                	Intersection intersectionClicked = plan.getIntersections().get(key);
	                	System.out.println("Intersection ID "+intersectionClicked.getId());
	                	LivraisonController.setIntersection(intersectionClicked);
	                	if(LivraisonController.getDemandeL()!=null){
	                	Parent root;
	    		        try {
	    		        	root = FXMLLoader.load(getClass().getResource("../fxml/Livraison.fxml"));
	    		            Stage stage = new Stage();
	    		            stage.setTitle("Modifier Livraison");
	    		            stage.setScene(new Scene(root));
	    		            
	    		            stage.show();
	    		            
	    		        }
	    		        catch (IOException e) {
	    		            e.printStackTrace();
	    		        }
	                	
	            } 
	            }
	            
	            /*else {

	                Node p = ((Node) (t.getSource()));

	                orgTranslateX = p.getTranslateX();
	                orgTranslateY = p.getTranslateY();
	               

	            }*/
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
	    
	    
	}

