package model;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Tournee {


        final static double VITESSE=(double)15/3.6; //TODO 15KM/h ATTENTION il faudra peut �tre changer cette foutue vitesse
		private Date heureDepart;
		private Date heureArrivee;
		private ArrayList <Chemin> itineraire;
		private ArrayList <Livraison> listeLivraison;
		
		public Tournee(ArrayList<Chemin> itineraire2, DemandeLivraison dl) {
		    this.heureDepart=dl.getHeureDepart();
			this.itineraire=itineraire2;
			long dureeTotale=0;
			this.setListeLivraison(dl.getLivraisons());
			
			for(int i=0;i<dl.getLivraisons().size();i++) {
				dureeTotale+=dl.getLivraisons().get(i).getDuree()*1000; // duree de livraison en seconde
			}
			
			System.out.println("Temps d'attente : "+dureeTotale);
			
			long dureeTrajets=0;
			for(int i=0; i<itineraire2.size();i++){
				for(int j=0;j<itineraire2.get(i).getTroncons().size();j++){
					dureeTrajets+= itineraire2.get(i).getTroncons().get(j).getLongueur()*1000/VITESSE;//Duree des trajets en seconde
				}
			}
			System.out.println("Temps de trajet : "+dureeTrajets);
			
			dureeTotale+=dureeTrajets;
			heureArrivee=new Date(heureDepart.getTime()+dureeTotale);
			Date duree = new Date(heureArrivee.getTime()-heureDepart.getTime()-3600000);// Soustraire 1 heure en millisecondes (?)
			
			SimpleDateFormat dureeHms = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat dateJhms = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			
		    System.out.println("Duree : "+dureeHms.format(duree)+"\nDepart : "+dateJhms.format(heureDepart)+"\nArrivee : "+dateJhms.format(heureArrivee));
			
			
	}
		public ArrayList <Chemin> getItineraire(){
			return itineraire;
		}
		public Date getHeureDepart() {
			return heureDepart;
		}
		private void setHeureDepart(Date heureDepart) {
			this.heureDepart = heureDepart;
		}
		public Date getHeureArrive() {
			return heureArrivee;
		}
		private void setHeureArrive(Date heureArrive) {
			this.heureArrivee = heureArrive;
		}
		private void setListeLivraison(ArrayList <Livraison> liste) {
			this.listeLivraison=liste;
		}
		public ArrayList <Livraison> getListeLivraison() {
			return listeLivraison;
		}
		
}
