package com.zhidao.dao;

import java.util.List;
import java.util.Map;

import com.zhidao.bean.GridChannelItem;

import android.content.ContentValues;

 

public interface GridChannelDaoInface {
	public boolean addCache(GridChannelItem item);

	public boolean deleteCache(String whereClause, String[] whereArgs);

	public boolean updateCache(ContentValues values, String whereClause,
			String[] whereArgs);

	public Map<String, String> viewCache(String selection,
			String[] selectionArgs);

	public List<Map<String, String>> listCache(String selection,
			String[] selectionArgs);

	public void clearFeedTable();
}
