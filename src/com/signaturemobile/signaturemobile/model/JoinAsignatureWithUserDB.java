package com.signaturemobile.signaturemobile.model;

import java.io.Serializable;

/**
 * UserDAOSql object reference User DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class JoinAsignatureWithUserDB implements Serializable{
		
		/**
		 * Default serial version UID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Instance name asignature SQL Lite
		 */
		private String nameAsignature;
		
		/**
		 * Instance user name SQL Lite
		 */
		private String userName;
		
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

}
