package com.signaturemobile.signaturemobile.model;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.signaturemobile.signaturemobile.xml.BaseHandler;

/**
 * ParseableObject is the base implementation for XML response fragments
 *
 * @author <a href="mailto:info@movilok.com">Movilok Interactividad Movil S.L.</a>
 */
public class ParseableObject extends BaseHandler {
	/**
	 * Enumerates the basic analyzing states
	 */
	protected enum AnalyzingState {IDLE};

    /**
     * The notification to post
     */
    public String notificationToPost;
    
    /**
     * The operation token
     */
    protected String operationToken;
    
    /**
     * The current analysis state;
     */
    protected AnalyzingState xmlAnalysisState = AnalyzingState.IDLE;
    
    /**
     * Auxiliary string to construct element data
     */
    private StringBuffer auxStringBuffer;
    
    /**
     * Default constructor
     */
    public ParseableObject() {
        super();
    }

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		auxStringBuffer = new StringBuffer();
		xmlAnalysisState = AnalyzingState.IDLE;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		auxStringBuffer = new StringBuffer();
	}

    /* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		auxStringBuffer.append(ch, start, length);
	}

	/**
     * Gets the notification to post
     * @return the notificationToPost
     */
    public String getNotificationToPost() {
        return notificationToPost;
    }

    /**
     * Gets the operation token
     * @return the operationToken
     */
    public String getOperationToken() {
        return operationToken;
    }

    /**
     * Sets the operation token
     * @param operationToken the operationToken to set
     */
    public void setOperationToken(String operationToken) {
        this.operationToken = operationToken;
    }
    
    /**
     * Gets the auxiliary element string
     */
    protected String getElementString() {
    	if (auxStringBuffer == null) {
    		return null;
    	} else {
    		return auxStringBuffer.toString();
    	}
    }
} 
