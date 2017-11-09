package model;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.util.Pair;
import util.tsp.Dijkstra;

public class Tournee {


    static final double VITESSE=(double)15/3.6; // 15KM/h 
	private Date heureDepart;
	private Date heureArrivee;
	private ArrayList <Chemin> itineraire;
	private ArrayList <Livraison> listeLivraisons;
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
		tempsPassage = new Date[getItineraire().size()][2];
		
		for(int i=0;i<getItineraire().size();i++){
			System.out.println("itineraire  "+getItineraire().get(i));
			for(int j=0;j<getItineraire().get(i).getTroncons().size();j++){
				dureeTotale+= getItineraire().get(i).getTroncons().get(j).getLongueur()*1000/VITESSE;//Duree des trajets en seconde
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
	}
		
	
	public Pair <Integer, Tournee> SupprimerLivraison(Plan plan,Intersection inter,Livraison l){
		Dijkstra d = new Dijkstra();
		int index=-1;
		int indiceListeLivraison = 0;
		Intersection origine=null;
		Intersection destination=null;
		if(getListeLivraison().contains(l)){
			
			ArrayList<Chemin> nouvelItineraire=new ArrayList<Chemin>(itineraire);
			for(int i=0;i<getItineraire().size();i++){
				Chemin chemin=getItineraire().get(i);
				System.out.println("Intersection" + inter.getId());
				System.out.println("Chemin" + chemin.getDestination().getId());
					if(chemin.getDestination().getId()==inter.getId()){
						origine=chemin.getOrigine();
						nouvelItineraire.remove(chemin);
						indiceListeLivraison = i;
			     	}
					if(chemin.getOrigine().getId()==inter.getId()){
						destination=chemin.getDestination();
						nouvelItineraire.remove(chemin);
					}
			}
			
			if(origine!=null && destination !=null){
				//Chemin nouveau_chemin=plan.trouverChemin(origine,destination);
				d.algoDijkstra(plan, origine);
			    Chemin nouveau_chemin = plan.creerChemin(origine, destination);
				nouvelItineraire.add(indiceListeLivraison, nouveau_chemin );
			}
			this.itineraire = nouvelItineraire;
			index=listeLivraisons.indexOf(l);
			listeLivraisons.remove(l);

			initTempsPassage();
		}
		
		else {
			System.err.println("ERREUR ! La livraison ne fait pas partie de la tournee actuelle");
		}
		
		
		Pair <Integer, Tournee> paire = new Pair<Integer, Tournee>(index,this);
		return paire;
	}
	
	
	public Tournee AjouterLivraison(Plan plan,Intersection inter,Livraison l, int index){
		Dijkstra d = new Dijkstra();

		if(!getListeLivraison().contains(l)){
			ArrayList<Chemin> nouvelItineraire=new ArrayList<Chemin>(itineraire);
			Chemin cheminASupprimer = nouvelItineraire.get(index);
			nouvelItineraire.remove(index);
			
			//On cree le premier chemin
		    d.algoDijkstra(plan, cheminASupprimer.getOrigine());
		    Chemin nouveauChemin1 = plan.creerChemin(cheminASupprimer.getOrigine(), inter);
			
			//On ajoute le chemin � la fin ------> On ajoute le chemin � la bonne place dans la liste
			nouvelItineraire.add(index,nouveauChemin1);

			// Idem pour le deuxi�me chemin
		    d.algoDijkstra(plan, inter);
		    Chemin nouveauChemin2 = plan.creerChemin(inter, cheminASupprimer.getDestination());
			nouvelItineraire.add(index+1,nouveauChemin2);
		    setItineraire(nouvelItineraire);
		    listeLivraisons.add(index, l);
		    
		}else {
			System.err.println("ERREUR ! La livraison fait partie de la tournee actuelle");
			return null;
		}
		initTempsPassage();
		return this;
	}	
	
	// modifier seulement la duree de livraison d'un pdl
	public boolean ModifierLivraison(Plan plan, Livraison liv, int duree){
		
		if(this.getListeLivraison().contains(liv)){
			int i=this.getListeLivraison().indexOf(liv);
			liv.setDuree(duree);
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
	
	
	// verifier si l'heure d'arrive a une livrasion correspond aux plages horaires 
	public int VerifierPlagesHorairesUneLiv(Livraison liv) {
		// juste checker si l'horaire d'arrivee fait partie de la ph
		// valeur 0 : pas d'attente et pas tendu --> bleu
		// valeur 1 : pas d'attente et tendu --> orange
		// valeur 2 : attente --> PURPLE
		// valeur 3 : plage horaire violee --> rouge

		int valeurPH = 0; 

		for(Livraison l : getListeLivraison()){
			if(l.toString().equals(liv.toString())){
				liv = l;
			}
		}

		Date[] tempsPassage=getTempsPassage()[getListeLivraison().indexOf(liv)];

		boolean attente = true;
		Date horaireArr = tempsPassage[0];

		if (liv.getDebutPlageHoraire() != null && liv.getFinPlageHoraire() != null ) {
			Date debutPH = liv.getDebutPlageHoraire();
			Date finPH = liv.getFinPlageHoraire(); 
			Date tempsRestantAvantFinPHdate = new Date(finPH.getTime() - horaireArr.getTime());
			long tempsRestantAvantFinPH = tempsRestantAvantFinPHdate.getTime();

			//arrive apres DPH et avant FPH donc n'attend pas 
			if (horaireArr.getTime() >= debutPH.getTime() && horaireArr.getTime() < finPH.getTime()) {
				attente = false; 
			}

			if (attente == false && tempsRestantAvantFinPH > 30*60000 + liv.getDuree()*1000 ) { // pas d'attente et pas tendu 
				valeurPH = 0;
			}

			if (attente == false && tempsRestantAvantFinPH <= 30*60000 + liv.getDuree()*1000) { // pas d'attente et tendu 
				valeurPH = 1;
			}

			if (attente == true && tempsRestantAvantFinPH >= liv.getDuree()*1000) { // attente
				valeurPH = 2;
			}

			if (tempsRestantAvantFinPH < liv.getDuree()*1000 ) {// plage violee
				valeurPH = 3;
			}	
		}
		else {
			valeurPH = 0;
		}
		
		
		return valeurPH;

	}
	
	public int[] VerifierPlagesHorairesTournee() {
		int nbLivraisons = getListeLivraison().size();
		int[] tableauPlageHoraire = new int[nbLivraisons]  ;  // initialiser taille a nombre de liv 
		
		for (int i = 0; i < nbLivraisons; i++) {
			int valeurPH = VerifierPlagesHorairesUneLiv(getListeLivraison().get(i));
			tableauPlageHoraire[i]= valeurPH;
		}
		
		return tableauPlageHoraire;
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
		
}
