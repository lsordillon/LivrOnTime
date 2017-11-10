package modele;

import java.util.ArrayList;

/**
 * La classe Intersection stocke les intersections du plan. Pour chacune on
 * memorise son id, ses coordonnees ainsi que la liste des troncons partant d
 * elle. Une fois les plus courts chemins calcules a l'aide de Dijkstra on
 * stocke egalement l intersection qui la precede dans le chemin et sa distance
 * par rapport a l origine du calcul.
 * 
 * @author Matthieu
 *
 */
public class Intersection {
	private long id;
	private int x;
	private int y;
	private ArrayList<Troncon> tronconsVersVoisins;
	private Intersection predecesseur;
	private double distance;

	/**
	 * Constructeur par defaut de la classe Intersection
	 */
	public Intersection() {

	}

	/**
	 * Constructeur de la classe Intersection, il cree l objet a partir d un id
	 * et de coordonnees recuperes dans un fichier xml
	 * 
	 * @param id
	 * @param x
	 * @param y
	 */
	public Intersection(long id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.tronconsVersVoisins = new ArrayList<Troncon>();
	}

	public Intersection getPredecesseur() {
		return predecesseur;
	}

	public void setPredecesseur(Intersection predecesseur) {
		this.predecesseur = predecesseur;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public ArrayList<Troncon> getTronconsVersVoisins() {
		return this.tronconsVersVoisins;
	}

	public ArrayList<Troncon> setTronconsVersVoisins(Troncon troncon) {
		tronconsVersVoisins.add(troncon);
		return this.tronconsVersVoisins;
	}

	public String toString() {
		String affichage = "[ID =" + id + ", NbTroncons" + tronconsVersVoisins.size() + "]";
		return affichage;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
