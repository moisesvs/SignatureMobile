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
public class AsignatureDB implements Serializable {
	
		////////////////////////////////////////////////////////////////////////
		// Defines rows
		////////////////////////////////////////////////////////////////////////
		
		public static final String ID_ASIGNATURE = "id";
	    public static final String NAME_ASIGNATURE = "nameAsignature";
	    public static final String NUMBER_STUDENTS = "numberStudents";
	
		////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////
	    
		/**
		 * Default serial version UID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 	Id of the asignature
		 */
		@DatabaseField(generatedId = true, columnName = ID_ASIGNATURE)
		private int idAsignature;
		
		/**
		 * Instance name asignature SQL Lite
		 */
		@DatabaseField(columnName = NAME_ASIGNATURE)
		private String nameAsignature;
		
		/**
		 * Instance number students
		 */
		@DatabaseField(columnName = NUMBER_STUDENTS)
		private int numbersStudents;
		
		/**
		 * Default constructor
		 */
		public AsignatureDB (){}
		
		/**
		 * Constructor
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

		/**
		 * @return the idAsignature
		 */
		public int getIdAsignature() {
			return idAsignature;
		}

		/**
		 * @param idAsignature the idAsignature to set
		 */
		public void setIdAsignature(int idAsignature) {
			this.idAsignature = idAsignature;
		}

}
