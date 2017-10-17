package model;
import java.util.Date;


public class Livraison {
	 
	private int duree;
	private Date debutPlageHoraire;
	private Date finPlageHoraire;
	private Intersection destination;
	
	public int getDuree() {
		return duree;
	}
	
	public Date getDebutPlageHoraire() {
		return this.debutPlageHoraire;
	}
		
	public Livraison (int duree, Intersection destination ) {
		this.duree = duree;	
		this.debutPlageHoraire = null;
		this.finPlageHoraire = null;
		this.destination = destination;
	}
	
	public Livraison (int duree, Intersection destination, Date debut, Date fin ) {
		this.duree = duree;	
		this.debutPlageHoraire = debut;
		this.finPlageHoraire = fin;
		this.destination = destination;
	}
	
	
	public String toString() {
		return "Livraison [destination=" + destination + ", duree=" + duree + ", debut=" + debutPlageHoraire + ", fin="
				+ finPlageHoraire + "]";
	}
	
}
