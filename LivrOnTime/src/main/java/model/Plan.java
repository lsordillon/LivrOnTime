package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import DijikstraCalcul.Graph;

public class Plan {
	

	private ArrayList <Livraison> solution;
	private ArrayList <Chemin> analyse;
	
	private GrapheComplet graphe_complet=new GrapheComplet(Livraisons);
	private static HashMap<Long,Intersection> Intersections = new HashMap();
	private static ArrayList<Long> id_intersections=new ArrayList<Long>();
	private static int N_intersections;;
	private static ArrayList<Troncon> Troncons = new ArrayList<Troncon>();
	private static int N_Troncons;
	private static DemandeLivraison DL;
	private static Intersection entropot=DL.getAdresseEntrepot();
	private static ArrayList<Livraison> Livraisons=DL.getLivraisons();
	private static int N_livraisons;
	private static ArrayList<Intersection> IntersectionsDL=RemplirIntersectionsDL();
	private static int N_IntersectionsDL=IntersectionsDL.size();
	
	

        
  private static ArrayList<Intersection> RemplirIntersectionsDL(){
	  ArrayList<Intersection> list = null;
	 list.add(entropot);
	   for(int i=0;i<N_livraisons; i++)
		   list.add(Livraisons.get(i).getDestination());
		   return list;
	   
	  
  }
	
public void CreerIntersections(NodeList noeuds) {
	  final int nbNoeuds = noeuds.getLength();
      for(int j = 0; j<nbNoeuds; j++) {
          final Element noeud = (Element) noeuds.item(j);
         long k = Long.parseLong((String) noeud.getAttribute("id"));
          Intersection intersection = new Intersection( k , Integer.parseInt(noeud.getAttribute("x")) , Integer.parseInt(noeud.getAttribute("y")));
          Intersections.put(k,intersection);
          id_intersections.add(k);
      } 
      N_intersections=Intersections.size();
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
  N_Troncons=Troncons.size();

}

public int conversion(Intersection inter){
	return  id_intersections.indexOf(inter.getId());
	
}
public Intersection retroConversion(int i){
	return Intersections.get(id_intersections.get(i));
}

//le graphe qui contient tt les point de plan
public Graph SuperGraphe(){
	Graph g=new Graph(N_intersections);
	for(int i=0; i<N_Troncons; i++){
		
		int j=g.addEdge(conversion(Troncons.get(i).getOrigine()),conversion(Troncons.get(i).getDestination()),Troncons.get(i).getLongueur());
	
	}
	return g;
}



 Graph GrapheDuPlan= SuperGraphe();
 double distance[];
 int precedence[];
 
 //PCC= PlusCourtChemin
public void CalculPCC(Intersection inter){
	 distance=GrapheDuPlan.shortestPath(conversion(inter));
	 precedence=GrapheDuPlan.predecenceTable();
}
public Troncon trouverTroncon(Intersection origine, Intersection distination){
	int i;
	for(i=0; i<N_Troncons; i++)
		if(Troncons.get(i).getOrigine()==origine && Troncons.get(i).getDestination()==distination) 
		break;
		
	 return Troncons.get(i);
}

public void m(){
	double cout[][] = null;
	Chemin chemin[][]=null;
	Date HeureD=DL.getHeureDepart();
	Date HeureA;
	for(int i=0;i<N_IntersectionsDL;i++){
		CalculPCC(IntersectionsDL.get(i));
		   for(int j=0; j<N_IntersectionsDL;j++){
			   cout[i][j]=distance[conversion(IntersectionsDL.get(i))];
			   if(i!=j)
			     {
			       chemin[i][j].setHeureDepart(HeureD);
			       chemin[i][j].setOrigine(IntersectionsDL.get(i));
			       chemin[i][j].setDestination(IntersectionsDL.get(j));
			       HeureA=HeureD;///a completer
			       chemin[i][j].setHeureArrivee(HeureD);
			       int k=i;
			       
			       while(k!=-1){
			    	   k=precedence[conversion(IntersectionsDL.get(k))];
			    	   if(k!=-1){
			    		  
			    	   }
			       }
			     }
			   
			   
		   }
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
