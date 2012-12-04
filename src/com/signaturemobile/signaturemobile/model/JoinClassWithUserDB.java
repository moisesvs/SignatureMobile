package com.signaturemobile.signaturemobile.model;

import java.io.Serializable;

/**
 * UserDAOSql object reference User DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class JoinClassWithUserDB implements Serializable{
		
		/**
		 * Default serial version UID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Instance name class SQL Lite
		 */
		private String nameClass;
		
		/**
		 * Instance user name SQL Lite
		 */
		private String userName;
		
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

}
