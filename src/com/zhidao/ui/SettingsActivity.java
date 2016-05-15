package com.zhidao.ui;


import com.example.zhidao.R;
import com.zhidao.base.BaseFragment;
import com.zhidao.base.BaseHomeActivity;
import com.zhidao.fragment.PersonalFragment.IProgressControllor;
import com.zhidao.fragment.SettingsFragment;

import android.view.View;

 

public class SettingsActivity extends BaseHomeActivity implements IProgressControllor{

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return getString(R.string.settings_title);
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void onHomeActionClick() {
		// TODO Auto-generated method stub
		setResult(RESULT_OK);
		finish();
	}

	@Override
	protected BaseFragment getFragment() {
		// TODO Auto-generated method stub
		return SettingsFragment.newInstance();
	}

	@Override
	protected void addActions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		setResult(RESULT_OK);
		finish();
	}
	
	@Override
	public void showActionBarProgress(){
		actionBar.setProgressBarVisibility(View.VISIBLE);
	}
	
	@Override
	public void hideActionBarProgress(){
		actionBar.setProgressBarVisibility(View.GONE);
	}
}
