package model;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Tournee {


        final static double VITESSE=(double)15/3.6; //TODO 15KM/h ATTENTION il faudra peut etre changer cette foutue vitesse
		private Date heureDepart;
		private Date heureArrivee;
		private ArrayList <Chemin> itineraire;
		private ArrayList <Livraison> listeLivraisons;
		private Date[][] tempsPassage;
		
		public Tournee(ArrayList<Chemin> itineraire2, DemandeLivraison dl) {
		    this.heureDepart=dl.getHeureDepart();
			this.itineraire=itineraire2;
			long dureeTotale=0;
			
			listeLivraisons = new ArrayList<Livraison>();
			for (Chemin chemin : itineraire2){
		    	for(Livraison l : dl.getLivraisons()){
		    		if(l.getDestination().getId() == chemin.getDestination().getId()){
		    			listeLivraisons.add(l);
		    		}
		    	}
		    }
			
			tempsPassage = new Date[itineraire2.size()][3];
			
			double dureeTrajets=0;
			for(int i=0;i<itineraire2.size();i++){
				for(int j=0;j<itineraire2.get(i).getTroncons().size();j++){
					dureeTrajets+= itineraire2.get(i).getTroncons().get(j).getLongueur()*1000/VITESSE;//Duree des trajets en seconde
				}
				
				if (i<listeLivraisons.size())
					dureeTotale+=listeLivraisons.get(i).getDuree()*1000; // duree de livraison en ms
			}
			System.out.println("Temps de trajet : "+dureeTrajets);
			System.out.println("Temps d'attente : "+dureeTotale);
			
			dureeTotale+=dureeTrajets;
			heureArrivee=new Date(heureDepart.getTime()+dureeTotale);
			Date duree = new Date(dureeTotale- 3600000);// Soustraire 1 heure en millisecondes (probl�me avec la date absolue par rapport � une dur�e brute en ms)
			
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
		this.listeLivraisons=liste;
	}
	
	public ArrayList <Livraison> getListeLivraison() {
		return listeLivraisons;
	}
		
}
