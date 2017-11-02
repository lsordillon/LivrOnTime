package model;

import java.io.Serializable;

public class Troncon implements Serializable {
	private Intersection destination;
	private double longueur ;
	private String nomRue;
	private Intersection origine;
	
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
		return nomRue;
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
