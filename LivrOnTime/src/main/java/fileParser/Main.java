package fileParser;

import Intersection;
import Plan;
import Troncon;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XmlParser parser = new XmlParser();
		Plan plan = new Plan();
		
		parser.Reader("planLyonMoyen.xml");
		
		plan.CreerIntersections(XmlParser.noeuds);
		plan.CreerTroncons(XmlParser.troncons);
		
		for ( Troncon i : Plan.Troncons){
			System.out.println(i.toString());
		}
	}

}
