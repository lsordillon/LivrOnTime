package modele;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * La classe DemandeLivraison permet de construire
 * un objet qui stocke une liste de livraison, une adresse d'entrepot
 * et une heure de depart. C est a partir de cet objet qu on pourra
 * calculer une tournee.
 * @author Matthieu
 *
 */
public class DemandeLivraison {
	
	private Date heureDepart;
	private Intersection adresseEntrepot;
	private ArrayList <Livraison> listeLivraisons;
	
	/**
	 * Constructeur de la classe DemandeLivraison, il cree
	 * l objet a partir d une liste de livraisons, d un entrepot
	 * et d un plan.
	 * @param livraisons
	 * @param entrepot
	 * @param plan
	 */
	public DemandeLivraison(NodeList livraisons, NodeList entrepot,Plan plan) {
		
		
		HashMap<Long, Intersection> intersections =  plan.getIntersections();
		
		final Element noeudEntrepot = (Element) entrepot.item(0);
		long j = Long.parseLong((String) noeudEntrepot.getAttribute("adresse"));
        Intersection intersection = intersections.get(j);
        
        String heure = noeudEntrepot.getAttribute("heureDepart");
        String [] items = heure.split(":");

        Date heureDebutPlage = new Date();
        Date heureFinPlage = new Date();
       
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(items[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(items[1]));
        cal.set(Calendar.SECOND, Integer.parseInt(items[2]));
        
        Date heureD = cal.getTime();

		this.heureDepart = heureD;
		this.adresseEntrepot = intersection;

		final int nbNoeuds = livraisons.getLength();
		listeLivraisons =  new ArrayList<Livraison>();
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
	            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(items[0]));
	            cal.set(Calendar.MINUTE, Integer.parseInt(items[1]));
	            cal.set(Calendar.SECOND, Integer.parseInt(items[2]));
	            
	            heureDebutPlage = cal.getTime();
	        }
	        
	        if (noeud.getAttributeNode("finPlage")!=null){
	        	fin = true;
	        	heure = noeud.getAttribute("finPlage");
	            items = heure.split(":");
	            
	            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(items[0]));
	            cal.set(Calendar.MINUTE, Integer.parseInt(items[1]));
	            cal.set(Calendar.SECOND, Integer.parseInt(items[2]));
	            
	            heureFinPlage = cal.getTime();
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
	       
	        
	    }  
	}
	
	public Date getHeureDepart() {
		return this.heureDepart;
	}
	
	public Intersection getAdresseEntrepot(){
		return this.adresseEntrepot;
	}
	
	public ArrayList<Livraison> getLivraisons() {
		return this.listeLivraisons;
	}
	
	public ArrayList<Intersection> getIntersections(){
		ArrayList<Intersection> intersections=new ArrayList<Intersection>();
		intersections.add(adresseEntrepot);
		for(int i=0; i<listeLivraisons.size();i++) {
			intersections.add(listeLivraisons.get(i).getDestination());
		}
		return intersections;
	}
}
