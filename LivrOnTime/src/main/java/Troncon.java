

public class Troncon {
	Intersection destination;
	double longueur ;
	String nomRue;
	Intersection origine;
	
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

	
	

}
