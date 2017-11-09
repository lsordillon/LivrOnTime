package util.tsp;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class TemplateTSP implements TSP {
	
	private Integer[] meilleureSolution;
	private long coutMeilleureSolution = 0;
	private Boolean tempsLimiteAtteint;
	
	public Boolean getTempsLimiteAtteint(){
		return tempsLimiteAtteint;
	}
	
	public void chercheSolution(long tpsLimite, int nbSommets, long[][] cout, long[] duree,long[][] temps){
		
		
		tempsLimiteAtteint = false;
		coutMeilleureSolution = Integer.MAX_VALUE;
		meilleureSolution = new Integer[nbSommets];
		ArrayList<Integer> nonVus = new ArrayList<Integer>();
		for (int i=1; i<nbSommets; i++) nonVus.add(i);
		ArrayList<Integer> vus = new ArrayList<Integer>(nbSommets);
		vus.add(0); // le premier sommet visite est 0
		branchAndBound(0, nonVus, vus, 0, cout, duree, System.currentTimeMillis(), tpsLimite,temps);
	}
	
	public Integer getMeilleureSolution(int i){
		if ((meilleureSolution == null) || (i<0) || (i>=meilleureSolution.length))
			return null;
		return meilleureSolution[i];
	}
	
	public long getCoutMeilleureSolution(){
		return coutMeilleureSolution;
	}
	
	/**
	 * Methode devant etre redefinie par les sous-classes de TemplateTSP
	 * @param sommetCourant
	 * @param nonVus : tableau des sommets restant a visiter
	 * @param cout : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
	 * @param duree : duree[i] = duree pour visiter le sommet i, avec 0 <= i < nbSommets
	 * @return une borne inferieure du cout des permutations commencant par sommetCourant, 
	 * contenant chaque sommet de nonVus exactement une fois et terminant par le sommet 0
	 */
	protected abstract int bound(Integer sommetCourant, ArrayList<Integer> nonVus, long[][] cout, long[] duree);
	
	/**
	 * Methode devant etre redefinie par les sous-classes de TemplateTSP
	 * @param sommetCrt
	 * @param nonVus : tableau des sommets restant a visiter
	 * @param cout : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
	 * @param duree : duree[i] = duree pour visiter le sommet i, avec 0 <= i < nbSommets
	 * @return un iterateur permettant d'iterer sur tous les sommets de nonVus
	 */
	protected abstract Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, long[][] cout, long[] duree);
	
	/**
	 * Methode definissant le patron (template) d'une resolution par separation et evaluation (branch and bound) du TSP
	 * @param sommetCourant le dernier sommet visite
	 * @param nonVus la liste des sommets qui n'ont pas encore ete visites
	 * @param vus la liste des sommets visites (y compris sommetCrt)
	 * @param coutVus la somme des couts des arcs du chemin passant par tous les sommets de vus + la somme des duree des sommets de vus
	 * @param cout : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
	 * @param duree : duree[i] = duree pour visiter le sommet i, avec 0 <= i < nbSommets
	 * @param tpsDebut : moment ou la resolution a commence
	 * @param tpsLimite : limite de temps pour la resolution
	 */	
	 void branchAndBound(int sommetCourant, ArrayList<Integer> nonVus, ArrayList<Integer> vus, long coutVus, long[][] cout, long[] duree, long tpsDebut, long tpsLimite, long[][] temps){
		 if (System.currentTimeMillis() - tpsDebut > tpsLimite){
			 tempsLimiteAtteint = true;
			 System.err.println("Temps limite atteint !");
			 return;
		 }
	    if (nonVus.size() == 0){ // tous les sommets ont ete visites
	    	coutVus += cout[sommetCourant][0];
	    	if (coutVus < coutMeilleureSolution){ // on a trouve une solution meilleure que meilleureSolution
	    		vus.toArray(meilleureSolution);
	    		coutMeilleureSolution = coutVus;
	    	}
	    } else if (coutVus + bound(sommetCourant, nonVus, cout, duree) < coutMeilleureSolution){
	        Iterator<Integer> it = iterator(sommetCourant, nonVus, cout, duree);
	        while (it.hasNext()){
	        	Integer prochainSommet = it.next();
	        	vus.add(prochainSommet);
	        	nonVus.remove(prochainSommet);

	        	//Code simple permettant de savoir si la plage horaire est respect�e
	        	if ((temps[prochainSommet][1])>coutVus+cout[sommetCourant][prochainSommet]) {
		        	if ((temps[prochainSommet][0])>coutVus+cout[sommetCourant][prochainSommet]) {

	    				branchAndBound(prochainSommet, nonVus, vus,temps[prochainSommet][0], cout, duree, tpsDebut, tpsLimite,temps);
	    			}
	    			else {
	    				branchAndBound(prochainSommet, nonVus, vus, coutVus + cout[sommetCourant][prochainSommet] + duree[prochainSommet], cout, duree, tpsDebut, tpsLimite,temps);
	    			}
	        	}

	        	//Si la plage horaire est respect�e, on fait le branchement.
	        	//TODO Il faudrait essayer de bypass cette contrainte pour mettre quand m�me un iti avec des plages non respect�es (en fait juste coder le 'else').

	        	
	        	vus.remove(prochainSommet);
	        	nonVus.add(prochainSommet);
	        }	    
	    }
	}
}

