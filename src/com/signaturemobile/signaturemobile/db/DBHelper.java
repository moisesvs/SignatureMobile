package com.signaturemobile.signaturemobile.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.signaturemobile.signaturemobile.model.AsignatureDB;
import com.signaturemobile.signaturemobile.model.ClassDB;
import com.signaturemobile.signaturemobile.model.JoinAsignatureWithClassDB;
import com.signaturemobile.signaturemobile.model.JoinAsignatureWithUserDB;
import com.signaturemobile.signaturemobile.model.JoinClassWithUserDB;
import com.signaturemobile.signaturemobile.model.UserDB;

/**
 * DBHelper object reference helper DB
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
 
	/**
	 * The data base name
	 */
    private static final String DATABASE_NAME = "androcode_ormlite.db";
    
    /**
     * The version data base
     */
    private static final int DATABASE_VERSION = 1;
 
    /**
     * Asignature DAO
     */
    private Dao <AsignatureDB, Integer> asignatureDao;
    
    /**
     * User DAO
     */
    private Dao <UserDB, Integer> userDao;
    
    /**
     * Class DAO
     */
    private Dao <ClassDB, Integer> classDao;
    
    /**
     * Join asignature with user
     */
    private Dao <JoinAsignatureWithUserDB, Integer> joinAsignatureWithUsers;
    
    /**
     * Join class with user
     */
    private Dao <JoinClassWithUserDB, Integer> joinClassWithUsers;
    
    /**
     * Join asignature with class
     */
    private Dao <JoinAsignatureWithClassDB, Integer> joinAsignatureWithClass;

    /**
     * Db Helper context
     * @param context the context application
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * On create sql table
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, AsignatureDB.class);
            TableUtils.createTable(connectionSource, UserDB.class);
            TableUtils.createTable(connectionSource, ClassDB.class);
            TableUtils.createTable(connectionSource, JoinAsignatureWithUserDB.class);
            TableUtils.createTable(connectionSource, JoinClassWithUserDB.class);
            TableUtils.createTable(connectionSource, JoinAsignatureWithClassDB.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * On upgrade the sql lite
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onCreate(db, connectionSource);
    }

    /**
     * Get asignature DAO
     * @return Dao 
     * @throws SQLException throws exception it is possible
     */
    public Dao<AsignatureDB, Integer> getAsignatureDAO() throws SQLException {
        if (asignatureDao == null) {
        	asignatureDao = getDao(AsignatureDB.class);
        }
        return asignatureDao;
    }
    
    /**
     * Get user DAO
     * @return Dao 
     * @throws SQLException throws exception it is possible
     */
    public Dao<UserDB, Integer> getUserDAO() throws SQLException {
        if (userDao == null) {
        	userDao = getDao(UserDB.class);
        }
        return userDao;
    }
    
    /**
     * Get class DAO
     * @return Dao 
     * @throws SQLException throws exception it is possible
     */
    public Dao<ClassDB, Integer> getClassDAO() throws SQLException {
        if (classDao == null) {
        	classDao = getDao(ClassDB.class);
        }
        return classDao;
    }
    
    /**
     * Get join asignature with user DAO
     * @return Dao 
     * @throws SQLException throws exception it is possible
     */
    public Dao<JoinAsignatureWithUserDB, Integer> getJoinAsignatureWithUser() throws SQLException {
        if (joinAsignatureWithUsers == null) {
        	joinAsignatureWithUsers = getDao(JoinAsignatureWithUserDB.class);
        }
        return joinAsignatureWithUsers;
    }
    
    /**
     * Get join asignature with users DAO
     * @return Dao 
     * @throws SQLException throws exception it is possible
     */
    public Dao<JoinClassWithUserDB, Integer> getJoinClassWithUser() throws SQLException {
        if (joinClassWithUsers == null) {
        	joinClassWithUsers = getDao(JoinClassWithUserDB.class);
        }
        return joinClassWithUsers;
    }
    
    /**
     * Get join class with users DAO
     * @return Dao 
     * @throws SQLException throws exception it is possible
     */
    public Dao<JoinAsignatureWithClassDB, Integer> getJoinAsignatureWithClass() throws SQLException {
        if (joinAsignatureWithClass == null) {
        	joinAsignatureWithClass = getDao(JoinAsignatureWithClassDB.class);
        }
        return joinAsignatureWithClass;
    }
 
}
