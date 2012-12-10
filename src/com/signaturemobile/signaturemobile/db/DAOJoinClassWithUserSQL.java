package com.signaturemobile.signaturemobile.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.signaturemobile.signaturemobile.SignatureMobileApplication;
import com.signaturemobile.signaturemobile.model.JoinClassWithUserDB;

/**
 * DAOJoinClassWithUserSQL object facade to communication to Join class with user DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class DAOJoinClassWithUserSQL {
		
		/**
		 * Context application
		 */
		private SignatureMobileApplication application;
		
		/**
		 * Default constructor
		 * @param contextApplication context application
		 */
		public DAOJoinClassWithUserSQL (Context contextApplication){
			this.application = (SignatureMobileApplication) contextApplication;
		}
	    
	    /**
	     * Create join class with user and save into database
	     * @param nameClass the name class to save database
	     * @param nameUser the name user to save database
	     * @param mac user to save database
	     * @return Create join class with user
	     */
	    public boolean createJoinClassWithUser (String nameClass, String nameUser, String mac){
	        boolean result = false;
	    	if ((nameClass != null) && (nameUser != null) && (mac != null)) {
		    	// Open database in mode write
	    		DBHelper helper = application.getHelper();
		        if(helper != null) {
		        	try {
		        		JoinClassWithUserDB joinClassWithUserDb = new JoinClassWithUserDB(nameClass, nameUser, mac);
		        		helper.getJoinClassWithUser().create(joinClassWithUserDb);
		        		result = true;
		        	} catch (Exception e) {
		        		result = false;
		        	} finally {
		        		// close
		        		application.closeDBHelper();
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
		public JoinClassWithUserDB searchClassFromNameClass(String nameClassUser){
			JoinClassWithUserDB classDbResult = null;
			if (nameClassUser != null){
	            try {
	            	Dao<JoinClassWithUserDB, Integer> dao = application.getHelper().getJoinClassWithUser();
	                QueryBuilder <JoinClassWithUserDB, Integer> queryBuilder = dao.queryBuilder();
	                queryBuilder.setWhere(queryBuilder.where().eq(JoinClassWithUserDB.NAME_CLASS, nameClassUser));
	                List<JoinClassWithUserDB> classDb = dao.query(queryBuilder.prepare());
	                if (classDb.isEmpty()) {
	    				classDbResult = null;
	                } else {
	                	classDbResult = classDb.get(0);
	                }
	            } catch (Exception e) {
					classDbResult = null;
	            } finally {
	        		// close
	        		application.closeDBHelper();
	        	}
	            
			} else {
				classDbResult = null;
			}
			
			return classDbResult;
		}
	    
	    /**
	     * Delete join class with user database
	     * @param joinClassWithUserDBObject the class DB to save database
	     * @return If the user has been deleted or not
	     */
	    public boolean deleteJoinClassWithUser (JoinClassWithUserDB joinClassWithUserDBObject){
	        boolean result = false;
	    	if (joinClassWithUserDBObject != null) {
		        try {
	        		Dao <JoinClassWithUserDB, Integer> dao = application.getHelper().getJoinClassWithUser();
	        		dao.delete(joinClassWithUserDBObject);
		        } catch (Exception e) {
		        	result = false;
		        } finally {
	        		// close
	        		application.closeDBHelper();
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
            
			try {
            	Dao<JoinClassWithUserDB, Integer> dao = application.getHelper().getJoinClassWithUser();
                QueryBuilder <JoinClassWithUserDB, Integer> queryBuilder = dao.queryBuilder();
                listDbResult = dao.query(queryBuilder.prepare());
            } catch (Exception e) {
            	listDbResult = null;
            } finally {
        		// close
        		application.closeDBHelper();
        	}
	            
			return listDbResult;
		}
		
}
