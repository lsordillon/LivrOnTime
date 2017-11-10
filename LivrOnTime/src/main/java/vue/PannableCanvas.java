package vue;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;

/**
 * La classe PannableCanvas gere la creation du 
 * canvas sur lequel le plan est dessine.
 * @author Matthieu
 *
 */
public class PannableCanvas extends Pane {

    DoubleProperty echelle = new SimpleDoubleProperty(1.0);

    /**
     * Constructeur de la classe PannableCanvas.
     */
    public PannableCanvas() {
        setPrefSize(600, 600);
     
        // add scale transform
        scaleXProperty().bind(echelle);
        scaleYProperty().bind(echelle);
        
    }

    public double getScale() {
        return echelle.get();
    }

    public void setScale( double scale) {
    	echelle.set(scale);
    }

    public void setPivot( double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }
}
