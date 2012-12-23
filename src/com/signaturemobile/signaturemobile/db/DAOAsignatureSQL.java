package com.signaturemobile.signaturemobile.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.signaturemobile.signaturemobile.SignatureMobileApplication;
import com.signaturemobile.signaturemobile.model.AsignatureDB;

/**
 * DAOClassSQL object facade to communication to Class DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class DAOAsignatureSQL {
		
		/**
		 * Context application
		 */
		private SignatureMobileApplication application;
	
		/**
		 * Default constructor
		 * @param contextApplication context application
		 */
		public DAOAsignatureSQL (Context contextApplication){
			this.application = (SignatureMobileApplication) contextApplication;
		}
	    
	    /**
	     * Create class and save into database
	     * @param nameAsignature the name class to save database
	     * @param numberStudents the number students
	     * @return Create class
	     */
	    public boolean createAsignature (String nameAsignature, int numberStudents){
	        boolean result = false;
	    	if (nameAsignature != null) {
		    	// Open database in mode write
	    		DBHelper helper = application.getHelper();
		        if(helper != null) {
		        	try {
		        		AsignatureDB asignature = new AsignatureDB(nameAsignature, numberStudents);
		        		helper.getAsignatureDAO().create(asignature);
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
		 * Search asignature from name asignature
		 * @param name class user 
		 * @return the class db result or null if not find
		 */
		public AsignatureDB searchAsignatureFromNameClass(String nameAsignatureUser){
			AsignatureDB asignatureDbResult = null;
			if (nameAsignatureUser != null){
	            try {
	            	Dao<AsignatureDB, Integer> dao = application.getHelper().getAsignatureDAO();
	                QueryBuilder <AsignatureDB, Integer> queryBuilder = dao.queryBuilder();
	                queryBuilder.setWhere(queryBuilder.where().eq(AsignatureDB.NAME_ASIGNATURE, nameAsignatureUser));
	                List<AsignatureDB> asignatures = dao.query(queryBuilder.prepare());
	                if (asignatures.isEmpty()) {
	    				asignatureDbResult = null;
	                } else {
	                	asignatureDbResult = asignatures.get(0);
	                }
	            } catch (Exception e) {
					asignatureDbResult = null;
	            } finally {
	        		// close
	        		application.closeDBHelper();
	        	}
	            
	            
			} else {
				asignatureDbResult = null;
			}
			
			return asignatureDbResult;
		}
	    
	    /**
	     * Delete asignature into database
	     * @param asignatureDBObject the asignature DB to save database
	     * @return If the user has been deleted or not
	     */
	    public boolean deleteAsignature (AsignatureDB asignatureDBObject){
	        boolean result = false;
	    	if (asignatureDBObject != null) {
		        try {
	        		Dao <AsignatureDB, Integer> dao = application.getHelper().getAsignatureDAO();
	        		dao.delete(asignatureDBObject);
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
		 * List asignature
		 * @return the list fill with asignature all
		 */
		public List<AsignatureDB> listAsignature(){
			List<AsignatureDB> listDbResult = new ArrayList<AsignatureDB>();
            
			try {
            	Dao<AsignatureDB, Integer> dao = application.getHelper().getAsignatureDAO();
                QueryBuilder <AsignatureDB, Integer> queryBuilder = dao.queryBuilder();
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
