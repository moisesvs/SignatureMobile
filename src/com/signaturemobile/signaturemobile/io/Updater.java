package com.signaturemobile.signaturemobile.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.os.Handler;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.Session;
import com.signaturemobile.signaturemobile.SignatureMobileApplication;
import com.signaturemobile.signaturemobile.ToolBox;
import com.signaturemobile.signaturemobile.model.ParseableObject;
import com.signaturemobile.signaturemobile.model.StatusEnabledResponse;
import com.signaturemobile.signaturemobile.utils.Tools;

/**
 * Updater is a proxy class used for client to server comunications
 *
 * @author <a href="mailto:info@movilok.com">Movilok Interactividad Movil S.L.</a>
 */
public class Updater {
    
    /**
     * The Updater tag
     */
    private static final String TAG = "Updater";

    /**
     * Operation types
     */
    private static final int DOWNLOAD_RESOURCE = 0;
    
    /**
     * The application toolbox
     */
    private ToolBox toolbox;

    /**
     * Current active operations
     */
    private Hashtable<String, OperationInformation> activeOperations;
    
    /**
     * The invoker itself
     */
    private HttpInvoker invoker;
    
    /**
     * A handler to send messages to the view
     */
    private final Handler messageHandler = new Handler();

    /**
     * Common HTTP Headers
     */
    private Map<String, String> commonHTTPHeaders;
    
    
    /**
     * Thread pool core size
     */
    private static final int THREAD_POOL_CORE_SIZE = 2;

    /**
     * Thread pool maximum size
     */
    private static final int THREAD_POOL_MAX_SIZE = 4;
    
    /**
     * Keep alive time for threads in excess over core size
     */
    private static final long THREAD_POOL_KEEP_ALIVE_TIME = 10;

    /**
     * The default size for thread queue
     */
    private static final int DEFAULT_QUEUE_SIZE = 6;

    /**
     * The operation thread queue
     */
    private ArrayBlockingQueue<Runnable> threadQueue;
    
    /**
     * The operation thread pool
     */
    private ThreadPoolExecutor threadPool;
    
    /**
     * Default constructor
     * @param toolbox the toolbox
     * @param invoker The HTTP Invoker
     */
    public Updater(ToolBox toolbox, HttpInvoker invoker) {
        this.invoker = invoker;
        this.toolbox = toolbox;
        this.activeOperations = new Hashtable<String, OperationInformation>();
        this.threadQueue = new ArrayBlockingQueue<Runnable>(DEFAULT_QUEUE_SIZE);
        this.threadPool = new ThreadPoolExecutor(
        		THREAD_POOL_CORE_SIZE, THREAD_POOL_MAX_SIZE,
        		THREAD_POOL_KEEP_ALIVE_TIME, TimeUnit.SECONDS, this.threadQueue);
    }
    
    /**
     * Download an image fron a url
     * @param url the url
     * @return the image
     * @throws IOException
     * @throws ConnectionCancelledException
     */
    public Bitmap downloadImage(String url) throws IOException, ConnectionCancelledException {

        String token = invoker.token();
    	
        OperationInformation operationInfo = new OperationInformation(token, Constants.DOWNLOAD_RESOURCE_OPERATION,
    			null, url);
    	activeOperations.put(token, operationInfo);
    	
    	Bitmap result = null;

        try {
            
            Map<String, String> httpHeaders = getCommonHTTPHeaders();
            result = invoker.getImage(url, httpHeaders, token);         

            invokeOperationSuccessInMainThread(token);
        
        } catch (Throwable t) {
            Tools.logThrowable(TAG, t);
            if (t instanceof ConnectionCancelledException) {
                invokeOperationCancelledInMainThread(token);
            } else {
                invokeOperationFailureInMainThread(token, t);
            }
        }

    	return result;
    	
    }
    
