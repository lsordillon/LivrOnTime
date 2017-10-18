package LivrOnTime;

import java.util.ArrayList;

import vue.DessinerPlan;
import model.DemandeLivraison;
import model.Livraison;
import model.Plan;
import model.Troncon;
import util.parserXML.XmlParserLivraison;
import util.parserXML.XmlParserPlan;

public class MainTest {

	public static void main(String[] args) {
		// Test Parser Plan
		XmlParserPlan parser = new XmlParserPlan();
		Plan plan = new Plan();
		
		parser.Reader("src/main/java/resources/planLyonMoyen.xml");
		
		plan.CreerIntersections(XmlParserPlan.noeuds);
		plan.CreerTroncons(XmlParserPlan.troncons);
		
		// Affichage des troncons
		for ( Troncon i : plan.getTroncons()){
			System.out.println(i.toString());
		}
		
		
		
		// Test Parser Livraison
		XmlParserLivraison parserLivraison = new XmlParserLivraison();
		parserLivraison.Reader("src/main/java/resources/DLmoyen5TW4.xml");
		DemandeLivraison dl = new DemandeLivraison(XmlParserLivraison.livraisons,XmlParserLivraison.entrepot,plan);
		
		ArrayList<Livraison> liste = dl.getLivraisons();
		
		for(Livraison i : liste) {
			System.out.println(i);
		}
		System.out.println(dl.getHeureDepart());
		
		
	}

}
