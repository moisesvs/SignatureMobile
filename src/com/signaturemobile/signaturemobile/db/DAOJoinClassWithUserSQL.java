package com.signaturemobile.signaturemobile.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.model.JoinClassWithUserDB;
import com.signaturemobile.signaturemobile.persistence.JoinClassWithUserSQLite;
import com.signaturemobile.signaturemobile.persistence.SQLiteBaseData;

/**
 * DAOJoinClassWithUserSQL object facade to communication to Join class with user DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class DAOJoinClassWithUserSQL {
		
		/**
		 * Instance Class SQL Lite
		 */
		private SQLiteBaseData joinClassWithUserDB;
	
		/**
		 * Default constructor
		 * @param contextApplication context application
		 */
		public DAOJoinClassWithUserSQL (Context contextApplication){
	        joinClassWithUserDB = new JoinClassWithUserSQLite(contextApplication, "DAOJoinClassWithUserSQL", null, Constants.VERSION_DB_JOIN_USER_CLASS);
		}
	    
	    /**
	     * Create join class with user and save into database
	     * @param nameClass the name class to save database
	     * @param nameUser the name user to save database
	     * @return Create join class with user
	     */
	    public boolean createJoinClassWithUser (String nameClass, String nameUser){
	        boolean result = false;
	    	if ((nameClass != null) && (nameUser != null)) {
		    	// Open database in mode write
		        SQLiteDatabase db = joinClassWithUserDB.getWritableDatabase();
		        
		        if(db != null) {
	                db.execSQL("INSERT INTO JoinClassWithUser (code, nameClass, username) " +
	                           "VALUES (" + 1 + ", '" + nameClass + "', '" + nameUser + "')");
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
		public JoinClassWithUserDB searchClassFromNameClass(String nameClassUser){
			JoinClassWithUserDB joinClassWithUserDbResult = null;
			
			if (nameClassUser != null){
				String[] registers = new String[] {"nameClass", "username"};
		        String[] args = new String[] {nameClassUser};
		    	
		        // Open database in mode write
		        SQLiteDatabase db = joinClassWithUserDB.getWritableDatabase();
		        
		        Cursor c = db.query("JoinClassWithUser", registers, "nameClass=?", args, null, null, null);
		         
		        if (c.moveToFirst()) {
		             do {
		                  String nameClass = c.getString(0);
		                  String username = c.getString(1);

		                  joinClassWithUserDbResult = new JoinClassWithUserDB(nameClass, username);
		             } while(c.moveToNext());
		        } else {
		        	joinClassWithUserDbResult = null;
		        }
		        // close connection db
	            db.close();
			} else {
				joinClassWithUserDbResult = null;
			}
			
			return joinClassWithUserDbResult;
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
		        SQLiteDatabase db = joinClassWithUserDB.getWritableDatabase();

		        if(db != null) {
		        	db.delete("JoinClassWithUser", "nameClass=? AND ", args);
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
		public List<JoinClassWithUserDB> listJoinClassWithUser(){
			List<JoinClassWithUserDB> listDbResult = new ArrayList<JoinClassWithUserDB>();
			String[] registers = new String[] {"nameClass", "username"};
	    	
	        // Open database in mode write
	        SQLiteDatabase db = joinClassWithUserDB.getWritableDatabase();
	        Cursor c = db.query("JoinClassWithUser", registers, null, null, null, null, null);
	         
	        if (c.moveToFirst()) {
	             do {
	                  String nameAsignature = c.getString(0);
	                  String numberStudents = c.getString(1);
	                  JoinClassWithUserDB classDbResult = new JoinClassWithUserDB(nameAsignature, numberStudents);
	                  listDbResult.add(classDbResult);
	             } while(c.moveToNext());
	        }
	        // close connection db
            db.close();
			
			return listDbResult;
		}
		
}
