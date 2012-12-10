package com.signaturemobile.signaturemobile.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.signaturemobile.signaturemobile.SignatureMobileApplication;
import com.signaturemobile.signaturemobile.model.JoinAsignatureWithUserDB;

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
	     * @param mac to save database
	     * @return Create join class with user
	     */
	    public boolean createJoinAsignatureWithUser (String nameAsignature, String nameUser, String mac){
	        boolean result = false;
	    	if ((nameAsignature != null) && (nameUser != null)) {
		    	// Open database in mode write
	    		DBHelper helper = application.getHelper();
		        if(helper != null) {
		        	try {
		        		JoinAsignatureWithUserDB classDb = new JoinAsignatureWithUserDB(nameAsignature, nameUser, mac);
		        		helper.getJoinAsignatureWithUser().create(classDb);
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
	        		application.closeDBHelper();
	        	}
	            
			} else {
				joinDbResult = null;
			}
			
			return joinDbResult;
		}
	    
		/**
		 * Search asignature from name asignature
		 * @param name class user 
		 * @return the class db result or null if not find
		 */
		public JoinAsignatureWithUserDB searchAsignatureFromNameUser(String nameUser){
			JoinAsignatureWithUserDB joinDbResult = null;
			if (nameUser != null){
	            try {
	            	Dao<JoinAsignatureWithUserDB, Integer> dao = application.getHelper().getJoinAsignatureWithUser();
	                QueryBuilder <JoinAsignatureWithUserDB, Integer> queryBuilder = dao.queryBuilder();
	                queryBuilder.setWhere(queryBuilder.where().eq(JoinAsignatureWithUserDB.USERNAME, nameUser));
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
	        		application.closeDBHelper();
	        	}
	            
			} else {
				joinDbResult = null;
			}
			
			return joinDbResult;
		}
		
		/**
		 * Search class from name class
		 * @param nameAsignature the name asignature to search
		 * @param mac user 
		 * @return true if the find or false in other case
		 */
		public boolean exitsAsignatureFromMacUser(String nameAsignature, String mac){
			boolean result = false;
			if (mac != null){
	            try {
	            	Dao<JoinAsignatureWithUserDB, Integer> dao = application.getHelper().getJoinAsignatureWithUser();
	                QueryBuilder <JoinAsignatureWithUserDB, Integer> queryBuilder = dao.queryBuilder();
	                Where<JoinAsignatureWithUserDB, Integer> where = queryBuilder.where();
	                where.eq(JoinAsignatureWithUserDB.MAC, mac);
	                where.and();
	                where.eq(JoinAsignatureWithUserDB.NAME_ASIGNATURE, nameAsignature);
	                List<JoinAsignatureWithUserDB> classDb = dao.query(queryBuilder.prepare());
	                if (classDb.isEmpty()) {
	                	result = false;
	                } else {
	                	result = true;
	                }
	            } catch (Exception e) {
	            	result = false;
	            } finally {
	        		// close
	        		application.closeDBHelper();
	        	}
	            
			} else {
				result = false;
			}
			
			return result;
		}
	    
	    /**
	     * Delete join class with user database
	     * @param joinClassWithUserDBObject the class DB to save database
	     * @return If the user has been deleted or not
	     */
	    public boolean deleteJoinAsignatureWithJoin (JoinAsignatureWithUserDB joinAsignatureWithUserDBObject){
	        boolean result = false;
	    	if (joinAsignatureWithUserDBObject != null) {
		        try {
	        		Dao <JoinAsignatureWithUserDB, Integer> dao = application.getHelper().getJoinAsignatureWithUser();
	        		dao.delete(joinAsignatureWithUserDBObject);
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
	     * Delete join class with user database
	     * @param joinClassWithUserDBObject the class DB to save database
	     * @return If the user has been deleted or not
	     */
	    public boolean deleteJoinAsignatureWithAsignature (String nameAsignature){
	        boolean result = false;
	    	if (nameAsignature != null) {
		        try {
		        	JoinAsignatureWithUserDB joinAsignatureWithUser = searchAsignatureFromNameAsignature(nameAsignature);
		        	if (joinAsignatureWithUser != null) {
		        		Dao <JoinAsignatureWithUserDB, Integer> dao = application.getHelper().getJoinAsignatureWithUser();
		        		dao.delete(joinAsignatureWithUser);
			        	result = true;
		        	} else {
			        	result = false;
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
	     * Delete join class with user database
	     * @param username the user name DB to delete database
	     * @return If the user has been deleted or not
	     */
	    public boolean deleteJoinAsignatureWithUser (String username){
	        boolean result = false;
	    	if (username != null) {
		        try {
		        	JoinAsignatureWithUserDB joinAsignatureWithUser = searchAsignatureFromNameUser(username);
		        	if (joinAsignatureWithUser != null) {
		        		Dao <JoinAsignatureWithUserDB, Integer> dao = application.getHelper().getJoinAsignatureWithUser();
		        		dao.delete(joinAsignatureWithUser);
			        	result = true;
		        	} else {
			        	result = false;
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
		 * @return the list fill with class all
		 */
		public List<JoinAsignatureWithUserDB> listJoinAsignatureWithUser(String nameAsignature){
			List<JoinAsignatureWithUserDB> listDbResult = new ArrayList<JoinAsignatureWithUserDB>();
            
			try {
            	Dao<JoinAsignatureWithUserDB, Integer> dao = application.getHelper().getJoinAsignatureWithUser();
                QueryBuilder <JoinAsignatureWithUserDB, Integer> queryBuilder = dao.queryBuilder();
                queryBuilder.setWhere(queryBuilder.where().eq(JoinAsignatureWithUserDB.NAME_ASIGNATURE, nameAsignature));
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
