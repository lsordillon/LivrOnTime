package vue;


import java.util.ArrayList;
import java.util.HashMap;

import controller.AccueilController;
import model.Chemin;
import model.DemandeLivraison;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.Tournee;
import model.Troncon;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

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
    DessinerPlan dessinerPlan;
    AccueilController controleur;
    Paint couleurSelectionne;
	Intersection intersectionSelectionnee;

    public SceneGestures( PannableCanvas canvas, DessinerPlan dessinerPlan, AccueilController controleur) {
        this.canvas = canvas;
        this.dessinerPlan = dessinerPlan;
        this.controleur = controleur;
        intersectionSelectionnee = null;
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
            if( event.isSecondaryButtonDown()) {

	            sceneDragContext.mouseAnchorX = event.getSceneX();
	            sceneDragContext.mouseAnchorY = event.getSceneY();
	
	            sceneDragContext.translateAnchorX = canvas.getTranslateX();
	            sceneDragContext.translateAnchorY = canvas.getTranslateY();
            }
            
           if(event.isPrimaryButtonDown()) {
            		
            }
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
            
            double scale = canvas.getScale();// currently we only use Y, same value is used for X
            double oldScale = scale;
            
            scale *= Math.pow(1.01, event.getDeltaY());
            scale = clamp( scale, MIN_SCALE, MAX_SCALE);

            double f = (scale / oldScale)-1;

            double dx = (event.getSceneX() - (canvas.getBoundsInParent().getWidth()/2 + canvas.getBoundsInParent().getMinX()));
            double dy = (event.getSceneY() - (canvas.getBoundsInParent().getHeight()/2 + canvas.getBoundsInParent().getMinY()));

            canvas.setScale( scale);
            // note: pivot value must be untransformed, i. e. without scaling
            canvas.setPivot(f*dx, f*dy);
            //System.out.println(scale);

            event.consume();
            

        }

    };


    public static double clamp( double value, double min, double max) {

        if( Double.compare(value, min) < 0)
        {
            return min;
        }
        if( Double.compare(value, max) > 0)
        {
            return max;
        }

        return value;
    }
}




/**
 * An application with a zoomable and pannable canvas.
 */
public class DessinerPlan {
	
	//Variables de mise a l'echelle
	private int divX, minusX,divY,minusY;
	private int minX,minY,maxX=0,maxY=0;
	
	//Taille supposee du canvas, a ne pas laisser en dur
	double sizeCanvas=400.0;
	double widthStroke=0.5;
	
	Pane overlay = new Pane();
	PannableCanvas canvas = new PannableCanvas();
	public static HashMap<Long,Circle> dessine = new HashMap<Long,Circle>();
	
	MouseGestures mg;

    public static HashMap<Long, Circle> getDessine() {
		return dessine;
	}

	public static void setDessine(HashMap<Long, Circle> dessine) {
		DessinerPlan.dessine = dessine;
	}

