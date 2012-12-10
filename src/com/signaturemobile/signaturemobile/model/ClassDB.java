package com.signaturemobile.signaturemobile.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * ClassDAOSql object reference class DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
@DatabaseTable
public class ClassDB implements Serializable {
		
		////////////////////////////////////////////////////////////////////////
		// Defines rows
		////////////////////////////////////////////////////////////////////////
		
		public static final String ID_CLASS = "id";
	    public static final String NAME_CLASS = "nameClass";
	    public static final String NUMBER_STUDENTS = "numberStudents";
	
		////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////

	    /**
		 * Defautl serial version UID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Instance name class SQL Lite
		 */
		@DatabaseField(generatedId = true, columnName = ID_CLASS)
		private int idClass;
		
		/**
		 * Instance name class SQL Lite
		 */
		@DatabaseField(columnName = NAME_CLASS)
		private String nameClass;
		
		/**
		 * Instance number students
		 */
		@DatabaseField(columnName = NUMBER_STUDENTS)
		private int numbersStudents;
		
		/**
		 * Default constructor
		 */
		public ClassDB (){}
		
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
