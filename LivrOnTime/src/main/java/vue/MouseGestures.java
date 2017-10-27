package vue;


import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Intersection;
import model.Plan;


	public class MouseGestures {
		
	    double orgSceneX, orgSceneY;
	    double orgTranslateX, orgTranslateY;
	    private Plan plan;
	    long key;
	  
	    public MouseGestures(Plan plan) {
			this.plan=plan;
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
	                
	                p.setFill(Color.CYAN);
	                p.setStroke(Color.CYAN);
	                
	                	for(Circle circle : DessinerPlan.dessine.values()){
	                		if(circle.equals(p)){
	                			key = getKeyByValue(DessinerPlan.dessine, circle);
	                		}
	                }
	                	Intersection intersectionClicked = plan.getIntersections().get(key);
	                	System.out.println("Intersecrion ID "+intersectionClicked.getId());

	            } else {

	                Node p = ((Node) (t.getSource()));

	                orgTranslateX = p.getTranslateX();
	                orgTranslateY = p.getTranslateY();
	               

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
	}

