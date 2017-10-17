package util.parserXML;

import model.Intersection;
import model.Plan;
import model.Troncon;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XmlParser parser = new XmlParser();
		Plan plan = new Plan();
		
		parser.Reader("src/main/java/resources/planLyonMoyen.xml");
		
		plan.CreerIntersections(XmlParser.noeuds);
		plan.CreerTroncons(XmlParser.troncons);
		
		// Affichage des troncons
		for ( Troncon i : Plan.getTroncons()){
			System.out.println(i.toString());
		}
	}

}
