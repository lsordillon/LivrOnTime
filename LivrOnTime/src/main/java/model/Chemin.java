package model;

import java.util.Date;
import java.util.ArrayList;

public class Chemin {

	private Date heureArrivee;
	private Intersection origine;
	private Intersection destination;
	private ArrayList<Troncon> troncons;
	private Date heureDepart;

	public Date getHeureDepart() {
		return heureDepart;
	}

	public void setHeureDepart(Date heureDepart) {
		this.heureDepart = heureDepart;
	}

	public Date getHeureArrivee() {
		return heureArrivee;
	}

	public void setHeureArrivee(Date heureArrivee) {
		this.heureArrivee = heureArrivee;
	}

	public Intersection getOrigine() {
		return origine;
	}

	public void setOrigine(Intersection origine) {
		this.origine = origine;
	}

	public Intersection getDestination() {
		return destination;
	}

	public void setDestination(Intersection destination) {
		this.destination = destination;
	}

	public ArrayList<Troncon> getTroncons() {
		return troncons;
	}

	public void setTroncons(ArrayList<Troncon> troncons) {
		this.troncons = troncons;
	}

	@Override
	public String toString() {
		return "Chemin [heureArrivee=" + heureArrivee + ", origine=" + origine + ", destination=" + destination
				+ ", troncons=" + troncons + ", heureDepart=" + heureDepart + "]";
	}
	
	

}
