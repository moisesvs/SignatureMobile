package com.signaturemobile.signaturemobile.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.model.UserDB;
import com.signaturemobile.signaturemobile.persistence.SQLiteBaseData;
import com.signaturemobile.signaturemobile.persistence.UserSQLite;
import com.signaturemobile.signaturemobile.utils.Tools;

/**
 * DAOUserSQL object facade to communication to User DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class DAOUserSQL {
		
		/**
		 * Instance User SQL Lite
		 */
		private SQLiteBaseData userDB;
	
		/**
		 * Default constructor
		 * @param contextApplication context application
		 */
		public DAOUserSQL (Context contextApplication){
	        userDB = new UserSQLite(contextApplication, "DBUser", null, Constants.VERSION_DB_USER);
		}
	    
	    /**
	     * Create user and save into database
	     * @param username the username to save database
	     * @param password the password user to save database
	     * @param userTwitter the user twitter to save database
	     * @param mac the mac device bluetooth save database
	     * @param tickets the tickets user device bluetooth save database
	     * @param tokenNFC the token NFC
	     * @return If the user has been created or not
	     */
	    public boolean createUser (String username, String password, String userTwitter, String mac, String tickets, Date dateCreateUser, Date lastSignUser, String tokenNFC){
	        boolean result = false;
	    	if ((username != null) && (password != null) && (userTwitter != null) && (mac != null) && (tickets != null)
	        		&& (dateCreateUser != null) && (lastSignUser != null)) {
		    	// Open database in mode write
		        SQLiteDatabase db = userDB.getWritableDatabase();
		        
		        if(db != null) {
	                db.execSQL("INSERT INTO Users (code, username, password, usertwitter, mac, tickets, dateCreateUser, dateLastRegister, tokenNfc) " +
	                           "VALUES (" + 1 + ", ' " + username + "', '" + password + "', '" + userTwitter + "', '" + mac + "', '" 
	                		+ tickets + "', '" + dateCreateUser.getTime() + "', '" + 0 + "', '" + tokenNFC + "')");
		            db.close();
		            result = true;
		        }
	        }
	    	
	    	return result;
	    }
	    
		/**
		 * Search device from to the mac
		 * @param mac value mac
		 * @return the user db result or null if not find
		 */
		public UserDB searchUserDeviceMAC(String mac){
			UserDB userDbResult = null;
			
			if (mac != null){
				String[] registers = new String[] {"username", "password", "usertwitter", "mac", "tickets", "dateCreateUser, dateLastRegister, tokenNfc"};
		        String[] args = new String[] {mac};
		    	
		        // Open database in mode write
		        SQLiteDatabase db = userDB.getWritableDatabase();
		        
		        Cursor c = db.query("Users", registers, "mac=?", args, null, null, null);
		         
		        if (c.moveToFirst()) {
		             //Recorremos el cursor hasta que no haya más registros
		             do {
		                  String username = c.getString(0);
		                  String password = c.getString(1);
		                  String userTwitter = c.getString(2);
		                  String macUser = c.getString(3);
		                  String tickets = c.getString(4);
		                  long dateCreateUser = c.getLong(5);
		                  long dateLastSignUser = c.getLong(6);
		                  String tokenNFC = c.getString(7);
		                  userDbResult = new UserDB(username, password, userTwitter, macUser, tickets, new Date(dateCreateUser), new Date (dateLastSignUser), tokenNFC);
		                  
		             } while(c.moveToNext());
		        } else {
		        	userDbResult = null;
		        }
		        // close connection db
	            db.close();
			} else {
				userDbResult = null;
			}
			
			return userDbResult;
		}
	    
		/**
		 * Search device from token nfc
		 * @param token nfc 
		 * @return the user db result or null if not find
		 */
		public UserDB searchUserDeviceTokenNFC(String tokenNFC){
			UserDB userDbResult = null;
			
			if (tokenNFC != null){
				String[] registers = new String[] {"username", "password", "usertwitter", "mac", "tickets", "dateCreateUser, dateLastRegister, tokenNFC"};
		        String[] args = new String[] {tokenNFC};
		    	
		        // Open database in mode write
		        SQLiteDatabase db = userDB.getWritableDatabase();
		        
		        Cursor c = db.query("Users", registers, "tokenNFC=?", args, null, null, null);
		         
		        if (c.moveToFirst()) {
		             //Recorremos el cursor hasta que no haya más registros
		             do {
		                  String username = c.getString(0);
		                  String password = c.getString(1);
		                  String userTwitter = c.getString(2);
		                  String macUser = c.getString(3);
		                  String tickets = c.getString(4);
		                  long dateCreateUser = c.getLong(5);
		                  long dateLastSignUser = c.getLong(6);
		                  String tokenNFCUser = c.getString(7);
		                  
		                  userDbResult = new UserDB(username, password, userTwitter, macUser, tickets, new Date(dateCreateUser), new Date (dateLastSignUser), tokenNFCUser);
		                  
		             } while(c.moveToNext());
		        } else {
		        	userDbResult = null;
		        }
		        // close connection db
	            db.close();
			} else {
				userDbResult = null;
			}
			
			return userDbResult;
		}
	    
	    /**
	     * Delete user into database
	     * @param username the username to save database
	     * @param password the password user to save database
	     * @param userTwitter the user twitter to save database
	     * @param mac the mac device bluetooth save database
	     * @param tickets the tickets user device bluetooth save database
	     * @param tokenNFC the token NFC
	     * @return If the user has been created or not
	     */
	    public boolean deleteUser (UserDB userDBParam){
	        boolean result = false;
	    	if (userDBParam != null) {
	    		String mac = userDBParam.getMac();
		    	// Open database in mode write
		        String[] args = new String[] {mac};
		        SQLiteDatabase db = userDB.getWritableDatabase();

		        if(db != null) {
		        	db.delete("Users", "mac=?", args);
	                db.close();
		            result = true;
		        }
	        }
	    	
	    	return result;
	    }
		
		/**
		 * Search device from to the mac
		 * @param mac value mac
		 * @return the user db result or null if not find
		 */
		public boolean updateTicketsFromMAC(String mac, int ticketsSet){
			boolean result = false;
			
			if (mac != null){
		        String[] args = new String[] {mac};
		    	
		        // Open database in mode write
		        SQLiteDatabase db = userDB.getWritableDatabase();
		        
		        ContentValues contentValue = new ContentValues();
		        contentValue.put("tickets", String.valueOf(ticketsSet));
		        contentValue.put("dateLastRegister", String.valueOf(Tools.createDateOnlyToday().getTime()));
        		int numberRows = db.update("Users", contentValue, "mac=?", args);
		        if (numberRows != 0) {
		        	result = true;
		        } else {
		        	result = false;
		        }
		        // close connection db
	            db.close();
			} else {
				result = false;
			}
			
			return result;
		}
		
		/**
		 * Search device from to the mac
		 * @param date value date
		 * @return the list fill with user db that sign in date parameter
		 */
		public List<UserDB> listFromDate(){
			List<UserDB> listDbResult = new ArrayList<UserDB>();
			String[] registers = new String[] {"username", "password", "usertwitter", "mac", "tickets", "dateCreateUser, dateLastRegister, tokenNFC"};
	    	
	        // Open database in mode write
	        SQLiteDatabase db = userDB.getWritableDatabase();
	        
	        Cursor c = db.query("Users", registers, null, null, null, null, null);
	         
	        if (c.moveToFirst()) {
	             //Recorremos el cursor hasta que no haya más registros
	             do {
	                  String username = c.getString(0);
	                  String password = c.getString(1);
	                  String userTwitter = c.getString(2);
	                  String macUser = c.getString(3);
	                  String tickets = c.getString(4);
	                  long dateCreateUser = c.getLong(5);
	                  long dateLastSignUser = c.getLong(6);
	                  String tokenNFC = c.getString(7);
	                  
	                  UserDB userDbResult = new UserDB(username, password, userTwitter, macUser, tickets, new Date(dateCreateUser), new Date (dateLastSignUser), tokenNFC);
	                  listDbResult.add(userDbResult);
	                  
	             } while(c.moveToNext());
	        }
	        // close connection db
            db.close();
			
			return listDbResult;
		}
	    
}
