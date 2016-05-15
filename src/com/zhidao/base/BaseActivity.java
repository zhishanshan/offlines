package com.zhidao.base;

import com.zhidao.app.MyApplication;
import com.zzhidao.utils.Constant;
import com.zzhidao.utils.Sputil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
 
 

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity implements OnSharedPreferenceChangeListener{
	
	protected static String TAG ;
	
	protected MyApplication mMyApplication;
	protected Sputil sputil;
	protected Resources mResources;
	protected Context mContext;
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		TAG = this.getClass().getSimpleName();
		initConfigure();
		PushAgent.getInstance(mContext).onAppStart();
	}


	private void initConfigure() {
		mContext = this;
		if(null == mMyApplication){
			mMyApplication = MyApplication.getInstance();
		}
		mMyApplication.addActivity(this);
		if(null == sputil){
			sputil = new Sputil(this, Constant.PRE_NAME);
		}
		sputil.getInstance().registerOnSharedPreferenceChangeListener(this);
		mResources = getResources();
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		//�����ڼ������ò�����Ȼ��������Ӧ
	}

	/**
	 * Activity ��ת
	 * @param context
	 * @param targetActivity
	 * @param bundle
	 */
	public void redictToActivity(Context context,Class<?> targetActivity,Bundle bundle){
		Intent intent = new Intent(context, targetActivity);
		if(null != bundle){
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
}
