package model;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DemandeLivraison {
	
	private Date heureDepart;
	private Intersection adresseEntrepot;
	private ArrayList <Livraison> listeLivraisons;
	
	public Date getHeureDepart() {
		return this.heureDepart;
	}
	
	public Intersection getAdresseEntrepot(){
		return this.adresseEntrepot;
	}
	
	public ArrayList<Livraison> getLivraisons() {
		return this.listeLivraisons;
	}
	
	public DemandeLivraison(NodeList livraisons, NodeList entrepot) {
		
		
		HashMap<Long, Intersection> intersections =  Plan.getIntersections();
		
		final Element noeudEntrepot = (Element) entrepot.item(0);
		long j = Long.parseLong((String) noeudEntrepot.getAttribute("adresse"));
        Intersection intersection = intersections.get(j);
        
        String heure = noeudEntrepot.getAttribute("heureDepart");
        String [] items = heure.split(":");

        Date heureDebutPlage = new Date();
        Date heureFinPlage = new Date();
        Date heureD = new Date();
        heureD.setHours(Integer.parseInt(items[0]));
        heureD.setMinutes(Integer.parseInt(items[1]));
        heureD.setSeconds(Integer.parseInt(items[2]));
        

		this.heureDepart = heureD;
		this.adresseEntrepot = intersection;
		
		
		final int nbNoeuds = livraisons.getLength();
		listeLivraisons =  new ArrayList();
	    for(int i = 0; i<nbNoeuds; i++) {
	    	
	    	boolean debut = false;
	    	boolean fin = false;
	    	
	        final Element noeud = (Element) livraisons.item(i);
	        
	        long k = Long.parseLong((String) noeud.getAttribute("adresse"));
	        intersection = intersections.get(k);
	        
	        int duree = Integer.parseInt((String) noeud.getAttribute("duree"));
	        
	        if (noeud.getAttributeNode("debutPlage")!=null){
	        	
	        	debut = true;
	        	heure = noeud.getAttribute("debutPlage");
	            items = heure.split(":");
	            heureDebutPlage.setHours(Integer.parseInt(items[0]));
	            heureDebutPlage.setMinutes(Integer.parseInt(items[1]));
	            heureDebutPlage.setSeconds(Integer.parseInt(items[2]));
	        }
	        if (noeud.getAttributeNode("finPlage")!=null){
	        	fin = true;
	        	heure = noeud.getAttribute("finPlage");
	            items = heure.split(":");
	            heureFinPlage.setHours(Integer.parseInt(items[0]));
	            heureFinPlage.setMinutes(Integer.parseInt(items[1]));
	            heureFinPlage.setSeconds(Integer.parseInt(items[2]));
	        }
	        Livraison aLivrer = null;
	        if(debut && fin) {
	        	aLivrer = new Livraison (duree, intersection, heureDebutPlage, heureFinPlage);
	        }
	        else if (debut && !fin) {
	        	aLivrer = new Livraison (duree, intersection, heureDebutPlage, null);
	        }
	        else if (!debut && fin) {
	        	aLivrer = new Livraison (duree, intersection, null, heureFinPlage);
	        }
	        else {
	        	aLivrer = new Livraison (duree,intersection);
	        }
	        
	        this.listeLivraisons.add(aLivrer);
	        System.out.println(aLivrer);
	        
	    }  
	}
}
