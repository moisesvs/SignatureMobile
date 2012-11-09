package com.signaturemobile.signaturemobile.model;

import java.io.Serializable;
import java.util.Date;

/**
 * UserDAOSql object reference User DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class UserDB implements Serializable{
		
		/**
		 * Defautl serial version UID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Instance User SQL Lite
		 */
		private String username;
		
		/**
		 * Password user DAO
		 */
		private String password;
		
		/**
		 * Username twitter
		 */
		private String userTwitter;
		
		/**
		 * MAC user
		 */
		private String mac;
		
		/**
		 * Ticket user
		 */
		private String tickets;
		
		/**
		 * Date create user
		 */
		private Date dateCreateUser;
		
		/**
		 * Date last sing user
		 */
		private Date dateLastSignUser;
		
		/**
		 * Token NFC
		 */
		private String tokenNFC;
		
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
		public UserDB (String username, String password, String userTwitter, String mac, String tickets,
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
		public String getTickets() {
			return tickets;
		}

		/**
		 * @return the tickets
		 */
		public int getNumberTickets() {
			int numberTickets = 0;
			if (tickets != null){
				try {
					numberTickets = Integer.parseInt(tickets);
				} catch (NumberFormatException e){
					numberTickets = 0;
				}
			}
			return numberTickets;
		}
		
		/**
		 * @param tickets the tickets to set
		 */
		public void setTickets(String tickets) {
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

}
