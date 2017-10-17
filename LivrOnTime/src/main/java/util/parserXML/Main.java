package util.parserXML;

import model.Intersection;
import model.Plan;
import model.Troncon;

public class Main {

	public static void main(String[] args) {
		// Test Parser Plan
		XmlParserPlan parser = new XmlParserPlan();
		Plan plan = new Plan();
		
		parser.Reader("src/main/java/resources/planLyonMoyen.xml");
		
		plan.CreerIntersections(XmlParserPlan.noeuds);
		plan.CreerTroncons(XmlParserPlan.troncons);
		
		// Affichage des troncons
		for ( Troncon i : Plan.getTroncons()){
			System.out.println(i.toString());
		}
		
		
		
		// Test Parser Livraison
		XmlParserLivraison parserLivraison = new XmlParserLivraison();
		//for ( Livraison i : )
		
	}

}
