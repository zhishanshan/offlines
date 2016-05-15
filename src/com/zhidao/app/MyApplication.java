package com.zhidao.app;

import java.io.File;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import cn.bmob.v3.BmobUser;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
 
 
 
//import com.xgr.wonderful.entity.QiangYu;
//import com.xgr.wonderful.entity.User;
//import com.xgr.wonderful.utils.ActivityManagerUtils;
import com.zhidao.db.SQLHelperGrid;
import com.zhidao.entity.QiangYu;
import com.zhidao.entity.User;
import com.zzhidao.utils.ActivityManagerUtils;

public class MyApplication extends Application{

//	public static String TAG;
	private QiangYu currentQiangYu = null;
	private static MyApplication myApplication ;
	private SQLHelperGrid sqlHelper;
	
 	public static MyApplication getInstance(){
 		return myApplication;
 	}
	
	public User getCurrentUser() {
		User user = BmobUser.getCurrentUser(myApplication, User.class);
		if(user!=null){
			return user;
		}
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initImageLoader(getApplicationContext());
		myApplication = this;
//		initImageLoader();
	}
	/** ��ȡApplication */
	public static MyApplication getApp(){
		return myApplication;
	}
	
	
	/** ��ȡ���ݿ�Helper */
	public SQLHelperGrid getSQLHelperGrid() {
		if (sqlHelper == null)
			sqlHelper = new SQLHelperGrid(myApplication);
		return sqlHelper;
	}

	
 
	public QiangYu getCurrentQiangYu() {
		return currentQiangYu;
	}

	public void setCurrentQiangYu(QiangYu currentQiangYu) {
		this.currentQiangYu = currentQiangYu;
	}
 
	public void addActivity(Activity ac){
		ActivityManagerUtils.getInstance().addActivity(ac);
	}
	
	public void exit(){
		ActivityManagerUtils.getInstance().removeAllActivity();
	}
	
	public Activity getTopActivity(){
		return ActivityManagerUtils.getInstance().getTopActivity();
	}
	
	/**
	 *  ��ʼ��imageLoader
	 */
	public void initImageLoader(Context context){
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,"zhidao/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//										.memoryCache(new LruMemoryCache(5*1024*1024))// max width, max height���������ÿ�������ļ�����󳤿�
//										.memoryCacheSize(10*1024*1024)
										.threadPoolSize(3)//�̳߳��ڼ��ص�����
										.threadPriority(Thread.NORM_PRIORITY - 2)
										.denyCacheImageMultipleSizesInMemory()
										.discCache(new UnlimitedDiscCache(cacheDir))
										.discCacheFileNameGenerator(new HashCodeFileNameGenerator())//�������ʱ���URI������HASHCODE����
										.writeDebugLogs()
										.build();
		ImageLoader.getInstance().init(config);
	}
	 
	public DisplayImageOptions getOptions(int drawableId){
		return new DisplayImageOptions.Builder()
		.showImageOnLoading(drawableId)
		.showImageForEmptyUri(drawableId)
		.showImageOnFail(drawableId)
		.resetViewBeforeLoading(true)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	
 
	

	/** �ݻ�Ӧ�ý���ʱ����� */
	public void onTerminate() {
		if (sqlHelper != null)
			sqlHelper.close();
		super.onTerminate();
	}

	public void clearAppCache() {
	}
}
