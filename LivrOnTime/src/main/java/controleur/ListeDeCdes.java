package controleur;

import java.util.LinkedList;

/**
 * La classe ListeDeCdes permet d implementer le design pattern undo/redo a
 * partir des classes de commandes
 * 
 * @author Matthieu
 *
 */
public class ListeDeCdes {

	private LinkedList<Commande> liste;
	private int indiceCourant;

	/**
	 * Constructeur de la classe ListeDeCdes
	 */
	public ListeDeCdes() {
		indiceCourant = -1;
		liste = new LinkedList<Commande>();
	}

	/**
	 * La methode ajoute permet d ajouter une commande a la liste de commandes.
	 * 
	 * @param commande
	 */
	public void ajoute(Commande commande) {
		int i = indiceCourant + 1;

		while (i < liste.size()) {
			liste.remove(i);
		}

		indiceCourant++;
		liste.add(indiceCourant, commande);
	}

	/**
	 * Implemente le design pattern undo
	 */
	public void undo() {
		if (indiceCourant >= 0) {
			Commande commande = liste.get(indiceCourant);
			indiceCourant--;
			commande.undoCde();
		}

	}

	/**
	 * Implemente le design pattern redo
	 */
	public void redo() {
		if (indiceCourant < liste.size() - 1) {
			indiceCourant++;
			Commande commande = liste.get(indiceCourant);
			commande.redoCde();
		}
	}

	/**
	 * La methode reset permet de remettre a zero la liste de commandes dans le
	 * cas ou on charge une nouvelle demande de livraisons.
	 */
	public void reset() {
		indiceCourant = -1;
		liste.clear();
	}

}
