package com.signaturemobile.signaturemobile.io;

import java.util.Hashtable;
import java.util.Vector;

/**
 * NotificationCenter send notifications to registered listeners
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class NotificationCenter {
    
    private Hashtable<String, Vector<NotificationListener>> registry;
    
    /**
     * Default constructor
     */
    public NotificationCenter() {
        registry = new Hashtable<String, Vector<NotificationListener>>();
    }

    /**
     * Register a new listener
     * @param notification the notification name
     * @param listener the notification listener
     */
    public synchronized void registerListener(String notification, NotificationListener listener) {
        Vector<NotificationListener> listeners = registry.get(notification);
        if (listeners != null) {
            if (!listeners.contains(listener)) {
                listeners.addElement(listener);
            }
        } else {
            listeners = new Vector<NotificationListener>();
            listeners.addElement(listener);
            registry.put(notification, listeners);
        }
    }
    
    /**
     * Unregister a previously registered listener
     * @param notification the notification
     * @param listener the listeners
     */
    public synchronized void unregisterListener(String notification, NotificationListener listener) {
        Vector<NotificationListener> listeners = registry.get(notification);
        if (listeners != null) {
            if (listeners.contains(listener)) {
                listeners.removeElement(listener);
            }
        }
    }
    
    /**
     * Post a notification to notification listener
     * @param notification the notification name
     * @param info the notification info
     */
    public void postNotification(String notification, Object info) {
        Vector<NotificationListener> listeners = registry.get(notification);
        if (listeners != null) {
        	NotificationListener listener;
            int size = listeners.size();
            for(int i = 0; i < size; i++) {
                listener = (NotificationListener) listeners.elementAt(i);
                if (listener != null) {
                    listener.notificationPosted(notification, info);
                }
            }
        }
    }
    
    /**
     * NotificationListener must be implemented by those interested in notification listening 
     */
    public interface NotificationListener {
        
        /**
         * Receives a notification
         * @param notification the notification name
         * @param info the notification info
         */
        public void notificationPosted(String notification, Object info);
        
    }

}

