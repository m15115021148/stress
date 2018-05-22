package com.meigsmart.slb767_stress.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenMeng on 2017/11/1.
 */

public class FunctionDao {
    private DBOpenHelper locationHelper;
    private SQLiteDatabase locationDb;
    public static String TABLE = "STRESS";
    public static String ID = "id";
    public static String NAME = "sName";
    public static String RESULTS = "results";
    public static String REASON = "reason";
    public static String SELECT = "sSelect";

    public FunctionDao(Context context) {
        locationHelper = new DBOpenHelper(context);
    }

    /**
     * 添加一条新记录
     *
     * @param bean
     */
    public void addData(FunctionBean bean) {
        locationDb = locationHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, bean.getsName());
        values.put(SELECT, bean.getsSelect());
        values.put(RESULTS, bean.getResults());
        values.put(REASON,bean.getReason());

        locationDb.insert(TABLE, null, values);
        locationDb.close();
    }

    /**
     * 删除指定记录
     */
    public void delete(String name) {
        locationDb = locationHelper.getReadableDatabase();
        if (locationDb.isOpen())
            locationDb.delete(TABLE, NAME + "=?", new String[]{name});
        locationDb.close();
    }

    /**
     * 删除记录
     */
    public void deleteAll() {
        locationDb = locationHelper.getReadableDatabase();
        if (locationDb.isOpen())
            locationDb.delete(TABLE, null, null);
        locationDb.close();
    }

    /**
     * 更新数据
     */
    public void update(String name,int select,String reason) {
        locationDb = locationHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(SELECT, select);
        if (!TextUtils.isEmpty(reason))values.put(REASON,reason);
        if (locationDb.isOpen())
            locationDb.update(TABLE, values, NAME + "=?", new String[]{name});
        locationDb.close();
    }

    /**
     * 重置
     */
    public void reset() {
        locationDb = locationHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(SELECT, 0);
        if (locationDb.isOpen())
            locationDb.update(TABLE, values, null, null);
        locationDb.close();
    }

    /**
     * 查询记录
     *
     * @return
     */
    public FunctionBean getData(String name) {
        FunctionBean bean = new FunctionBean();

        String sql = "select * from " + TABLE + " where "+ NAME + "=? ";
        locationDb = locationHelper.getReadableDatabase();
        Cursor cursor = locationDb.rawQuery(sql, new String[]{name});

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(ID));
            String sName = cursor.getString(cursor.getColumnIndex(NAME));
            int result = cursor.getInt(cursor.getColumnIndex(RESULTS));
            String reason = cursor.getString(cursor.getColumnIndex(REASON));
            int select = cursor.getInt(cursor.getColumnIndex(SELECT));

            bean.setId(id);
            bean.setsName(sName);
            bean.setsSelect(select);
            bean.setResults(result);
            bean.setReason(reason);

        }
        cursor.close();
        //关闭数据库
        locationDb.close();
        return bean;
    }

    /**
     * 查询所有的记录
     *
     * @return
     */
    public List<FunctionBean> getAllData() {
        List<FunctionBean> dataList = new ArrayList<>();

        String sql = "select * from " + TABLE;
        locationDb = locationHelper.getReadableDatabase();
        Cursor cursor = locationDb.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(ID));
            String sName = cursor.getString(cursor.getColumnIndex(NAME));
            int result = cursor.getInt(cursor.getColumnIndex(RESULTS));
            String reason = cursor.getString(cursor.getColumnIndex(REASON));
            int select = cursor.getInt(cursor.getColumnIndex(SELECT));

            FunctionBean bean = new FunctionBean();
            bean.setId(id);
            bean.setsName(sName);
            bean.setsSelect(select);
            bean.setResults(result);
            bean.setReason(reason);

            dataList.add(bean);
        }
        cursor.close();
        locationDb.close();
        return dataList;
    }

}