    /**
     * Download the text content of a url
     * @param url the url
     * @return the web content
     * @throws IOException
     * @throws ConnectionCancelledException
     */
    public String downloadText(String url) throws IOException, ConnectionCancelledException {

        String token = invoker.token();
    	OperationInformation operationInfo = new OperationInformation(token, Constants.DOWNLOAD_RESOURCE_OPERATION,
    			null, url);
    	activeOperations.put(token, operationInfo);
    	
    	String result = null;

        try {
            
            Map<String, String> httpHeaders = getCommonHTTPHeaders();
            result = invoker.getText(url, httpHeaders, token);      

            invokeOperationSuccessInMainThread(token);
        
        } catch (Throwable t) {
            Tools.logThrowable(TAG, t);
            if (t instanceof ConnectionCancelledException) {
                invokeOperationCancelledInMainThread(token);
            } else {
                invokeOperationFailureInMainThread(token, t);
            }
        }
    	
    	return result;
    	
    }
    
    /**
     * Build a request from a list of parameters and its values
     * @param parameters the parameters names
     * @param values the parameter values
     * @return the request
     */
    private String getRequest(String[] parameters, String[] values) {
	   StringBuffer request = new StringBuffer();
	   String param, value;
	   for (int i = 0; i < parameters.length; i++) {
		   param = parameters[i];
		   value = values[i];
		   request.append(param).append("=");
		   request.append(HttpInvoker.encode(value));
		   if (i < parameters.length - 1) {
			   request.append("&");
		   }
	   }
	   return request.toString();	   
    }
    
    
    /**
     * Invoke operation in a separate thread 
     * @param uri the URI
     * @param body the parameter sequence
     * @param handler the handler
     * @param token the request token
     */
    private void invokeOperationInSecondaryThread(final String uri, final String body, final boolean clearCookies, final Map<String, String> httpHeaders, final ParseableObject handler, final String token) {

       invokeOperationInSecondaryThread(uri, body, clearCookies, httpHeaders, handler, token, true);
    
    }
    
    /**
     * Invoke operation in a separate thread 
     * @param uri the URI
     * @param body the parameter sequence
     * @param httpHeaders 
     * @param handler the handler
     * @param token the request token
     */
    private void invokeOperationInSecondaryThread(final String uri, final String body, final boolean clearCookies, final Map<String, String> httpHeaders, final ParseableObject handler, final String token, final boolean postMethod) {
    
    	OperationInvocation invocation = new OperationInvocation(uri, body, clearCookies, httpHeaders, handler, token, false, null, null);
        this.threadPool.execute(invocation);
    
    }
    
    /**
     * Invoke operation in a separate thread 
     * @param uri the URI
     * @param body the parameter sequence
     * @param httpHeaders 
     * @param handler the handler
     * @param token the request token
     */
    private void invokeOperationInSecondaryThreadAuth(final String uri, final String body, final boolean clearCookies, final Map<String, String> httpHeaders, final ParseableObject handler, final String token, final boolean postMethod, final String username, final String password) {
        
    	OperationInvocation invocation = new OperationInvocation(uri, body, clearCookies, httpHeaders, handler, token, false, username, password);
        this.threadPool.execute(invocation);
    
    }
 
    /**
     * OperationInvocation provides a runnable invocation object for server operations
     */
    private class OperationInvocation implements Runnable {
    	
    	/**
    	 * Target URI
    	 */
    	private String uri;
    	
    	/**
    	 * The request body
    	 */
    	private String body;
    	
    	/**
    	 * True if it is required to clear cookies before invocation
    	 */
    	private boolean clearCookies;

    	/**
    	 * Extra HTTP headers
    	 */
    	private Map<String, String> httpHeaders;
    	
    	/**
    	 * XML handler object
    	 */
    	private ParseableObject handler;
    	
    	/**
    	 * HttpInvoker token
    	 */
    	private String token;
    	
    	/**
    	 * True if post method is required
    	 */
    	private boolean postMethod;
    	
    	/**
    	 * Username
    	 */
    	private String username;
    	
    	/**
    	 * Password
    	 */
    	private String password;
    	
    	/**
    	 * Constructor
    	 * 
    	 * @param uri target URI
    	 * @param body request body
    	 * @param clearCookies true if it is required to clear cookies before invocation
    	 * @param httpHeaders extra HTTP headers
    	 * @param handler XML handler object
    	 * @param token HttpInvoker token
    	 * @param postMethod true if post method is required
    	 * @param username the username
    	 * @param password the password
    	 */
    	public OperationInvocation(String uri, String body, boolean clearCookies, Map<String, String> httpHeaders, ParseableObject handler, String token, boolean postMethod, String username, String password) {
    		this.uri = uri;
    		this.body = body;
    		this.clearCookies = clearCookies;
    		this.httpHeaders = httpHeaders;
    		this.handler = handler;
    		this.token = token;
    		this.postMethod = postMethod;
    		this.username = username;
    		this.password = password;
    	}

