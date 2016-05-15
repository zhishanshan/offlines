package com.zhidao.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.zhidao.app.MyApplication;
import com.zhidao.db.DBHelper;
import com.zhidao.db.DatabaseUtil;
import com.zhidao.db.DBHelper.FavTable;
import com.zhidao.entity.QiangYu;
import com.zzhidao.utils.LogUtils;

public class DatabaseUtil {
	 private static final String TAG="DatabaseUtil";

	    private static DatabaseUtil instance;

	    /** ���ݿ������ **/
	    private DBHelper dbHelper;

	    public synchronized static DatabaseUtil getInstance(Context context) {
	        if(instance == null) {
	            instance=new DatabaseUtil(context);
	        }
	        return instance;
	    }

	    /**
	     * ��ʼ��
	     * @param context
	     */
	    private DatabaseUtil(Context context) {
	        dbHelper=new DBHelper(context);
	    }

	    /**
	     * ����
	     */
	    public static void destory() {
	        if(instance != null) {
	            instance.onDestory();
	        }
	    }

	    /**
	     * ����
	     */
	    public void onDestory() {
	        instance=null;
	        if(dbHelper != null) {
	            dbHelper.close();
	            dbHelper=null;
	        }
	    }
	    
	    
	    public void deleteFav(QiangYu qy){
	    	Cursor cursor=null;
	    	String where = FavTable.USER_ID+" = '"+MyApplication.getInstance().getCurrentUser().getObjectId()
	    			+"' AND "+FavTable.OBJECT_ID+" = '"+qy.getObjectId()+"'";
	    	cursor=dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
	    	if(cursor != null && cursor.getCount() > 0) {
           	cursor.moveToFirst();
           	int isLove = cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE));
           	if(isLove==0){
           		dbHelper.delete(DBHelper.TABLE_NAME, where, null);
           	}else{
           		ContentValues cv = new ContentValues();
           		cv.put(FavTable.IS_FAV, 0);
           		dbHelper.update(DBHelper.TABLE_NAME, cv, where, null);
           	}
	    	}
	    	if(cursor != null) {
	            cursor.close();
	            dbHelper.close();
	        }
	    }
	    
	    
	    public boolean isLoved(QiangYu qy){
	    	Cursor cursor = null;
	    	String where = FavTable.USER_ID+" = '"+MyApplication.getInstance().getCurrentUser().getObjectId()
	    			+"' AND "+FavTable.OBJECT_ID+" = '"+qy.getObjectId()+"'";
	    	cursor=dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
	    	if(cursor!=null && cursor.getCount()>0){
	    		cursor.moveToFirst();
	    		 if(cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE))==1){
	    			 return true;
	    		 }
	    	}
	    	return false;
	    }
	    
	    public long insertFav(QiangYu qy){
	    	long uri = 0;
	    	Cursor cursor=null;
	    	String where = FavTable.USER_ID+" = '"+MyApplication.getInstance().getCurrentUser().getObjectId()
	    			+"' AND "+FavTable.OBJECT_ID+" = '"+qy.getObjectId()+"'";
           cursor=dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
           if(cursor != null && cursor.getCount() > 0) {
           	cursor.moveToFirst();
           	ContentValues conv = new ContentValues();
           	conv.put(FavTable.IS_FAV, 1);
           	conv.put(FavTable.IS_LOVE, 1);
           	dbHelper.update(DBHelper.TABLE_NAME, conv, where, null);
           }else{
		    	ContentValues cv = new ContentValues();
		    	cv.put(FavTable.USER_ID, MyApplication.getInstance().getCurrentUser().getObjectId());
		    	cv.put(FavTable.OBJECT_ID, qy.getObjectId());
		    	cv.put(FavTable.IS_LOVE, qy.getMyLove()==true?1:0);
		    	cv.put(FavTable.IS_FAV,qy.getMyFav()==true?1:0);
		    	uri = dbHelper.insert(DBHelper.TABLE_NAME, null, cv);
           }
	    	 if(cursor != null) {
		            cursor.close();
		            dbHelper.close();
		        }
	    	return uri;
	    }
	    
