package com.signaturemobile.signaturemobile.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
 
public class JoinClassWithUserSQLite extends SQLiteBaseData {

	/**
	 * Sql statement create table
	 */
	protected static String sqlCreateTable = "CREATE TABLE JoinClassWithUser (code INTEGER, nameClass TEXT, username TEXT)";

	/**
	 * Sql statement delete table
	 */
	protected static String sqlDeleteTable = "DROP TABLE IF EXISTS JoinClassWithUser";

    /**
     * Default constructor
     * @param context the context application
     * @param name the name of the database
     * @param factory cursor factory of the database
     * @param version version database
     */
    public JoinClassWithUserSQLite(Context context, String name,
                               CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * When the database is create
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    	super.onCreate(db);
    	// create database
    	db.execSQL(sqlCreateTable);
    }

    /**
     * 	When a new version of database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionPrevious, int versionNew) {
    	super.onUpgrade(db, versionPrevious, versionNew);
    	// delete databse
        db.execSQL(sqlDeleteTable);
        // create database
        db.execSQL(sqlCreateTable);
    }
}