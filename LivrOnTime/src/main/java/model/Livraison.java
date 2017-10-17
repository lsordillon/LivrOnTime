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
		
	public Livraison (int duree, Date debut, Date fin, Intersection destination ) {
		this.duree = duree;	
		this.debutPlageHoraire = debut;
		this.finPlageHoraire = fin;
		this.destination = destination;
	}
	
}
