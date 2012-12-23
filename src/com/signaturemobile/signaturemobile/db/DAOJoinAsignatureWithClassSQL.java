package com.signaturemobile.signaturemobile.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.SignatureMobileApplication;
import com.signaturemobile.signaturemobile.model.JoinAsignatureWithClassDB;
import com.signaturemobile.signaturemobile.model.JoinClassWithUserDB;

/**
 * DAOJoinAsignatureWithClassSQL object facade to communication to Join asignature with class DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class DAOJoinAsignatureWithClassSQL {
	
		/**
		 * Context application
		 */
		private SignatureMobileApplication application;
	
		/**
		 * Default constructor
		 * @param contextApplication context application
		 */
		public DAOJoinAsignatureWithClassSQL (Context contextApplication){
			this.application = (SignatureMobileApplication) contextApplication;
		}
	    
	    /**
	     * Create join asignature with class and save into database
	     * @param idAsignature the name class to save database
	     * @param idClass the name user to save database
	     * @return Create join asignature with class
	     */
	    public boolean createJoinAsignatureWithClass (int idAsignature, int idClass){
	        boolean result = false;
	    	if ((idAsignature != Constants.NULL_VALUES) && (idClass != Constants.NULL_VALUES)) {
		    	// Open database in mode write
	    		DBHelper helper = application.getHelper();
		        if(helper != null) {
		        	try {
		        		JoinAsignatureWithClassDB asignatureDb = new JoinAsignatureWithClassDB(idAsignature, idClass);
		        		helper.getJoinAsignatureWithClass().create(asignatureDb);
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
	     * Delete join class with class database
	     * @param joinAsignatureWithClassDBObject the class DB to save database
	     * @return If the user has been deleted or not
	     */
	    public boolean deleteJoinAsignatureWithJoin (JoinAsignatureWithClassDB joinAsignatureWithClassDBObject){
	        boolean result = false;
	    	if (joinAsignatureWithClassDBObject != null) {
		        try {
	        		Dao <JoinAsignatureWithClassDB, Integer> dao = application.getHelper().getJoinAsignatureWithClass();
	        		dao.delete(joinAsignatureWithClassDBObject);
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
		 * Search class from name class
		 * @param idClass id class 
		 * @return the class db result or null if not find
		 */
		public JoinClassWithUserDB searchJoinAsignatureWithClassFromIdClass(int idClass){
			JoinClassWithUserDB classDbResult = null;
			if (idClass != Constants.NULL_VALUES){
	            try {
	            	Dao<JoinClassWithUserDB, Integer> dao = application.getHelper().getJoinClassWithUser();
	                QueryBuilder <JoinClassWithUserDB, Integer> queryBuilder = dao.queryBuilder();
	                queryBuilder.setWhere(queryBuilder.where().eq(JoinClassWithUserDB.ID_CLASS, idClass));
	    			List<JoinClassWithUserDB> listDbResult = dao.query(queryBuilder.prepare());
	                if (listDbResult.isEmpty()) {
	    				classDbResult = null;
	                } else {
	                	classDbResult = listDbResult.get(0);
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
		 * List join class with user
		 * @return the list fill with class all
		 */
		public List<JoinAsignatureWithClassDB> listJoinAsignatureWithClass(int idAsignature){
			List<JoinAsignatureWithClassDB> listDbResult = new ArrayList<JoinAsignatureWithClassDB>();

			try {
            	Dao<JoinAsignatureWithClassDB, Integer> dao = application.getHelper().getJoinAsignatureWithClass();
                QueryBuilder <JoinAsignatureWithClassDB, Integer> queryBuilder = dao.queryBuilder();
                queryBuilder.setWhere(queryBuilder.where().eq(JoinAsignatureWithClassDB.ID_ASIGNATURE, idAsignature));
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
