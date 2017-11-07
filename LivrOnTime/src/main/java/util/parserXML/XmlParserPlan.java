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
import org.xml.sax.SAXParseException;

public class XmlParserPlan {
	
	static Document doc;
	public static NodeList noeuds;
	public static NodeList troncons;
	private String messageErreur;
	
	public Document lecteur(String nomFichier) throws FileNotFoundException{
	    	
	    	Document document= null;
	    	
	        try {
	        	 
	        	DocumentBuilderFactory usine = DocumentBuilderFactory.newInstance();
	        			usine.setNamespaceAware(true);
	        			DocumentBuilder constructeur = usine.newDocumentBuilder();
	        			document= constructeur.parse(nomFichier);
	            
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
	        noeuds = racine.getElementsByTagName("noeud");	      
	        troncons = racine.getElementsByTagName("troncon");  	
	
	    	 return doc;
	}
	    
	public boolean validationXSD(InputStream xml, InputStream xsd)
	    {
	        try
	        {
	            SchemaFactory usine = 
	                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	            Schema schema = usine.newSchema(new StreamSource(xsd));
	            Validator validateur = schema.newValidator();
	            validateur.validate(new StreamSource(xml));
	            return true;
	        }
	        catch(SAXParseException ex)
	        {
	        	messageErreur = "Erreur a la ligne "+ ex.getLineNumber()+" : " + ex.getMessage();
	            return false;
	        }
	        catch(Exception ex)
	        {
	            return false;
	        }
	    }
	
	public String getMessageErreur() {
		return messageErreur;
	}
	public void setMessageErreur(String messageErreur) {
		this.messageErreur = messageErreur;
	}
	   
}