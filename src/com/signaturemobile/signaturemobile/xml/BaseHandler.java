package com.signaturemobile.signaturemobile.xml;

import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;


/**
 * BaseHandler extends DefaultHandler to provide support for lexical events
 * 
 *  @author <a href="mailto:info@movilok.com">Movilok Interactividad Movil S.L.</a>
 */
public class BaseHandler extends DefaultHandler implements LexicalHandler {

    /**
     * Notifies the start of a DTD 
     * @param name the name 
     * @param publicId the publid identifier
     * @param systemId the system identifier
     * @throws SAXException on parsing errors
     * 
     */
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
    	// empty
    }

    /**
     * Notifies the start of an XML entity
     * @param name the entity name
     * @throws SAXException on parsing errors
     */
    public void startEntity(String name) throws SAXException {
    	// empty
    }

    /**
     * Notifies the start of a CDATA
     * @throws SAXException on parsing errors 
     */
    public void startCDATA() throws SAXException {
    	// empty
    }

    /**
     * Notifies a comment
     * @param ch the character array where the comment is stored
     * @param start the start of the comment fragment
     * @param length the length of the comment fragment
     * @throws SAXException on parsing errors
     */
    public void comment(char[] ch, int start, int length) throws SAXException {
    	// empty
    }

    /**
     * Notifies the end of a CDATA
     * @throws SAXException on parsing errors
     */
    public void endCDATA() throws SAXException {
    	// empty
    }

    /**
     * Notifies the end of an XML entity
     * @param name the entity name
     * @throws SAXException on parsing errors
     */
    public void endEntity(String name) throws SAXException {
    	// empty
    }

    /**
     * Notifies the end of a DTD
     * @throws SAXException on parsing errors
     */
    public void endDTD() throws SAXException {
    	// empty
    }
}
