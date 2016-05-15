package com.zhidao.ui;

import java.util.ArrayList;

import net.youmi.android.offers.OffersManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
 
import com.example.zhidao.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
 
 
import com.tencent.stat.common.User;
 
 
import com.zhidao.adapter.NewsFragmentPagerAdapter;
import com.zhidao.adapter.QiangContentAdapter;
import com.zhidao.app.MyApplication;
import com.zhidao.bean.GridChannelItem;
import com.zhidao.bean.GridChannelManage;
import com.zhidao.db.SQLHelperGrid;
import com.zhidao.fragment.Mainfragment;
import com.zhidao.fragment.NaviFragment;
import com.zhidao.fragment.NewsFragment;
import com.zhidao.tool.BaseTools;
import com.zhidao.view.ColumnHorizontalScrollView;
import com.zzhidao.utils.ActivityUtil;
import com.zzhidao.utils.LogUtils;
 

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {

 	public static final String TAG = "MainActivity";
 	private NaviFragment naviFragment;
 	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private SlidingMenu mSlidingMenu;
	LinearLayout mRadioGroup_content;
	LinearLayout ll_more_columns;
	RelativeLayout rl_column;
	//�ײ��ؼ��ĳ�ʼ��
	RelativeLayout dwlayout;
	RelativeLayout sousuolayout;
	RelativeLayout tixinglayout;
	RelativeLayout wodelayout;
	
	/** ����CODE */
	public final static int CHANNELREQUEST = 1;
	/** �������ص�RESULTCODE */
	public final static int CHANNELRESULT = 10;
	
	//	Ƶ����ӿؼ��ĳ�ʼ��
	private ImageView button_more_columns;
	//�����������ʼ��
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
   	private ViewPager mViewPager;
	
	//Fragment������
//private NewsFragmentPagerAdapter adapter;
	
	// ���ŷ����б�
	private ArrayList<GridChannelItem> userChannelList=new ArrayList<GridChannelItem>();
	// ��ǰѡ�е���Ŀ���Ƽ�
	private int columnSelectIndex = 0;
	/** ����Ӱ����*/
	public ImageView shade_left;
	/** ����Ӱ���� */
	public ImageView shade_right;
	/** ��Ļ��� */
	private int mScreenWidth = 0;
	/** Item��� */
	private int mItemWidth = 0;
 
 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
//		Bmob.initialize(this, "d1cd98e9dca54436292335e603068234");
		//���ӷ�����
//		User user = BmobUser.getCurrentUser(this, User.class);
       
		
		mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 7;// һ��Item���Ϊ��Ļ��1/7
		initView();
		initFragment();

				
			}
	/** ��ʼ��layout�ؼ�*/
	private void initView() {
		mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
		/*
		mViewPager = (ViewPager) findViewById(R.id.viewpager);//���÷�����Ϣ�ĵط�
		
		mAdapter = new QiangContentAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOffscreenPageLimit(4);
		*/
		
		
		shade_left = (ImageView) findViewById(R.id.shade_left);
		shade_right = (ImageView) findViewById(R.id.shade_right);
		//main_bottom
		dwlayout=(RelativeLayout) findViewById(R.id.dw_layout);
		sousuolayout=(RelativeLayout) findViewById(R.id.sousuo_layout);
		tixinglayout=(RelativeLayout) findViewById(R.id.tixing_layout);
     wodelayout=(RelativeLayout) findViewById(R.id.wode_layout);
	     
	    
	     //		main_bottom����
	     dwlayout.setOnClickListener( this);
	     sousuolayout.setOnClickListener( this);
	     tixinglayout.setOnClickListener( this);
	     wodelayout.setOnClickListener( this);
	     
		
		button_more_columns.setOnClickListener(new OnClickListener() {@Override
			public void onClick(View v) {
//			���Ƶ���ļ����¼���Ҫ�������ݸ���һ������
			Intent intent_channel=new Intent(MainActivity.this,GridChannelActivity.class);
			startActivityForResult(intent_channel, CHANNELREQUEST);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		}});
		setChangelView();
	}
 	
	/** 
	 *  ����Ŀ����仯ʱ�����
	 * */
	private void setChangelView() {
		initColumnData();
		initTabColumn();
		initFragment();
	}
	/** ��ȡColumn��Ŀ ����*/
	private void initColumnData() {
		userChannelList = ((ArrayList<GridChannelItem>)GridChannelManage.getManage(MyApplication.getApp().getSQLHelperGrid()).getUserChannel());
	}
	/** 
	 *  ��ʼ��Column��Ŀ��
	 * */
	@SuppressWarnings({ "deprecation", "deprecation" })
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count =  userChannelList.size();
		mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
		for(int i = 0; i< count; i++){
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , LayoutParams.WRAP_CONTENT);
			params.leftMargin = 10;
			params.rightMargin = 10;
