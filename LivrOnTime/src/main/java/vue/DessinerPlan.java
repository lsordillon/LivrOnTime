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
            System.out.println(canvas.getTranslateX() + " "+ canvas.getTranslateY());
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
            System.out.println(scale);
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
	public int divX = 40;
	public int minus =1300;
	int minX,minY,maxX=0,maxY=0;
	
	Pane overlay = new Pane();
	PannableCanvas canvas = new PannableCanvas();
	HashMap<Long,Circle> dessine = new HashMap<Long,Circle>();
	

    // Methode qui dessine les troncons
    public void dessinerTroncon(Intersection D, Intersection O) {
    	int x,y;
    	   
    	
    	Circle circle1 = new Circle(1);
    	 circle1.setStroke(Color.BLACK);
         circle1.setFill(Color.BLACK);
         x= (int)((D.getX() - minus)*1500.0 / divX);
         y=(int)((D.getY() - minus) *1500.0/ divX);
         circle1.relocate(y , -x);
         x= (int)((O.getX() - minus)*1500.0/ divX);
         y=(int)((O.getY() - minus)*1500.0/ divX );
    	Circle circle2 = new Circle(1);
        circle2.setStroke(Color.BLACK);
        circle2.setFill(Color.BLACK);
        circle2.relocate(y , -x);
    	
    	if (!dessine.containsKey(D.getId())){
    		canvas.getChildren().add(circle1);
    		dessine.put(D.getId(), circle1);
    	}
    	
            
        if (!dessine.containsKey(O.getId())){
        	canvas.getChildren().add(circle2);
        	dessine.put(O.getId(), circle2);
        }
        Line line = new Line(circle1.getLayoutX(), circle1.getLayoutY(), circle2.getLayoutX(), circle2.getLayoutY());
        line.setStrokeWidth(2);

        canvas.getChildren().add(line);
        

        
		

    }

    public Group Dessiner(Plan plan) {
       
    	Group group = new Group();
        
     
    	
    	int minX,minY,maxX=0,maxY=0;
    	//Calcul du minX et du min Y 
	   	 for (Troncon T: plan.getTroncons()){
	   		if(T.getDestination().getX()>maxX||T.getOrigine().getX()>maxY)
	   		{
	   			maxX=Math.max(T.getDestination().getX(), T.getOrigine().getX());
	   		}
	   		
	   		if(T.getDestination().getY()>maxY||T.getOrigine().getY()>maxY)
	   		{
	   			maxY=Math.max(T.getDestination().getY(), T.getOrigine().getY());
	   		}
	   	 }
    	
    	minY=maxY;
    	minX=maxX;
    	
    	
    	//Calcul du minX et du min Y 
    	 for (Troncon T: plan.getTroncons()){
    		if(T.getDestination().getX()<minX||T.getOrigine().getX()<minX)
    		{
    			minX=Math.min(T.getDestination().getX(), T.getOrigine().getX());
    		}
    		
    		if(T.getDestination().getY()<minY||T.getOrigine().getY()<minY)
    		{
    			minY=Math.min(T.getDestination().getY(), T.getOrigine().getY());
    		}
    	 }
        // create sample nodes which can be dragged
    	minus=Math.min(minX, minY);
        divX=Math.max(maxX, maxY)-minus;
       
        for (Troncon T: plan.getTroncons()){
        	
        	dessinerTroncon(T.getDestination(), T.getOrigine());
        }
        canvas.setTranslateX(-(maxX-minX)*1000/divX);
        canvas.setTranslateY((maxY-minY)*1000/divX);
        System.out.println("youyou "+canvas.getTranslateX()+" "+canvas.getTranslateY());
        group.getChildren().add(canvas);
        System.out.println(canvas.getWidth());
        return group;
               
    }
    
    public Group Dessiner(DemandeLivraison dl) {
    	ArrayList<Livraison> livraisons = dl.getLivraisons();
    	Group group = new Group();
    	 canvas.setTranslateX(-(maxX-minX)*1000/divX);
         canvas.setTranslateY((maxY-minY)*1000/divX);
        System.out.println("youyou "+canvas.getTranslateX()+" "+canvas.getTranslateY());
        
        Circle circle = dessine.get(dl.getAdresseEntrepot().getId());
        canvas.getChildren().remove(circle);
    	circle.setStroke(Color.RED);
    	circle.setFill(Color.RED);
    	circle.setRadius(15);
    	 canvas.getChildren().add(circle);
        
        for (Livraison livraison : livraisons){
        	circle = dessine.get(livraison.getDestination().getId());
        	canvas.getChildren().remove(circle);
        	circle.setStroke(Color.BLUE);
        	circle.setFill(Color.BLUE);
        	circle.setRadius(8);
            canvas.getChildren().add(circle);
        }
      
        return group;
		             
	}
    
    public void PannableScene(Scene scene) {
    	
    	   SceneGestures sceneGestures = new SceneGestures(canvas);
           scene.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
           scene.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
           scene.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
		
	}
    
   
}
