package com.zhidao.ui;

import java.util.ArrayList;
import java.util.List;
 
import com.example.zhidao.R;
import com.zhidao.adapter.GridDragAdapter;
import com.zhidao.adapter.GridOtherAdapter;
import com.zhidao.app.MyApplication;
import com.zhidao.base.BaseActivity;
import com.zhidao.bean.GridChannelItem;
import com.zhidao.bean.GridChannelManage;
import com.zhidao.db.SQLHelperGrid;
import com.zhidao.view.DragGrid;
import com.zhidao.view.OtherGridView;


import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
/**
 * Ƶ������
 * @Author RA
 * @Blog http://blog.csdn.net/vipzjyno1
 */
public class GridChannelActivity extends BaseActivity implements OnItemClickListener {
	public static String TAG = "ChannelActivity";
	/** �û���Ŀ��GRIDVIEW */
	private DragGrid userGridView;
	/** ������Ŀ��GRIDVIEW */
	private OtherGridView otherGridView;
	/** �û���Ŀ��Ӧ���������������϶� */
	GridDragAdapter userAdapter;
	/** ������Ŀ��Ӧ�������� */
	GridOtherAdapter otherAdapter;
	/** ������Ŀ�б� */
	ArrayList<GridChannelItem> otherChannelList = new ArrayList<GridChannelItem>();
	/** �û���Ŀ�б� */
	ArrayList<GridChannelItem> userChannelList = new ArrayList<GridChannelItem>();
	/** �Ƿ����ƶ�����������Ƕ���������Ž��е����ݸ��棬�����������Ϊ�˱������̫Ƶ����ɵ����ݴ��ҡ� */	
	boolean isMove = false;
	private TextView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subscribe_activity);
		initView();
		initData();
	}
	
	/** ��ʼ������*/
	private void initData() {
		MyApplication app = (MyApplication) getApplication();	
		if(app == null){
			Log.i("INFO", "appΪ��");
		}
		SQLHelperGrid sqlHelperGrid = app.getSQLHelperGrid();
		if(sqlHelperGrid == null){
			Log.i("INFO", "sqlHelperGrid Ϊ��");
		}
 		GridChannelManage manage = GridChannelManage.getManage(sqlHelperGrid);
 		if(manage == null){
 			Log.i("INFO", "manageΪ��");
 		}
		List<GridChannelItem> userChannel = manage.getUserChannel();
		if(userChannel == null){
			Log.i("INFO", "userChannel Ϊ��");
		}

		userChannelList = ((ArrayList<GridChannelItem>)GridChannelManage.getManage(MyApplication.getApp().getSQLHelperGrid()).getUserChannel());
	    otherChannelList = ((ArrayList<GridChannelItem>)GridChannelManage.getManage(MyApplication.getApp().getSQLHelperGrid()).getOtherChannel());
	    userAdapter = new GridDragAdapter(this, userChannelList);
	    userGridView.setAdapter(userAdapter);
	    otherAdapter = new GridOtherAdapter(this, otherChannelList);
	    otherGridView.setAdapter(this.otherAdapter);
	    //����GRIDVIEW��ITEM�ĵ������
	    otherGridView.setOnItemClickListener(this);
	    userGridView.setOnItemClickListener(this);
	   
	}
	
	/** ��ʼ������*/
	private void initView() {
		userGridView = (DragGrid) findViewById(R.id.userGridView);
		otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** GRIDVIEW��Ӧ��ITEM��������ӿ�  */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view, final int position,long id) {
		//��������ʱ��֮ǰ������û��������ô���õ���¼���Ч
		if(isMove){
			return;
		}
		switch (parent.getId()) {
		case R.id.userGridView:
			//positionΪ 0��1 �Ĳ����Խ����κβ���
			if (position != 0 && position != 1) {
				final ImageView moveImageView = getView(view);
				if (moveImageView != null) {
					TextView newTextView = (TextView) view.findViewById(R.id.text_item);
					final int[] startLocation = new int[2];
					newTextView.getLocationInWindow(startLocation);
					final GridChannelItem channel = ((GridDragAdapter) parent.getAdapter()).getItem(position);//��ȡ�����Ƶ������
					otherAdapter.setVisible(false);
					//��ӵ����һ��
					otherAdapter.addItem(channel);
					new Handler().postDelayed(new Runnable() {
						public void run() {
							try {
								int[] endLocation = new int[2];
								//��ȡ�յ������
								otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
								MoveAnim(moveImageView, startLocation , endLocation, channel,userGridView);
								userAdapter.setRemove(position);
							} catch (Exception localException) {
							}
						}
					}, 50L);
				}
			}
			break;
		case R.id.otherGridView:
			final ImageView moveImageView = getView(view);
			if (moveImageView != null){
				TextView newTextView = (TextView) view.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final GridChannelItem channel = ((GridOtherAdapter) parent.getAdapter()).getItem(position);
				userAdapter.setVisible(false);
				//��ӵ����һ��
				userAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							//��ȡ�յ������
							userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
							MoveAnim(moveImageView, startLocation , endLocation, channel,otherGridView);
							otherAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
			break;
		default:
			break;
		}
	}
	/**
	 * ���ITEM�ƶ�����
	 * @param moveView 
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation,int[] endLocation, final GridChannelItem moveChannel,
			final GridView clickGridView) {
		int[] initLocation = new int[2];
		//��ȡ���ݹ�����VIEW������
		moveView.getLocationInWindow(initLocation);
		//�õ�Ҫ�ƶ���VIEW,�������Ӧ��������
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
		//�����ƶ�����
		TranslateAnimation moveAnimation = new TranslateAnimation(
				startLocation[0], endLocation[0], startLocation[1],
				endLocation[1]);
		moveAnimation.setDuration(300L);//����ʱ��
		//��������
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);//����Ч��ִ����Ϻ�View���󲻱�������ֹ��λ��
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				isMove = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				moveViewGroup.removeView(mMoveView);
				// instanceof �����ж�2��ʵ���ǲ���һ�����жϵ������DragGrid����OtherGridView
				if (clickGridView instanceof DragGrid) {
					otherAdapter.setVisible(true);
					otherAdapter.notifyDataSetChanged();
					userAdapter.remove();
				}else{
					userAdapter.setVisible(true);
					userAdapter.notifyDataSetChanged();
					otherAdapter.remove();
				}
				isMove = false;
			}
		});
	}
	
	/**
	 * ��ȡ�ƶ���VIEW�������ӦViewGroup��������
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}
	
	/**
	 * �����ƶ���ITEM��Ӧ��ViewGroup��������
	 */
	private ViewGroup getMoveViewGroup() {
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}
	
	/**
	 * ��ȡ�����Item�Ķ�ӦView��
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}
	
	/** �˳�ʱ�򱣴�ѡ������ݿ������  */
	private void saveChannel() {
//		AppApplication app = (AppApplication)getApplication();
		GridChannelManage.getManage(MyApplication.getApp().getSQLHelperGrid()).deleteAllChannel();
		GridChannelManage.getManage(MyApplication.getApp().getSQLHelperGrid()).saveUserChannel(userAdapter.getChannnelLst());
		GridChannelManage.getManage(MyApplication.getApp().getSQLHelperGrid()).saveOtherChannel(otherAdapter.getChannnelLst());
	}
	
	@Override
	public void onBackPressed() {
		saveChannel();
		super.onBackPressed();
	}
}
