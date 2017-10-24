package model;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Tournee {

        final static double VITESSE=(double)15/3600; //15KM/h
		private Date heureDepart;
		private Date heureArrive;
		private ArrayList <Chemin> itineraire;
		
		public Tournee(ArrayList<Chemin> itineraire2, DemandeLivraison dl) {
		    this.heureDepart=dl.getHeureDepart();
			this.itineraire=itineraire2;
			double dureeTotal=0;
			
			for(int i=0;i<dl.getLivraisons().size();i++)
				dureeTotal+=dl.getLivraisons().get(i).getDuree(); // duree de livraison en seconde
			dureeTotal*=1000; //la conversion en milliseconde
			
			for(int i=0; i<itineraire2.size();i++){
				for(int j=0;j<itineraire2.get(i).getTroncons().size();j++){
					dureeTotal+=(double) itineraire2.get(i).getTroncons().get(j).getLongueur()/VITESSE;
				}
			}
			heureArrive=new Date(heureDepart.getTime()+(long)dureeTotal);
			Date duree = new Date(heureArrive.getTime()-heureDepart.getTime()-3600000);// Soustraire 1 heure en millisecondes (?)
			
			SimpleDateFormat dureeHms = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat dateJhms = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			
		    System.out.println("Durée : "+dureeHms.format(duree)+"\nDépart : "+dateJhms.format(heureDepart)+"\nArrivée : "+dateJhms.format(heureArrive));
			
			
	}
		public ArrayList <Chemin> getItineraire(){
			return itineraire;
		}
		/*private Date getHeureDepart() {
			return heureDepart;
		}
		private void setHeureDepart(Date heureDepart) {
			this.heureDepart = heureDepart;
		}
		private Date getHeureArrive() {
			return heureArrive;
		}
		private void setHeureArrive(Date heureArrive) {
			this.heureArrive = heureArrive;
		}*/
		
}
