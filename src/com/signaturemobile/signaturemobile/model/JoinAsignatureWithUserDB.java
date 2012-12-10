package com.signaturemobile.signaturemobile.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

/**
 * JoinAsignatureWithUserDB object reference JoinAsignatureWithUserDB DB
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
	    public static final String MAC = "mac";

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
		 * Instance user name SQL Lite
		 */
		@DatabaseField(columnName = MAC)
		private String mac;
		
		/**
		 * Default constructor
		 */
		public JoinAsignatureWithUserDB (){}
		
		/**
		 * Default contructor
		 * @param nameClass name class db
		 * @param userName user name db
		 * @param mac db
		 */
		public JoinAsignatureWithUserDB (String nameClass, String userName, String mac){
			this.nameAsignature = nameClass;
			this.userName = userName;
			this.mac = mac;
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

		/**
		 * @return the mac
		 */
		public String getMac() {
			return mac;
		}

		/**
		 * @param mac the mac to set
		 */
		public void setMac(String mac) {
			this.mac = mac;
		}

}
