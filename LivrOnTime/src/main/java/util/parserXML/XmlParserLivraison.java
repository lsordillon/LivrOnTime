package util.parserXML;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XmlParserLivraison {
	
	static Document doc;
	public static NodeList entrepot;
	public static NodeList livraisons;
	
	
	    public Document Reader(String file_name){
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
}
