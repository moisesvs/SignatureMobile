package com.signaturemobile.signaturemobile.model;

import java.io.Serializable;

/**
 * ClassDAOSql object reference class DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class ClassDB implements Serializable {
		
		/**
		 * Defautl serial version UID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Instance name class SQL Lite
		 */
		private String nameClass;
		
		/**
		 * Instance number students
		 */
		private int numbersStudents;
		
		/**
		 * Default contructor
		 * @param nameClass name class user db
		 * @param numbersStudents numbers students db
		 */
		public ClassDB (String nameClass, int numbersStudents){
			this.nameClass = nameClass;
			this.numbersStudents = numbersStudents;
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
		 * @return the numbersStudents
		 */
		public int getNumbersStudents() {
			return numbersStudents;
		}

		/**
		 * @param numbersStudents the numbersStudents to set
		 */
		public void setNumbersStudents(int numbersStudents) {
			this.numbersStudents = numbersStudents;
		}

}
