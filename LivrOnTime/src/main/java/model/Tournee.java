package model;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.util.Pair;
import util.tsp.Dijkstra;

/**
 * La classe Tournee construit et stocke l itineraire
 * entre tous les points de livraison a atteindre.
 * Elle stocke egalement l heure de depart et l heure d arrivee
 * ainsi que les temps de passage aux differentes livraisons.
 * @author Matthieu
 *
 */
public class Tournee {


    static final double VITESSE=(double)15/3.6; // 15KM/h 
	private Date heureDepart;
	private Date heureArrivee;
	private ArrayList <Chemin> itineraire;
	private ArrayList <Livraison> listeLivraisons;
	private Date[][] tempsPassage;
	
	/**
	 * Le constructeur de la classe Tournee il cree l objet a partir
	 * d une liste de chemin et d une demande de livraison
	 * @param itineraire
	 * @param dl
	 */
	public Tournee(ArrayList<Chemin> itineraire, DemandeLivraison dl) {
	    this.heureDepart=dl.getHeureDepart();
		this.itineraire=itineraire;
		
		listeLivraisons = new ArrayList<Livraison>();
		for (Chemin chemin : itineraire){
	    	for(Livraison l : dl.getLivraisons()){
	    		if(l.getDestination().getId() == chemin.getDestination().getId()){
	    			listeLivraisons.add(l);
	    		}
	    	}
	    }	
	}
	
	/**
	 * La methode initTempsPassage initialise
	 * le tableau des temps de passage.
	 */
	public void initTempsPassage() {
		long dureeTotale=heureDepart.getTime();
		tempsPassage = new Date[getItineraire().size()][2];
		
		for(int i=0;i<getItineraire().size();i++){
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
		
	/**
	 * La methode SupprimerLivraison permet d'enlever une livraison
	 * donnee de la tournee.
	 * @param plan
	 * @param inter
	 * @param l
	 * @return
	 * @throws Exception
	 */
	public Pair <Integer, Tournee> SupprimerLivraison(Plan plan,Intersection inter,Livraison l) throws Exception{
		
		
		if(listeLivraisons.size()>-1) {
			int index=-1;
			int indiceListeLivraison = 0;
			Intersection origine=null;
			Intersection destination=null;
			if(listeLivraisons.contains(l)){
				Dijkstra d = new Dijkstra();
				ArrayList<Chemin> nouvelItineraire=new ArrayList<Chemin>(itineraire);
				for(int i=0;i<itineraire.size();i++){
					Chemin chemin=itineraire.get(i);
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
					d.algoDijkstra(plan, origine);
	
					Chemin nouveauChemin = new Chemin(origine, destination, plan);
	
					nouvelItineraire.add(indiceListeLivraison, nouveauChemin );
				}
	
				index=listeLivraisons.indexOf(l);
				listeLivraisons.remove(l);
				setItineraire(nouvelItineraire);
				initTempsPassage();
			}
			
			Pair <Integer, Tournee> paire = new Pair<Integer, Tournee>(index,this);
			System.out.println(listeLivraisons.size());
			return paire;
		
		}
		else {
			System.err.println("ERREUR ! La livraison ne fait pas partie de la tournee actuelle");
			return null;
		}

	}
	
	/**
	 * La methode AjouterLivraison permet d ajouter une livraison dans la tournee
	 * a un index donnee.
	 * @param plan
	 * @param inter
	 * @param l
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public Tournee AjouterLivraison(Plan plan,Intersection inter,Livraison l, int index) throws Exception{
		Dijkstra d = new Dijkstra();

		if(!getListeLivraison().contains(l)){
			ArrayList<Chemin> nouvelItineraire=new ArrayList<Chemin>(itineraire);
			Chemin cheminASupprimer = nouvelItineraire.get(index);
			nouvelItineraire.remove(index);
			
		    d.algoDijkstra(plan, cheminASupprimer.getOrigine());
		    Chemin nouveauChemin1 = new Chemin(cheminASupprimer.getOrigine(), inter, plan);
			nouvelItineraire.add(index,nouveauChemin1);

		    d.algoDijkstra(plan, inter);
		    Chemin nouveauChemin2 = new Chemin(inter, cheminASupprimer.getDestination(), plan);
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
	
	
	/**
	 * Cette methode ModifierLivraison permet de modifier
	 * la duree d'une livraison donnee.
	 * @param plan
	 * @param liv
	 * @param duree
	 * @return
	 */
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
	
	
	/**
	 * Cette methode ModifierLivraison permet de modifier
	 * la plage horaire d une livraison donnee.
	 * @param plan
	 * @param liv
	 * @param DPH
	 * @param FPH
	 * @return
	 */
	public Livraison ModifierLivraison(Plan plan, Livraison liv, Date DPH, Date FPH){
		
		if(this.getListeLivraison().contains(liv)){
			int i=this.getListeLivraison().indexOf(liv);
			  if(DPH==null || FPH==null){
				  liv = new Livraison(liv.getDuree(),liv.getDestination());
			  }else{
	          liv.setDebutPlageHoraire(DPH);
			  liv.setFinPlageHoraire(FPH);
			  }
			  this.getListeLivraison().set(i, liv);
			 

		}
		else {
			System.err.println("ERREUR ! La livraison ne fait pas partie de la tournee actuelle");
			return liv;
		}	
		this.initTempsPassage();
		return liv;
	}
	
	
	/**
	 * La methode VerifierPlagesHorairesUneLiv permet
	 * de verifier que l heure d arrivee sur le lieu de la
	 * livraison respecte bien la plage horaire associee.
	 * @param liv
	 * @return
	 */
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
	
	/**
	 * La methode VerifierPlagesHorairesTournee permet
	 * de verifier les plages horaires pour toute la tournee.
	 * @return
	 */
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
