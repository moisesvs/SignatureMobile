package com.signaturemobile.signaturemobile.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.signaturemobile.signaturemobile.SignatureMobileApplication;
import com.signaturemobile.signaturemobile.model.AsignatureDB;
import com.signaturemobile.signaturemobile.model.ClassDB;

/**
 * DAOClassSQL object facade to communication to Class DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class DAOClassSQL {
	
		/**
		 * Context application
		 */
		private SignatureMobileApplication application;
	
		/**
		 * Default constructor
		 * @param contextApplication context application
		 */
		public DAOClassSQL (Context contextApplication){
			this.application = (SignatureMobileApplication) contextApplication;
		}
	    
	    /**
	     * Create class and save into database
	     * @param nameClass the name class to save database
	     * @param numberStudents the number students
	     * @return Create class
	     */
	    public boolean createClass (String nameClass, int numberStudents){
	    	
	        boolean result = false;
	    	if (nameClass != null) {
		    	// Open database in mode write
	    		DBHelper helper = application.getHelper();
		        if(helper != null) {
		        	try {
		        		ClassDB classDb = new ClassDB(nameClass, numberStudents);
		        		helper.getClassDAO().create(classDb);
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
		public ClassDB searchClassFromNameClass(String nameClassUser){
			ClassDB classDbResult = null;
			if (nameClassUser != null){
	            try {
	            	Dao<ClassDB, Integer> dao = application.getHelper().getClassDAO();
	                QueryBuilder <ClassDB, Integer> queryBuilder = dao.queryBuilder();
	                queryBuilder.setWhere(queryBuilder.where().eq(AsignatureDB.NAME_ASIGNATURE, nameClassUser));
	                List<ClassDB> classDb = dao.query(queryBuilder.prepare());
	                if (classDb.isEmpty()) {
	    				classDbResult = null;
	                } else {
	                	classDbResult = classDb.get(0);
	                }
	            } catch (Exception e) {
					classDbResult = null;
	            } finally {
	        		// close
	            	application.getHelper().close();
	        	}
	            
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
		        try {
	        		Dao <ClassDB, Integer> dao = application.getHelper().getClassDAO();
	        		dao.delete(classDBObject);
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
		 * Search device from to the mac
		 * @param numberStudents value mac
		 * @return the user db result or null if not find
		 */
		public boolean updateStudentsFromNameClass(String nameClass, int numberStudents){
			boolean result = false;

			if (nameClass != null) {
		        try {
	        		Dao <ClassDB, Integer> dao = application.getHelper().getClassDAO();
	        		ClassDB classAux = searchClassFromNameClass(nameClass);
	        		if (classAux != null) {
	        			classAux.setNumbersStudents(numberStudents);
		        		dao.update(classAux);
	        		} else {
	        			result = false;
	        		}
	        		
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
		 * List class
		 * @return the list fill with class all
		 */
		public List<ClassDB> listClass(){
			List<ClassDB> listDbResult = new ArrayList<ClassDB>();
            
			try {
            	Dao<ClassDB, Integer> dao = application.getHelper().getClassDAO();
                QueryBuilder <ClassDB, Integer> queryBuilder = dao.queryBuilder();
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
