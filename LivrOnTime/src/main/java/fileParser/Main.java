package fileParser;

import Models.Intersection;
import Models.Plan;
import Models.Troncon;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XmlParser parser = new XmlParser();
		Plan plan = new Plan();
		
		parser.Reader("planLyonMoyen.xml");
		
		plan.CreerIntersections(XmlParser.noeuds);
		plan.CreerTroncons(XmlParser.troncons);
		
		// Affichage des tronçons
		for ( Troncon i : Plan.getTroncons()){
			System.out.println(i.toString());
		}
	}

}
