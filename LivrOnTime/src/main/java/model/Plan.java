package model;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Plan {
	

	private ArrayList <Livraison> solution;
	private ArrayList <Chemin> analyse;
	private GrapheComplet Graphe;
	private static HashMap<Long,Intersection> Intersections = new HashMap();
	private static ArrayList<Troncon> Troncons = new ArrayList<Troncon>();
	

        
        
	
public void CreerIntersections(NodeList noeuds) {
	  final int nbNoeuds = noeuds.getLength();
      for(int j = 0; j<nbNoeuds; j++) {
          final Element noeud = (Element) noeuds.item(j);
         long k = Long.parseLong((String) noeud.getAttribute("id"));
          Intersection intersection = new Intersection( k , Integer.parseInt(noeud.getAttribute("x")) , Integer.parseInt(noeud.getAttribute("y")));
          Intersections.put(k,intersection);
      }  
}
public void CreerTroncons(NodeList troncons) {
  final int nbTroncons = troncons.getLength();
  for(int j = 0; j<nbTroncons; j++) {
      final Element troncon = (Element) troncons.item(j);
      Long k = Long.parseLong(troncon.getAttribute("destination"));
      Intersection intersectionD = Plan.Intersections.get(k);
      k = Long.parseLong(troncon.getAttribute("origine"));
      Intersection intersectionO = Plan.Intersections.get(k); 
      Troncons.add(new Troncon(intersectionD, Double.parseDouble(troncon.getAttribute("longueur")), troncon.getAttribute("nomRue") , intersectionO));
  
  }

}
public static HashMap<Long, Intersection> getIntersections() {
	return Intersections;
}
public static void setIntersections(HashMap<Long, Intersection> intersections) {
	Intersections = intersections;
}
public static ArrayList<Troncon> getTroncons() {
	return Troncons;
}
public static void setTroncons(ArrayList<Troncon> troncons) {
	Troncons = troncons;
}

}
