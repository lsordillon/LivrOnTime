package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import DijikstraCalcul.Graph;
import util.tsp.Dijkstra;
import util.tsp.TSP;
import util.tsp.TSP1;

public class Plan {
	

	private ArrayList <Livraison> solution;
	private Chemin[][] chemin;
	
	private GrapheComplet graphe_complet;
	private HashMap<Long,Intersection> Intersections = new HashMap();
	private ArrayList<Long> id_intersections=new ArrayList<Long>();
	private int N_intersections;;
	private ArrayList<Troncon> Troncons = new ArrayList<Troncon>();
	private int N_Troncons;
	
	//private static DemandeLivraison DL

	
	

        
  private ArrayList<Intersection> RemplirIntersectionsDL(DemandeLivraison DL){
	  ArrayList<Intersection> list = new ArrayList();
	// list.add(entropot);
	  int N_livraisons = DL.getLivraisons().size();
	   for(int i=0;i<N_livraisons; i++)
		   list.add(DL.getLivraisons().get(i).getDestination());
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
      Intersection intersectionD = Intersections.get(k);
      k = Long.parseLong(troncon.getAttribute("origine"));
      Intersection intersectionO = Intersections.get(k); 
      Troncons.add(new Troncon(intersectionD, Double.parseDouble(troncon.getAttribute("longueur")), troncon.getAttribute("nomRue") , intersectionO));
  
  }
  N_Troncons=Troncons.size();

}

public void TronconsVoisins() {
	
	for(int i =0; i< Troncons.size();i++) {
		Troncons.get(i).getOrigine().setTronconsVersVoisins(Troncons.get(i));
	}
	
	/*Collection <Intersection> inter = Intersections.values();
	ArrayList inters = new ArrayList(inter);
	for ( int i =0; i<inters.size();i++) {
		ArrayList<Troncon> tr = ((Intersection) inters.get(i)).getTronconsVersVoisins();
		System.out.println(inters.get(i));
		for ( int j =0; j<tr.size();j++) {
			System.out.println("Voisin : " + tr.get(j).getDestination() + "   " + tr.get(j).getLongueur() );
		}
	}*/
}

//le graphe qui contient tt les point de plan
public Graph SuperGraphe(){
	Graph g=new Graph(N_intersections);
	for(int i=0; i<N_Troncons; i++){
		
		int j=g.addEdge(conversion(Troncons.get(i).getOrigine()),conversion(Troncons.get(i).getDestination()),Troncons.get(i).getLongueur());
	
	}
	return g;
}

// ------------- Calcul Itinéraire

// L'intersection est lié à son numéro de placement dans la liste
public int conversion(Intersection inter){
	return  id_intersections.indexOf(inter.getId());	
}
public Intersection retroConversion(int i){
	return Intersections.get(id_intersections.get(i));
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

public void calculDijkstra(DemandeLivraison DL){
		Intersection entropot=DL.getAdresseEntrepot();
		ArrayList<Livraison> Livraisons=DL.getLivraisons();
		ArrayList<Intersection> IntersectionsDL=RemplirIntersectionsDL(DL);
		int N_IntersectionsDL=IntersectionsDL.size();
		double cout[][] = null;
	chemin=null;
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
			       
			       double tempsSup = cout[i][j]/(15.0/36.0); // conversion correcte a faire A DEPLACER
			       Calendar cal = Calendar.getInstance();
			       cal.setTime(HeureD);
			       cal.add(Calendar.SECOND, (int)(tempsSup));
			       HeureA = cal.getTime();
			       
			       
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
	
	graphe_complet=new GrapheComplet(Livraisons,cout);
	
}








public HashMap<Long, Intersection> getIntersections() {
	return Intersections;
}
public void setIntersections(HashMap<Long, Intersection> intersections) {
	Intersections = intersections;
}
public ArrayList<Troncon> getTroncons() {
	return Troncons;
}
public void setTroncons(ArrayList<Troncon> troncons) {
	Troncons = troncons;
}


public Tournee calculerLaTournee(DemandeLivraison dl) {
	
	Dijkstra d = new Dijkstra();
	Intersection ptDepart = dl.getAdresseEntrepot();
	d.algoDijkstra(this, ptDepart);
	//-----------------------------------------

	int tpLimite = 10;
	TSP etape2 = new TSP1 ();
	int nbSommet=dl.getLivraisons().size();
	ArrayList <Chemin> itineraire = new ArrayList ();
	
	calculDijkstra(dl);
	etape2.chercheSolution(tpLimite,nbSommet, graphe_complet.getCout(),graphe_complet.getDuree());
	
	for(int i=0; i<nbSommet-1;i++) {
		System.out.print(etape2.getMeilleureSolution(i)+";");
		itineraire.add(chemin[etape2.getMeilleureSolution(i)][etape2.getMeilleureSolution(i-1)]);
	}
	
	System.out.println(" ");

	itineraire.add(chemin[nbSommet-1][0]);
	System.out.println(etape2.getMeilleureSolution(nbSommet-1));
	
	Tournee tournee = new Tournee(itineraire);
	
	return tournee;
}

}
