package model;
import java.util.ArrayList;

public class Tournee {


		private DemandeLivraison demande;
		private ArrayList <Chemin> itineraire;
		
		public Tournee(ArrayList<Chemin> itineraire2) {
		itineraire=itineraire2;
	}
}
