package com.zhidao.entity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Business extends BmobUser {
public static final String TAG = "business";
	
	private String registernum;
	private BmobFile avatar;
	private BmobRelation relation;
	private String type;
	public BmobFile getAvatar() {
		return avatar;
	}
	public void setAvatar(BmobFile avatar) {
		this.avatar = avatar;
	}
	public String getRegisternum() {
		return registernum;
	}
	public void setRegisternum(String registernum) {
		this.registernum = registernum;
	}
	public BmobRelation getRelation() {
		return relation;
	}
	public void setRelation(BmobRelation relation) {
		this.relation = relation;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
