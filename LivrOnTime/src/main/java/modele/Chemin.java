package modele;

import java.util.Date;

import modele.Intersection;

import java.util.ArrayList;

/**
 * La classe Chemin stocke le trajet d un point a un autre du plan grace a une
 * liste de troncons. Elle stocke aussi les heures de depart et d arrivee.
 * 
 * @author Matthieu
 *
 */
public class Chemin {

	private Date heureArrivee;
	private Intersection origine;
	private Intersection destination;
	private ArrayList<Troncon> troncons;
	private Date heureDepart;

	/**
	 * Constructeur de la classe Chemin, il cree l objet a partir d une
	 * intersection de depart, d une intersection d arrivee et d un plan.
	 * 
	 * @param depart
	 * @param courante
	 * @param plan
	 */
	public Chemin(Intersection depart, Intersection courante, Plan plan) {
		this.setOrigine(depart);
		this.setDestination(courante);

		// Grace au predecesseur des intersections, je cree une liste de troncon
		// qui constituent le chemin total
		ArrayList<Troncon> troncons = new ArrayList<Troncon>();

		while (courante != depart) {
			Troncon troncon = plan.trouverTroncon(courante.getPredecesseur(), courante);
			if (troncon != null) {
				troncons.add(0, troncon);
			}
			courante = courante.getPredecesseur();
		}
		this.setTroncons(troncons);
	}
	public Chemin(){
		
	}

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
	public boolean equals(Object obj) {

	       if(obj instanceof Chemin )
			return (this.getOrigine().equals(((Chemin) obj).getOrigine()) && this.getDestination().equals(((Chemin)obj).getDestination()));
		return false;
		}
}