    	/**
    	 * The runnable method
    	 */
    	public void run() {
            try {
            	if (postMethod) {
            		invoker.post(uri, clearCookies, HttpInvoker.OPERATIONS_CONTENT_TYPE, body, httpHeaders, handler, token, false, null, null);
            	} else {
            		String uri2 = uri;
            		if ((body != null) && (body.length() > 0)) {
            			uri2 = new StringBuffer(uri).append("?").append(body).toString();
            		}
            		invoker.get(uri2, clearCookies, httpHeaders, handler, token, false, username, password);
            	}
                invokeOperationSuccessInMainThread(token);
            } catch (Throwable t) {
                Tools.logThrowable(TAG, t);
                if (t instanceof ConnectionCancelledException) {
                    invokeOperationCancelledInMainThread(token);
                } else {
                    invokeOperationFailureInMainThread(token, t);
                }
            }
		}
   
    }
    
    /**
     * Invoke operation failure in main thread
     * @param token the operation token
     * @param throwable the failure
     */
    private void invokeOperationSuccessInMainThread(final String token) {
        messageHandler.post(new Runnable() {
          public void run() {
              try {
                  operationSuccess(token);
              } catch (Throwable t) {
                  Tools.logThrowable(TAG, t);
              }
          }
        });
    }
    
    /**
     * Invoke operation failure in main thread
     * @param token the operation token
     * @param throwable the failure
     */
    private void invokeOperationCancelledInMainThread(final String token) {
        messageHandler.post(new Runnable() {
            public void run() {
                try {
                    operationCancelled(token);
                } catch (Throwable t) {
                    Tools.logThrowable(TAG, t);
                }
            }
        });
    }
    
    /**
     * Invoke operation failure in main thread
     * @param token the operation token
     * @param throwable the failure
     */
    private void invokeOperationFailureInMainThread(final String token, final Throwable throwable) {
        messageHandler.post(new Runnable() {
            public void run() {
                try {
                    operationFailed(token, throwable);
                } catch (Throwable t) {
                    Tools.logThrowable(TAG, t);
                }
            }
        });
    }
    
