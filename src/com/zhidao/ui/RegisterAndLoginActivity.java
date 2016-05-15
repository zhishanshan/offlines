package com.zhidao.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import cn.bmob.v3.Bmob;

import com.example.zhidao.R;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.zhidao.base.BasePageActivity;
import com.zhidao.proxy.UserProxy;
import com.zhidao.proxy.UserProxy.ILoginListener;
import com.zhidao.proxy.UserProxy.IResetPasswordListener;
import com.zhidao.proxy.UserProxy.ISignUpListener;
import com.zhidao.view.DeletableEditText;
import com.zzhidao.utils.ActivityUtil;
import com.zzhidao.utils.LogUtils;

import fr.castorflex.android.StringUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;



/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-3-13
 * TODO
 */

public class RegisterAndLoginActivity extends BasePageActivity 
	implements OnClickListener,ILoginListener,ISignUpListener,IResetPasswordListener{

	ActionBar actionbar;
	TextView loginTitle;
	TextView registerTitle;
	TextView resetPassword;
	
	DeletableEditText userNameInput;
	DeletableEditText userPasswordInput;
	DeletableEditText userEmailInput;
	
	Button registerButton;
	SmoothProgressBar progressbar;
	UserProxy userProxy;
	
	private enum UserOperation{
		LOGIN,REGISTER,RESET_PASSWORD
	}
	
	UserOperation operation = UserOperation.LOGIN;
	
	@Override
	protected void setLayoutView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_register);
	}

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		actionbar = (ActionBar)findViewById(R.id.actionbar_register);
		
		loginTitle = (TextView)findViewById(R.id.login_menu);
		registerTitle = (TextView)findViewById(R.id.register_menu);
		resetPassword = (TextView)findViewById(R.id.reset_password_menu);
		
		userNameInput = (DeletableEditText)findViewById(R.id.user_name_input);
		userPasswordInput = (DeletableEditText)findViewById(R.id.user_password_input);
		userEmailInput = (DeletableEditText)findViewById(R.id.user_email_input);
		
		registerButton = (Button)findViewById(R.id.register);
		progressbar = (SmoothProgressBar)findViewById(R.id.sm_progressbar);
	}

	@Override
	protected void setupViews(Bundle bundle) {
		// TODO Auto-generated method stub
		 actionbar.setTitle("�˺Ź���");
		 actionbar.setDisplayHomeAsUpEnabled(true);
		 actionbar.setHomeAction(new Action() {
			
			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				finish();
			}
			
			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.logo;
			}
		});
		 updateLayout(operation);

		 userProxy = new UserProxy(mContext);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub'
		loginTitle.setOnClickListener(this);
		registerTitle.setOnClickListener(this);
		resetPassword.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		
	}

	@Override
	protected void fetchData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register:
			if(operation == UserOperation.LOGIN){
				if(TextUtils.isEmpty(userNameInput.getText())){
					userNameInput.setShakeAnimation();
					Toast.makeText(mContext, "�������û���", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(userPasswordInput.getText())){
					userPasswordInput.setShakeAnimation();
					Toast.makeText(mContext, "����������", Toast.LENGTH_SHORT).show();
					return;
				}
				
				userProxy.setOnLoginListener(this);
				LogUtils.i(TAG,"login begin....");
				progressbar.setVisibility(View.VISIBLE);
				userProxy.login(userNameInput.getText().toString().trim(), userPasswordInput.getText().toString().trim());

			}else if(operation == UserOperation.REGISTER){
				if(TextUtils.isEmpty(userNameInput.getText())){
					userNameInput.setShakeAnimation();
					Toast.makeText(mContext, "�������û���", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(userPasswordInput.getText())){
					userPasswordInput.setShakeAnimation();
					Toast.makeText(mContext, "����������", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(userEmailInput.getText())){
					userEmailInput.setShakeAnimation();
					Toast.makeText(mContext, "�����������ַ", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!StringUtils.isValidEmail(userEmailInput.getText())){
					userEmailInput.setShakeAnimation();
					Toast.makeText(mContext, "�����ʽ����ȷ", Toast.LENGTH_SHORT).show();
					return;
				}
				
				userProxy.setOnSignUpListener(this);
				LogUtils.i(TAG,"register begin....");
				progressbar.setVisibility(View.VISIBLE);
				userProxy.signUp(userNameInput.getText().toString().trim(),
						userPasswordInput.getText().toString().trim(), 
						userEmailInput.getText().toString().trim());
			}else{
				if(TextUtils.isEmpty(userEmailInput.getText())){
					userEmailInput.setShakeAnimation();
					Toast.makeText(mContext, "�����������ַ", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!StringUtils.isValidEmail(userEmailInput.getText())){
					userEmailInput.setShakeAnimation();
					Toast.makeText(mContext, "�����ʽ����ȷ", Toast.LENGTH_SHORT).show();
					return;
				}
				
				userProxy.setOnResetPasswordListener(this);
				LogUtils.i(TAG,"reset password begin....");
				progressbar.setVisibility(View.VISIBLE);
//				userProxy.resetPassword(userEmailInput.getText().toString().trim());
			}
			break;
		case R.id.login_menu:
			operation = UserOperation.LOGIN;
			updateLayout(operation);
			break;
		case R.id.register_menu:
			operation = UserOperation.REGISTER;
			updateLayout(operation);
			break;
		case R.id.reset_password_menu:
			operation = UserOperation.RESET_PASSWORD;
			updateLayout(operation);
			break;
		default:
			break;
		}
	}

	private void updateLayout(UserOperation op){
		if(op == UserOperation.LOGIN){
			loginTitle.setTextColor(Color.parseColor("#D95555"));
			loginTitle.setBackgroundResource(R.drawable.bg_login_tab);
			loginTitle.setPadding(16, 16, 16, 16);
			loginTitle.setGravity(Gravity.CENTER);
			
			
			registerTitle.setTextColor(Color.parseColor("#888888"));
			registerTitle.setBackgroundDrawable(null);
			registerTitle.setPadding(16, 16, 16, 16);
			registerTitle.setGravity(Gravity.CENTER);
			
			resetPassword.setTextColor(Color.parseColor("#888888"));
			resetPassword.setBackgroundDrawable(null);
			resetPassword.setPadding(16, 16, 16, 16);
			resetPassword.setGravity(Gravity.CENTER);
			
			userNameInput.setVisibility(View.VISIBLE);
			userPasswordInput.setVisibility(View.VISIBLE);
			userEmailInput.setVisibility(View.GONE);
			registerButton.setText("��¼");
		}else if(op == UserOperation.REGISTER){
			loginTitle.setTextColor(Color.parseColor("#888888"));
			loginTitle.setBackgroundDrawable(null);
			loginTitle.setPadding(16, 16, 16, 16);
			loginTitle.setGravity(Gravity.CENTER);
			
			registerTitle.setTextColor(Color.parseColor("#D95555"));
			registerTitle.setBackgroundResource(R.drawable.bg_login_tab);
			registerTitle.setPadding(16, 16, 16, 16);
			registerTitle.setGravity(Gravity.CENTER);
			
			resetPassword.setTextColor(Color.parseColor("#888888"));
			resetPassword.setBackgroundDrawable(null);
			resetPassword.setPadding(16, 16, 16, 16);
			resetPassword.setGravity(Gravity.CENTER);
			
			userNameInput.setVisibility(View.VISIBLE);
			userPasswordInput.setVisibility(View.VISIBLE);
			userEmailInput.setVisibility(View.VISIBLE);
			registerButton.setText("ע��");
		}else{
			loginTitle.setTextColor(Color.parseColor("#888888"));
			loginTitle.setBackgroundDrawable(null);
			loginTitle.setPadding(16, 16, 16, 16);
			loginTitle.setGravity(Gravity.CENTER);
			
			registerTitle.setTextColor(Color.parseColor("#888888"));
			registerTitle.setBackgroundDrawable(null);
			registerTitle.setPadding(16, 16, 16, 16);
			registerTitle.setGravity(Gravity.CENTER);
			
			resetPassword.setTextColor(Color.parseColor("#D95555"));
			resetPassword.setBackgroundResource(R.drawable.bg_login_tab);
			resetPassword.setPadding(16, 16, 16, 16);
			resetPassword.setGravity(Gravity.CENTER);
			
			
			userNameInput.setVisibility(View.GONE);
			userPasswordInput.setVisibility(View.GONE);
			userEmailInput.setVisibility(View.VISIBLE);
			registerButton.setText("�һ�����");
		}
	}

	private void dimissProgressbar(){
		if(progressbar!=null&&progressbar.isShown()){
			progressbar.setVisibility(View.GONE);
		}
	}

	@Override
	public void onLoginSuccess() {
		// TODO Auto-generated method stub
		dimissProgressbar();
		ActivityUtil.show(this, "��¼�ɹ���");
		LogUtils.i(TAG,"login sucessed!");
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onLoginFailure(String msg) {
		// TODO Auto-generated method stub
		dimissProgressbar();
		ActivityUtil.show(this, "��¼ʧ�ܡ���ȷ���������Ӻ������ԡ�");
		LogUtils.i(TAG,"login failed!"+msg);
	}

	@Override
	public void onSignUpSuccess() {
		// TODO Auto-generated method stub
		dimissProgressbar();
		ActivityUtil.show(this, "ע��ɹ�");
		LogUtils.i(TAG,"register successed��");
		operation = UserOperation.LOGIN;
		updateLayout(operation);
	}

	@Override
	public void onSignUpFailure(String msg) {
		// TODO Auto-generated method stub
		dimissProgressbar();
		ActivityUtil.show(this, "ע��ʧ�ܡ���ȷ���������Ӻ������ԡ�");
		LogUtils.i(TAG,"register failed��");
	}

	@Override
	public void onResetSuccess() {
		// TODO Auto-generated method stub
		dimissProgressbar();
		ActivityUtil.showL(this, "�뵽�����޸�������ٵ�¼��");
		LogUtils.i(TAG,"reset successed��");
		operation = UserOperation.LOGIN;
		updateLayout(operation);
	}

	@Override
	public void onResetFailure(String msg) {
		// TODO Auto-generated method stub
		dimissProgressbar();
		ActivityUtil.show(this, "��������ʧ�ܡ���ȷ���������Ӻ������ԡ�");
		LogUtils.i(TAG,"register failed��");
	}

}
