package com.signaturemobile.signaturemobile.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.SignatureMobileApplication;
import com.signaturemobile.signaturemobile.model.AsignatureDB;
import com.signaturemobile.signaturemobile.model.ClassDB;
import com.signaturemobile.signaturemobile.model.JoinAsignatureWithUserDB;
import com.signaturemobile.signaturemobile.model.JoinClassWithUserDB;

/**
 * DAOJoinClassWithUserSQL object facade to communication to Join class with user DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class DAOJoinAsignatureWithUserSQL {
	
		/**
		 * Context application
		 */
		private SignatureMobileApplication application;
	
		/**
		 * Default constructor
		 * @param contextApplication context application
		 */
		public DAOJoinAsignatureWithUserSQL (Context contextApplication){
			this.application = (SignatureMobileApplication) contextApplication;
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
	    		DBHelper helper = application.getHelper();
		        if(helper != null) {
		        	try {
		        		JoinAsignatureWithUserDB classDb = new JoinAsignatureWithUserDB(nameAsignature, nameUser);
		        		helper.getJoinAsignatureWithUser().create(classDb);
		        		result = true;
		        	} catch (Exception e) {
		        		result = false;
		        	} finally {
		        		// close
		        		application.getHelper().close();
		        	}

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
			JoinAsignatureWithUserDB joinDbResult = null;
			if (nameAsignatureUser != null){
	            try {
	            	Dao<JoinAsignatureWithUserDB, Integer> dao = application.getHelper().getJoinAsignatureWithUser();
	                QueryBuilder <JoinAsignatureWithUserDB, Integer> queryBuilder = dao.queryBuilder();
	                queryBuilder.setWhere(queryBuilder.where().eq(JoinAsignatureWithUserDB.NAME_ASIGNATURE, nameAsignatureUser));
	                List<JoinAsignatureWithUserDB> classDb = dao.query(queryBuilder.prepare());
	                if (classDb.isEmpty()) {
	    				joinDbResult = null;
	                } else {
	                	joinDbResult = classDb.get(0);
	                }
	            } catch (Exception e) {
					joinDbResult = null;
	            } finally {
	        		// close
	            	application.getHelper().close();
	        	}
	            
			} else {
				joinDbResult = null;
			}
			
			return joinDbResult;
		}
	    
	    /**
	     * Delete join class with user database
	     * @param joinClassWithUserDBObject the class DB to save database
	     * @return If the user has been deleted or not
	     */
	    public boolean deleteJoinClassWithUser (JoinAsignatureWithUserDB joinAsignatureWithUserDBObject){
	        boolean result = false;
	    	if (joinAsignatureWithUserDBObject != null) {
		        try {
	        		Dao <JoinAsignatureWithUserDB, Integer> dao = application.getHelper().getJoinAsignatureWithUser();
	        		dao.delete(joinAsignatureWithUserDBObject);
		        } catch (Exception e) {
		        	result = false;
		        } finally {
	        		// close
	            	application.getHelper().close();
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
            
			try {
            	Dao<JoinAsignatureWithUserDB, Integer> dao = application.getHelper().getJoinAsignatureWithUser();
                QueryBuilder <JoinAsignatureWithUserDB, Integer> queryBuilder = dao.queryBuilder();
                listDbResult = dao.query(queryBuilder.prepare());
            } catch (Exception e) {
            	listDbResult = null;
            } finally {
        		// close
            	application.getHelper().close();
        	}
	            
			return listDbResult;
		}
		
}
