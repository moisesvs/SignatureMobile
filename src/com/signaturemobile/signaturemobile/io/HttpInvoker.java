package com.signaturemobile.signaturemobile.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.ToolBox;
import com.signaturemobile.signaturemobile.utils.Tools;

/**
 * HttpInvoker is a proxy class used for client to server comunications
 *
 * @author <a href="mailto:info@movilok.com">Movilok Interactividad Movil S.L.</a>
 */
public class HttpInvoker {
    /**
     * The content type used on post operations 
     */
    public static final String OPERATIONS_CONTENT_TYPE = "application/x-www-form-urlencoded; charset=utf-8";

    /**
     * The invoker tag
     */
    private static final String TAG = "HttpInvoker";
    /**
     * The character set used for post operation 
     */
    private static final String POST_CHARSET = "utf-8";

    /**
     * The character set used as default for operation responses 
     */
    private static final String DEFAULT_INPUT_CHARSET = "utf-8";

    /**
     * The inner HTTP client
     */
    private DefaultHttpClient httpClient;    

    /**
     * The XML parser
     */
    private XMLReader xmlReader;

    /**
     * Max number of redirects
     */
    private static final int MAX_REDIRECTS = 5;

    /**
     * Current registered connections
     */
    private Hashtable<String, HttpRequestBase> tokenConnections;

    /**
     * Current registered cancellations
     */
    private Hashtable<String, HttpRequestBase> tokenCancellations;

    /**
     * Current HTTP connections
     */
    private Vector<HttpRequestBase> connections;

    /**
     * Random token generator
     */
    private Random random;

    /**
     * The toolbox
     */
    private ToolBox toolbox;

    /**
     * Last response contents
     */
    private String lastFailedResponse = null;

