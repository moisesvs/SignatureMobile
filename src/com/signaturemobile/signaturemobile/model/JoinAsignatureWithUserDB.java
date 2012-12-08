package com.signaturemobile.signaturemobile.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

/**
 * UserDAOSql object reference User DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class JoinAsignatureWithUserDB implements Serializable{
	
	
		////////////////////////////////////////////////////////////////////////
		// Defines rows
		////////////////////////////////////////////////////////////////////////
		
		public static final String ID_JOIN_ASIGNATURE_USER = "id";
	    public static final String NAME_ASIGNATURE = "nameAsignature";
	    public static final String USERNAME = "username";
	
		////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////
	
		/**
		 * Default serial version UID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 	Id join asignature user
		 */
		@DatabaseField(generatedId = true, columnName = ID_JOIN_ASIGNATURE_USER)
		private int idJoinAsignatureUser;
		
		/**
		 * Instance name asignature SQL Lite
		 */
		@DatabaseField(columnName = NAME_ASIGNATURE)
		private String nameAsignature;
		
		/**
		 * Instance user name SQL Lite
		 */
		@DatabaseField(columnName = USERNAME)
		private String userName;
		
		/**
		 * Default constructor
		 */
		public JoinAsignatureWithUserDB (){}
		
		/**
		 * Default contructor
		 * @param nameClass name class db
		 * @param userName user name db
		 */
		public JoinAsignatureWithUserDB (String nameClass, String userName){
			this.nameAsignature = nameClass;
			this.userName = userName;
		}

		/**
		 * @return the nameClass
		 */
		public String getNameClass() {
			return nameAsignature;
		}

		/**
		 * @param nameAsignature the nameClass to set
		 */
		public void setNameAsignature(String nameAsignature) {
			this.nameAsignature = nameAsignature;
		}

		/**
		 * @return the userName
		 */
		public String getUserName() {
			return userName;
		}

		/**
		 * @param userName the userName to set
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}

		/**
		 * @return the idJoinAsignatureUser
		 */
		public int getIdJoinAsignatureUser() {
			return idJoinAsignatureUser;
		}

		/**
		 * @param idJoinAsignatureUser the idJoinAsignatureUser to set
		 */
		public void setIdJoinAsignatureUser(int idJoinAsignatureUser) {
			this.idJoinAsignatureUser = idJoinAsignatureUser;
		}

}
