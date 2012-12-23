package com.signaturemobile.signaturemobile.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.signaturemobile.signaturemobile.Constants;
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
	     * @param idClass the id class to save database
	     * @param nameClass the name class to save database
	     * @param nameUser the name user to save database
	     * @param mac user to save database
	     * @return Create join class with user
	     */
	    public boolean createJoinClassWithUser (int idClass, String nameClass, int idUser, String mac){
	        boolean result = false;
	    	if ((idUser != Constants.NULL_VALUES) && (nameClass != null) && (idUser != Constants.NULL_VALUES) && (mac != null)) {
		    	// Open database in mode write
	    		DBHelper helper = application.getHelper();
		        if(helper != null) {
		        	try {
		        		JoinClassWithUserDB joinClassWithUserDb = new JoinClassWithUserDB(idClass, nameClass, idUser, mac);
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
		 * Search class from name class
		 * @param idClass id class 
		 * @param idUser id user 
		 * @return the class db result or null if not find
		 */
		public JoinClassWithUserDB searchClassFromIdClassAndIdUser(int idClass, int idUser){
			JoinClassWithUserDB classDbResult = null;
			if ((idClass != Constants.NULL_VALUES) && (idUser != Constants.NULL_VALUES)){
	            try {
	            	Dao<JoinClassWithUserDB, Integer> dao = application.getHelper().getJoinClassWithUser();
	                QueryBuilder <JoinClassWithUserDB, Integer> queryBuilder = dao.queryBuilder();
	                Where<JoinClassWithUserDB, Integer> where = queryBuilder.where();
	                where.eq(JoinClassWithUserDB.ID_CLASS, idClass);
	                where.and();
	                where.eq(JoinClassWithUserDB.ID_USER, idUser);
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
	     * Delete join class with user database
	     * @param idUser the id user
	     * @return If the user has been deleted or not
	     */
	    public boolean deleteJoinClassWithUser (int idUser){
	        boolean result = false;
	    	if (idUser != Constants.NULL_VALUES) {
		        try {
		        	List<JoinClassWithUserDB> listJoinClassWithUser = listJoinClassWithUserFromIdUser(idUser);
	        		Dao <JoinClassWithUserDB, Integer> dao = application.getHelper().getJoinClassWithUser();
		        	for (JoinClassWithUserDB join : listJoinClassWithUser) {
			        	if (join != null) {
			        		dao.delete(join);
				        	result = true;
			        	} else {
				        	result = false;
			        	}
		        	}

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
		 * @param idClass id class
		 * @return the list fill with class all
		 */
		public List<JoinClassWithUserDB> listJoinClassWithUserFromIdClass(int idClass){
			List<JoinClassWithUserDB> listDbResult = new ArrayList<JoinClassWithUserDB>();
            
			try {
            	Dao<JoinClassWithUserDB, Integer> dao = application.getHelper().getJoinClassWithUser();
                QueryBuilder <JoinClassWithUserDB, Integer> queryBuilder = dao.queryBuilder();
                queryBuilder.setWhere(queryBuilder.where().eq(JoinClassWithUserDB.ID_CLASS, idClass));
                listDbResult = dao.query(queryBuilder.prepare());
            } catch (Exception e) {
            	listDbResult = null;
            } finally {
        		// close
        		application.closeDBHelper();
        	}
	            
			return listDbResult;
		}
		
		/**
		 * List join class with user
		 * @param idUser id user
		 * @return the list fill with class all
		 */
		public List<JoinClassWithUserDB> listJoinClassWithUserFromIdUser(int idUser){
			List<JoinClassWithUserDB> listDbResult = new ArrayList<JoinClassWithUserDB>();
            
			try {
            	Dao<JoinClassWithUserDB, Integer> dao = application.getHelper().getJoinClassWithUser();
                QueryBuilder <JoinClassWithUserDB, Integer> queryBuilder = dao.queryBuilder();
                queryBuilder.setWhere(queryBuilder.where().eq(JoinClassWithUserDB.ID_USER, idUser));
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
