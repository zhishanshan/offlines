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
	//底部控件的初始化
	RelativeLayout dwlayout;
	RelativeLayout sousuolayout;
	RelativeLayout tixinglayout;
	RelativeLayout wodelayout;
	
	/** 请求CODE */
	public final static int CHANNELREQUEST = 1;
	/** 调整返回的RESULTCODE */
	public final static int CHANNELRESULT = 10;
	
	//	频道添加控件的初始化
	private ImageView button_more_columns;
	//横向滚动条初始化
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
   	private ViewPager mViewPager;
	
	//Fragment适配器
//private NewsFragmentPagerAdapter adapter;
	
	// 新闻分类列表
	private ArrayList<GridChannelItem> userChannelList=new ArrayList<GridChannelItem>();
	// 当前选中的栏目：推荐
	private int columnSelectIndex = 0;
	/** 左阴影部分*/
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;
 
 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
//		Bmob.initialize(this, "d1cd98e9dca54436292335e603068234");
		//连接服务器
//		User user = BmobUser.getCurrentUser(this, User.class);
       
		
		mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7
		initView();
		initFragment();

				
			}
	/** 初始化layout控件*/
	private void initView() {
		mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
		/*
		mViewPager = (ViewPager) findViewById(R.id.viewpager);//放置发布信息的地方
		
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
	     
	    
	     //		main_bottom监听
	     dwlayout.setOnClickListener( this);
	     sousuolayout.setOnClickListener( this);
	     tixinglayout.setOnClickListener( this);
	     wodelayout.setOnClickListener( this);
	     
		
		button_more_columns.setOnClickListener(new OnClickListener() {@Override
			public void onClick(View v) {
//			添加频道的监听事件，要返回数据给上一个界面
			Intent intent_channel=new Intent(MainActivity.this,GridChannelActivity.class);
			startActivityForResult(intent_channel, CHANNELREQUEST);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		}});
		setChangelView();
	}
 	
	/** 
	 *  当栏目项发生变化时候调用
	 * */
	private void setChangelView() {
		initColumnData();
		initTabColumn();
		initFragment();
	}
	/** 获取Column栏目 数据*/
	private void initColumnData() {
		userChannelList = ((ArrayList<GridChannelItem>)GridChannelManage.getManage(MyApplication.getApp().getSQLHelperGrid()).getUserChannel());
	}
	/** 
	 *  初始化Column栏目项
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
	 *  选择的Column里面的Tab
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
		//判断是否选中
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
	 *  初始化Fragment
	 * */
	private void initFragment() {
//		fragments.clear();//清空
		mSlidingMenu = getSlidingMenu();
		setBehindContentView(R.layout.frame_navi); // 给滑出的slidingmenu的fragment制定layout
		naviFragment = new NaviFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_navi, naviFragment).commit();
		// 设置slidingmenu的属性
		mSlidingMenu.setMode(SlidingMenu.LEFT);// 设置slidingmeni从哪侧出现
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 只有在边上才可以打开
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 偏移量
		mSlidingMenu.setFadeEnabled(true);
		mSlidingMenu.setFadeDegree(0.5f);
		mSlidingMenu.setMenu(R.layout.frame_navi);
		// 导航打开监听事件
				mSlidingMenu.setOnOpenListener(new OnOpenListener() {
					@Override
					public void onOpen() {
					}
				});
				// 导航关闭监听事件
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
			mainfragment.setArguments(data);//选哪个频道就会出现哪个频道的名字
        	fragments.add(mainfragment);
		
		}
	NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mAdapetr);
//		左右滑动时内容的变化
 	mViewPager.setOnPageChangeListener(pageListener);
		 
	}
	
	
	/** 
	 *  ViewPager切换监听方法
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
		// 每次选中之前先清除掉上次的选中状态
		
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
			
//			 当前用户登录
			BmobUser currentUser = BmobUser.getCurrentUser(MainActivity.this);
			if (currentUser != null) {
				// 允许用户使用应用,即有了用户的唯一标识符，可以作为发布内容的字段
				String name = currentUser.getUsername();
				String email = currentUser.getEmail();
				LogUtils.i(TAG, "username:" + name + ",email:" + email);
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, EditActivity.class);
				startActivity(intent);
			} else {
				// 缓存用户对象为空时， 可打开用户注册界面…
				Toast.makeText(MainActivity.this, "请先登录。", Toast.LENGTH_SHORT)
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

	 
//	 * 连续按两次返回键就退出
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
				if (firstTime + 2000 > System.currentTimeMillis()) {
					MyApplication.getInstance().exit();
					super.onBackPressed();
				} else {
					ActivityUtil.show(MainActivity.this, "再按一次退出程序");
				}
				firstTime = System.currentTimeMillis();
			}
	
 


	
	
	
	
	
	// 当栏目项发生变化时候调用
	 


	
	
}
