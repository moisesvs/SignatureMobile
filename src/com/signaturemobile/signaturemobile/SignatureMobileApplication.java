package com.signaturemobile.signaturemobile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.signaturemobile.signaturemobile.db.DAOAsignatureSQL;
import com.signaturemobile.signaturemobile.db.DAOClassSQL;
import com.signaturemobile.signaturemobile.db.DAOJoinAsignatureWithClassSQL;
import com.signaturemobile.signaturemobile.db.DAOJoinAsignatureWithUserSQL;
import com.signaturemobile.signaturemobile.db.DAOJoinClassWithUserSQL;
import com.signaturemobile.signaturemobile.db.DAOUserSQL;
import com.signaturemobile.signaturemobile.db.DBHelper;
import com.signaturemobile.signaturemobile.io.BluetoohInvoker;
import com.signaturemobile.signaturemobile.io.HttpInvoker;
import com.signaturemobile.signaturemobile.io.NotificationCenter;
import com.signaturemobile.signaturemobile.io.NotificationCenter.NotificationListener;
import com.signaturemobile.signaturemobile.io.Updater;

/**
 * The application class
 *  
 * @author <a href="mailto:moisesvs@gmail.com">Mooisés Vázquez Sánchez</a>
 */
public class SignatureMobileApplication extends Application implements NotificationListener {

	public static final int INITIALIZING_STATUS = 0;
	public static final int INITIALIZED_STATUS = 1;
    
    /**
     * Application instance
     */
    private static SignatureMobileApplication instance;
    
    /**
     * The application toolbox
     */
    private ToolBox toolbox;
    
    /**
     * The updater
     */
    private Updater updater;
    
    /**
     * The session
     */
    private Session session;

    /**
     * Bluetooh invoker
     */
    private BluetoohInvoker invokerBluetooh;
    
    /**
     * Dao user SQL
     */
    private DAOUserSQL daoUserSQL;
    
    /**
     * Dao asignature SQL
     */
    private DAOAsignatureSQL daoAsignatureSQL;
    
    /**
     * Dao class SQL
     */
    private DAOClassSQL daoClassSQL;
    
    /**
     * Dao asignature join with user SQL
     */
    private DAOJoinAsignatureWithUserSQL daoJoinAsignatureWithUserSQL;
    
    /**
     * Dao class join with user SQL
     */
    private DAOJoinClassWithUserSQL daoJoinClassWithUserSQL;
    
    /**
     * Dao class join asignature with class
     */
    private DAOJoinAsignatureWithClassSQL daoJoinAsignatureWithClass;
    
    /**
     * The notification center
     */
    private NotificationCenter notificationCenter;

    /**
     * Initialization end
     */
    public boolean initialized = false;
    
    /**
     * Initialization failed
     */
    public boolean initializationFailed = false;

    /**
     * Current activity
     */
    private Activity currentActivity;

    /**
     * DB helper content all DAO objects
     */
    protected DBHelper dBHelper;
    
    /**
     * Empthy constructor
     */
    public SignatureMobileApplication() {
    	// Nothing
    }
	
	/**
     * Called when the application is starting, before any other application objects have been created.
     * Implementations should be as quick as possible (for example using lazy initialization of state)
     * since the time spent in this function directly impacts the performance of starting the first
     * activity, service, or receiver in a process.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        try {
			setupApp();
		} catch (InstantiationException e) {
			showErrorMessage(this, e);
		}
    }
    
    /**
     * This method is for use in emulated process environments. It will never be called on
     * a production Android device, where processes are removed by simply killing them; no
     * user code (including this callback) is executed when doing so. 
     */
    @Override    
    public void onTerminate() {
        closeApp();
        super.onTerminate();
    }
    
