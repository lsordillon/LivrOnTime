package modele;


/**
 * La classe Troncon permet de stocker les informations
 * sur la rue reliant 2 intersections adjacentes sur le graphe.
 * Elle memorise l origine et la destination de la rue ainsi que
 * la longueur et le nom de la rue.
 * @author Matthieu
 *
 */
public class Troncon {
	
	private Intersection destination;
	private double longueur ;
	private String nomRue;
	private Intersection origine;
	
	/**
	 * Constructeur de la classe Troncon, il cree l objet a partir
	 * d une destination, d une longueur, d'un nom et d une origine.
	 * @param destination
	 * @param longueur
	 * @param nomRue
	 * @param origine
	 */
	public Troncon(Intersection destination, double longueur, String nomRue, Intersection origine) {
		super();
		this.destination = destination;
		this.longueur = longueur;
		this.nomRue = nomRue;
		this.origine = origine;
	}

	@Override
	public String toString() {
		return "Troncon [destination=" + destination + ", longueur=" + longueur + ", nomRue=" + nomRue + ", origine="
				+ origine + "]";
	}

	public Intersection getDestination() {
		return destination;
	}

	public void setDestination(Intersection destination) {
		this.destination = destination;
	}

	public double getLongueur() {
		return longueur;
	}

	public void setLongueur(double longueur) {
		this.longueur = longueur;
	}

	public String getNomRue() {
		if(nomRue.equals("")){
			return "Rue sans nom";
		}
		
		else{
			return nomRue;
		}
	}

	public void setNomRue(String nomRue) {
		this.nomRue = nomRue;
	}

	public Intersection getOrigine() {
		return origine;
	}

	public void setOrigine(Intersection origine) {
		this.origine = origine;
	}
}