	// Methode qui dessine les troncons
    public void dessinerTroncon(Intersection D, Intersection O,Plan plan) {
    	int x,y;
    	   
    	
    	Circle circle1 = new Circle(1);
    	 circle1.setStroke(Color.BLACK);
         circle1.setFill(Color.BLACK);
         
         //Mise a l'echelle
         x= (int)((D.getX() - minusX)*sizeCanvas / divX);
         y=(int)((D.getY() - minusY) *sizeCanvas/ divY);
         circle1.setRadius(widthStroke/2);
         
         //Centrage
         circle1.relocate(y + sizeCanvas/2, -x+sizeCanvas);
         
        //Mise a l'echelle
        x= (int)((O.getX() - minusX)*sizeCanvas/ divX);
        y=(int)((O.getY() - minusY)*sizeCanvas/ divY );
    	Circle circle2 = new Circle(1);
    	circle2.setRadius(widthStroke/2);
        circle2.setStroke(Color.BLACK);
        circle2.setFill(Color.BLACK);
        //Centrage
        circle2.relocate(y+ sizeCanvas/2 , -x+ sizeCanvas);
        //Rendre les circles clickable
        mg.makeClickable(circle1);
        mg.makeClickable(circle2);
        
       
    	
    	
        Line line = new Line(circle1.getLayoutX(), circle1.getLayoutY(), circle2.getLayoutX(), circle2.getLayoutY());
        
        line.setStrokeWidth(2);
        line.setFill(Color.WHITE);
        line.setStroke(Color.WHITE);
        String adress = "";
        for(Troncon troncon : AccueilController.getPlan().getTroncons()){
        	if(troncon.getDestination().getId()== D.getId() && troncon.getOrigine().getId()==O.getId()){
        		adress = troncon.getNomRue();
        	}
        		
        }
        Tooltip tp = new Tooltip(adress);
        line.setOnMouseEntered(new EventHandler<MouseEvent>() {
             @Override
             public void handle(MouseEvent t) {
                  Node  node =(Node)t.getSource();
                  tp.show(node, line.getLayoutX()+t.getSceneX(), line.getLayoutY()+t.getSceneY());
                }
            });
        line.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                 
                 tp.hide();
               }
           });

        canvas.getChildren().add(line);
        
        if (!dessine.containsKey(D.getId())){
    		canvas.getChildren().add(circle1);
    		dessine.put(D.getId(), circle1);
    	}else{
    		((Circle) dessine.get(D.getId())).toFront();
    	}
    	
            
        if (!dessine.containsKey(O.getId())){
        	canvas.getChildren().add(circle2);
        	dessine.put(O.getId(), circle2);
        }else{
        	((Circle) dessine.get(O.getId())).toFront();
        }

    }

    public Group Dessiner(Plan plan) {
    	canvas = new PannableCanvas();
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
    	minusX=minX;
        divX=maxX-minusX;
        minusY=minY;
        divY=maxY-minusY;
        
        mg = new MouseGestures(plan, canvas);
       
        for (Troncon T: plan.getTroncons()){
        	
        	dessinerTroncon(T.getDestination(), T.getOrigine(),plan);
        }
        
   
        group.getChildren().add(canvas);
        return group;
               
    }
    
    public Group Dessiner(DemandeLivraison dl) {
    	ArrayList<Livraison> livraisons = dl.getLivraisons();
    	Group group = new Group();
        
        Circle circle = dessine.get(dl.getAdresseEntrepot().getId());
        canvas.getChildren().remove(circle);
    	circle.setStroke(Color.RED);
    	circle.setFill(Color.RED);
    	circle.setRadius(widthStroke*6);
    	 canvas.getChildren().add(circle);
        
        for (Livraison livraison : livraisons){
        	circle = dessine.get(livraison.getDestination().getId());
        	canvas.getChildren().remove(circle);
        	circle.setStroke(Color.BLUE);
        	circle.setFill(Color.BLUE);
        	circle.setRadius(widthStroke*4);
            canvas.getChildren().add(circle);
        }
     
      
        return group;
		             
	}
    
    public void PannableScene(Scene scene, AccueilController controleur) {
    	
    	   SceneGestures sceneGestures = new SceneGestures(canvas, this, controleur);
           scene.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
           scene.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
           scene.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
		
	}
   
    
    public Group afficherChemin(Tournee tournee){
		Group group = new Group();
		
		Circle circle1 = dessine.get(tournee.getItineraire().get(0).getOrigine().getId());
    	canvas.getChildren().remove(circle1);
    	circle1.setStroke(Color.GREEN);
    	circle1.setFill(Color.GREEN);
    	circle1.setRadius(widthStroke*4);
    	canvas.getChildren().add(circle1);
    	circle1.toFront();
    	Circle circle2;
    	for (Chemin c : tournee.getItineraire()){
    		for(Troncon t: c.getTroncons()){
    			circle1=dessine.get(t.getOrigine().getId());
    			circle2=dessine.get(t.getDestination().getId());
    			
    			 Line line = new Line(circle1.getLayoutX(), circle1.getLayoutY(), circle2.getLayoutX(), circle2.getLayoutY());
    		        
    			 line.setStroke(Color.GREEN);
    			line.setFill(Color.GREEN);
    		        line.setStrokeWidth(widthStroke*4);
    		      
    		        canvas.getChildren().add(line);
    		        circle1.toFront();
    		        circle2.toFront();
    		}
    		    
    	}
		
    	
    	return group;
    }

	public int getDivX() {
		return divX;
	}

	public void setDivX(int divX) {
		this.divX = divX;
	}

	public int getMinusX() {
		return minusX;
	}

	public void setMinusX(int minusX) {
		this.minusX = minusX;
	}

	public int getDivY() {
		return divY;
	}

	public void setDivY(int divY) {
		this.divY = divY;
	}

	public int getMinusY() {
		return minusY;
	}

	public void setMinusY(int minusY) {
		this.minusY = minusY;
	}

	public double getSizeCanvas() {
		return sizeCanvas;
	}

	public void setSizeCanvas(double sizeCanvas) {
		this.sizeCanvas = sizeCanvas;
	}
    
   
}
