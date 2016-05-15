package com.zhidao.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBUtilGrid {
	private static DBUtilGrid mInstance;
	private Context mContext;
	private SQLHelperGrid mSQLHelp;
	private SQLiteDatabase mSQLiteDatabase;

	private DBUtilGrid(Context context) {
		mContext = context;
		mSQLHelp = new SQLHelperGrid(context);
		mSQLiteDatabase = mSQLHelp.getWritableDatabase();
	}
	/**
	 * 初始化数据库操作DBUtil类
	 */
	public static DBUtilGrid getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBUtilGrid(context);
		}
		return mInstance;
	}
	/**
	 * 关闭数据库
	 */
	public void close() {
		mSQLHelp.close();
		mSQLHelp = null;
		mSQLiteDatabase.close();
		mSQLiteDatabase = null;
		mInstance = null;
	}

	/**
	 * 添加数据
	 */
	public void insertData(ContentValues values) {
		mSQLiteDatabase.insert(SQLHelperGrid.TABLE_CHANNEL, null, values);
	}

	/**
	 * 更新数据
	 * 
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 */
	public void updateData(ContentValues values, String whereClause,
			String[] whereArgs) {
		mSQLiteDatabase.update(SQLHelperGrid.TABLE_CHANNEL, values, whereClause,
				whereArgs);
	}

	/**
	 * 删除数据
	 * 
	 * @param whereClause
	 * @param whereArgs
	 */
	public void deleteData(String whereClause, String[] whereArgs) {
		mSQLiteDatabase
				.delete(SQLHelperGrid.TABLE_CHANNEL, whereClause, whereArgs);
	}

	/**
	 * 查询数据
	 * 
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public Cursor selectData(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		Cursor cursor = mSQLiteDatabase.query(SQLHelperGrid.TABLE_CHANNEL,columns, selection, selectionArgs, groupBy, having, orderBy);
		return cursor;
	}
}