package controller;

import java.util.LinkedList;



public class ListeDeCdes {
	
	
	private LinkedList<Commande> liste;
	private int indiceCrt;
	
	public ListeDeCdes(){
		indiceCrt = -1;
		liste = new LinkedList<Commande>();
	}
	
	public void ajoute(Commande c){
        int i = indiceCrt+1;
        while(i<liste.size()){
            liste.remove(i);
            indiceCrt++;
        	liste.add(indiceCrt, c);
        	c.redoCde();
        }
        
    }
	
	
	public void undo(){
		if (indiceCrt >= 0){
			Commande cde = liste.get(indiceCrt);
			indiceCrt--;
			cde.undoCde();
		}
	}
	
	
	public void annule(){
		if (indiceCrt >= 0){
			liste.remove(indiceCrt);
			indiceCrt--;
		}
	}

	
	public void redo(){
		if (indiceCrt < liste.size()-1){
			indiceCrt++;
			Commande cde = liste.get(indiceCrt);
			cde.redoCde();
		}
	}
	
	//ce fait � chaque ouverture du plan
	   public void reset(){
	        indiceCrt = -1;
	        liste.clear();  
	    }

}
