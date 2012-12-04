package com.signaturemobile.signaturemobile.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.model.JoinAsignatureWithUserDB;
import com.signaturemobile.signaturemobile.model.JoinClassWithUserDB;
import com.signaturemobile.signaturemobile.persistence.JoinAsignatureWithUserSQLite;
import com.signaturemobile.signaturemobile.persistence.SQLiteBaseData;

/**
 * DAOJoinClassWithUserSQL object facade to communication to Join class with user DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class DAOJoinAsignatureWithUserSQL {
	
		/**
		 * Name database
		 */
		private static String NAME_DATABASE = "DAOJoinAsignatureWithUserSQL";
	
		/**
		 * Instance Class SQL Lite
		 */
		private SQLiteBaseData joinAsignatureWithUserDB;
	
		/**
		 * Default constructor
		 * @param contextApplication context application
		 */
		public DAOJoinAsignatureWithUserSQL (Context contextApplication){
	        joinAsignatureWithUserDB = new JoinAsignatureWithUserSQLite(contextApplication, NAME_DATABASE, null, Constants.VERSION_DB_JOIN_USER_ASIGNATURE);
		}
	    
	    /**
	     * Create join class with user and save into database
	     * @param nameAsignature the name class to save database
	     * @param nameUser the name user to save database
	     * @return Create join class with user
	     */
	    public boolean createJoinClassWithUser (String nameAsignature, String nameUser){
	        boolean result = false;
	    	if ((nameAsignature != null) && (nameUser != null)) {
		    	// Open database in mode write
		        SQLiteDatabase db = joinAsignatureWithUserDB.getWritableDatabase();
		        
		        if(db != null) {
	                db.execSQL("INSERT INTO JoinAsignatureWithUserSQLite (code, nameAsignature, username) " +
	                           "VALUES (" + 1 + ", '" + nameAsignature + "', '" + nameUser + "')");
		            db.close();
		            result = true;
		        }
	        }
	    	
	    	return result;
	    }
	    
		/**
		 * Search class from name class
		 * @param name class user 
		 * @return the class db result or null if not find
		 */
		public JoinAsignatureWithUserDB searchAsignatureFromNameAsignature(String nameAsignatureUser){
			JoinAsignatureWithUserDB joinAsignatureWithUserDbResult = null;
			
			if (nameAsignatureUser != null){
				String[] registers = new String[] {"nameAsignature", "username"};
		        String[] args = new String[] {nameAsignatureUser};
		    	
		        // Open database in mode write
		        SQLiteDatabase db = joinAsignatureWithUserDB.getWritableDatabase();
		        
		        Cursor c = db.query("JoinAsignatureWithUserSQLite", registers, "nameAsignature=?", args, null, null, null);
		         
		        if (c.moveToFirst()) {
		             do {
		                  String nameAsignature = c.getString(0);
		                  String username = c.getString(1);

		                  joinAsignatureWithUserDbResult = new JoinAsignatureWithUserDB(nameAsignature, username);
		             } while(c.moveToNext());
		        } else {
		        	joinAsignatureWithUserDbResult = null;
		        }
		        // close connection db
	            db.close();
			} else {
				joinAsignatureWithUserDbResult = null;
			}
			
			return joinAsignatureWithUserDbResult;
		}
	    
	    /**
	     * Delete join class with user database
	     * @param joinClassWithUserDBObject the class DB to save database
	     * @return If the user has been deleted or not
	     */
	    public boolean deleteJoinClassWithUser (JoinClassWithUserDB joinClassWithUserDBObject){
	        boolean result = false;
	    	if (joinClassWithUserDBObject != null) {
	    		String className = joinClassWithUserDBObject.getNameClass();
		    	// Open database in mode write
		        String[] args = new String[] {className};
		        SQLiteDatabase db = joinAsignatureWithUserDB.getWritableDatabase();

		        if(db != null) {
		        	db.delete(NAME_DATABASE, "nameAsignature=? AND ", args);
	                db.close();
		            result = true;
		        }
	        }
	    	
	    	return result;
	    }
		
		/**
		 * List join class with user
		 * @return the list fill with class all
		 */
		public List<JoinAsignatureWithUserDB> listJoinAsignatureWithUser(String asignature){
			List<JoinAsignatureWithUserDB> listDbResult = new ArrayList<JoinAsignatureWithUserDB>();
			String[] registers = new String[] {"nameAsignature", "username"};
	        String[] args = new String[] {asignature};

	        // Open database in mode write
	        SQLiteDatabase db = joinAsignatureWithUserDB.getWritableDatabase();
	        Cursor c = db.query("JoinAsignatureWithUserSQLite", registers, "nameAsignature=?", args, null, null, null);
	         
	        if (c.moveToFirst()) {
	             do {
	                  String nameAsignature = c.getString(0);
	                  String numberStudents = c.getString(1);
	                  JoinAsignatureWithUserDB asignatureDbResult = new JoinAsignatureWithUserDB(nameAsignature, numberStudents);
	                  listDbResult.add(asignatureDbResult);
	             } while(c.moveToNext());
	        }
	        // close connection db
            db.close();
			
			return listDbResult;
		}
		
}
