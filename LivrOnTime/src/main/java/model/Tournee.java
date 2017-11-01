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
		/**
		 * Ce tableau correspond aux temps de passage estimés de la livraison aux différents points de livraison. Plus d'explication avec la fonction {@link Tournee#getTempsPassage}
		 */
		private Date[][] tempsPassage;
		
		public Tournee(ArrayList<Chemin> itineraire2, DemandeLivraison dl) {
		    this.heureDepart=dl.getHeureDepart();
			this.itineraire=itineraire2;
			long dureeTotale=heureDepart.getTime();
			
			listeLivraisons = new ArrayList<Livraison>();
			for (Chemin chemin : itineraire2){
		    	for(Livraison l : dl.getLivraisons()){
		    		if(l.getDestination().getId() == chemin.getDestination().getId()){
		    			listeLivraisons.add(l);
		    		}
		    	}
		    }
			
			tempsPassage = new Date[itineraire2.size()][2];
			
			for(int i=0;i<itineraire2.size();i++){
				for(int j=0;j<itineraire2.get(i).getTroncons().size();j++){
					dureeTotale+= itineraire2.get(i).getTroncons().get(j).getLongueur()*1000/VITESSE;//Duree des trajets en seconde
				}
				if (i<listeLivraisons.size()) {
					if (listeLivraisons.get(i).getDebutPlageHoraire()!=null && listeLivraisons.get(i).getDebutPlageHoraire().getTime()>dureeTotale) {
						tempsPassage[i][0] = new Date(dureeTotale);
						tempsPassage[i][1] = new Date(listeLivraisons.get(i).getDebutPlageHoraire().getTime());
						dureeTotale = listeLivraisons.get(i).getDebutPlageHoraire().getTime();
					}
					else {
						tempsPassage[i][0] = new Date(dureeTotale);
					}
					dureeTotale+=listeLivraisons.get(i).getDuree()*1000; // duree de livraison en ms
				}
				else {
					tempsPassage[i][0]=new Date(dureeTotale);
				}
			}
			
			
			heureArrivee=new Date(dureeTotale);
			Date duree = new Date(dureeTotale- heureDepart.getTime()-3600000);// Soustraire 1 heure en millisecondes (problème avec la date absolue par rapport à une durée brute en ms)
			
			SimpleDateFormat dureeHms = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat dateJhms = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			
		    System.out.println("Duree : "+dureeHms.format(duree)+"\nDepart : "+dateJhms.format(heureDepart)+"\nArrivee : "+dateJhms.format(heureArrivee));
		    
		    for (Date[] tmp:tempsPassage) {
		    	System.out.print("Trajet : heure d'arrivée = "+dateJhms.format(tmp[0]));
		    	System.out.println((tmp[1]==null?"":("     Temps d'attente = "+dureeHms.format(new Date(tmp[1].getTime()-tmp[0].getTime()-3600000))+ " min, livraison à "+dureeHms.format(tmp[1]))));
		    	
		    }
			
			
	}
		
	/**
	 * <p>Getter de l'attribut tempsPassage qui recense les temps de passage estimés aux différents points de livraison. Il s'agit d'un tableau 2D contenant :</p>
	 * <ul>
	 * <li> en première colonne (tempsPassage[i][0]) la date d'arrivée sur le point de livraison d'index i</li>
	 * <li> en deuxième colonne (tempsPassage[i][1]) la date de début de livraison</li>
	 * </ul>
	 * <p>Toutes les dates du tableau sont au format normal. On peut les traiter avec la classe {@link SimpleDateFormat} qui permet de formater facilement l'affichage.</p>
	 * <p>Pour récupérer le temps d'attente à partir de ce tableau, il suffit de faire la soustraction entre la date de livraison et la date d'arrivée sur le point de livraison :</p>
	 * <p>tempsPassage[i][1].getTime()-tempsPassage[i][0].getTime()</p>
	 * <p>ATTENTION : La durée obtenue doit être transformée pour être utilisée par la classe SimpleDateFormat (pas d'explication) en lui soustrayant 1 heure en millisecondes (3 600 000).
	 * Vous pouvez retrouver un exemple simple à la fin du constructeur de {@link Tournee#Tournee(ArrayList, DemandeLivraison)}.
	 * @return le tableau 2D avec les temps de passage
	 * @see Tournee#tempsPassage
	 */
	public Date[][] getTempsPassage(){
		return tempsPassage;
	}
		
	public ArrayList <Chemin> getItineraire(){
		return itineraire;
	}
	
	public Date getHeureDepart() {
		return heureDepart;
	}
	
	public Date getHeureArrive() {
		return heureArrivee;
	}
	
	public ArrayList <Livraison> getListeLivraison() {
		return listeLivraisons;
	}
	
	
	//Unused setters (yet)
	/*private void setHeureArrive(Date heureArrive) {
		this.heureArrivee = heureArrive;
	}
	
	private void setListeLivraison(ArrayList <Livraison> liste) {
		this.listeLivraisons=liste;
	}
	private void setHeureDepart(Date heureDepart) {
		this.heureDepart = heureDepart;
	}*/
	
		
}