//	    public int deleteFav(QiangYu qy){
//	    	int row = 0;
//	    	String where = FavTable.USER_ID+" = "+qy.getAuthor().getObjectId()
//	    			+" AND "+FavTable.OBJECT_ID+" = "+qy.getObjectId();
//	    	row = dbHelper.delete(DBHelper.TABLE_NAME, where, null);
//	    	return row;
//	    }
	    
	    
	    /**
	     * �������ݵ��ղ�״̬
	     * @param context
	     * @param lists
	     */
	    public List<QiangYu> setFav(List<QiangYu> lists) {
	        Cursor cursor=null;
	        if(lists != null && lists.size() > 0) {
	            for(Iterator iterator=lists.iterator(); iterator.hasNext();) {
	            	QiangYu content=(QiangYu)iterator.next();
	            	String where = FavTable.USER_ID+" = '"+MyApplication.getInstance().getCurrentUser().getObjectId()//content.getAuthor().getObjectId()
	    	    			+"' AND "+FavTable.OBJECT_ID+" = '"+content.getObjectId()+"'";
	                cursor=dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
	                if(cursor != null && cursor.getCount() > 0) {
	                	cursor.moveToFirst();
	                	if(cursor.getInt(cursor.getColumnIndex(FavTable.IS_FAV))==1){
	                		content.setMyFav(true);
	                	}else{
	                		content.setMyFav(false);
	                	}
	                    if(cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE))==1){
	                    	content.setMyLove(true);
	                    }else{
	                    	content.setMyLove(false);
	                    }
	                }
	                LogUtils.i(TAG,content.getMyFav()+".."+content.getMyLove());
	            }
	        }
	        if(cursor != null) {
	            cursor.close();
	            dbHelper.close();
	        }
	        return lists;
	    }
	    
	    /**
	     * �������ݵ��ղ�״̬
	     * @param context
	     * @param lists
	     */
	    public List<QiangYu> setFavInFav(List<QiangYu> lists) {
	        Cursor cursor=null;
	        if(lists != null && lists.size() > 0) {
	            for(Iterator iterator=lists.iterator(); iterator.hasNext();) {
	            	QiangYu content=(QiangYu)iterator.next();
	            	content.setMyFav(true);
	            	String where = FavTable.USER_ID+" = '"+MyApplication.getInstance().getCurrentUser().getObjectId()
	    	    			+"' AND "+FavTable.OBJECT_ID+" = '"+content.getObjectId()+"'";
	                cursor=dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
	                if(cursor != null && cursor.getCount() > 0) {
	                	cursor.moveToFirst();
	                    if(cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE))==1){
	                    	content.setMyLove(true);
	                    }else{
	                    	content.setMyLove(false);
	                    }
	                }
	                LogUtils.i(TAG,content.getMyFav()+".."+content.getMyLove());
	            }
	        }
	        if(cursor != null) {
	            cursor.close();
	            dbHelper.close();
	        }
	        return lists;
	    }
	    
	    
	    public ArrayList<QiangYu> queryFav() {
	        ArrayList<QiangYu> contents=null;
	        // ContentResolver resolver = context.getContentResolver();
	        Cursor cursor=dbHelper.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
	        LogUtils.i(TAG, cursor.getCount() + "");
	        if(cursor == null) {
	            return null;
	        }
	        contents=new ArrayList<QiangYu>();
	        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
	        	QiangYu content=new QiangYu();
	        	content.setMyFav(cursor.getInt(3)==1?true:false);
	        	content.setMyLove(cursor.getInt(4)==1?true:false);
	            LogUtils.i(TAG,cursor.getColumnIndex("isfav")+".."+cursor.getColumnIndex("islove")+".."+content.getMyFav()+"..."+content.getMyLove());
	            contents.add(content);
	        }
	        if(cursor != null) {
	            cursor.close();
	        }
	        // if (contents.size() > 0) {
	        // return contents;
	        // }
	        return contents;
	    }

}
