package com.signaturemobile.signaturemobile.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.model.AsignatureDB;
import com.signaturemobile.signaturemobile.persistence.AsignatureSQLite;
import com.signaturemobile.signaturemobile.persistence.SQLiteBaseData;

/**
 * DAOClassSQL object facade to communication to Class DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class DAOAsignatureSQL {
		
		/**
		 * Instance Asignature SQL Lite
		 */
		private SQLiteBaseData asignatureDB;
	
		/**
		 * Default constructor
		 * @param contextApplication context application
		 */
		public DAOAsignatureSQL (Context contextApplication){
	        asignatureDB = new AsignatureSQLite(contextApplication, "DBAsignature", null, Constants.VERSION_DB_ASIGNATURE);
		}
	    
	    /**
	     * Create class and save into database
	     * @param nameAsignature the name class to save database
	     * @param numberStudents the number students
	     * @return Create class
	     */
	    public boolean createClass (String nameAsignature, String numberStudents){
	        boolean result = false;
	    	if ((nameAsignature != null) && (numberStudents != null)) {
		    	// Open database in mode write
		        SQLiteDatabase db = asignatureDB.getWritableDatabase();
		        
		        if(db != null) {
	                db.execSQL("INSERT INTO Asignature (code, nameAsignature, numberStudents) " +
	                           "VALUES (" + 1 + ", '" + nameAsignature + "', '" + numberStudents + "')");
		            db.close();
		            result = true;
		        }
	        }
	    	
	    	return result;
	    }
	    
		/**
		 * Search asignature from name asignature
		 * @param name class user 
		 * @return the class db result or null if not find
		 */
		public AsignatureDB searchClassFromNameClass(String nameAsignatureUser){
			AsignatureDB classDbResult = null;
			
			if (nameAsignatureUser != null){
				String[] registers = new String[] {"nameAsignature", "numberStudents"};
		        String[] args = new String[] {nameAsignatureUser};
		    	
		        // Open database in mode write
		        SQLiteDatabase db = asignatureDB.getWritableDatabase();
		        
		        Cursor c = db.query("Asignature", registers, "nameAsignature=?", args, null, null, null);
		         
		        if (c.moveToFirst()) {
		             //Recorremos el cursor hasta que no haya más registros
		             do {
		                  String nameAsignature = c.getString(0);
		                  String numberStudents = c.getString(1);
		                  int numberStudentsInt = 0;
		                  try {
		                	  numberStudentsInt = Integer.parseInt(numberStudents);
		                  } catch (Throwable e){
		                	  // nothing
		                  }
		                  
		                  classDbResult = new AsignatureDB(nameAsignature, numberStudentsInt);
		             } while(c.moveToNext());
		        } else {
		        	classDbResult = null;
		        }
		        // close connection db
	            db.close();
			} else {
				classDbResult = null;
			}
			
			return classDbResult;
		}
	    
	    /**
	     * Delete asignature into database
	     * @param asignatureDBObject the asignature DB to save database
	     * @return If the user has been deleted or not
	     */
	    public boolean deleteAsignature (AsignatureDB asignatureDBObject){
	        boolean result = false;
	    	if (asignatureDBObject != null) {
	    		String className = asignatureDBObject.getNameAsignature();
		    	// Open database in mode write
		        String[] args = new String[] {className};
		        SQLiteDatabase db = asignatureDB.getWritableDatabase();

		        if(db != null) {
		        	db.delete("Asignature", "nameAsignature=?", args);
	                db.close();
		            result = true;
		        }
	        }
	    	
	    	return result;
	    }
		
		/**
		 * Search device from to the mac
		 * @param numberStudents value mac
		 * @return the user db result or null if not find
		 */
		public boolean updateStudentsFromNameAsignature(String nameAsignature, int numberStudents){
			boolean result = false;
			
			if (nameAsignature != null){
		        String[] args = new String[] {nameAsignature};
		    	
		        // Open database in mode write
		        SQLiteDatabase db = asignatureDB.getWritableDatabase();
		        
		        ContentValues contentValue = new ContentValues();
		        contentValue.put("numberStudents", String.valueOf(numberStudents));
        		int numberRows = db.update("Asignature", contentValue, "nameAsignature=?", args);
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
		 * List asignature
		 * @return the list fill with asignature all
		 */
		public List<AsignatureDB> listAsignature(){
			List<AsignatureDB> listDbResult = new ArrayList<AsignatureDB>();
			String[] registers = new String[] {"nameAsignature", "numberStudents"};
	    	
	        // Open database in mode write
	        SQLiteDatabase db = asignatureDB.getWritableDatabase();
	        
	        Cursor c = db.query("Asignature", registers, null, null, null, null, null);
	         
	        if (c.moveToFirst()) {
	             //Recorremos el cursor hasta que no haya más registros
	             do {
	                  String nameAsignature = c.getString(0);
	                  String numberStudents = c.getString(1);
	                  int numberStudentsInt = 0;
	                  try {
	                	  numberStudentsInt = Integer.parseInt(numberStudents);
	                  } catch (Throwable e){
	                	  // nothing
	                  }
	                  AsignatureDB asignatureDbResult = new AsignatureDB(nameAsignature, numberStudentsInt);
	                  listDbResult.add(asignatureDbResult);
	                  
	             } while(c.moveToNext());
	        }
	        // close connection db
            db.close();
			
			return listDbResult;
		}
		
}
