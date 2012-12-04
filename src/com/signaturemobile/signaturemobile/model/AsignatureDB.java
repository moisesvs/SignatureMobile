package com.signaturemobile.signaturemobile.model;

import java.io.Serializable;

/**
 * ClassDAOSql object reference class DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class AsignatureDB implements Serializable {
		
		/**
		 * Defautl serial version UID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Instance name asignature SQL Lite
		 */
		private String nameAsignature;
		
		/**
		 * Instance number students
		 */
		private int numbersStudents;
		
		/**
		 * Default contructor
		 * @param nameClass name class user db
		 * @param numbersStudents numbers students db
		 */
		public AsignatureDB (String nameClass, int numbersStudents){
			this.nameAsignature = nameClass;
			this.numbersStudents = numbersStudents;
		}

		/**
		 * @return the nameClass
		 */
		public String getNameAsignature() {
			return nameAsignature;
		}

		/**
		 * @param nameAsignature the nameClass to set
		 */
		public void setNameAsignature(String nameAsignature) {
			this.nameAsignature = nameAsignature;
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
