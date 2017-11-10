package modele;

import java.io.Serializable;
import java.util.Date;

/**
 * La classe Livraison stocke toutes les informations
 * concernant une livraison a effectuer lors de la tournee :
 * l adresse, la duree, et eventuellement la plage horaire associee.
 * @author Matthieu
 *
 */
public class Livraison {
	 
	
	private int duree; // en secondes 
	private Date debutPlageHoraire;
	private Date finPlageHoraire;
	private Intersection destination;
	
	/**
	 * Constructeur par defaut de la classe Livraison
	 */
	public Livraison(){
		
	}
	
	/**
	 * Constructeur de la classe Livraison, il cree l objet a partir
	 * d une adresse et d une duree.
	 * @param duree
	 * @param destination
	 */
	public Livraison (int duree, Intersection destination ) {
		this.duree = duree;	
		this.debutPlageHoraire = null;
		this.finPlageHoraire = null;
		this.destination = destination;
	}
	
	/**
	 * Constructeur de la classe Livraison, il cree l objet a partir
	 * d une adresse, d une duree et d une plage horaire.
	 * @param duree
	 * @param destination
	 * @param debut
	 * @param fin
	 */
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
		if(finPlageHoraire!=null)
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
		if(debutPlageHoraire!=null)
		this.debutPlageHoraire = debutPlageHoraire;
	}
	
	public int getDuree() {
		return duree;
	}

	public Date getDebutPlageHoraire() {
		return this.debutPlageHoraire;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof Livraison){
			return ((Livraison) obj).toString().equals(this.toString()) ;
		}else{
			return false ;
		}
		
		
	}
	
	
}
