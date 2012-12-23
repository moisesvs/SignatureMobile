package com.signaturemobile.signaturemobile.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.SignatureMobileApplication;
import com.signaturemobile.signaturemobile.model.UserDB;

/**
 * DAOUserSQL object facade to communication to User DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class DAOUserSQL {
	
		/**
		 * Context application
		 */
		private SignatureMobileApplication application;
	
		/**
		 * Default constructor
		 * @param contextApplication context application
		 */
		public DAOUserSQL (Context contextApplication){
			this.application = (SignatureMobileApplication) contextApplication;
		}
	    
	    /**
	     * Create user and save into database
	     * @param username the username to save database
	     * @param password the password user to save database
	     * @param userTwitter the user twitter to save database
	     * @param mac the mac device bluetooth save database
	     * @param tickets the tickets user device bluetooth save database
	     * @param tokenNFC the token NFC
	     * @return If the user has been created or not
	     */
	    public boolean createUser (String username, String password, String userTwitter, String mac, int tickets, Date dateCreateUser, Date lastSignUser, String tokenNFC){
	        boolean result = false;
	    	
	    	if ((username != null) && (password != null) && (userTwitter != null) && (mac != null) && (tickets != -1)
	        		&& (dateCreateUser != null) && (lastSignUser != null)) {
		    	// Open database in mode write
	    		DBHelper helper = application.getHelper();
		        if(helper != null) {
		        	try {
		        		UserDB user = new UserDB(username,password, userTwitter, mac, tickets, dateCreateUser, lastSignUser, tokenNFC);
		        		helper.getUserDAO().create(user);
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
		 * Search device from to the mac
		 * @param mac value mac
		 * @return the user db result or null if not find
		 */
		public UserDB searchUserDeviceMAC(String mac){
			UserDB userDbResult = null;
			if (mac != null){
	            try {
	            	Dao<UserDB, Integer> dao = application.getHelper().getUserDAO();
	                QueryBuilder <UserDB, Integer> queryBuilder = dao.queryBuilder();
	                queryBuilder.setWhere(queryBuilder.where().eq(UserDB.MAC, mac));
	                List<UserDB> users = dao.query(queryBuilder.prepare());
	                if (users.isEmpty()) {
	    				userDbResult = null;
	                } else {
	                	userDbResult = users.get(0);
	                }
	            } catch (Exception e) {
					userDbResult = null;
	            } finally {
	        		// close
	        		application.closeDBHelper();
	        	}
	            
	            
			} else {
				userDbResult = null;
			}
			
			return userDbResult;
		}
	    
		/**
		 * Search device from token nfc
		 * @param token nfc 
		 * @return the user db result or null if not find
		 */
		public UserDB searchUserDeviceTokenNFC(String tokenNFC){
			UserDB userDbResult = null;
			if (tokenNFC != null){
	            try {
	            	Dao<UserDB, Integer> dao = application.getHelper().getUserDAO();
	                QueryBuilder <UserDB, Integer> queryBuilder = dao.queryBuilder();
	                queryBuilder.setWhere(queryBuilder.where().eq(UserDB.TOKEN_NFC, tokenNFC));
	                List<UserDB> users = dao.query(queryBuilder.prepare());
	                if (users.isEmpty()) {
	    				userDbResult = null;
	                } else {
	                	userDbResult = users.get(0);
	                }
	            } catch (Exception e) {
					userDbResult = null;
	            } finally {
	        		// close
	        		application.closeDBHelper();
	        	}
	            
	            
			} else {
				userDbResult = null;
			}
			
			return userDbResult;
		}
		
		/**
		 * Search device from username
		 * @param username the username 
		 * @return the user db result or null if not find
		 */
		public UserDB searchUserDeviceUsername(String username){
			UserDB userDbResult = null;
			if (username != null){
	            try {
	            	Dao <UserDB, Integer> dao = application.getHelper().getUserDAO();
	                QueryBuilder <UserDB, Integer> queryBuilder = dao.queryBuilder();
	                queryBuilder.setWhere(queryBuilder.where().eq(UserDB.USERNAME, username));
	                List<UserDB> users = dao.query(queryBuilder.prepare());
	                if (users.isEmpty()) {
	    				userDbResult = null;
	                } else {
	                	userDbResult = users.get(0);
	                }
	            } catch (Exception e) {
					userDbResult = null;
	            } finally {
	        		// close
	        		application.closeDBHelper();
	        	}
	            
	            
			} else {
				userDbResult = null;
			}
			
			return userDbResult;
		}
		
		/**
		 * Search device from username
		 * @param username the username 
		 * @return the user db result or null if not find
		 */
		public UserDB searchUserDeviceId(int idUser){
			UserDB userDbResult = null;
			if (idUser != Constants.NULL_VALUES){
	            try {
	            	Dao <UserDB, Integer> dao = application.getHelper().getUserDAO();
	                QueryBuilder <UserDB, Integer> queryBuilder = dao.queryBuilder();
	                queryBuilder.setWhere(queryBuilder.where().eq(UserDB.ID_USERNAME, idUser));
	                List<UserDB> users = dao.query(queryBuilder.prepare());
	                if (users.isEmpty()) {
	    				userDbResult = null;
	                } else {
	                	userDbResult = users.get(0);
	                }
	            } catch (Exception e) {
					userDbResult = null;
	            } finally {
	        		// close
	        		application.closeDBHelper();
	        	}
	            
	            
			} else {
				userDbResult = null;
			}
			
			return userDbResult;
		}
	    
	    /**
	     * Delete user into database
	     * @param username the username to save database
	     * @param password the password user to save database
	     * @param userTwitter the user twitter to save database
	     * @param mac the mac device bluetooth save database
	     * @param tickets the tickets user device bluetooth save database
	     * @param tokenNFC the token NFC
	     * @return If the user has been created or not
	     */
	    public boolean deleteUser (UserDB userDBParam){
	        boolean result = false;
	    	if (userDBParam != null) {
		        try {
	        		Dao <UserDB, Integer> dao = application.getHelper().getUserDAO();
	        		dao.delete(userDBParam);
	    	        result = true;
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
		 * Search device from to the mac
		 * @param mac value mac
		 * @return the user db result or null if not find
		 */
		public boolean updateTicketsFromMAC(String mac, int ticketsSet){
			boolean result = false;
			
	    	if (mac != null) {
		        try {
	        		UserDB userAux = searchUserDeviceMAC(mac);
	        		if (userAux != null) {
		        		Dao <UserDB, Integer> dao = application.getHelper().getUserDAO();
	        			userAux.setTickets(ticketsSet);
	        			userAux.setDateLastSignUser(new Date());
		        		dao.update(userAux);
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
		 * Search device from to the mac
		 * @param date value date
		 * @return the list fill with user db that sign in date parameter
		 */
		public List<UserDB> listFromDate(){
			List<UserDB> listDbResult = new ArrayList<UserDB>();
            
			try {
            	Dao<UserDB, Integer> dao = application.getHelper().getUserDAO();
                QueryBuilder <UserDB, Integer> queryBuilder = dao.queryBuilder();
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
