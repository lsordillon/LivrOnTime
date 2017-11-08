package controller;

import java.util.LinkedList;


public class ListeDeCdes {
	
	private LinkedList<Commande> liste;
	private int indiceCourant;
	
	public ListeDeCdes(){
		indiceCourant = -1;
		liste = new LinkedList<Commande>();
	}
	
	
	public void ajoute(Commande commande){
        int i = indiceCourant+1;
        
        while( i<liste.size() ){
            liste.remove(i);
        }
        
        indiceCourant++;
        liste.add(indiceCourant, commande);
       //commande.redoCde();
    }
	
	

	public void undo(){
		if (indiceCourant >= 0){
			Commande commande = liste.get(indiceCourant);
			indiceCourant--;
			commande.undoCde();
		}
	}
	

	public void annule(){
		if (indiceCourant >= 0){
			Commande commande = liste.get(indiceCourant);
			liste.remove(indiceCourant);
			indiceCourant--;
			commande.undoCde();
		}
	}

	
	public void redo(){
		if (indiceCourant < liste.size()-1){
			indiceCourant++;
			Commande commande = liste.get(indiceCourant);
			commande.redoCde();
		}
	}
	
	
	   public void reset(){
	        indiceCourant = -1;
	        liste.clear();  
	    }

}
