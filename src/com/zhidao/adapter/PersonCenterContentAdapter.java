package com.zhidao.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.zhidao.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhidao.app.MyApplication;
import com.zhidao.entity.QiangYu;
import com.zhidao.entity.User;
import com.zhidao.sns.TencentShare;
import com.zhidao.sns.TencentShareEntity;
import com.zhidao.ui.CommentActivity;
import com.zhidao.ui.RegisterAndLoginActivity;
import com.zzhidao.utils.ActivityUtil;
import com.zzhidao.utils.Constant;
import com.zzhidao.utils.LogUtils;
 
 
 

/**
 * @author kingofglory email: kingofglory@yeah.net blog: http:www.google.com
 * @date 2014-2-24 TODO
 */

public class PersonCenterContentAdapter extends BaseContentAdapter<QiangYu> {

	public static final String TAG = "AIContentAdapter";
	public static final int SAVE_FAVOURITE = 2;

	public PersonCenterContentAdapter(Context context, List<QiangYu> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.ai_item, null);
			viewHolder.userName = (TextView) convertView
					.findViewById(R.id.user_name);
			viewHolder.userLogo = (ImageView) convertView
					.findViewById(R.id.user_logo);
			viewHolder.favMark = (ImageView) convertView
					.findViewById(R.id.item_action_fav);
			viewHolder.contentText = (TextView) convertView
					.findViewById(R.id.content_text);
			viewHolder.contentImage = (ImageView) convertView
					.findViewById(R.id.content_image);
			viewHolder.love = (TextView) convertView
					.findViewById(R.id.item_action_love);
			viewHolder.hate = (TextView) convertView
					.findViewById(R.id.item_action_hate);
			viewHolder.share = (TextView) convertView
					.findViewById(R.id.item_action_share);
			viewHolder.comment = (TextView) convertView
					.findViewById(R.id.item_action_comment);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final QiangYu entity = dataList.get(position);
		LogUtils.i("user", entity.toString());
		User user = entity.getAuthor();
		if (user == null) {
			LogUtils.i("user", "USER IS NULL");
		}
		if (user.getAvatar() == null) {
			LogUtils.i("user", "USER avatar IS NULL");
		}
		String avatarUrl = null;
		if (user.getAvatar() != null) {
			avatarUrl = user.getAvatar().getFileUrl(mContext);
		}
		ImageLoader.getInstance().displayImage(
				avatarUrl,
				viewHolder.userLogo,
				MyApplication.getInstance().getOptions(
						R.drawable.user_icon_default_main));
		viewHolder.userLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.getInstance().setCurrentQiangYu(entity);
				// User currentUser =
				// BmobUser.getCurrentUser(mContext,User.class);
				// if(currentUser != null){//已登�?
				// Intent intent = new Intent();
				// intent.setClass(MyApplication.getInstance().getTopActivity(),
				// PersonalActivity.class);
				// mContext.startActivity(intent);
				// }else{//未登�?
				// ActivityUtil.show(mContext, "请先登录�?");
				// Intent intent = new Intent();
				// intent.setClass(MyApplication.getInstance().getTopActivity(),
				// RegisterAndLoginActivity.class);
				// MyApplication.getInstance().getTopActivity().startActivityForResult(intent,
				// Constant.GO_SETTINGS);
				// }
			}
		});
		viewHolder.userName.setText(entity.getAuthor().getUsername());
		viewHolder.contentText.setText(entity.getContent());
		if (null == entity.getContentfigureurl()) {
			viewHolder.contentImage.setVisibility(View.GONE);
		} else {
			viewHolder.contentImage.setVisibility(View.VISIBLE);
			ImageLoader
					.getInstance()
					.displayImage(
							entity.getContentfigureurl().getFileUrl(mContext) == null ? ""
									: entity.getContentfigureurl().getFileUrl(
											mContext),
							viewHolder.contentImage,
							MyApplication.getInstance().getOptions(
									R.drawable.bg_pic_loading));
		}
		viewHolder.love.setText(entity.getLove() + "");
		LogUtils.i("love", entity.getMyLove() + "..");
		if (entity.getMyLove()) {
			viewHolder.love.setTextColor(Color.parseColor("#D95555"));
		} else {
			viewHolder.love.setTextColor(Color.parseColor("#000000"));
		}
		viewHolder.hate.setText(entity.getHate() + "");
		viewHolder.love.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (entity.getMyLove()) {
					return;
				}
				entity.setLove(entity.getLove() + 1);
				viewHolder.love.setTextColor(Color.parseColor("#D95555"));
				viewHolder.love.setText(entity.getLove() + "");
				entity.setMyLove(true);
				entity.increment("love", 1);
				entity.update(mContext, new UpdateListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						LogUtils.i(TAG, "���޳ɹ�~");
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub

					}
				});
			}
		});
		viewHolder.hate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				entity.setHate(entity.getHate() + 1);
				viewHolder.hate.setText(entity.getHate() + "");
				entity.increment("hate", 1);
				entity.update(mContext, new UpdateListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						ActivityUtil.show(mContext, "��ȳɹ�~");
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub

					}
				});
			}
		});
		viewHolder.share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// share to sociaty
				ActivityUtil.show(mContext, "��������ѿ�Ŷ~");
				final TencentShare tencentShare = new TencentShare(
						MyApplication.getInstance().getTopActivity(),
						getQQShareEntity(entity));
				tencentShare.shareToQQ();
			}
		});
		viewHolder.comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 评论
				MyApplication.getInstance().setCurrentQiangYu(entity);
				Intent intent = new Intent();
				intent.setClass(MyApplication.getInstance().getTopActivity(),
						CommentActivity.class);
				mContext.startActivity(intent);
			}
		});

		if (entity.getMyFav()) {
			viewHolder.favMark
					.setImageResource(R.drawable.ic_action_fav_choose);
		} else {
			viewHolder.favMark
					.setImageResource(R.drawable.ic_action_fav_normal);
		}
		viewHolder.favMark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 收藏
				ActivityUtil.show(mContext, "�ղ�");
				onClickFav(v, entity);

			}
		});
		return convertView;
	}

	private TencentShareEntity getQQShareEntity(QiangYu qy) {
		String title = "����ö������ķ羰";
		String comment = "�����������ķ羰��";
		String img = null;
		if (qy.getContentfigureurl() != null) {
			img = qy.getContentfigureurl().getFileUrl(mContext);
		} else {
			img = "http://www.codenow.cn/appwebsite/website/yyquan/uploads/53af6851d5d72.png";
		}
		String summary = qy.getContent();

		String targetUrl = "http://yuanquan.bmob.cn";
		TencentShareEntity entity = new TencentShareEntity(title, img,
				targetUrl, summary, comment);
		return entity;
	}

	public static class ViewHolder {
		public ImageView userLogo;
		public TextView userName;
		public TextView contentText;
		public ImageView contentImage;

		public ImageView favMark;
		public TextView love;
		public TextView hate;
		public TextView share;
		public TextView comment;
	}

	private void onClickFav(View v, QiangYu qiangYu) {
		// TODO Auto-generated method stub
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if (user != null && user.getSessionToken() != null) {
			BmobRelation favRelaton = new BmobRelation();

			qiangYu.setMyFav(!qiangYu.getMyFav());
			if (qiangYu.getMyFav()) {
				((ImageView) v)
						.setImageResource(R.drawable.ic_action_fav_choose);
				favRelaton.add(qiangYu);
				ActivityUtil.show(mContext, "�ղسɹ���");
			} else {
				((ImageView) v)
						.setImageResource(R.drawable.ic_action_fav_normal);
				favRelaton.remove(qiangYu);
				ActivityUtil.show(mContext, "ȡ���ղء�");
			}

			user.setFavorite(favRelaton);
			user.update(mContext, new UpdateListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					LogUtils.i(TAG, "�ղسɹ���");
					// try get fav to see if fav success
					// getMyFavourite();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					LogUtils.i(TAG, "�ղ�ʧ�ܡ���������~");
					ActivityUtil.show(mContext, "�ղ�ʧ�ܡ���������~" + arg0);
				}
			});
		} else {
			// 前往登录注册界面
			ActivityUtil.show(mContext, "�ղ�ǰ���ȵ�¼��");
			Intent intent = new Intent();
			intent.setClass(mContext, RegisterAndLoginActivity.class);
			MyApplication.getInstance().getTopActivity()
					.startActivityForResult(intent, SAVE_FAVOURITE);
		}
	}

	private void getMyFavourite() {
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if (user != null) {
			BmobQuery<QiangYu> query = new BmobQuery<QiangYu>();
			query.addWhereRelatedTo("favorite", new BmobPointer(user));
			query.include("user");
			query.order("createdAt");
			query.setLimit(Constant.NUMBERS_PER_PAGE);
			query.findObjects(mContext, new FindListener<QiangYu>() {

				@Override
				public void onSuccess(List<QiangYu> data) {
					// TODO Auto-generated method stub
					LogUtils.i(TAG, "get fav success!" + data.size());
					ActivityUtil.show(mContext, "fav size:" + data.size());
				}

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					ActivityUtil.show(mContext, "��ȡ�ղ�ʧ�ܡ���������~");
				}
			});
		} else {
			// 前往登录注册界面
			ActivityUtil.show(mContext, "��ȡ�ղ�ǰ���ȵ�¼��");
			Intent intent = new Intent();
			intent.setClass(mContext, RegisterAndLoginActivity.class);
			MyApplication.getInstance().getTopActivity()
					.startActivityForResult(intent, Constant.GET_FAVOURITE);
		}
	}
}