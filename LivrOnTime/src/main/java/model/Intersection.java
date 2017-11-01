package model;

import java.util.ArrayList;

public class Intersection {
	private long id;
	private int x;
	private int y;
	private ArrayList<Troncon> tronconsVersVoisins;
	private Intersection predecesseur;
	private double distance;
	

	public Intersection(long id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
		tronconsVersVoisins = new ArrayList<Troncon>();
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
		
		String affichage = "[ID ="+id+", NbTroncons"+tronconsVersVoisins.size()+"]";
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
