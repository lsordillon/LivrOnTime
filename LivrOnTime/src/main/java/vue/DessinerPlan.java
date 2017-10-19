package vue;


import java.util.ArrayList;
import java.util.HashMap;

import com.sun.org.apache.xpath.internal.operations.Minus;

import model.DemandeLivraison;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Troncon;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

class PannableCanvas extends Pane {

    DoubleProperty myScale = new SimpleDoubleProperty(1.0);

    public PannableCanvas() {
        setPrefSize(600, 600);
     

        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
    }





 

    public double getScale() {
        return myScale.get();
    }

    public void setScale( double scale) {
        myScale.set(scale);
    }

    public void setPivot( double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }
}


class DragContext {

    double mouseAnchorX;
    double mouseAnchorY;

    double translateAnchorX;
    double translateAnchorY;

}

// Plan is draggable using the right mouse button 

class SceneGestures {

    private static final double MAX_SCALE = 10.0d;
    private static final double MIN_SCALE = .1d;

    private DragContext sceneDragContext = new DragContext();

    PannableCanvas canvas;

    public SceneGestures( PannableCanvas canvas) {
        this.canvas = canvas;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    public EventHandler<ScrollEvent> getOnScrollEventHandler() {
        return onScrollEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {

            // right mouse button => panning
            if( !event.isSecondaryButtonDown())
                return;

            sceneDragContext.mouseAnchorX = event.getSceneX();
            sceneDragContext.mouseAnchorY = event.getSceneY();

            sceneDragContext.translateAnchorX = canvas.getTranslateX();
            sceneDragContext.translateAnchorY = canvas.getTranslateY();

        }

    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {

            // right mouse button => panning
            if( !event.isSecondaryButtonDown())
                return;

            canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
            canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);

            event.consume();
        }
    };

    /**
     * Mouse wheel handler: zoom to pivot point
     */
    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

        public void handle(ScrollEvent event) {

            double delta = 1.2;

            double scale = canvas.getScale(); // currently we only use Y, same value is used for X
            double oldScale = scale;

            if (event.getDeltaY() < 0)
                scale /= delta;
            else
                scale *= delta;

            scale = clamp( scale, MIN_SCALE, MAX_SCALE);

            double f = (scale / oldScale)-1;

            double dx = (event.getSceneX() - (canvas.getBoundsInParent().getWidth()/2 + canvas.getBoundsInParent().getMinX()));
            double dy = (event.getSceneY() - (canvas.getBoundsInParent().getHeight()/2 + canvas.getBoundsInParent().getMinY()));

            canvas.setScale( scale);

            // note: pivot value must be untransformed, i. e. without scaling
            canvas.setPivot(f*dx, f*dy);

            event.consume();

        }

    };


    public static double clamp( double value, double min, double max) {

        if( Double.compare(value, min) < 0)
            return min;

        if( Double.compare(value, max) > 0)
            return max;

        return value;
    }
}



/**
 * An application with a zoomable and pannable canvas.
 */
public class DessinerPlan {
	public static final int divX = 40;
	public static final int minus =1300;
	
	Pane overlay = new Pane();
	PannableCanvas canvas = new PannableCanvas();
	HashMap<Long,Circle> dessiné = new HashMap<Long,Circle>();


    // Méthode qui dessine les tronçons
    public void dessinerTroncon(Intersection D, Intersection O) {
    	int x,y;
    	   
    	
    	Circle circle1 = new Circle(1);
    	 circle1.setStroke(Color.BLACK);
         circle1.setFill(Color.BLACK);
         x= D.getX() / divX - minus;
         y=D.getY() / divX - minus;
         circle1.relocate(x , y);
         
         x= O.getX() / divX -minus;
         y=O.getY() / divX - minus;
    	Circle circle2 = new Circle(1);
        circle2.setStroke(Color.BLACK);
        circle2.setFill(Color.BLACK);
        circle2.relocate(x , y);
    	
    	if (!dessiné.containsKey(D.getId())){
    		canvas.getChildren().add(circle1);
    		dessiné.put(D.getId(), circle1);
    	}
    	
            
        if (!dessiné.containsKey(O.getId())){
        	canvas.getChildren().add(circle2);
        	dessiné.put(O.getId(), circle2);
        }
        Line line = new Line(circle1.getLayoutX(), circle1.getLayoutY(), circle2.getLayoutX(), circle2.getLayoutY());
        line.setStrokeWidth(2);

        canvas.getChildren().add(line);
        

        
		

    }


   
    public Group Dessiner(Plan plan) {

       
    	Group group = new Group();
        canvas.setTranslateX(1000);
        canvas.setTranslateY(1000);
     

        // create sample nodes which can be dragged
        
        
        for (Troncon T: plan.getTroncons()){
        	
        	dessinerTroncon(T.getDestination(), T.getOrigine());
        }

       

        group.getChildren().add(canvas);
        return group;
       
        

    }
    
    public Group Dessiner(ArrayList<Livraison> livraisons) {
    	
    	Group group = new Group();
        canvas.setTranslateX(1000);
        canvas.setTranslateY(1000);
        for (Livraison livraison : livraisons){
        	Circle circle = dessiné.get(livraison.getDestination().getId());
        	canvas.getChildren().remove(circle);
        	circle.setStroke(Color.BLUE);
        	circle.setFill(Color.BLUE);
        	circle.setRadius(8);
            canvas.getChildren().add(circle);
        }
        group.getChildren().add(canvas);
        return group;
		
	}
    
    public void PannableScene(Scene scene) {
    	
    	   SceneGestures sceneGestures = new SceneGestures(canvas);
           scene.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
           scene.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
           scene.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
		
	}
    
   
}
