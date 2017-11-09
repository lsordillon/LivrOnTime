package vue;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Map.Entry;

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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.scene.layout.StackPane;


// Plan is draggable using the right mouse button 

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
	private ArrayList<Text> chiffres;
	
	public DessinerPlan (MouseGestures mouseGestures) {
		chiffres = new ArrayList<Text>();
		mg = mouseGestures;
	}

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
    		circle1.setStroke(Color.GREY);
        circle1.setFill(Color.GREY);

         
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
        circle2.setStroke(Color.GREY);
        circle2.setFill(Color.GREY);

        //Centrage
        circle2.relocate(y+ sizeCanvas/2 , -x+ sizeCanvas);
        //Rendre les circles clickable
        mg.makeClickable(circle1);
        mg.makeClickable(circle2);
        
    	
        Line line = new Line(circle1.getLayoutX(), circle1.getLayoutY(), circle2.getLayoutX(), circle2.getLayoutY());
        
        line.setStrokeWidth(widthStroke);
        line.setFill(Color.GREY);
        line.setStroke(Color.GREY);
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
    	dessine = new HashMap<Long,Circle>();
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
        
        mg.setCanvas(canvas);
        mg.setPlan(plan);
       
        for (Troncon T: plan.getTroncons()){
        	
        	dessinerTroncon(T.getDestination(), T.getOrigine(),plan);
        }
        
   
        group.getChildren().add(canvas);
        return group;
               
    }
    

    public Group Dessiner(DemandeLivraison dl, Plan plan) {
    		ArrayList<Livraison> livraisons = dl.getLivraisons();
    		Group group = new Group();
    		
    		Circle circle = dessine.get(dl.getAdresseEntrepot().getId());
    		canvas.getChildren().remove(circle);
    		circle.setStroke(Color.PLUM);
    		circle.setFill(Color.PLUM);
    		circle.setRadius(widthStroke*10);
    		canvas.getChildren().add(circle);

        
        for (Livraison livraison : livraisons){

        		circle = dessine.get(livraison.getDestination().getId());
        		canvas.getChildren().remove(circle);
        		circle.setStroke(Color.BLUE);
        		circle.setFill(Color.BLUE);
        		circle.setRadius(widthStroke*6);
            
            
            //ecoute
        		mg.rendreSurvolable(circle);
            
           //fin ecoute
           
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
		//circle1.toFront();
		
		actualiserCouleurPoints(tournee);
		
    	Circle circle2;
    	
    	for (Chemin c : tournee.getItineraire()){
    		
    		Text precedent = new Text();
    		
    		for(Troncon t: c.getTroncons()){
    			circle1=dessine.get(t.getOrigine().getId());
    			circle2=dessine.get(t.getDestination().getId());
    			
    			Line line = new Line(circle1.getLayoutX(), circle1.getLayoutY(), circle2.getLayoutX(), circle2.getLayoutY());
    		        
    			line.setStroke(Color.GREEN);
    			line.setFill(Color.GREEN);
    		    line.setStrokeWidth(widthStroke*4);
    		      
		        canvas.getChildren().add(line);
		        circle1.toFront();
		        
    		}
    		    
    	}
   	 
    	numeroterSommets(tournee);
    	passerChiffresDevant();
    	return group;
    }
    
    public void numeroterSommets(Tournee tournee) {
    	
    	if (!chiffres.isEmpty()) {
    		for(Text t : chiffres) {
    			canvas.getChildren().remove(t);
    		}
    		chiffres.clear();
    	}
    	
    	int indice = 1;
    	for (Chemin c : tournee.getItineraire()){
    		
    		Text chiffreOrigine = new Text(""+indice);
    		chiffreOrigine.setFill(Color.BLACK);
    		chiffreOrigine.setStroke(Color.BLACK);
	        chiffreOrigine.setBoundsType(TextBoundsType.VISUAL); 
	    	
	        chiffreOrigine.setX(dessine.get(c.getOrigine().getId()).getLayoutX() + widthStroke * 7);
	        chiffreOrigine.setY(dessine.get(c.getOrigine().getId()).getLayoutY() - widthStroke * 8);
	    	canvas.getChildren().add(chiffreOrigine);
	    	
	    	indice++;
	    	chiffres.add(chiffreOrigine);
	    	//System.out.println(c.getDestination());
    	}
    }
    
    public void passerChiffresDevant() {
    	for ( int i =0; i <chiffres.size(); i++) {
    		chiffres.get(i).toFront();
    	}
    }
    
    public void surlignerTroncon (Troncon t, Paint Couleur) {
    	
    	Circle circle1=DessinerPlan.dessine.get(t.getOrigine().getId());
		Circle circle2=DessinerPlan.dessine.get(t.getDestination().getId());
		
		Line line = new Line(circle1.getLayoutX(), circle1.getLayoutY(), circle2.getLayoutX(), circle2.getLayoutY());
	        
		line.setStroke(Couleur);
		line.setFill(Couleur);
	    line.setStrokeWidth(widthStroke*4);
	      
        canvas.getChildren().add(line);
        circle1.toFront();
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
	
	public static <T, E> T getKeyByValue(HashMap<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    
	public void actualiserCouleurPoints(Tournee tournee) {
	
		for (int i = 0; i<tournee.getItineraire().size()-1; i++){
			//retrouver lintersection correspondant a la livraison interLiv
			Livraison liv = tournee.getListeLivraison().get(i);
	        
			int[] valeursPH = tournee.VerifierPlagesHorairesTournee();
			
				if (valeursPH[i] == 0) {
					Circle cercleActuel = dessine.get(liv.getDestination().getId());
					cercleActuel.setFill(Color.BLUE);
					cercleActuel.setStroke(Color.BLUE);
				}
				if (valeursPH[i] == 1) {
					Circle cercleActuel = dessine.get(liv.getDestination().getId());
					cercleActuel.setFill(Color.ORANGE);
					cercleActuel.setStroke(Color.ORANGE);
				}
				if (valeursPH[i] == 2) {
					Circle cercleActuel = dessine.get(liv.getDestination().getId());
					cercleActuel.setFill(Color.PURPLE);
					cercleActuel.setStroke(Color.PURPLE);
				}
				if (valeursPH[i] == 3) {
					Circle cercleActuel = dessine.get(liv.getDestination().getId());
					cercleActuel.setFill(Color.RED);
					cercleActuel.setStroke(Color.RED);
				}
				if (valeursPH[i] == 4) {
					Circle cercleActuel = dessine.get(liv.getDestination().getId());
					cercleActuel.setFill(Color.BLUEVIOLET);
					cercleActuel.setStroke(Color.BLUEVIOLET);
				}
		
		}
	}
   
}
