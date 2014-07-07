package com.shuttlemap.android.map;

import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class MapService {

	
	public static MapDataSet getMapDateSet(String url){
		
		MapDataSet navigationDataSet = null;
	    try{           
	        final URL aUrl = new URL(url);
	        final URLConnection conn = aUrl.openConnection();
	        conn.setReadTimeout(15 * 1000);  // timeout for reading the google maps data: 15 secs
	        conn.connect();

	        /* Get a SAXParser from the SAXPArserFactory. */
	        SAXParserFactory spf = SAXParserFactory.newInstance(); 
	        SAXParser sp = spf.newSAXParser(); 

	        /* Get the XMLReader of the SAXParser we created. */
	        XMLReader xr = sp.getXMLReader();

	        /* Create a new ContentHandler and apply it to the XML-Reader*/ 
	        NavigationSaxHandler navSax2Handler = new NavigationSaxHandler(); 
	        xr.setContentHandler(navSax2Handler); 

	        /* Parse the xml-data from our URL. */ 
	        xr.parse(new InputSource(aUrl.openStream()));

	        /* Our NavigationSaxHandler now provides the parsed data to us. */ 
	        navigationDataSet = navSax2Handler.getParsedData(); 

	    } catch (Exception e) {
	        // Log.e(myapp.APP, "error with kml xml", e);
	        navigationDataSet = null;
	    }   

	    return navigationDataSet;
	}
}
