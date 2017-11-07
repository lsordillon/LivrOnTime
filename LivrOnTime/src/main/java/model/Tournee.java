package model;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import util.tsp.Dijkstra;

public class Tournee {


        final static double VITESSE=(double)15/3.6; // 15KM/h 
		private Date heureDepart;
		private Date heureArrivee;
		private ArrayList <Chemin> itineraire;
		private ArrayList <Livraison> listeLivraisons;
		
		/**
		 * Ce tableau correspond aux temps de passage estimes de la livraison 
		 * aux differents points de livraison. Plus d'explication avec la 
		 * fonction {@link Tournee#getTempsPassage}
		 */
		private Date[][] tempsPassage;
		
		public Tournee(ArrayList<Chemin> itineraire2, DemandeLivraison dl) {
		    this.heureDepart=dl.getHeureDepart();
			this.itineraire=itineraire2;
			
			
			listeLivraisons = new ArrayList<Livraison>();
			for (Chemin chemin : itineraire2){
		    	for(Livraison l : dl.getLivraisons()){
		    		if(l.getDestination().getId() == chemin.getDestination().getId()){
		    			listeLivraisons.add(l);
		    		}
		    	}
		    }
			
			
			
	}
		
	public void initTempsPassage() {
		long dureeTotale=heureDepart.getTime();
		tempsPassage = new Date[itineraire.size()][2];
		
		for(int i=0;i<itineraire.size();i++){
			for(int j=0;j<itineraire.get(i).getTroncons().size();j++){
				dureeTotale+= itineraire.get(i).getTroncons().get(j).getLongueur()*1000/VITESSE;//Duree des trajets en seconde
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
		Date duree = new Date(dureeTotale- heureDepart.getTime()-3600000);// Soustraire 1 heure en millisecondes (probleme avec la date absolue par rapport a une duree brute en ms)
		
		SimpleDateFormat dureeHms = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dateJhms = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		
	    System.out.println("Duree : "+dureeHms.format(duree)+"\nDepart : "+dateJhms.format(heureDepart)+"\nArrivee : "+dateJhms.format(heureArrivee));
	    
	    for (Date[] tmp:tempsPassage) {
	    	System.out.print("Trajet : heure d'arrivee = "+dateJhms.format(tmp[0]));
	    	System.out.println((tmp[1]==null?"":("     Temps d'attente = "+dureeHms.format(new Date(tmp[1].getTime()-tmp[0].getTime()-3600000))+ " min, livraison a "+dureeHms.format(tmp[1]))));
	    	
	    }
	}
		
	/**
	 * <p>Getter de l'attribut tempsPassage qui recense les temps de passage estimes aux differents points de livraison.
	 * Il s'agit d'un tableau 2D contenant :</p>
	 * <ul>
	 * <li> en premiere colonne (tempsPassage[i][0]) la date d'arrivee sur le point de livraison d'index i</li>
	 * <li> en deuxieme colonne (tempsPassage[i][1]) la date de debut de livraison</li>
	 * </ul>
	 * <p>Toutes les dates du tableau sont au format normal. On peut les traiter avec la classe {@link SimpleDateFormat}
	 * qui permet de formater facilement l'affichage.</p>
	 * <p>Pour recuperer le temps d'attente a partir de ce tableau, il suffit de faire la soustraction entre :
	 * la date de livraison et la date d'arrivee sur le point de livraison :</p>
	 * <p>tempsPassage[i][1].getTime()-tempsPassage[i][0].getTime()</p>
	 * 
	 * <p>ATTENTION : La duree obtenue doit etre transformee pour etre utilisee par la classe SimpleDateFormat
	 * en lui soustrayant 1 heure en millisecondes (3 600 000).
	 * Vous pouvez retrouver un exemple simple a la fin du constructeur de
	 *  {@link Tournee#Tournee(ArrayList, DemandeLivraison)}.
	 *  
	 * @return le tableau 2D avec les temps de passage
	 * @see Tournee#tempsPassage
	 */
	public Date[][] getTempsPassage(){
		return tempsPassage;
	}
		
	public ArrayList <Chemin> getItineraire(){
		return itineraire;
	}
	
	public void setItineraire(ArrayList<Chemin> itineraire) {
		this.itineraire = itineraire;
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
	
	public int SupprimerLivraison(Plan plan,Intersection inter,Livraison l){
		int index=-1;
		Intersection origine=null, destination=null;
		if(getListeLivraison().contains(l)){
			
			ArrayList<Chemin> nouvelItineraire=getItineraire();
			for(int i=0;i<getItineraire().size();i++){
				Chemin chemin=getItineraire().get(i);
					if(chemin.getDestination()==inter){
						origine=chemin.getOrigine();
						nouvelItineraire.remove(chemin);
						//indice=i;
			     	}
					if(chemin.getOrigine()==inter){
						destination=chemin.getDestination();
						nouvelItineraire.remove(chemin);
					}
			}
			System.out.println(origine +" Origine   dist"+destination);
			if(origine!=null && destination !=null){
				
				Chemin nouveau_chemin=plan.trouverChemin(origine,destination);
				nouvelItineraire.add( nouveau_chemin);
				this.itineraire = nouvelItineraire;
			}

			getListeLivraison().remove(l);

			index=listeLivraisons.indexOf(l);
			listeLivraisons.remove(l);
			this.initTempsPassage();
			
			
		}
		else {
			System.err.println("ERREUR ! La livraison ne fait pas partie de la tournee actuelle");
		}
		this.initTempsPassage();
		System.out.println("resultat fin"+ getItineraire());
		return index;
	}
	
	
	
	public boolean AjouterLivraison(Plan plan,Intersection inter,Livraison l, int index){
		Dijkstra d = new Dijkstra();
		

		if(!getListeLivraison().contains(l)){
			ArrayList<Chemin> nouvelItineraire=getItineraire();
			Chemin dernierChemin=nouvelItineraire.get(nouvelItineraire.size()-1);
			nouvelItineraire.remove(nouvelItineraire.size()-1);
		    
		    Chemin nouveauChemin=new Chemin();
		    d.algoDijkstra(plan, dernierChemin.getOrigine());
			nouveauChemin.setOrigine(dernierChemin.getOrigine());
			nouveauChemin.setDestination(inter);
            Intersection courante = inter;
			ArrayList<Troncon> troncons=new ArrayList<Troncon>();
			
			while (courante!=dernierChemin.getOrigine()) {
				Troncon troncon=plan.trouverTroncon(courante.getPredecesseur(),courante);
				
				if(troncon!=null) {
					troncons.add(0, troncon);
				}
				courante=courante.getPredecesseur();
			}
			nouveauChemin.setTroncons(troncons);
			nouvelItineraire.add(nouveauChemin);
		    
		    nouveauChemin=new Chemin();
		    d.algoDijkstra(plan, inter);
			nouveauChemin.setOrigine(inter);
			nouveauChemin.setDestination(dernierChemin.getDestination());
		
			courante = dernierChemin.getDestination();
		    troncons=new ArrayList<Troncon>();
			
		    while (courante!=inter) {
				Troncon troncon=plan.trouverTroncon(courante.getPredecesseur(),courante);
				
				if(troncon!=null) {
					troncons.add(0, troncon);
				}
				courante=courante.getPredecesseur();
			}
			nouveauChemin.setTroncons(troncons);
			
			nouvelItineraire.add(nouveauChemin);
		    setItineraire(nouvelItineraire);
		    getListeLivraison().add(index, l);
		    /*index=listeLivraisons.indexOf(l);
			listeLivraisons.add(index, l);
			this.initTempsPassage();*/
		}else {
			System.err.println("ERREUR ! La livraison fait partie de la tournee actuelle");
			return false;
		}
		this.initTempsPassage();
		System.out.println("resultat fin"+ getItineraire());
	return true;

		
		/*Chemin rChemin=itineraire.get(index);
	    itineraire.remove(index);
	    

	    d.algoDijkstra(plan, rChemin.getOrigine());
	    Chemin begChemin=plan.creerChemin(rChemin.getOrigine(),inter);
	    itineraire.add(index,begChemin);
	    
	    d.algoDijkstra(plan, inter);
	    Chemin endChemin=plan.creerChemin(inter,rChemin.getDestination());
	    itineraire.add(index+1,endChemin);
	    
	    
	    listeLivraisons.add(index,l);
	    
	    return true;*/


	}

		
	public boolean ModifierLivraison(Plan plan,Livraison liv,Intersection inter){
		Intersection origine=new Intersection(); 
		Intersection distination=new Intersection();
		
		//si cette livraison n'appartient pas d�ja a DL
		if(!this.getListeLivraison().contains(inter)){
			ArrayList<Chemin> nouvelItineraire=getItineraire();
			Dijkstra d=new Dijkstra();
			
			int index=0;
		
			for(int i=0;i<nouvelItineraire.size();i++){
				if(nouvelItineraire.get(i).getDestination()==liv.getDestination()){
					origine=nouvelItineraire.get(i).getOrigine();
					nouvelItineraire.remove(nouvelItineraire.get(i));;
					index=i;
				}
				if(nouvelItineraire.get(i).getOrigine()==liv.getDestination()){
					origine=nouvelItineraire.get(i).getDestination();
					nouvelItineraire.remove(nouvelItineraire.get(i));
				}
				
			}
			
			
			
			Chemin nouveauChemin=new Chemin();
		    d.algoDijkstra(plan, origine);
			nouveauChemin.setOrigine(origine);
			nouveauChemin.setDestination(inter);
		
			Intersection courante = inter;
			ArrayList<Troncon> troncons=new ArrayList<Troncon>();
			
			while (courante!=origine) {
				Troncon troncon=plan.trouverTroncon(courante.getPredecesseur(),courante);
				
				if(troncon!=null) {
					troncons.add(0, troncon);
				}
				else{
					System.err.println("un point inaccessible !");
				}
				courante=courante.getPredecesseur();
			}
			
			nouveauChemin.setTroncons(troncons);		
		    nouvelItineraire.add(index, nouveauChemin);
		    
		    nouveauChemin=new Chemin();
		    d.algoDijkstra(plan, inter);
			nouveauChemin.setOrigine(inter);
			nouveauChemin.setDestination(distination);
		
			courante =distination;
		    troncons=new ArrayList<Troncon>();
			
		    while (courante!=inter) {
				Troncon troncon=plan.trouverTroncon(courante.getPredecesseur(),courante);
				if(troncon!=null) {
					troncons.add(0, troncon);
				}
				else{
					System.err.println("un point inaccessible !");
				}
				courante=courante.getPredecesseur();
			}
			nouveauChemin.setTroncons(troncons);
			
			nouvelItineraire.add(++index,nouveauChemin);
			
			 setItineraire(nouvelItineraire);
			 liv.setDestination(inter);
		}else {
			System.err.println("ERREUR ! La livraison ne fait pas partie de la tournee actuelle");
			return false;
		}
		this.initTempsPassage();
		System.out.println("resultat fin"+ getItineraire());
		return true;
	}
	
	
	// modifier seulement la duree de livraison d'un pdl
	public boolean ModifierLivraison(Plan plan, Livraison liv, int duree){
		
		if(this.getListeLivraison().contains(liv)){
			int i=this.getListeLivraison().indexOf(liv);
			liv.setDuree(duree);
			this.getListeLivraison().set(i, liv);
		}
		else {
			System.err.println("ERREUR ! La livraison ne fait pas partie de la tournee actuelle");
			return false;
		}	
		this.initTempsPassage();
		return true;
	}
	
	
	// modifier seulement la plage horaire de la livraison
	// TODO : Permettre de la supprimer 
	public boolean ModifierLivraison(Plan plan, Livraison liv, Date DPH, Date FPH){
		
		if(this.getListeLivraison().contains(liv)){
			int i=this.getListeLivraison().indexOf(liv);
			
			if(DPH!= null) {
				liv.setDebutPlageHoraire(DPH);
			}
			
			if(FPH!=null) {
				liv.setFinPlageHoraire(FPH);
			}
			
			this.getListeLivraison().set(i, liv);
		}
		else {
			System.err.println("ERREUR ! La livraison ne fait pas partie de la tournee actuelle");
			return false;
		}	
		this.initTempsPassage();
		return true;
	}
	
	// verifier une fois modifiees les plages horaires violees
	public boolean VerifierPlagesHoraires() {				
		return true;
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
