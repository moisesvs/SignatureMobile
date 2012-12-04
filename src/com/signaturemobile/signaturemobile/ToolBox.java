package com.signaturemobile.signaturemobile;

import java.io.File;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.signaturemobile.signaturemobile.db.DAOAsignatureSQL;
import com.signaturemobile.signaturemobile.db.DAOClassSQL;
import com.signaturemobile.signaturemobile.db.DAOJoinAsignatureWithUserSQL;
import com.signaturemobile.signaturemobile.db.DAOJoinClassWithUserSQL;
import com.signaturemobile.signaturemobile.db.DAOUserSQL;
import com.signaturemobile.signaturemobile.io.BluetoohInvoker;
import com.signaturemobile.signaturemobile.io.NotificationCenter;
import com.signaturemobile.signaturemobile.io.Updater;

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
     * The dao user sql
     */
    private DAOUserSQL daoUserSQL;
    
    /**
     * The dao asignature sql
     */
    private DAOAsignatureSQL daoAsignatureSQL;
    
    /**
     * The dao class sql
     */
    private DAOClassSQL daoClassSQL;

	/**
     * The dao join asignature with user sql
     */
    private DAOJoinAsignatureWithUserSQL daoJoinAsignatureWithUser;
    
    /**
     * The dao join class with user sql
     */
    private DAOJoinClassWithUserSQL daoJoinClassWithUser;
    
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
    		DAOUserSQL daoUserSQL, DAOAsignatureSQL daoAsignatureSQL, DAOClassSQL daoClassSQL, DAOJoinAsignatureWithUserSQL daoJoinAsignatureWithUser, DAOJoinClassWithUserSQL daoJoinClassWithUserSQL) {
        this.application = application;
        this.updater = updater;
        this.bluetoohInvoker = bluetoohInvoker;
        this.session = session;
        this.notificationCenter = notificationCenter;
        this.daoUserSQL = daoUserSQL;
        this.daoAsignatureSQL = daoAsignatureSQL;
        this.daoClassSQL = daoClassSQL;
        this.daoJoinAsignatureWithUser = daoJoinAsignatureWithUser;
        this.daoJoinClassWithUser = daoJoinClassWithUserSQL;

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

	/**
	 * @return the daoAsignatureSQL
	 */
	public DAOAsignatureSQL getDaoAsignatureSQL() {
		return daoAsignatureSQL;
	}

	/**
	 * @param daoAsignatureSQL the daoAsignatureSQL to set
	 */
	public void setDaoAsignatureSQL(DAOAsignatureSQL daoAsignatureSQL) {
		this.daoAsignatureSQL = daoAsignatureSQL;
	}

	/**
	 * @param daoClassSQL the daoClassSQL to set
	 */
	public void setDaoClassSQL(DAOClassSQL daoClassSQL) {
		this.daoClassSQL = daoClassSQL;
	}

	/**
	 * @return the daoJoinAsignatureWithUser
	 */
	public DAOJoinAsignatureWithUserSQL getDaoJoinAsignatureWithUser() {
		return daoJoinAsignatureWithUser;
	}

	/**
	 * @param daoJoinAsignatureWithUser the daoJoinAsignatureWithUser to set
	 */
	public void setDaoJoinAsignatureWithUser(DAOJoinAsignatureWithUserSQL daoJoinAsignatureWithUser) {
		this.daoJoinAsignatureWithUser = daoJoinAsignatureWithUser;
	}

	/**
	 * @return the daoJoinClassWithUser
	 */
	public DAOJoinClassWithUserSQL getDaoJoinClassWithUser() {
		return daoJoinClassWithUser;
	}

	/**
	 * @param daoJoinClassWithUser the daoJoinClassWithUser to set
	 */
	public void setDaoJoinClassWithUser(DAOJoinClassWithUserSQL daoJoinClassWithUser) {
		this.daoJoinClassWithUser = daoJoinClassWithUser;
	}
    
    /**
	 * @return the daoClassSQL
	 */
	public DAOClassSQL getDaoClassSQL() {
		return daoClassSQL;
	}
} 
