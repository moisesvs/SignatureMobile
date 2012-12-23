package com.signaturemobile.signaturemobile.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * UserDAOSql object reference User DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
@DatabaseTable
public class JoinClassWithUserDB implements Serializable{
	
		////////////////////////////////////////////////////////////////////////
		// Defines rows
		////////////////////////////////////////////////////////////////////////
		
		public static final String ID_JOIN_CLASS_WITH_USER = "id";
		public static final String ID_CLASS = "idClass";
	    public static final String NAME_CLASS = "nameClass";
	    public static final String ID_USER = "username";
	    public static final String MAC = "mac";

		////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////
	
		/**
		 * Default serial version UID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 	Id of the class
		 */
		@DatabaseField(generatedId = true, columnName = ID_JOIN_CLASS_WITH_USER)
		private int idJoinClassWithUser;
		
		/**
		 * 	Id of the class
		 */
		@DatabaseField(columnName = ID_CLASS)
		private int idClass;
		
		/**
		 * Instance name class SQL Lite
		 */
		@DatabaseField(columnName = NAME_CLASS)
		private String nameClass;
		
		/**
		 * Instance user name SQL Lite
		 */
		@DatabaseField(columnName = ID_USER)
		private int idUser;
		
		/**
		 * Instance user name SQL Lite
		 */
		@DatabaseField(columnName = MAC)
		private String mac;
		
		/**
		 * Default constructor
		 */
		public JoinClassWithUserDB (){}
		
		/**
		 * Default constructor
		 * @param idClass id class db
		 * @param nameClass name class db
		 * @param userName user name db
		 * @param mac mac db
		 */
		public JoinClassWithUserDB (int idClass, String nameClass, int idUserName, String mac){
			this.idClass = idClass;
			this.nameClass = nameClass;
			this.idUser = idUserName;
			this.mac = mac;
		}

		/**
		 * @return the nameClass
		 */
		public String getNameClass() {
			return nameClass;
		}

		/**
		 * @param nameClass the nameClass to set
		 */
		public void setNameClass(String nameClass) {
			this.nameClass = nameClass;
		}

		/**
		 * @return the idUser
		 */
		public int getIdUser() {
			return idUser;
		}

		/**
		 * @param userName the userName to set
		 */
		public void setIdUserName(int idUserName) {
			this.idUser = idUserName;
		}

		/**
		 * @return the idClass
		 */
		public int getIdClass() {
			return idClass;
		}

		/**
		 * @param idClass the idClass to set
		 */
		public void setIdClass(int idClass) {
			this.idClass = idClass;
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

		/**
		 * @return the idJoinClassWithUser
		 */
		public int getIdJoinClassWithUser() {
			return idJoinClassWithUser;
		}

		/**
		 * @param idJoinClassWithUser the idJoinClassWithUser to set
		 */
		public void setIdJoinClassWithUser(int idJoinClassWithUser) {
			this.idJoinClassWithUser = idJoinClassWithUser;
		}

}