    /**
     * Default constructor
     */
    public HttpInvoker(ToolBox toolbox) {

        this.toolbox = toolbox;
        
        try {
            HttpParams httpParams = new BasicHttpParams();
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", new PlainSocketFactory(), 80));
            if (Constants.TRUST_ALL_CERTIFICATES) {
                registry.register(new Scheme("https", new PermissiveSocketFactory(), 443));
            } else {
                registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            }
            httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(httpParams, registry), httpParams);
            httpClient.getParams().setParameter("http.protocol.max-redirects", MAX_REDIRECTS);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            xmlReader = parser.getXMLReader();
        } catch (Throwable t) {
            Log.e("HttpInvoker", "", t);
        }        

        
        random = new Random(System.currentTimeMillis());
        tokenConnections = new Hashtable<String, HttpRequestBase>();
        tokenCancellations = new Hashtable<String, HttpRequestBase>();
        connections = new Vector<HttpRequestBase>();
    
    }
    
    /**
     * Get a new token
     * @return the next token
     */
    public String token() {
        String token = Long.toString(random.nextLong());
        return token;
    }

    /**
     * Get last failed response contents
     * @return last failed response contents
     */
    public String getLastFailedResponse() {
        return this.lastFailedResponse;
    }

    /**
     * Gets the target URL
     * @param uri the URI
     * @param bis use BIS
     * @return the URL
     */
    private static String getTargetUrl(String uri) {
        StringBuffer sb = new StringBuffer();
        boolean hasProtocol = (uri.indexOf("://") > 0);
        if (!hasProtocol) {
//            String server = Constants.TARGET_SERVERS[Constants.TARGET][Constants.ENVIRONMENT];
//            sb.append(server);
        }
        sb.append(uri);
        String url = sb.toString();
        return url;
    }

    /**
     * Get a XML content
     * @param uri the resource URI
     * @param type 
     * @param body 
     * @param bis use BIS
     * @param handler 
     * @param token 
     * @throws IOException on a communication error
     * @throws SAXException on parsing errors
     * @throws ConnectionCancelledException 
     */
    public void get(String uri, boolean clearCookies, DefaultHandler handler, String token, boolean auth, String username, String password) throws IOException, SAXException, ConnectionCancelledException {
        get(uri, clearCookies, null, handler, token, auth, username, password);
    }

    /**
     * Get a XML content
     * @param uri the resource URI
     * @param type 
     * @param body 
     * @param bis use BIS
     * @param handler 
     * @param token 
     * @throws IOException on a communication error
     * @throws SAXException on parsing errors
     * @throws ConnectionCancelledException 
     */
    public void post(String uri, boolean clearCookies, String type, String body, DefaultHandler handler, String token, boolean auth, String username, String password) throws IOException, SAXException, ConnectionCancelledException {
        post(uri, clearCookies, type, body, null, handler, token, auth, username, password);
    }/**
     * Get a XML content
     * @param uri the resource URI
     * @param type 
     * @param body 
     * @param httpHeaders
     * @param bis use BIS
     * @param handler 
     * @param token 
     * @throws IOException on a communication error
     * @throws SAXException on parsing errors
     * @throws ConnectionCancelledException 
     */
    public void get(String uri, boolean clearCookies, Map<String, String> httpHeaders, DefaultHandler handler, String token, boolean auth, String username, String password) throws IOException, SAXException, ConnectionCancelledException {
        invoke(HttpGet.METHOD_NAME, uri, clearCookies, null, null, httpHeaders, handler, token, auth, username, password);
    }

    /**
     * Get a XML content
     * @param uri the resource URI
     * @param type 
     * @param body
     * @param httpHeaders
     * @param bis use BIS
     * @param handler 
     * @param token 
     * @throws IOException on a communication error
     * @throws SAXException on parsing errors
     * @throws ConnectionCancelledException 
     */
    public void post(String uri, boolean clearCookies, String type, String body, Map<String, String> httpHeaders, DefaultHandler handler, String token, boolean auth, String username, String password) throws IOException, SAXException, ConnectionCancelledException {
        invoke(HttpPost.METHOD_NAME, uri, clearCookies, type, body, httpHeaders, handler, token, auth, username, password);
    }

    /**
     * Get a XML content
     * @param method the HTTP method
     * @param uri the resource URI
     * @param type 
     * @param body
     * @param handler 
     * @param token 
     * @throws IOException on a communication error
     * @throws SAXException on parsing errors
     * @throws ConnectionCancelledException 
     */
    protected void invoke(String method, String uri, boolean clearCookies, String type, String body, Map<String, String> httpHeaders, DefaultHandler handler, String token, boolean auth, String username, String password) throws IOException, SAXException, ConnectionCancelledException {

        String target = getTargetUrl(uri);
        String charset = POST_CHARSET;        
        retrieve(method, target, clearCookies, type, charset, body, httpHeaders, handler, null, token, auth, username, password);

    }

    /**
     * Retrieves and parses an XML document
     * @param method the HTTP method
     * @param url the URL used to retrieve the XML document
     * @param type 
     * @param body 
     * @param httpHeaders
     * @param handler a SAX handler
     * @param token 
     * @throws IOException 
     * @throws SAXException 
     * @throws ConnectionCancelledException 
     */
    private void retrieve(String method, String url, boolean clearCookies, String type, String charset, String body, Map<String, String> httpHeaders, DefaultHandler handler, 
            String userAgent, String token, boolean auth, String username, String password) throws IOException, SAXException, ConnectionCancelledException {


        if (url != null) {

            InputStream stream;
            String encoding;

            if (url.startsWith("assets://")) {

                String asset = url.substring(9);
                stream = open(asset);
                encoding = "UTF-8";

                read(stream, encoding, handler);

            } else {

                HttpRequestBase request = null;

                try {

                    request = open(url, clearCookies, type, charset, body, httpHeaders, userAgent, token);

                    if (auth){
                        
                        URI uri = null;
                        try {
                            uri = new URI(url);
                        } catch (URISyntaxException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        
                        httpClient.getCredentialsProvider().setCredentials(
                                new AuthScope(uri.getHost(), uri.getPort(), AuthScope.ANY_SCHEME),
                                new UsernamePasswordCredentials(username, password));
                    }
                    
                    HttpResponse response = httpClient.execute(request);

                    if (Constants.TRACE_OPERATIONS) {
                        Tools.logLine(TAG, "getting response code... ");
                    }
                    int code = response.getStatusLine().getStatusCode();
                    if (Constants.TRACE_OPERATIONS) {
                        Tools.logLine(TAG, "response code = " + code);
                    }
                    if (code != HttpURLConnection.HTTP_OK) {
                        throw new IOException("Response code error");
                    }

                    HttpEntity entity = response.getEntity();
                    encoding = getCharset(entity.getContentType().getValue());
                    stream = entity.getContent();

                    read(stream, encoding, handler);

                } catch (IOException ioe) {
                    throw ioe;
                }catch (SAXException se) {
                    throw se;
                } finally {

                    boolean cancelled = false;

                    if (token != null) {
                        cancelled = tokenCancellations.containsKey(token);
                        tokenConnections.remove(token);
                        tokenCancellations.remove(token);
                    }

                    closeConnection(request);

                    if (cancelled) {
                        throw new ConnectionCancelledException();
                    }

                }

            }

        }

    }


    /**
     * Open the HTTP connection, closing previous if any
     * @param method the HTTP method
     * @param url the target URL
     * @param type the content type to be sent if POST operation is invoked
     * @param body the content to be sent if POST operation is invoked
     * @param httpHeaders 
     * @return the HTTP connection
     * @throws IOException on communication errors
     */
    private HttpRequestBase open(String url, boolean clearCookies, String type, String charset, String body, Map<String, String> httpHeaders, String userAgent, String token) throws IOException {

        HttpRequestBase request = null;

        HttpParams params = this.httpClient.getParams();
        if (clearCookies) {
            params.removeParameter(ClientPNames.COOKIE_POLICY);
        } else {
            params.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);
        }

        if (body != null) {

            HttpPost post = new HttpPost(url);

            addConnection(post);

            if (token != null) {
                tokenConnections.put(token, post);
            }


            if (userAgent != null) {
                post.setHeader("User-Agent", userAgent);
            }
            StringEntity content = new StringEntity(body, charset);
            content.setContentType(type);
            post.setEntity(content);

            if (Constants.TRACE_OPERATIONS) {
                Tools.logLine(TAG, "URL: " + url);
                Tools.logLine(TAG, "method = POST");
                Tools.logLine(TAG, "content type: " + type);
                Tools.logLine(TAG, "content length: " + content.getContentLength());
                Tools.logLine(TAG, "content = " + body);
            }

            request = post;

        } else {

            HttpGet get = new HttpGet(url);

            addConnection(get);

            if (token != null) {
                tokenConnections.put(token, get);
            }


            if (userAgent != null) {
                get.setHeader("User-Agent", userAgent);
            }

            if (Constants.TRACE_OPERATIONS) {
                Tools.logLine(TAG, "URL: " + url);
                Tools.logLine(TAG, "method = GET");
            }
            
            request = get;

        }
        
        if (httpHeaders != null) {
            Iterator<String> keys = httpHeaders.keySet().iterator();
            String key;
            while (keys.hasNext()) {
                key = keys.next();
                request.setHeader(key, httpHeaders.get(key));
            }
        }

        return request;

    }

    /**
     * Open the HTTP connection, closing previous if any
     * @param method the HTTP method
     * @param url the target URL
     * @param type the content type to be sent if POST operation is invoked
     * @param body the content to be sent if POST operation is invoked
     * @return the HTTP connection
     * @throws IOException on communication errors
     */
    private InputStream open(String asset) throws IOException {

        Context context = this.toolbox.getApplication();
        AssetManager assets = context.getAssets();
        InputStream stream = assets.open(asset, AssetManager.ACCESS_BUFFER);

        return stream;

    }

    /**
     * Reads from the connection and closes it.
     * @param response connection response
     * @return String with result of the operation
     * @throws SAXException 
     */
    private void read(InputStream stream, String charset, DefaultHandler handler) throws IOException, SAXException {

        Reader reader = null;
        byte[] data =  null;
        String response = null;
        boolean failed = true;

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while((length = stream.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            data = baos.toByteArray();

            boolean knownCharset = false;
            if (charset != null) {
                knownCharset = true;
            } else {
                charset = "UTF-8";
            }
            
            if (Constants.TRACE_OPERATIONS) {
                response = new String(data, charset);
                Tools.logLine(TAG, "response contents = " + response);
            }
            
            stream.close();
            
            if (handler != null) {
//                if (handler instanceof ParseableCacheableObject) {
//                    ((ParseableCacheableObject) handler).setData(data);
//                }
                if (knownCharset) {
                    if (response == null) {
                        response = new String(data, charset);
                    }
                    reader = new StringReader(response);
                    parse(reader, handler);
                } else {
                    stream = new ByteArrayInputStream(data);
                    parse(stream, handler);
                }
            }
            
            failed = false;

        } finally {
            if (failed) {
                if (response == null) {
                    response = new String(data, charset);
                }
                this.lastFailedResponse = response;
            }
            try {
                if (reader != null) {
                    reader.close();
                    stream = null;
                }
            } catch  (Throwable ignored) {
            	// empty
            }
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch  (Throwable ignored) {
            	// empty
            }
        }

    }

    /**
     * URL encoding
     * @param s the input string
     * @return the encoded string
     */
    public static String encode(String s) {
        String encoded = s;
        if (s != null) {
            try {
                encoded = URLEncoder.encode(s, "UTF-8");
            } catch (Exception ex) {
                encoded = null;
            }
        }
        encoded = Tools.replaceAll(encoded, "+", "%20");
        return (encoded);
    }

    /**
     * Abort a connection
     * @param token the connection token
     */
    public void abort(String token) {
        if (token != null) {
            HttpRequestBase connection = this.tokenConnections.get(token);
            if (connection != null) {
                this.tokenCancellations.put(token, connection);
                closeConnection(connection);
            }
        }
    }

    /**
     * Abort current connections
     */
    public void abortConnections() {
        String token;
        for (Enumeration<String> e = this.tokenConnections.keys(); e.hasMoreElements();) {
            token = e.nextElement();
            abort(token);
        }
        int size = connections.size();
        HttpRequestBase connection;
        for (int i = 0; (i < size); i++) {
            connection = connections.elementAt(i);
            if (connection != null) {
                closeConnection(connection);
            }
        }
    }

    /**
     * Add connection
     */
    protected void addConnection(HttpRequestBase connection) {
        if (connection != null) {
            synchronized (connections) {
                if (!connections.contains(connection)) {
                    connections.addElement(connection);
                }
            }
        }
    }

    /**
     * Close connection
     */
    protected void closeConnection(HttpRequestBase connection) {
        if (connection != null) {
            synchronized (connections) {
                if (connections.contains(connection)) {
                    connections.removeElement(connection);
                }
            }
            try {
                connection.abort();
            } catch (Throwable ignored) {
            	// empty
            }
        }
    }

    /**
     * Gets the charset from content type header
     * @param contentType the content type header
     * @return the charset
     */
    private String getCharset(String contentType) {
        String charset = null;
        if (contentType != null) {
            int pos = contentType.indexOf(";");
            if (pos >= 0) {
                pos = contentType.indexOf("=", pos);
                if (pos >= 0) {
                    charset = contentType.substring(pos + 1).trim();
                }
            }
        }
        if (charset == null) {
            charset = DEFAULT_INPUT_CHARSET;
            if (Constants.TRACE_OPERATIONS) {
                Tools.logLine(TAG, "charset unknown, using " + charset);
            }
        } else {
            if (Constants.TRACE_OPERATIONS) {
                Tools.logLine(TAG, "charset = " + charset);
            }
        }
        return charset;
    }

    /**
     * Reads from the connection and closes it.
     * @param reader connection response
     * @param handler the XML handler
     * @return String with result of the operation
     * @throws SAXException 
     */
    private void parse(Reader reader, DefaultHandler handler) throws IOException, SAXException {

        synchronized(this.xmlReader) {

            xmlReader.setContentHandler(handler);            
            xmlReader.parse(new InputSource(reader));            
        }
    }

    /**
     * Reads from the connection and closes it.
     * @param stream connection response
     * @param handler the XML handler
     * @return String with result of the operation
     * @throws SAXException 
     */
    public void parse(InputStream stream, DefaultHandler handler) throws IOException, SAXException {

        synchronized(this.xmlReader) {

            xmlReader.setContentHandler(handler);            
            xmlReader.parse(new InputSource(stream));            
        }
    }
    
    /**
     * Retrieves a raw web content
     * @param method the HTTP method
     * @param url the URL used to retrieve the XML document
     * @param httpHeaders 
     * @param type 
     * @param body 
     * @param handler a SAX handler
     * @param token 
     * @throws IOException 
     * @throws SAXException 
     * @throws ConnectionCancelledException 
     */
    public Bitmap getImage(String url, Map<String, String> httpHeaders, String token) throws IOException, ConnectionCancelledException {
    	
        Tools.logLine(TAG, "Loading bitmap for: " + url);
        
        Bitmap bitmap = null;
        if (url != null) {        	
            InputStream stream;                   
            HttpRequestBase request = null;

            try {
        		String charset = DEFAULT_INPUT_CHARSET;
                request = open(url, false, null, charset, null, httpHeaders, null, token);

                HttpResponse response = httpClient.execute(request);

                if (Constants.TRACE_OPERATIONS) {
                    Tools.logLine(TAG, "getting response code... ");
                }
                int code = response.getStatusLine().getStatusCode();
                if (Constants.TRACE_OPERATIONS) {
                    Tools.logLine(TAG, "response code = " + code);
                }
                if (code != HttpURLConnection.HTTP_OK) {
                    throw new IOException("Response code error");
                }

                HttpEntity entity = response.getEntity();                
                stream = entity.getContent();
                if (stream != null) {
                	bitmap = createBitmap(stream);
                }                   

            } finally {

                boolean cancelled = false;

                if (token != null) {
                    cancelled = tokenCancellations.containsKey(token);
                    tokenConnections.remove(token);
                    tokenCancellations.remove(token);
                }

                closeConnection(request);

                if (cancelled) {
                    throw new ConnectionCancelledException();
                }

            }
            
        }
        return bitmap;

    }
    
    /**
     * Creates a bitmap from input stream
     * @param stream the stream
     * @return the bitmap
     * @throws IOException on communication errors
     */
    private Bitmap createBitmap(InputStream stream) throws IOException {

        Bitmap bitmap = null;

        if (stream != null) {
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while((length = stream.read(buffer)) >= 0) {
                baos.write(buffer, 0, length);
            }
            buffer = baos.toByteArray();
//            bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);  
            
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);     
        
        }

        return bitmap;

    }
    
    /**
     * Retrieves a raw web content
     * @param method the HTTP method
     * @param url the URL used to retrieve the XML document
     * @param httpHeaders 
     * @param type 
     * @param body 
     * @param handler a SAX handler
     * @param token 
     * @throws IOException 
     * @throws SAXException 
     * @throws ConnectionCancelledException 
     */
    public String getText(String url, Map<String, String> httpHeaders, String token) throws IOException, ConnectionCancelledException {
    	
    	String text = null;
        if (url != null) {        	
            InputStream stream;                   
            HttpRequestBase request = null;
            String encoding;
            try {
        		String charset = DEFAULT_INPUT_CHARSET;
                request = open(url, false, null, charset, null, httpHeaders, null, token);

                HttpResponse response = httpClient.execute(request);

                if (Constants.TRACE_OPERATIONS) {
                    Tools.logLine(TAG, "getting response code... ");
                }
                int code = response.getStatusLine().getStatusCode();
                if (Constants.TRACE_OPERATIONS) {
                    Tools.logLine(TAG, "response code = " + code);
                }
                if (code != HttpURLConnection.HTTP_OK) {
                    throw new IOException("Response code error");
                }

                HttpEntity entity = response.getEntity();
                encoding = getCharset(entity.getContentType().getValue());
                stream = entity.getContent();
                text = readText(stream, encoding); 

            } finally {

                boolean cancelled = false;

                if (token != null) {
                    cancelled = tokenCancellations.containsKey(token);
                    tokenConnections.remove(token);
                    tokenCancellations.remove(token);
                }

                closeConnection(request);

                if (cancelled) {
                    throw new ConnectionCancelledException();
                }

            }
            
        }
        return text;

    }

	private String readText(InputStream stream, String charset) throws IOException, UnsupportedEncodingException {
		Reader reader = null;
        byte[] data =  null;
        String response = null;
        boolean failed = true;

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while((length = stream.read(buffer)) >= 0) {
                baos.write(buffer, 0, length);
            }
            data = baos.toByteArray();
            
            if (charset == null) {               
                charset = "UTF-8";
            }
            
            response = new String(data, charset);
            if (Constants.TRACE_OPERATIONS) {                
                Tools.logLine(TAG, "response contents = " + response);
            }
            
            stream.close();
                        
            
            failed = false;

        } finally {
            if (failed) {
                if (response == null) {
                    response = new String(data, charset);
                }
                this.lastFailedResponse = response;
            }
            try {
                if (reader != null) {
                    reader.close();
                    stream = null;
                }
            } catch  (Throwable ignored) {
            	// empty
            }
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch  (Throwable ignored) {
            	// empty
            }
        }
        return response;
    }
    
}

