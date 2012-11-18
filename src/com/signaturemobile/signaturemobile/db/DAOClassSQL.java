package com.signaturemobile.signaturemobile.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.model.ClassDB;
import com.signaturemobile.signaturemobile.persistence.ClassSQLite;
import com.signaturemobile.signaturemobile.persistence.SQLiteBaseData;

/**
 * DAOClassSQL object facade to communication to Class DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class DAOClassSQL {
		
		/**
		 * Instance Class SQL Lite
		 */
		private SQLiteBaseData classDB;
	
		/**
		 * Default constructor
		 * @param contextApplication context application
		 */
		public DAOClassSQL (Context contextApplication){
	        classDB = new ClassSQLite(contextApplication, "DBClass", null, Constants.VERSION_DB_CLASS);
		}
	    
	    /**
	     * Create class and save into database
	     * @param nameClass the name class to save database
	     * @param numberStudents the number students
	     * @return Create class
	     */
	    public boolean createClass (String nameClass, String numberStudents){
	        boolean result = false;
	    	if ((nameClass != null) && (numberStudents != null)) {
		    	// Open database in mode write
		        SQLiteDatabase db = classDB.getWritableDatabase();
		        
		        if(db != null) {
	                db.execSQL("INSERT INTO Class (code, nameClass, numberStudents) " +
	                           "VALUES (" + 1 + ", ' " + nameClass + "', '" + numberStudents + "')");
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
		public ClassDB searchClassFromNameClass(String nameClassUser){
			ClassDB classDbResult = null;
			
			if (nameClassUser != null){
				String[] registers = new String[] {"nameClass", "numberStudents"};
		        String[] args = new String[] {nameClassUser};
		    	
		        // Open database in mode write
		        SQLiteDatabase db = classDB.getWritableDatabase();
		        
		        Cursor c = db.query("Class", registers, "nameClass=?", args, null, null, null);
		         
		        if (c.moveToFirst()) {
		             //Recorremos el cursor hasta que no haya más registros
		             do {
		                  String nameClass = c.getString(0);
		                  String numberStudents = c.getString(1);
		                  int numberStudentsInt = 0;
		                  try {
		                	  numberStudentsInt = Integer.parseInt(numberStudents);
		                  } catch (Throwable e){
		                	  // nothing
		                  }
		                  
		                  classDbResult = new ClassDB(nameClass, numberStudentsInt);
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
	     * Delete user into database
	     * @param classDB the class DB to save database
	     * @return If the user has been deleted or not
	     */
	    public boolean deleteClass (ClassDB classDBObject){
	        boolean result = false;
	    	if (classDBObject != null) {
	    		String className = classDBObject.getNameClass();
		    	// Open database in mode write
		        String[] args = new String[] {className};
		        SQLiteDatabase db = classDB.getWritableDatabase();

		        if(db != null) {
		        	db.delete("Class", "nameClass=?", args);
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
		public boolean updateStudentsFromNameClass(String nameClass, int numberStudents){
			boolean result = false;
			
			if (nameClass != null){
		        String[] args = new String[] {nameClass};
		    	
		        // Open database in mode write
		        SQLiteDatabase db = classDB.getWritableDatabase();
		        
		        ContentValues contentValue = new ContentValues();
		        contentValue.put("numberStudents", String.valueOf(numberStudents));
        		int numberRows = db.update("Class", contentValue, "nameClass=?", args);
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
		 * List class
		 * @return the list fill with class all
		 */
		public List<ClassDB> listClass(){
			List<ClassDB> listDbResult = new ArrayList<ClassDB>();
			String[] registers = new String[] {"nameClass", "numberStudents"};
	    	
	        // Open database in mode write
	        SQLiteDatabase db = classDB.getWritableDatabase();
	        
	        Cursor c = db.query("Class", registers, null, null, null, null, null);
	         
	        if (c.moveToFirst()) {
	             //Recorremos el cursor hasta que no haya más registros
	             do {
	                  String nameClass = c.getString(0);
	                  String numberStudents = c.getString(1);
	                  int numberStudentsInt = 0;
	                  try {
	                	  numberStudentsInt = Integer.parseInt(numberStudents);
	                  } catch (Throwable e){
	                	  // nothing
	                  }
	                  ClassDB classDbResult = new ClassDB(nameClass, numberStudentsInt);
	                  listDbResult.add(classDbResult);
	                  
	             } while(c.moveToNext());
	        }
	        // close connection db
            db.close();
			
			return listDbResult;
		}
		
}
