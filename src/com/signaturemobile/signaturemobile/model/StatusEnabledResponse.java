package com.signaturemobile.signaturemobile.model;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * StatusEnabledResponse is the base implementation for XML response fragments
 *
 * @author <a href="mailto:info@movilok.com">Movilok Interactividad Movil S.L.</a>
 */
public class StatusEnabledResponse extends ParseableObject {
	/**
	 * Enumerates the basic analyzing states
	 */
	protected enum AnalyzingState {IDLE, ANALYZING_CODE, ANALYZING_DESCRIPTION};
		
	// Element names parsing constants
	/**
	 * Defines the response code element tag
	 */
	public static String RESPONSE_CODE_ELEMENT_TAG = "codest";

	/**
	 * Defines the response description element tag
	 */
	public static String RESPONSE_DESCRIPTION_ELEMENT_TAG = "desc";

	/**
	 * Response status codes
	 */
	public static final int OK_STATUS_CODE = 0;
	public static final int ERROR_BAD_REQUEST_CODE = 1;
	public static final int ERROR_PROCESSING_CODE = 2;
	public static final int NO_RESULTS_FOUND_CODE = 3;
	public static final int INVALID_ROUTE_CODE = 4;
	
    /**
     * The error code
     */
    protected int statusCode;
    
    /**
     * The error message
     */
    protected String statusDescription;
    
    /**
     * Flag to indicate an error
     */
    protected boolean error;
    
    /**
     * The current analysis state;
     */
    protected AnalyzingState xmlAnalysisState = AnalyzingState.IDLE;
    
    /**
     * Default constructor
     */
    public StatusEnabledResponse() {
        super();
        resetStatusEnabledResponseAttributes();      
    }
    
    
    
    /* (non-Javadoc)
	 * @see com.cepsa.cepsa.model.ParseableObject#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		resetStatusEnabledResponseAttributes();
	}

	/* (non-Javadoc)
	 * @see com.cepsa.cepsa.model.ParseableObject#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (xmlAnalysisState.equals(AnalyzingState.IDLE)) {
			String lowercaseLocalName = localName.toLowerCase();
			
			if (lowercaseLocalName.equals(RESPONSE_CODE_ELEMENT_TAG)) {
				xmlAnalysisState = AnalyzingState.ANALYZING_CODE;
			} else if (lowercaseLocalName.equals(RESPONSE_DESCRIPTION_ELEMENT_TAG)) {
				xmlAnalysisState = AnalyzingState.ANALYZING_DESCRIPTION;
			}
		}
	}



	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if (xmlAnalysisState.equals(AnalyzingState.ANALYZING_CODE)) {
			try {
				statusCode = Integer.parseInt(getElementString());
			} catch (NumberFormatException e) {
				statusCode = -1;
			}
		} else if (xmlAnalysisState.equals(AnalyzingState.ANALYZING_DESCRIPTION)) {
			statusDescription = getElementString();
		}
		
		xmlAnalysisState = AnalyzingState.IDLE;
	}



	/**
     * Gets the error code
     * @return the error code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the error code
     * @param statusCode the error code to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Gets the error message
     * @return the error message
     */
    public String getStatusDescription() {
        return statusDescription;
    }

    /**
     * Sets the error message
     * @param errorMessage the error message to set
     */
    public void setStatusDescription(String description) {
        this.statusDescription = description;
    }

    /**
     * Gets the error flag
     * @return the error flag
     */
    public boolean isError() {
        return (this.statusCode != OK_STATUS_CODE);
    }

    /**
     * Sets the error flag
     * @param error the error flag value to set
     */
    public void setError(boolean error) {
        this.error = error;
    }

	private void resetStatusEnabledResponseAttributes() {
        this.statusCode = -1;
        this.statusDescription = null;
        this.error = false;
	}
} 
