package com.zhidao.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

 
import com.zhidao.dao.GridChannelDao;
import com.zhidao.db.SQLHelperGrid;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

 



public class GridChannelManage {
	public static GridChannelManage GridchannelManage;
	/**
	 * 默认的用户选择频道列表
	 * */
	public static List<GridChannelItem> defaultUserChannels;
	/**
	 * 默认的其他频道列表
	 * */
	public static List<GridChannelItem> defaultOtherChannels;
	private GridChannelDao channelDao;
	/** 判断数据库中是否存在用户数据 */
	private boolean userExist = false;
	static {
		defaultUserChannels = new ArrayList<GridChannelItem>();
		defaultOtherChannels = new ArrayList<GridChannelItem>();
		defaultUserChannels.add(new GridChannelItem(1, "推荐", 1, 1));
		defaultUserChannels.add(new GridChannelItem(2, "超市", 2, 1));
		defaultUserChannels.add(new GridChannelItem(3, "服装", 3, 1));
		defaultUserChannels.add(new GridChannelItem(4, "租房", 4, 1));
		defaultUserChannels.add(new GridChannelItem(5, "电子", 5, 1));
		defaultUserChannels.add(new GridChannelItem(6, "药店", 6, 1));
		defaultUserChannels.add(new GridChannelItem(7, "书店", 7, 1));
		defaultOtherChannels.add(new GridChannelItem(8, "美容", 1, 0));
		defaultOtherChannels.add(new GridChannelItem(9, "汽车", 2, 0));
		defaultOtherChannels.add(new GridChannelItem(10, "汽修", 3, 0));
		defaultOtherChannels.add(new GridChannelItem(11, "特产", 4, 0));
		defaultOtherChannels.add(new GridChannelItem(12, "美食", 5, 0));
		defaultOtherChannels.add(new GridChannelItem(13, "医院", 6, 0));
		defaultOtherChannels.add(new GridChannelItem(14, "影楼", 7, 0));
		defaultOtherChannels.add(new GridChannelItem(15, "酒吧", 8, 0));
		defaultOtherChannels.add(new GridChannelItem(16, "健身", 9, 0));
		defaultOtherChannels.add(new GridChannelItem(17, "娱乐", 10, 0));
		defaultOtherChannels.add(new GridChannelItem(18, "入住", 11, 0));
	}
	private GridChannelManage(SQLHelperGrid paramDBHelper) throws SQLException {
		if (channelDao == null)
			
			channelDao = new GridChannelDao(paramDBHelper.getContext());
		// NavigateItemDao(paramDBHelper.getDao(NavigateItem.class));
		return;
	}
	

	/**
	 * 初始化频道管理类
	 * @param paramDBHelper
	 * @throws SQLException
	 */
	public static GridChannelManage getManage(SQLHelperGrid dbHelper)throws SQLException {
		if (GridchannelManage == null)
			GridchannelManage = new GridChannelManage(dbHelper);
		return GridchannelManage;
	}

	/**
	 * 清除所有的频道
	 */
	public void deleteAllChannel() {
		channelDao.clearFeedTable();
	}
	/**
	 * 获取其他的频道
	 * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
	 */
	public List<GridChannelItem> getUserChannel() {
		Object cacheList = channelDao.listCache(SQLHelperGrid.SELECTED + "= ?",new String[] { "1" });
		if (cacheList != null && !((List) cacheList).isEmpty()) {
			userExist = true;
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();
			List<GridChannelItem> list = new ArrayList<GridChannelItem>();
			for (int i = 0; i < count; i++) {
				GridChannelItem navigate = new GridChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelperGrid.ID)));
				navigate.setName(maplist.get(i).get(SQLHelperGrid.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelperGrid.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelperGrid.SELECTED)));
				list.add(navigate);
			}
			return list;
		}
		initDefaultChannel();
		return defaultUserChannels;
	}
	
	/**
	 * 获取其他的频道
	 * @return 数据库存在用户配置 ? 数据库内的其它频道 : 默认其它频道 ;
	 */
	public List<GridChannelItem> getOtherChannel() {
		Object cacheList = channelDao.listCache(SQLHelperGrid.SELECTED + "= ?" ,new String[] { "0" });
		List<GridChannelItem> list = new ArrayList<GridChannelItem>();
		if (cacheList != null && !((List) cacheList).isEmpty()){
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();
			for (int i = 0; i < count; i++) {
				GridChannelItem navigate= new GridChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelperGrid.ID)));
				navigate.setName(maplist.get(i).get(SQLHelperGrid.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelperGrid.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelperGrid.SELECTED)));
				list.add(navigate);
			}
			return list;
		}
		if(userExist){
			return list;
		}
		cacheList = defaultOtherChannels;
		return (List<GridChannelItem>) cacheList;
	}
	
	/**
	 * 保存用户频道到数据库
	 * @param userList
	 */
	public void saveUserChannel(List<GridChannelItem> userList) {
		for (int i = 0; i < userList.size(); i++) {
			GridChannelItem GridchannelItem = (GridChannelItem) userList.get(i);
			GridchannelItem.setOrderId(i);
			GridchannelItem.setSelected(Integer.valueOf(1));
			channelDao.addCache(GridchannelItem);
		}
	}
	
	/**
	 * 保存其他频道到数据库
	 * @param otherList
	 */
	public void saveOtherChannel(List<GridChannelItem> otherList) {
		for (int i = 0; i < otherList.size(); i++) {
			GridChannelItem GridchannelItem = (GridChannelItem) otherList.get(i);
			GridchannelItem.setOrderId(i);
			GridchannelItem.setSelected(Integer.valueOf(0));
			channelDao.addCache(GridchannelItem);
		}
	}
	
	/**
	 * 初始化数据库内的频道数据
	 */
	private void initDefaultChannel(){
		Log.d("deleteAll", "deleteAll");
		deleteAllChannel();
		saveUserChannel(defaultUserChannels);
		saveOtherChannel(defaultOtherChannels);
	}
}
