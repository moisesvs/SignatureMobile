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
		
		public static final String ID_CLASS = "id";
	    public static final String NAME_CLASS = "nameClass";
	    public static final String USERNAME = "username";
	
		////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////
	
		/**
		 * Default serial version UID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 	Id of the class
		 */
		@DatabaseField(generatedId = true, columnName = ID_CLASS)
		private int idClass;
		
		/**
		 * Instance name class SQL Lite
		 */
		@DatabaseField(columnName = NAME_CLASS)
		private String nameClass;
		
		/**
		 * Instance user name SQL Lite
		 */
		@DatabaseField(columnName = USERNAME)
		private String userName;
		
		/**
		 * Default constructor
		 */
		public JoinClassWithUserDB (){}
		
		/**
		 * Default contructor
		 * @param nameClass name class db
		 * @param userName user name db
		 */
		public JoinClassWithUserDB (String nameClass, String userName){
			this.nameClass = nameClass;
			this.userName = userName;
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

}
