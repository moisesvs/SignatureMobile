package com.signaturemobile.signaturemobile.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteBaseData extends SQLiteOpenHelper {

		/**
		 * Constructor SQL lite base data
		 * @param context the context application
		 * @param name the name application
		 * @param factory the factory applicationx
		 * @param version the version application
		 */
	    public SQLiteBaseData(Context context, String name,
	                               CursorFactory factory, int version) {
	        super(context, name, factory, version);
	    }
	 
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    	// nothing
	    }
	 
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int versionPrevious, int versionNew) {
	    	// nothing
	    }

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#close()
		 */
		@Override
		public synchronized void close() {
			super.close();
		}

		/**
		 * Create and/or open a database. This will be the same object returned by getWritableDatabase() unless some problem, 
		 * such as a full disk, requires the database to be opened read-only. In that case, a read-only database object will be returned.		 */
		@Override
		public synchronized SQLiteDatabase getReadableDatabase() {
			// TODO Auto-generated method stub
			return super.getReadableDatabase();
		}

		/**
		 * A helper class to manage database creation and version management.
		 */
		@Override
		public synchronized SQLiteDatabase getWritableDatabase() {
			// TODO Auto-generated method stub
			return super.getWritableDatabase();
		}

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#onOpen(android.database.sqlite.SQLiteDatabase)
		 */
		@Override
		public void onOpen(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			super.onOpen(db);
		}
	    
}