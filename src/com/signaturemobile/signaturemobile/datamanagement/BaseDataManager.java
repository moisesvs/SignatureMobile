/*
 * Moisés Vázquez Sánchez
 */ 
package com.signaturemobile.signaturemobile.datamanagement;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;

import com.signaturemobile.signaturemobile.ToolBox;

/**
 * BaseDataManager provides a base, abstract class for all managers of basic data that are used throughout the
 * application (resources, master data, layers)
 * 
 * @author <a href="mailto:moisesvs@gmail.com">Mooisés Vázquez Sánchez</a>
 */
public abstract class BaseDataManager {
	
	/**
	 * A reference to the toolbox
	 */
	protected ToolBox toolbox;
	
	/**
	 * Default constructor
	 * @param toolbox the toolbox
	 */
	public BaseDataManager(ToolBox toolbox) {
		this.toolbox = toolbox;
	}
	
	/**
	 * Load the local data
	 */
	public void load() {
		if (hasInternalStorage()) {
			//load the resources from internal storage
			loadPreferences();			
		} else {
			// load data from the stored xml			
			try {
	            loadFromXml();	            
	            savePreferences();
            } catch (Throwable th) {
	            //Log.d()
	           
            } 
		}
	}
	
	/**
	 * Get if there are data stored in the local persistence
	 * @return true if there are data stored in the local persistence, false if not
	 */
	protected abstract boolean hasInternalStorage();
	
	/**
	 * Load the local data (internal storage in shared preferences) 
	 */
	protected abstract void loadPreferences();
	
	/**
	 * Load the local data from preloaded XML files
	 */
	protected abstract void loadFromXml();
	
	/**
	 * Save the data in internal storage
	 */
	protected abstract void savePreferences();
	
	/**
	 * Find the next XML tag with a given name in an XML parser
	 * @param parser the XML parser
	 * @param tagName the next XML tag to find
	 * @throws XmlPullParserException if XML pull parser exception
	 * @throws IOException if input output exception readind the underlying file
	 */
	protected void gotoNextTag(XmlResourceParser parser, String tagName) throws XmlPullParserException, IOException {
		boolean found = false;
		String tag;
		int eventType = parser.getEventType();
		while ((!found) && (eventType != XmlPullParser.END_DOCUMENT))
		{
			if (eventType == XmlPullParser.START_TAG) {
				tag = parser.getName();
				if (tagName.equalsIgnoreCase(tag)) {
					found = true;
				}
			}
			if (!found) {
    			parser.next();
    			eventType = parser.getEventType();
			}
			
		}
	}
	
	/**
	 * Find the value inside the next XML tag with a given name in an XML parser
	 * @param parser the XML parser
	 * @param tagName the next XML tag whose value is to be found
	 * @throws XmlPullParserException if XML pull parser exception
	 * @throws IOException if input output exception readind the underlying file
	 */
	protected String getNextValue(XmlResourceParser parser, String tagName) throws XmlPullParserException, IOException {
		gotoNextTag(parser, tagName);
		String result = parser.nextText();
		return result;
	}
	
	/**
	 * Save the data in local persistence
	 */
	public abstract void save(); 

	
}
