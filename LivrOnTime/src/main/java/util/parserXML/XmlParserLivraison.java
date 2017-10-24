package util.parserXML;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import model.Livraison;

/** Cette classe est une classe utilitaire permettant
 * le parsage du fichier xml de demande de livraison
 * et sa conversion en objet demande de livraison
 * 
 * @author Matthieu
 *
 */
public class XmlParserLivraison {
	
	static Document doc;
	public static NodeList entrepot;
	public static NodeList livraisons;
	
		/**
		 * Méthode parcourant un fichier xml pour en tirer les 
		 * informations necessaires a la creation de l'objet
		 * demande de livraison
		 * @param file_name, le nom du fichier a parser
		 * @return doc, un document contenant les informations utiles extraites du xml
		 * @throws FileNotFoundException
		 */
	    public Document Reader(String file_name) throws FileNotFoundException{
	    	Document document= null;
	    	
	    
	        try {
	        
	        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        			factory.setNamespaceAware(true);
	        			DocumentBuilder builder = factory.newDocumentBuilder();
	        			document= builder.parse(file_name);
	            
	        }catch (final ParserConfigurationException e) {
	        			    e.printStackTrace();
	        			}
	        			catch (final SAXException e) {
	        			    e.printStackTrace();
	        			}
	        			catch (final IOException e) {
	        			    e.printStackTrace();
	        			}
	            
	        final Element racine = document.getDocumentElement();
	        entrepot = racine.getElementsByTagName("entrepot");
	      
	        livraisons = racine.getElementsByTagName("livraison");

	        
	        
	         return doc;
	    	
}

	    public boolean validationXSD(InputStream xml, InputStream xsd)
	    {
	        try
	        {
	            SchemaFactory factory = 
	                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	            Schema schema = factory.newSchema(new StreamSource(xsd));
	            Validator validator = schema.newValidator();
	            validator.validate(new StreamSource(xml));
	            return true;
	        }
	        catch(Exception ex)
	        {
	            return false;
	        }
	    }
	    
	    public void AjouterLivraison(Livraison liv){
	    	
	    	Element livE=doc.createElement("Livraison");
	    	Element root = doc.getDocumentElement();
	    	root.appendChild(livE);
	    	
	    	Node adresse=doc.createAttribute("adresse");
	    	adresse.setNodeValue(String.valueOf(liv.getDestination().getId()));
	        livE.appendChild(adresse);
	        
	    	if(liv.getDebutPlageHoraire()!=null){
	    		Node DPH=doc.createAttribute("debutPlage");
	    		DPH.setNodeValue(liv.getDebutPlageHoraire().toString());
	    		livE.appendChild(DPH);
	    	}
	    	
	    	Node duree=doc.createAttribute("duree");
	    	duree.setNodeValue(String.valueOf(liv.getDuree()));
	    	livE.appendChild(duree);
	    	
	    	if(liv.getFinPlageHoraire()!=null){
	    		Node FPH=doc.createAttribute("finPlage");
	    		FPH.setNodeValue(liv.getFinPlageHoraire().toString());
	    		livE.appendChild(FPH);
	    	}
	 
	    	
	    }
	    
	    
	  public void supprimerLivraison(Livraison liv){
		  for (int i = 0; i <livraisons.getLength(); i++) {       
	            Element livE = (Element)livraisons.item(i);
	            String adresse = livE.getAttribute("adresse");
	            if(adresse.equals(String.valueOf(liv.getDestination().getId())))
	                livE.getParentNode().removeChild(livE);
		  }
	  }  

	  
}