    /**
     * Receives the notification of an operation failure 
     * @param token the operation token
     * @param throwable the failure
     */
    private void operationSuccess(String token) {

        OperationInformation operationInfo = activeOperations.get(token);
        if (operationInfo != null) {
            
            boolean success = true;
            int logicalErrorCode = -1;
            String logicalErrorMessage = null;            
            
            ParseableObject handler = operationInfo.getHandler();
            if ((handler != null) && (handler instanceof StatusEnabledResponse)) {
            	StatusEnabledResponse response = (StatusEnabledResponse) handler;
                int operationType = operationInfo.getType();
                switch(operationType) {
                case DOWNLOAD_RESOURCE:
                	
                    break;
                default:
                    logicalErrorCode = response.getStatusCode();
                    logicalErrorMessage = response.getStatusDescription();
                    success = (!response.isError());
                }
            }

            if (success) {
                System.out.println("Operation success");
            } else {
                if (logicalErrorMessage != null) {
                    System.out.println("Operation failed: " + logicalErrorCode + " (" + logicalErrorMessage + ")");
                } else {                   
                    System.out.println("Operation failed: " + logicalErrorCode);                    
                }
            }
            
            processFinishedOperation(operationInfo, success);
            
            activeOperations.remove(token);

            if (!success) {
                String errorMessage = logicalErrorMessage;                
                if (errorMessage != null) {
                    int operationType = operationInfo.getType();
                    switch (operationType) {                    
                    default:
                    	// TODO
                        //toolbox.getApplication().showErrorMessage(errorMessage);
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Receives the notification of an operation cancellation 
     * @param token the operation token
     */
    private void operationCancelled(String token) {

        System.out.println("Operation cancelled");
    
        OperationInformation operationInfo = activeOperations.get(token);
        if (operationInfo != null) {
            
            processFinishedOperation(operationInfo, false);
            
            activeOperations.remove(token);
            
        }
    
    }
    
    /**
     * Receives the notification of an operation failure 
     * @param token the operation token
     * @param throwable the failure
     */
    private void operationFailed(String token, Throwable throwable) {

        System.out.println("Operation failed with error: " + ((throwable != null) ? throwable.toString() : "..."));
    
        OperationInformation operationInfo = activeOperations.get(token);
        if (operationInfo != null) {
                        
            boolean success = false;                                             

            if (!success) {
                SignatureMobileApplication app = toolbox.getApplication();
                ParseableObject handler = operationInfo.getHandler();
                NotificationCenter notificationCenter = toolbox.getNotificationCenter();
            	switch (operationInfo.getType()) {
//                    case Constants.LAYERS_OPERATION:
//                    
//	            	default:
//                        notificationCenter.postNotification(Constants.kCommunicationsError, handler);
//	            		toolbox.getApplication().showErrorMessage(throwable);
//	            		break;
            	}
            }

            processFinishedOperation(operationInfo, success);
            
            activeOperations.remove(token);
            
        }
    
    }
    
    /**
     * Receives the notification of an operation failure 
     * @param token the operation token
     * @param throwable the failure
     */
    private void processFinishedOperation(OperationInformation operationInfo, boolean success) {

        if (operationInfo != null) {
            
            int operationType = operationInfo.getType();
            ParseableObject handler = operationInfo.getHandler();

            Session session = toolbox.getSession();
            if (session != null) {

//                switch (operationType) {
//                case LOGIN:
//                    if (success) {
//                    }
//                    break;                
//                default:
//                    break;
//                }
            }
    
            NotificationCenter notificationCenter = toolbox.getNotificationCenter();
            if (notificationCenter != null) {
                
                if (handler != null) {
                    String notificationToPost = handler.getNotificationToPost();
                    if (notificationToPost != null) {
                        notificationCenter.postNotification(notificationToPost, handler);
                    }
                }
        
                // For "end of operation" notifications
                switch (operationType) {
//                case LOGIN:
//                    //notificationCenter.postNotification(Constants.kNotificationLoginEnds, handler);
//                    break;                
                default:
                    break;
                }
                
            }   
        }
    }
    
    /**
     * Gets the target URI
     * @param operation the operation
     * @return the URI
     */
    private static String getOperationUri(int operation) {
        return Constants.TARGET_OPERATION[Constants.TARGET][Constants.ENVIRONMENT][operation];
    }
    
    /**
     * Aborts current HTTP operation
     */
    public void abort() {
        invoker.abortConnections();
    }
    
    /**
     * Abort an http operation
     * @param token the connection token
     */
    public void abort(String token) {
        invoker.abort(token);
    }
    
    /**
     * Get a new token
     * @return the next token
     */
    public String token() {
        return invoker.token();
    }
   
    /**
     * Returns the common HTTP headers map
     * @return
     */
    private Map<String, String> getCommonHTTPHeaders() {
        if (commonHTTPHeaders == null) {
            commonHTTPHeaders= new HashMap<String, String>();
            
        }
        
        return commonHTTPHeaders;
    }

    /**
     * Returns parameter language value
     * 
     * @return parameter language value
     */
    private String getLanguageParamValue() {
        return Locale.getDefault().toString().replace("_", "-");
    }

    /**
     * OperationInfo stores operation token and type  
     */
    private class OperationInformation {
        
//        /**
//         * The operation token
//         */
//        private String token;
        
        /**
         * The operation type
         */
        private int type;
        
        /**
         * The operation handler
         */
        private ParseableObject handler;
        
        /**
         * Constructor
         * @param token the token
         * @param type the type
         * @param handler the handler
         * @param parameters the parameters
         */
        public OperationInformation(String token, int type, ParseableObject handler, Object parameters) {
            this.type = type;
            this.handler = handler;
        }

        /**
         * Gets the operation type
         * @return the type
         */
        public int getType() {
            return type;
        }
        
        /**
         * Gets the operation handler
         * @return the handler
         */
        public ParseableObject getHandler() {
            return handler;
        }
    }
}

