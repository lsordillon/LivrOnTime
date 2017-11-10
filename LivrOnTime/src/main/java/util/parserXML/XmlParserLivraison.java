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

import modele.Livraison;

/** Cette classe est une classe utilitaire permettant
 * le parsage du fichier xml de demande de livraison
 * et sa conversion en objet demande de livraison.
 * @author Matthieu
 *
 */
public class XmlParserLivraison {
	
	static Document doc;
	public static NodeList entrepot;
	public static NodeList livraisons;
	private String messageErreur;
	
	/**
	 * Methode parcourant un fichier xml pour en tirer les 
	 * informations necessaires a la creation de l'objet
	 * demande de livraison
	 * @param nomFichier, le nom du fichier a parser
	 * @return doc, un document contenant les informations utiles extraites du xml
	 * @throws FileNotFoundException
	 */
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
        entrepot = racine.getElementsByTagName("entrepot");      
        livraisons = racine.getElementsByTagName("livraison");	        
        
         return doc;	    	
    }

    /**
     * La methode validationXSD permet de verifier la validite
     * du format du fichier charge.
     * @param xml
     * @param xsd
     * @return
     */
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
        	messageErreur = "Erreur a la ligne "+ ex.getLineNumber() ;
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
