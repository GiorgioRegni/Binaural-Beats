package com.ihunda.android.binauralbeat.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "bbt.db";
    // //////////////////////////////////////////////////////////////////////
    // Fields
    // //////////////////////////////////////////////////////////////////////
    private static final String TAG = DBHelper.class.getSimpleName();
    private static final int DB_VERSION = 2;

    private Context mContext;

    // //////////////////////////////////////////////////////////////////////
    // Public methods
    // //////////////////////////////////////////////////////////////////////
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
        getWritableDatabase();
    }

    public static HashMap<String, Object> where(String aVar, Object aValue) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put(aVar, aValue);
        return result;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
        try {
            TableUtils.createTable(cs, HistoryModel.class);
            TableUtils.createTable(cs, PresetModel.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource cs, int oldVersion, int newVersion) {
        /*
         * try { if(oldVersion < 2) { // no alter table exists
         * TableUtils.alterTable(); } } catch (SQLException e) { LogTag.e(
         * e.getMessage()); throw new RuntimeException(e); }
         */
        if (oldVersion == 1 && newVersion == 2) {
            try {
                if (!doesTableExist(db, "presetmodel")) {
                    TableUtils.createTable(cs, PresetModel.class);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            db.beginTransaction();
            db.execSQL("DROP TABLE IF EXISTS '" + "presetmodel" + "'");
            db.setVersion(newVersion);
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public <T> List<T> getAll(Class<T> clazz) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryForAll();
    }

    public <T> List<T> get(Class<T> clazz, String id) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.query(dao.queryBuilder().where().eq("id", id).prepare());
    }

    public <T> List<T> getAllOrdered(Class<T> clazz, String orderBy, boolean ascending) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        return dao.queryBuilder().orderBy(orderBy, ascending).query();
    }

    public <T> void fillObject(Class<T> clazz, T aObj) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        dao.createOrUpdate(aObj);
    }

    public <T> int insertObject(Class<T> clazz, T aObj) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        dao.create(aObj);
        return ((HistoryModel) aObj).getId();
    }

    public <T> void fillObjects(Class<T> clazz, ArrayList<T> aObjList) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        for (T obj : aObjList) {
            dao.createOrUpdate(obj);
        }
    }

//    public <T> T getById(Class<T> clazz, Object aId) throws SQLException {
//        Dao<T, Object> dao = getDao(clazz);
//        return dao.queryForId(aId);
//    }

    public <T> List<T> query(Class<T> clazz, Map<String, Object> aMap) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);

        return dao.queryForFieldValues(aMap);
    }

    public <T> List<T> queryNot(Class<T> clazz, String columnName, int value) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);

        return dao.queryBuilder().where().ne(columnName, value).query();
    }

    public <T> T queryFirst(Class<T> clazz, Map<String, Object> aMap) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        List<T> list = dao.queryForFieldValues(aMap);
        if (list.size() > 0)
            return list.get(0);
        else
            return null;
    }

//    public <T> Dao.CreateOrUpdateStatus createOrUpdate(T obj) throws SQLException {
//        Dao<T, ?> dao = getDao(obj.getClass());
//        return dao.createOrUpdate(obj);
//    }

    public <T> int deleteById(Class<T> clazz, Object aId) throws SQLException {
        Dao<T, Object> dao = getDao(clazz);
        return dao.deleteById(aId);
    }

    public <T> int deleteObjects(Class<T> clazz, Collection<T> aObjList) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);

        return dao.delete(aObjList);
    }

    public <T> void deleteAll(Class<T> clazz) throws SQLException {
        Dao<T, ?> dao = getDao(clazz);
        dao.deleteBuilder().delete();
    }

    public void ExportDB() {

        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/data/" + mContext.getPackageName() + "/databases/" + DB_NAME;

        File folder = new File(Environment.getExternalStorageDirectory() + "/BBeat");
        if (!folder.exists())
            folder.mkdir();

        File currentDB = new File(currentDBPath);
        File backupDB = new File(folder, DB_NAME);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(mContext, "DB Exported!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
