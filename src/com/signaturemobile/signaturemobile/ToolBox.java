package com.signaturemobile.signaturemobile;

import java.io.File;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.signaturemobile.signaturemobile.db.DAOUserSQL;
import com.signaturemobile.signaturemobile.io.BluetoohInvoker;
import com.signaturemobile.signaturemobile.io.NotificationCenter;
import com.signaturemobile.signaturemobile.io.Updater;
import com.signaturemobile.signaturemobile.persistence.Persistence;

/**
 * ToolBox provides application tools
 *  
 * @author <a href="mailto:moisesvs@gmail.com">Mooisés Vázquez Sánchez</a>
 */
public class ToolBox {

	/**
     * The application
     */
    private SignatureMobileApplication application;
    
    /**
     * The session
     */
    private Session session;
    
    /**
     * The updater
     */
    private Updater updater;
    
    /**
     * The notification center
     */
    private NotificationCenter notificationCenter;
    
    /**
     * The persistence
     */
    private Persistence persistence;
    
    /**
     * The dao user sql
     */
    private DAOUserSQL daoUserSQL;
    
    /**
     * Invoker bluetooh
     */
    private BluetoohInvoker bluetoohInvoker;
    
    /**
	 * Application display
	 */
	private Display display;
    
    /**
     * Default constructor
     */
    public ToolBox() {
    	// Nothing
    }

    /**
     * Setup the tool box
     * @param application the application to set
     * @param session the session to set
     * @param updater the updater to set
     * @param notificationCenter the notification to set
     * @param favoritesManager 
     * @param alertBuilderFactory 
     */
    public void setup(SignatureMobileApplication application, Updater updater, Session session, BluetoohInvoker bluetoohInvoker, NotificationCenter notificationCenter,
    		Persistence persistence, DAOUserSQL daoUserSQL) {
        this.application = application;
        this.updater = updater;
        this.bluetoohInvoker = bluetoohInvoker;
        this.session = session;
        this.notificationCenter = notificationCenter;
        this.persistence = persistence;
        this.daoUserSQL = daoUserSQL;

    	this.display = ((WindowManager)application.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    /**
     * Gets the application
     * @return the application
     */
    public SignatureMobileApplication getApplication() {
        return application;
    }

    /**
     * Gets the session
     * @return the session
     */
    public Session getSession() {
        return session;
    }
    
    /**
     * Notification center
     */
    public NotificationCenter getNotificationCenter(){
    	return notificationCenter;
    }
    
    /**
     * Get the persistence 
     * @return the persistence
     */
    public Persistence getPersistence() {
    	return persistence;
    }

	/**
	 * Gets application display
	 * 
	 * @return aplication display
	 */
	public Display getApplicationDisplay() {
		return display;
	}
	
	/**
     * Returns the cache directory File Object
     * 
     * @return The cache directory File Object
     */
    public File getCacheDirectory() {
        return SignatureMobileApplication.getInstance().getApplicationContext().getCacheDir();
    }

	/**
	 * @return the bluetoohInvoker
	 */
	public BluetoohInvoker getBluetoohInvoker() {
		return bluetoohInvoker;
	}

	/**
	 * @param bluetoohInvoker the bluetoohInvoker to set
	 */
	public void setBluetoohInvoker(BluetoohInvoker bluetoohInvoker) {
		this.bluetoohInvoker = bluetoohInvoker;
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
	 * @return the daoUserSQL
	 */
	public DAOUserSQL getDaoUserSQL() {
		return daoUserSQL;
	}

	/**
	 * @param daoUserSQL the daoUserSQL to set
	 */
	public void setDaoUserSQL(DAOUserSQL daoUserSQL) {
		this.daoUserSQL = daoUserSQL;
	}
} 
