package com.zzhidao.utils;

import com.example.zhidao.BuildConfig;

import android.util.Log;

 

/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-21
 * TODO Log工具类，设置�?关，防止发布版本时log信息泄露
 */

public class LogUtils {

		public static void v(String tag, String msg) {
			if (BuildConfig.DEBUG) {
				Log.v(tag, msg);
			}

		}

		public static void d(String tag, String msg) {
			if (BuildConfig.DEBUG) {
				Log.d(tag, msg);
			}

		}

		public static void i(String tag, String msg) {
			if (BuildConfig.DEBUG) {
				Log.i(tag, msg);
			}

		}

		public static void w(String tag, String msg) {
			if (BuildConfig.DEBUG) {
				Log.w(tag, msg);
			}

		}

		public static void e(String tag, String msg) {
			if (BuildConfig.DEBUG) {
				Log.e(tag, msg);
			}
		}

}
