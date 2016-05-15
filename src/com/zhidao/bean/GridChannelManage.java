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
	 * Ĭ�ϵ��û�ѡ��Ƶ���б�
	 * */
	public static List<GridChannelItem> defaultUserChannels;
	/**
	 * Ĭ�ϵ�����Ƶ���б�
	 * */
	public static List<GridChannelItem> defaultOtherChannels;
	private GridChannelDao channelDao;
	/** �ж����ݿ����Ƿ�����û����� */
	private boolean userExist = false;
	static {
		defaultUserChannels = new ArrayList<GridChannelItem>();
		defaultOtherChannels = new ArrayList<GridChannelItem>();
		defaultUserChannels.add(new GridChannelItem(1, "�Ƽ�", 1, 1));
		defaultUserChannels.add(new GridChannelItem(2, "����", 2, 1));
		defaultUserChannels.add(new GridChannelItem(3, "��װ", 3, 1));
		defaultUserChannels.add(new GridChannelItem(4, "�ⷿ", 4, 1));
		defaultUserChannels.add(new GridChannelItem(5, "����", 5, 1));
		defaultUserChannels.add(new GridChannelItem(6, "ҩ��", 6, 1));
		defaultUserChannels.add(new GridChannelItem(7, "���", 7, 1));
		defaultOtherChannels.add(new GridChannelItem(8, "����", 1, 0));
		defaultOtherChannels.add(new GridChannelItem(9, "����", 2, 0));
		defaultOtherChannels.add(new GridChannelItem(10, "����", 3, 0));
		defaultOtherChannels.add(new GridChannelItem(11, "�ز�", 4, 0));
		defaultOtherChannels.add(new GridChannelItem(12, "��ʳ", 5, 0));
		defaultOtherChannels.add(new GridChannelItem(13, "ҽԺ", 6, 0));
		defaultOtherChannels.add(new GridChannelItem(14, "Ӱ¥", 7, 0));
		defaultOtherChannels.add(new GridChannelItem(15, "�ư�", 8, 0));
		defaultOtherChannels.add(new GridChannelItem(16, "����", 9, 0));
		defaultOtherChannels.add(new GridChannelItem(17, "����", 10, 0));
		defaultOtherChannels.add(new GridChannelItem(18, "��ס", 11, 0));
	}
	private GridChannelManage(SQLHelperGrid paramDBHelper) throws SQLException {
		if (channelDao == null)
			
			channelDao = new GridChannelDao(paramDBHelper.getContext());
		// NavigateItemDao(paramDBHelper.getDao(NavigateItem.class));
		return;
	}
	

	/**
	 * ��ʼ��Ƶ��������
	 * @param paramDBHelper
	 * @throws SQLException
	 */
	public static GridChannelManage getManage(SQLHelperGrid dbHelper)throws SQLException {
		if (GridchannelManage == null)
			GridchannelManage = new GridChannelManage(dbHelper);
		return GridchannelManage;
	}

	/**
	 * ������е�Ƶ��
	 */
	public void deleteAllChannel() {
		channelDao.clearFeedTable();
	}
	/**
	 * ��ȡ������Ƶ��
	 * @return ���ݿ�����û����� ? ���ݿ��ڵ��û�ѡ��Ƶ�� : Ĭ���û�ѡ��Ƶ�� ;
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
	 * ��ȡ������Ƶ��
	 * @return ���ݿ�����û����� ? ���ݿ��ڵ�����Ƶ�� : Ĭ������Ƶ�� ;
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
	 * �����û�Ƶ�������ݿ�
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
	 * ��������Ƶ�������ݿ�
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
	 * ��ʼ�����ݿ��ڵ�Ƶ������
	 */
	private void initDefaultChannel(){
		Log.d("deleteAll", "deleteAll");
		deleteAllChannel();
		saveUserChannel(defaultUserChannels);
		saveOtherChannel(defaultOtherChannels);
	}
}
