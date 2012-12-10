package com.signaturemobile.signaturemobile.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

/**
 * JoinAsignatureWithClassDB object reference JoinAsignatureWithClassDB DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class JoinAsignatureWithClassDB implements Serializable{
	
	
		////////////////////////////////////////////////////////////////////////
		// Defines rows
		////////////////////////////////////////////////////////////////////////
		
		public static final String ID_JOIN_ASIGNATURE_CLASS = "id";
	    public static final String ID_ASIGNATURE = "idAsignature";
	    public static final String ID_CLASS = "idClass";

		////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////
	
		/**
		 * Default serial version UID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 	Id join asignature class
		 */
		@DatabaseField(generatedId = true, columnName = ID_JOIN_ASIGNATURE_CLASS)
		private int idJoinAsignatureClass;
		
		/**
		 * Instance name asignature SQL Lite
		 */
		@DatabaseField(columnName = ID_ASIGNATURE)
		private int idAsignature;
		
		/**
		 * Instance user name SQL Lite
		 */
		@DatabaseField(columnName = ID_CLASS)
		private int idClass;
		
		/**
		 * Default constructor
		 */
		public JoinAsignatureWithClassDB (){}
		
		/**
		 * Default contructor
		 * @param nameClass name class db
		 * @param userName user name db
		 * @param mac db
		 */
		public JoinAsignatureWithClassDB (int idAsignature, int idClass){
			this.idAsignature = idAsignature;
			this.idClass = idClass;
		}

		/**
		 * @return the idJoinAsignatureClass
		 */
		public int getIdJoinAsignatureClass() {
			return idJoinAsignatureClass;
		}

		/**
		 * @param idJoinAsignatureClass the idJoinAsignatureClass to set
		 */
		public void setIdJoinAsignatureClass(int idJoinAsignatureClass) {
			this.idJoinAsignatureClass = idJoinAsignatureClass;
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