    /**
     * Initialize application stuff
     * @throws InstantiationException 
     */
    private void setupApp() throws InstantiationException {
    	// create the basic tools
    	instance = this;

        // create object associated toolbox
        this.notificationCenter = new NotificationCenter();
        this.invokerBluetooh = new BluetoohInvoker(this, this.notificationCenter);
        this.daoUserSQL = new DAOUserSQL(this);
        this.daoAsignatureSQL = new DAOAsignatureSQL(this);
        this.daoClassSQL = new DAOClassSQL(this);
        this.daoJoinAsignatureWithUserSQL = new DAOJoinAsignatureWithUserSQL(this);
        this.daoJoinAsignatureWithClass = new DAOJoinAsignatureWithClassSQL(this);
        this.daoJoinClassWithUserSQL = new DAOJoinClassWithUserSQL(this);

        HttpInvoker invoker = new HttpInvoker(toolbox);
        this.updater = new Updater(this.toolbox, invoker);
        
        this.session = new Session();
        this.toolbox = new ToolBox();
        this.toolbox.setup(this, this.updater, this.session, this.invokerBluetooh, this.notificationCenter, this.daoUserSQL, 
        		this.daoAsignatureSQL, this.daoClassSQL, this.daoJoinAsignatureWithUserSQL, this.daoJoinClassWithUserSQL, this.daoJoinAsignatureWithClass);
 
        registerAll();
        // initialized thread app
        threadedSetupApp();
        
    }
    
