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
	    public static final String ID_ASIGNATURE = "idAsignature";
	    public static final String ID_USER = "idUser";
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
		 * Instance id asignature SQL Lite
		 */
		@DatabaseField(columnName = ID_ASIGNATURE)
		private int idAssignature;
		
		/**
		 * Instance id user name SQL Lite
		 */
		@DatabaseField(columnName = ID_USER)
		private int idUser;
		
		/**
		 * Instance user name SQL Lite
		 */
		@DatabaseField(columnName = MAC)
		private String nameUser;
		
		/**
		 * Default constructor
		 */
		public JoinAsignatureWithUserDB (){}
		
		/**
		 * Default contructor
		 * @param nameClass name class db
		 * @param userName user name db
		 * @param nameUser db
		 */
		public JoinAsignatureWithUserDB (int idClass, int idUser, String nameUser){
			this.idAssignature = idClass;
			this.idUser = idUser;
			this.nameUser = nameUser;
		}

		/**
		 * @return the nameClass
		 */
		public int getNameClass() {
			return idAssignature;
		}

		/**
		 * @param idAssignature the id assignature to set
		 */
		public void setNameAsignature(int idAssignature) {
			this.idAssignature = idAssignature;
		}

		/**
		 * @return the userName
		 */
		public int getIdUser() {
			return idUser;
		}

		/**
		 * @param idUser the id user to set
		 */
		public void setIdUser(int idUser) {
			this.idUser = idUser;
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
		public String getUserName() {
			return nameUser;
		}

		/**
		 * @param username the mac to set
		 */
		public void setUserName(String username) {
			this.nameUser = username;
		}

}