//			TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item, null);
			TextView localTextView = new TextView(this);
			localTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
			localTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			localTextView.setGravity(Gravity.CENTER);
			localTextView.setPadding(5, 0, 5, 0);
			localTextView.setId(i);
			localTextView.setText(userChannelList.get(i).getName());
			localTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
			if(columnSelectIndex == i){
				localTextView.setSelected(true);
			}
			localTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				
				public void onClick(View v) {
			          for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
				          View localView = mRadioGroup_content.getChildAt(i);
				          if (localView != v)
				        	  localView.setSelected(false);
				          else{
				        	  localView.setSelected(true);
// 			        	  mViewPager.setCurrentItem(i);
				          }
			          }
			          Toast.makeText(getApplicationContext(), userChannelList.get(v.getId()).getName(), Toast.LENGTH_SHORT).show();
				}
			});
			mRadioGroup_content.addView(localTextView, i ,params);
		}
	}
	/** 
	 *  ѡ���Column�����Tab
	 * */
	private void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			// rg_nav_content.getParent()).smoothScrollTo(i2, 0);
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
			// mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
			// mItemWidth , 0);
		}
		//�ж��Ƿ�ѡ��
		for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}
	
	/** 
	 *  ��ʼ��Fragment
	 * */
	private void initFragment() {
//		fragments.clear();//���
		mSlidingMenu = getSlidingMenu();
		setBehindContentView(R.layout.frame_navi); // ��������slidingmenu��fragment�ƶ�layout
		naviFragment = new NaviFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_navi, naviFragment).commit();
		// ����slidingmenu������
		mSlidingMenu.setMode(SlidingMenu.LEFT);// ����slidingmeni���Ĳ����
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// ֻ���ڱ��ϲſ��Դ�
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// ƫ����
		mSlidingMenu.setFadeEnabled(true);
		mSlidingMenu.setFadeDegree(0.5f);
		mSlidingMenu.setMenu(R.layout.frame_navi);
		// �����򿪼����¼�
				mSlidingMenu.setOnOpenListener(new OnOpenListener() {
					@Override
					public void onOpen() {
					}
				});
				// �����رռ����¼�
				mSlidingMenu.setOnClosedListener(new OnClosedListener() {

					@Override
					public void onClosed() {
					}
				});
		int count =  userChannelList.size();
		for(int i = 0; i< count;i++){
			Bundle data = new Bundle();
    		data.putString("text", userChannelList.get(i).getName());
//			NewsFragment newfragment = new NewsFragment();
    		Mainfragment mainfragment= new Mainfragment();
			mainfragment.setArguments(data);//ѡ�ĸ�Ƶ���ͻ�����ĸ�Ƶ��������
        	fragments.add(mainfragment);
		
		}
	NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mAdapetr);
//		���һ���ʱ���ݵı仯
 	mViewPager.setOnPageChangeListener(pageListener);
		 
	}
	
	
	/** 
	 *  ViewPager�л���������
	 * */
	public OnPageChangeListener pageListener= new OnPageChangeListener(){

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
//			mViewPager.setCurrentItem(position);
			selectTab(position);
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	 
	
public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dw_layout:
			setTabSeclection(0);
			break;
		case R.id.sousuo_layout:
			 
			setTabSeclection(1);
			break;
		case R.id.tixing_layout:
			 
			setTabSeclection(2);
			break;
		case R.id.wode_layout:
			
			setTabSeclection(3);
			break;
		default:
			break;
		}}
	
	
	private void setTabSeclection(int index) {
		// ÿ��ѡ��֮ǰ��������ϴε�ѡ��״̬
		
		switch (index) {
		case 0:
//			Intent intent1=new Intent(MainActivity.this ,SwitchActivity.class);
//			startActivity(intent1);
			break;
		case 1:
			
			break;
		case 2:
			if(mSlidingMenu.isMenuShowing()){
				mSlidingMenu.showContent();
			}else{
				mSlidingMenu.showMenu();
			}
		
			
			break;
		case 3:
			
//			 ��ǰ�û���¼
			BmobUser currentUser = BmobUser.getCurrentUser(MainActivity.this);
			if (currentUser != null) {
				// �����û�ʹ��Ӧ��,�������û���Ψһ��ʶ����������Ϊ�������ݵ��ֶ�
				String name = currentUser.getUsername();
				String email = currentUser.getEmail();
				LogUtils.i(TAG, "username:" + name + ",email:" + email);
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, EditActivity.class);
				startActivity(intent);
			} else {
				// �����û�����Ϊ��ʱ�� �ɴ��û�ע����桭
				Toast.makeText(MainActivity.this, "���ȵ�¼��", Toast.LENGTH_SHORT)
						.show();
				// redictToActivity(mContext, RegisterAndLoginActivity.class,
				// null);
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,
						RegisterAndLoginActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}
	
	
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		OffersManager.getInstance(MainActivity.this).onAppExit();
	}

 

	private static long firstTime;

	 
//	 * ���������η��ؼ����˳�
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
				if (firstTime + 2000 > System.currentTimeMillis()) {
					MyApplication.getInstance().exit();
					super.onBackPressed();
				} else {
					ActivityUtil.show(MainActivity.this, "�ٰ�һ���˳�����");
				}
				firstTime = System.currentTimeMillis();
			}
	
 


	
	
	
	
	
	// ����Ŀ����仯ʱ�����
	 


	
	
}