    private void threadedSetupApp() {
        
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {

                    // load the base managers data from local persistence      
                	readFileSession();
                	notificationCenter.postNotification(Constants.kAllResourcesLoadingEnds, null);

                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * Returns Application instance
     * 
     * @return application instance
     */
    public static SignatureMobileApplication getInstance() {
		return instance;
	}
    
    /**
     * @return the currentActivity
     */
    public Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * @param currentActivity the currentActivity to set
     */
    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

	/**
     * Close application stuff
     */
    private void closeApp() {
        unRegisterAll();
    }

    /**
     * Gets the toolbox
     * @return the toolbox
     */
    public ToolBox getToolBox() {
        return this.toolbox;
    }

	public void notificationPosted(String notification, Object info) {
	    if (Constants.kAllResourcesLoadingEnds.equals(notification)) {
//            this.resourcesManager.save();
	    	initialized = true;
	    	unRegisterAll();
	    	notificationCenter.postNotification(Constants.kInitializationEnds, null);
	    } else if (Constants.kConfigurationDataLoadingFailed.equals(notification)) {
	        // Cancel all connections;
	        if (!initializationFailed) { // Already posted
    	        initializationFailed = true;
                notificationCenter.postNotification(Constants.kInitializationEnds, null);
	        }
        }
    }
	
	/**
	 * Registers all notifications
	 */
	private void registerAll(){
        this.notificationCenter.registerListener(Constants.kAllResourcesLoadingEnds, this);
	}
	
	/**
	 * Unregisters all notifications
	 */
	private void unRegisterAll(){
        this.notificationCenter.unregisterListener(Constants.kAllResourcesLoadingEnds, this);
	}
	
	/**
	 * Get if the application is already initialized
	 * @return true if the application is initialized, false if not
	 */
	public boolean isInitialized() {
		return this.initialized;	
	}

	/**
	 * Get if the application initialization has failed
	 * @return true if the application initialization has failed, false if not
	 */
    public boolean hasFailed() {
        return this.initializationFailed;
    }

    /**
     * Shows information message within a modal view
     * 
     * @param context context to show the message
     * @param informationMessageId information message to show
     */
    public void showInformationMessage(Context context,
            int informationMessageId) {
        showInformationMessage(context, getString(informationMessageId));
    }

	/**
	 * Shows information message within a modal view
	 * 
	 * @param context context to show the message
	 * @param informationMessage information message to show
	 */
	public void showInformationMessage(final Context context, final String informationMessage) {
//	    handler.post(new Runnable() {
//	        public void run() { 
//	            AlertDialog.Builder builder = AlertBuilderFactory.createDialogBuilder(context, AlertTypes.INFO);
//	            builder.setMessage(informationMessage);
                
//                try {
//                    builder.show();
//                } catch (BadTokenException ignored) {
//                }
//	        } 
//	   });
	}

	/**
	 * Shows error message within a modal view
	 * 
     * @param context context to show the message
	 * @param errorMessage error message to show
	 * @param positiveButtonListener a listener for ok button
	 */
	public void showErrorMessage(final Context context, final String errorMessage,
	        final DialogInterface.OnClickListener positiveButtonListener) {
//	    handler.post(new Runnable() {
//            public void run() { 
//                AlertDialog.Builder builder = AlertBuilderFactory.createDialogBuilder(context, AlertTypes.ERROR);
//                builder.setMessage(errorMessage);
//                if (positiveButtonListener != null) {
//                    builder.setPositiveButton(R.string.ok_text, positiveButtonListener);
//                }
//                
//                try {
//                    builder.show();
//                } catch (BadTokenException ignored) {
//                }
//            }
//	    });
	}
	
	/**
     * Shows error message within a modal view
     * 
     * @param context context to show the message
     * @param errorMessage error message to show
     */
    public void showErrorMessage(final Context context, final String errorMessage) {
        showErrorMessage(context, errorMessage, null);
    }
    
    /**
     * Shows error message within a modal view
     * 
     * @param errorMessage error message to show
     */
    public void showErrorMessage(final String errorMessage) {
        showErrorMessage(currentActivity, errorMessage, null);
    }

    /**
     * Shows error message within a modal view
     * 
     * @param context context to show the message
     * @param errorMessageId error message string idto show
     */
    public void showErrorMessage(Context context, int errorMessageId) {
        showErrorMessage(context, getString(errorMessageId));
    }
	
	/**
	 * Shows error message associated to given throwable
	 * 
     * @param context context to show the message
	 * @param throwable error message with associated message to show
	 */
	public void showErrorMessage(Context context, Throwable throwable) {
		showErrorMessage(context, throwable.getLocalizedMessage());
	}
    
    /**
     * Shows error message associated to given throwable
     * 
     * @param throwable error message with associated message to show
     */
    public void showErrorMessage(Throwable throwable) {
        showErrorMessage(currentActivity, throwable.getLocalizedMessage());
    }

	/**
	 * @return the updater
	 */
	public Updater getUpdater() {
		return updater;
	}

	/**
	 * @param updater the updater to set
	 */
	public void setUpdater(Updater updater) {
		this.updater = updater;
	}
	
	/**
	 * Get DB Helper
	 * @return DBHelper the dbHelper
	 */
	public DBHelper getHelper() {
        if (dBHelper == null) {
        	dBHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return dBHelper;
    }
	
	/**
	 * Close DB helper
	 */
	public void closeDBHelper() {
        if (dBHelper != null) {
            OpenHelperManager.releaseHelper();
            dBHelper = null;
        }
    }
	
	/**
	 * Read file session
	 */
	private boolean readFileSession() {
		
		boolean result = true;
		
        File rootPath = android.os.Environment.getExternalStorageDirectory(); 
        File dir = new File (rootPath.getAbsolutePath() + "/" + Constants.NAME_FOLDER_APPLICATION);
        
        try {
			File file = new File(dir, Constants.NAME_FILE_INPUT_CSV);
			if (!file.exists()) {
				result  = false;
			} else {
				InputStream instream = new FileInputStream(file);
				InputStreamReader inputreader = new InputStreamReader(instream);
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line;
	
				// read every line of the file into the line-variable, on line at the time
				HashMap<String, List<String>> listCharged = toolbox.getSession().getChargeUsers();
				String currentClass = "";
				List<String> listUsers = null;
				while ((line = buffreader.readLine()) != null) {
			        // do something with the settings from the file
					if (line.startsWith(" ") || (line.startsWith("\t"))){
						// user
						line = line.trim().replace("\"", "");
						listUsers.add(line);
					} else {
						// class
						currentClass = line.trim().replace("\"", "");
						listUsers = new ArrayList<String>();
						listCharged.put(currentClass, listUsers);
					}
				}
				
			    // close the file again       
			    instream.close();
			}
        } catch (Exception e){
        	// nothing
        }
        
		return result;
	}

}