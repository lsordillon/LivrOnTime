package vue;

import controleur.AccueilControleur;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class SceneGestures {

    private static final double MAX_SCALE = 10.0d;
    private static final double MIN_SCALE = .1d;

    private DragContext sceneDragContext = new DragContext();

    private PannableCanvas canvas;
    private AccueilControleur controleur;

    public SceneGestures(AccueilControleur controleur) {
        this.controleur = controleur;
    }
    
    public void rendreCanvasZoomable(AccueilControleur controleur) {
    	if(canvas !=null) {
    		canvas.addEventFilter( MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        	canvas.addEventFilter( MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            canvas.addEventFilter( MouseEvent.MOUSE_ENTERED, onMouseEnteredEventHandler);
            canvas.addEventFilter( MouseEvent.MOUSE_EXITED, onMouseExitedEventHandler);
    	}
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
        }
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {

            // right mouse button => panning
            if( event.isSecondaryButtonDown()) {
	            canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
	            canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);
	
	            event.consume();
            }
        }
    };
    
    private EventHandler<MouseEvent> onMouseEnteredEventHandler = new EventHandler<MouseEvent>() {
    	public void handle(MouseEvent event) {
    		canvas.addEventFilter( ScrollEvent.ANY, onScrollEventHandler);	
    	}
    };
    
    private EventHandler<MouseEvent> onMouseExitedEventHandler = new EventHandler<MouseEvent>() {
    	public void handle(MouseEvent event) {
    		canvas.addEventFilter( ScrollEvent.ANY, new EventHandler<ScrollEvent>(){
    			public void handle(ScrollEvent event){
    				
    			}
    		});	
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

	public PannableCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(PannableCanvas canvas) {
		this.canvas = canvas;
	}
}
