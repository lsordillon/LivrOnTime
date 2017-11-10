package controleur;

/**
 * Interface de Commande permettant d implementer le design patter undo/redo
 * 
 * @author Matthieu
 *
 */
public interface Commande {

	void redoCde();

	void undoCde();

}
