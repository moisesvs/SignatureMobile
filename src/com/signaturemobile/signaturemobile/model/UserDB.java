package com.signaturemobile.signaturemobile.model;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * UserDAOSql object reference User DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
@DatabaseTable
public class UserDB implements Serializable {
	
		////////////////////////////////////////////////////////////////////////
		// Defines rows
		////////////////////////////////////////////////////////////////////////
		
		public static final String ID_USERNAME = "id";
	    public static final String USERNAME = "username";
	    public static final String PASSWORD = "password";
	    public static final String USER_TWITTER = "userTwitter";
	    public static final String MAC = "mac";
	    public static final String TICKETS = "tickets";
	    public static final String DATE_CREATE_USER = "dateCreateUser";
	    public static final String DATE_LAST_SIGN = "dateLastSign";
	    public static final String TOKEN_NFC = "tokenNfc";

		////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////
	
		/**
		 * Defautl serial version UID
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * 	Id of the user
		 */
		@DatabaseField(generatedId = true, columnName = ID_USERNAME)
		private int idAsignature;
		
		/**
		 * Instance User SQL Lite
		 */
		@DatabaseField(columnName = USERNAME)
		private String username;
		
		/**
		 * Password user DAO
		 */
		@DatabaseField(columnName = PASSWORD)
		private String password;
		
		/**
		 * Username twitter
		 */
		@DatabaseField(columnName = USER_TWITTER)
		private String userTwitter;
		
		/**
		 * MAC user
		 */
		@DatabaseField(columnName = MAC)
		private String mac;
		
		/**
		 * Ticket user
		 */
		@DatabaseField(columnName = TICKETS)
		private int tickets;
		
		/**
		 * Date create user
		 */
		@DatabaseField(columnName = DATE_CREATE_USER)
		private Date dateCreateUser;
		
		/**
		 * Date last sing user
		 */
		@DatabaseField(columnName = DATE_LAST_SIGN)
		private Date dateLastSignUser;
		
		/**
		 * Token NFC
		 */
		@DatabaseField(columnName = TOKEN_NFC)
		private String tokenNFC;
		
		/**
		 * Default constructor
		 */
		public UserDB (){}
		
		/**
		 * Default contructor
		 * @param username username user db
		 * @param password password user db
		 * @param userTwitter user twitter
		 * @param mac Mac user
		 * @param tickets User tickets
		 * @param dateCreateUser Date create user
		 * @param dateLastSignUser Date last signature user
		 */
		public UserDB (String username, String password, String userTwitter, String mac, int tickets,
				Date dateCreateUser, Date dateLastSignUser, String tokenNFC){
			this.username = username;
			this.password = password;
			this.userTwitter = userTwitter;
			this.mac = mac;
			this.tickets = tickets;
			this.dateCreateUser = dateCreateUser;
			this.dateLastSignUser = dateLastSignUser;
			this.tokenNFC = tokenNFC;
		}

		/**
		 * @return the username
		 */
		public String getUsername() {
			return username;
		}

		/**
		 * @param username the username to set
		 */
		public void setUsername(String username) {
			this.username = username;
		}

		/**
		 * @return the password
		 */
		public String getPassword() {
			return password;
		}

		/**
		 * @param password the password to set
		 */
		public void setPassword(String password) {
			this.password = password;
		}

		/**
		 * @return the userTwitter
		 */
		public String getUserTwitter() {
			return userTwitter;
		}

		/**
		 * @param userTwitter the userTwitter to set
		 */
		public void setUserTwitter(String userTwitter) {
			this.userTwitter = userTwitter;
		}

		/**
		 * @return the tickets
		 */
		public int getTickets() {
			return tickets;
		}
		
		/**
		 * @param tickets the tickets to set
		 */
		public void setTickets(int tickets) {
			this.tickets = tickets;
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
		 * @return the dateCreateUser
		 */
		public Date getDateCreateUser() {
			return dateCreateUser;
		}
		
		/**
		 * @return the dateCreateUser
		 */
		public long getDateCreateUserTime() {
			long result = 0;
			if (dateCreateUser != null)
				result = dateCreateUser.getTime();
			
			return result;
		}

		/**
		 * @param dateCreateUser the dateCreateUser to set
		 */
		public void setDateCreateUser(Date dateCreateUser) {
			this.dateCreateUser = dateCreateUser;
		}

		/**
		 * @return the dateLastSignUser
		 */
		public Date getDateLastSignUser() {
			return dateLastSignUser;
		}
		
		/**
		 * @return the dateCreateUser
		 */
		public long getDateLastSignUserTime() {
			long result = 0;
			if (dateLastSignUser != null)
				result = dateLastSignUser.getTime();
			
			return result;
		}
		
		/**
		 * @param dateLastSignUser the dateLastSignUser to set
		 */
		public void setDateLastSignUser(Date dateLastSignUser) {
			this.dateLastSignUser = dateLastSignUser;
		}

		/**
		 * @return the tokenNFC
		 */
		public String getTokenNFC() {
			return tokenNFC;
		}

		/**
		 * @param tokenNFC the tokenNFC to set
		 */
		public void setTokenNFC(String tokenNFC) {
			this.tokenNFC = tokenNFC;
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
