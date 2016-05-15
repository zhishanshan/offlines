package com.zzhidao.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * @author  ���繤����
 */
public class NetworkUtil{

	/**
	 * ���������Ƿ���á���ҪȨ�ޣ�
	 * <p>
	 * <b> < uses-permission
	 * android:name="android.permission.ACCESS_NETWORK_STATE" /> </b>
	 * </p>
	 * 
	 * @param context
	 *            ������
	 * @return ��������򷵻�true�����򷵻�false
	 */
	public static boolean isAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null && info.isAvailable();
	}

	/**
	 * �ж���������״̬
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetType(Context context) {

		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {

				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {

					if (info.getState() == NetworkInfo.State.CONNECTED) {
						if (info.getType() == ConnectivityManager.TYPE_WIFI) {
							// wifi
							return Constant.NETWORK_TYPE_WIFI;
						} else {
							// �ֻ�����
							return Constant.NETWORK_TYPE_MOBILE;
						}
					}
				}
			}
		} catch (Exception e) {
			// �������
			return Constant.NETWORK_TYPE_ERROR;
		}
		// �������
		return Constant.NETWORK_TYPE_ERROR;

	}

	/**
	 * ����Wifi�Ƿ�����
	 * 
	 * @param context
	 *            ������
	 * @return Wifi��������򷵻�true�����򷵻�false
	 */
	public static boolean isWIFIActivate(Context context) {
		return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
				.isWifiEnabled();
	}

	/**
	 * �޸�Wifi״̬
	 * 
	 * @param context
	 *            ������
	 * @param status
	 *            trueΪ����Wifi��falseΪ�ر�Wifi
	 */
	public static void changeWIFIStatus(Context context, boolean status) {
		((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
				.setWifiEnabled(status);
	}
}
