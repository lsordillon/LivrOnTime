package model;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Date;


public class Livraison implements Serializable {
	 
	
	private int duree; // en secondes 
	private Date debutPlageHoraire;
	private Date finPlageHoraire;
	private Intersection destination;
	
	public int getDuree() {
		return duree;
	}

	
	public Date getDebutPlageHoraire() {
		return this.debutPlageHoraire;
	}
	public Livraison(){
		
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

	public Date getFinPlageHoraire() {
		return finPlageHoraire;
	}

	public void setFinPlageHoraire(Date finPlageHoraire) {
		this.finPlageHoraire = finPlageHoraire;
	}

	public Intersection getDestination() {
		return destination;
	}

	public void setDestination(Intersection destination) {
		this.destination = destination;
	}

	public void setDuree(int duree) {
		this.duree = duree;
	}

	public void setDebutPlageHoraire(Date debutPlageHoraire) {
		this.debutPlageHoraire = debutPlageHoraire;
	}

	
}
