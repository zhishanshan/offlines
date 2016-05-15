package com.zhidao.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.zhidao.R;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
 
import com.zhidao.adapter.CommentAdapter;
import com.zhidao.app.MyApplication;
import com.zhidao.base.BasePageActivity;
import com.zhidao.db.DatabaseUtil;
import com.zhidao.entity.Comment;
import com.zhidao.entity.QiangYu;
import com.zhidao.entity.User;
import com.zhidao.sns.TencentShare;
import com.zhidao.sns.TencentShareEntity;
import com.zzhidao.utils.ActivityUtil;
import com.zzhidao.utils.Constant;
import com.zzhidao.utils.LogUtils;
 
/**
 * @author kingofglory email: kingofglory@yeah.net blog: http:www.google.com
 * @date 2014-4-2 TODO
 */

public class CommentActivity extends BasePageActivity implements
		OnClickListener {

	private ActionBar actionbar;
	private ListView commentList;
	private TextView footer;

	private EditText commentContent;
	private Button commentCommit;

	private TextView userName;
	private TextView commentItemContent;
	private ImageView commentItemImage;

	private ImageView userLogo;
	private ImageView myFav;
	private TextView comment;
	private TextView share;
	private TextView love;
	private TextView hate;

	private QiangYu qiangYu;
	private String commentEdit = "";

	private CommentAdapter mAdapter;

	private List<Comment> comments = new ArrayList<Comment>();

	private int pageNum;

	@Override
	protected void setLayoutView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_comment);
	}

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		actionbar = (ActionBar) findViewById(R.id.actionbar_comment);
		commentList = (ListView) findViewById(R.id.comment_list);
		footer = (TextView) findViewById(R.id.loadmore);

		commentContent = (EditText) findViewById(R.id.comment_content);
		commentCommit = (Button) findViewById(R.id.comment_commit);

		userName = (TextView) findViewById(R.id.user_name);
		commentItemContent = (TextView) findViewById(R.id.content_text);
		commentItemImage = (ImageView) findViewById(R.id.content_image);

		userLogo = (ImageView) findViewById(R.id.user_logo);
		myFav = (ImageView) findViewById(R.id.item_action_fav);
		comment = (TextView) findViewById(R.id.item_action_comment);
		share = (TextView) findViewById(R.id.item_action_share);
		love = (TextView) findViewById(R.id.item_action_love);
		hate = (TextView) findViewById(R.id.item_action_hate);

	}

	@Override
	protected void setupViews(Bundle bundle) {
		// TODO Auto-generated method stub
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		qiangYu = (QiangYu) getIntent().getSerializableExtra("data");// MyApplication.getInstance().getCurrentQiangYu();
		pageNum = 0;

		actionbar.setTitle("��������");
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

		mAdapter = new CommentAdapter(CommentActivity.this, comments);
		commentList.setAdapter(mAdapter);
		setListViewHeightBasedOnChildren(commentList);
		commentList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ActivityUtil.show(CommentActivity.this, "po" + position);
			}
		});
		commentList.setCacheColorHint(0);
		commentList.setScrollingCacheEnabled(false);
		commentList.setScrollContainer(false);
		commentList.setFastScrollEnabled(true);
		commentList.setSmoothScrollbarEnabled(true);

		initMoodView(qiangYu);
	}

	private void initMoodView(QiangYu mood2) {
		// TODO Auto-generated method stub
		if (mood2 == null) {
			return;
		}
		userName.setText(qiangYu.getAuthor().getUsername());
		commentItemContent.setText(qiangYu.getContent());
		if (null == qiangYu.getContentfigureurl()) {
			commentItemImage.setVisibility(View.GONE);
		} else {
			commentItemImage.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					qiangYu.getContentfigureurl().getFileUrl(
							CommentActivity.this) == null ? "" : qiangYu
							.getContentfigureurl().getFileUrl(
									CommentActivity.this),
					commentItemImage,
					MyApplication.getInstance().getOptions(
							R.drawable.bg_pic_loading),
					new SimpleImageLoadingListener() {

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
							float[] cons = ActivityUtil.getBitmapConfiguration(
									loadedImage, commentItemImage, 1.0f);
							RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
									(int) cons[0], (int) cons[1]);
							layoutParams.addRule(RelativeLayout.BELOW,
									R.id.content_text);
							commentItemImage.setLayoutParams(layoutParams);
						}

					});
		}

		love.setText(qiangYu.getLove() + "");
		if (qiangYu.getMyLove()) {
			love.setTextColor(Color.parseColor("#D95555"));
		} else {
			love.setTextColor(Color.parseColor("#000000"));
		}
		hate.setText(qiangYu.getHate() + "");
		if (qiangYu.getMyFav()) {
			myFav.setImageResource(R.drawable.ic_action_fav_choose);
		} else {
			myFav.setImageResource(R.drawable.ic_action_fav_normal);
		}

		User user = qiangYu.getAuthor();
		BmobFile avatar = user.getAvatar();
		if (null != avatar) {
			ImageLoader.getInstance().displayImage(
					avatar.getFileUrl(CommentActivity.this),
					userLogo,
					MyApplication.getInstance().getOptions(
							R.drawable.content_image_default),
					new SimpleImageLoadingListener() {

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
							LogUtils.i(TAG, "load personal icon completed.");
						}

					});
		}
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		footer.setOnClickListener(this);
		commentCommit.setOnClickListener(this);

		userLogo.setOnClickListener(this);
		myFav.setOnClickListener(this);
		love.setOnClickListener(this);
		hate.setOnClickListener(this);
		share.setOnClickListener(this);
		comment.setOnClickListener(this);
	}

	@Override
	protected void fetchData() {
		// TODO Auto-generated method stub
		fetchComment();
	}

	private void fetchComment() {
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.addWhereRelatedTo("relation", new BmobPointer(qiangYu));
		query.include("user");
		query.order("createdAt");
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		query.setSkip(Constant.NUMBERS_PER_PAGE * (pageNum++));
		query.findObjects(this, new FindListener<Comment>() {

			@Override
			public void onSuccess(List<Comment> data) {
				// TODO Auto-generated method stub
				LogUtils.i(TAG, "get comment success!" + data.size());
				if (data.size() != 0 && data.get(data.size() - 1) != null) {

					if (data.size() < Constant.NUMBERS_PER_PAGE) {
						ActivityUtil.show(mContext, "�Ѽ�������������~");
						footer.setText("���޸�������~");
					}

					mAdapter.getDataList().addAll(data);
					mAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(commentList);
					LogUtils.i(TAG, "refresh");
				} else {
					ActivityUtil.show(mContext, "���޸�������~");
					footer.setText("���޸�������~");
					pageNum--;
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ActivityUtil.show(CommentActivity.this, "��ȡ����ʧ�ܡ���������~");
				pageNum--;
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.user_logo:
			onClickUserLogo();
			break;
		case R.id.loadmore:
			onClickLoadMore();
			break;
		case R.id.comment_commit:
			onClickCommit();
			break;
		case R.id.item_action_fav:
			onClickFav(v);
			break;
		case R.id.item_action_love:
			onClickLove();
			break;
		case R.id.item_action_hate:
			onClickHate();
			break;
		case R.id.item_action_share:
			onClickShare();
			break;
		case R.id.item_action_comment:
			onClickComment();
			break;
		default:
			break;
		}
	}

	private void onClickUserLogo() {
		// TODO Auto-generated method stub
		// ��ת��������Ϣ����
		User currentUser = BmobUser.getCurrentUser(this, User.class);
		if (currentUser != null) {// �ѵ�¼
			Intent intent = new Intent();
			intent.setClass(MyApplication.getInstance().getTopActivity(),
					PersonalActivity.class);
			mContext.startActivity(intent);
		} else {// δ��¼
			ActivityUtil.show(this, "���ȵ�¼��");
			Intent intent = new Intent();
			intent.setClass(this, RegisterAndLoginActivity.class);
			startActivityForResult(intent, Constant.GO_SETTINGS);
		}
	}

	private void onClickLoadMore() {
		// TODO Auto-generated method stub
		fetchData();
	}

	private void onClickCommit() {
		// TODO Auto-generated method stub
		User currentUser = BmobUser.getCurrentUser(this, User.class);
		if (currentUser != null) {// �ѵ�¼
			commentEdit = commentContent.getText().toString().trim();
			if (TextUtils.isEmpty(commentEdit)) {
				ActivityUtil.show(this, "�������ݲ���Ϊ�ա�");
				return;
			}
			// comment now
			publishComment(currentUser, commentEdit);
		} else {// δ��¼
			ActivityUtil.show(this, "��������ǰ���ȵ�¼��");
			Intent intent = new Intent();
			intent.setClass(this, RegisterAndLoginActivity.class);
			startActivityForResult(intent, Constant.PUBLISH_COMMENT);
		}

	}

	private void publishComment(User user, String content) {

		final Comment comment = new Comment();
		comment.setUser(user);
		comment.setCommentContent(content);
		comment.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ActivityUtil.show(CommentActivity.this, "���۳ɹ���");
				if (mAdapter.getDataList().size() < Constant.NUMBERS_PER_PAGE) {
					mAdapter.getDataList().add(comment);
					mAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(commentList);
				}
				commentContent.setText("");
				hideSoftInput();

				// ����������ǿ��󶨵�һ��
				BmobRelation relation = new BmobRelation();
				relation.add(comment);
				qiangYu.setRelation(relation);
				qiangYu.update(mContext, new UpdateListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						LogUtils.i(TAG, "�������۳ɹ���");
						// fetchData();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						LogUtils.i(TAG, "��������ʧ�ܡ�" + arg1);
					}
				});

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ActivityUtil.show(CommentActivity.this, "����ʧ�ܡ���������~");
			}
		});
	}

	private void onClickFav(View v) {
		// TODO Auto-generated method stub

		User user = BmobUser.getCurrentUser(this, User.class);
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
			user.update(this, new UpdateListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					LogUtils.i(TAG, "�ղسɹ���");
					ActivityUtil.show(CommentActivity.this, "�ղسɹ���");
					// try get fav to see if fav success
					// getMyFavourite();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					LogUtils.i(TAG, "�ղ�ʧ�ܡ���������~");
					ActivityUtil.show(CommentActivity.this, "�ղ�ʧ�ܡ���������~"
							+ arg0);
				}
			});
		} else {
			// ǰ����¼ע�����
			ActivityUtil.show(this, "�ղ�ǰ���ȵ�¼��");
			Intent intent = new Intent();
			intent.setClass(this, RegisterAndLoginActivity.class);
			startActivityForResult(intent, Constant.SAVE_FAVOURITE);
		}

	}

	private void getMyFavourite() {
		User user = BmobUser.getCurrentUser(this, User.class);
		if (user != null) {
			BmobQuery<QiangYu> query = new BmobQuery<QiangYu>();
			query.addWhereRelatedTo("favorite", new BmobPointer(user));
			query.include("user");
			query.order("createdAt");
			query.setLimit(Constant.NUMBERS_PER_PAGE);
			query.findObjects(this, new FindListener<QiangYu>() {

				@Override
				public void onSuccess(List<QiangYu> data) {
					// TODO Auto-generated method stub
					LogUtils.i(TAG, "get fav success!" + data.size());
					ActivityUtil.show(CommentActivity.this,
							"fav size:" + data.size());
				}

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					ActivityUtil.show(CommentActivity.this, "��ȡ�ղ�ʧ�ܡ���������~");
				}
			});
		} else {
			// ǰ����¼ע�����
			ActivityUtil.show(this, "��ȡ�ղ�ǰ���ȵ�¼��");
			Intent intent = new Intent();
			intent.setClass(this, RegisterAndLoginActivity.class);
			startActivityForResult(intent, Constant.GET_FAVOURITE);
		}
	}

	boolean isFav = false;

	private void onClickLove() {
		// TODO Auto-generated method stub
		User user = BmobUser.getCurrentUser(this, User.class);
		if (user == null) {
			// ǰ����¼ע�����
			ActivityUtil.show(this, "���ȵ�¼��");
			Intent intent = new Intent();
			intent.setClass(this, RegisterAndLoginActivity.class);
			startActivity(intent);
			return;
		}
		if (qiangYu.getMyLove()) {
			ActivityUtil.show(CommentActivity.this, "���Ѿ��޹���");
			return;
		}
		isFav = qiangYu.getMyFav();
		if (isFav) {
			qiangYu.setMyFav(false);
		}
		qiangYu.setLove(qiangYu.getLove() + 1);
		love.setTextColor(Color.parseColor("#D95555"));
		love.setText(qiangYu.getLove() + "");
		qiangYu.increment("love", 1);
		qiangYu.update(mContext, new UpdateListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				qiangYu.setMyLove(true);
				qiangYu.setMyFav(isFav);
				DatabaseUtil.getInstance(mContext).insertFav(qiangYu);

				ActivityUtil.show(mContext, "���޳ɹ�~");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void onClickHate() {
		// TODO Auto-generated method stub
		qiangYu.setHate(qiangYu.getHate() + 1);
		hate.setText(qiangYu.getHate() + "");
		qiangYu.increment("hate", 1);
		qiangYu.update(mContext, new UpdateListener() {

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

	private void onClickShare() {
		// TODO Auto-generated method stub
		ActivityUtil.show(CommentActivity.this, "share to ...");
		final TencentShare tencentShare = new TencentShare(MyApplication
				.getInstance().getTopActivity(), getQQShareEntity(qiangYu));
		tencentShare.shareToQQ();
	}

	private TencentShareEntity getQQShareEntity(QiangYu qy) {
		String title = "����ö������ķ羰";
		String comment = "�����������ķ羰��";
		String img = null;
		if (qy.getContentfigureurl() != null) {
			img = qy.getContentfigureurl().getFileUrl(CommentActivity.this);
		} else {
			img = "http://www.codenow.cn/appwebsite/website/yyquan/uploads/53af6851d5d72.png";
		}
		String summary = qy.getContent();

		String targetUrl = "http://yuanquan.bmob.cn";
		TencentShareEntity entity = new TencentShareEntity(title, img,
				targetUrl, summary, comment);
		return entity;
	}

	private void onClickComment() {
		// TODO Auto-generated method stub
		commentContent.requestFocus();

		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.showSoftInput(commentContent, 0);
	}

	private void hideSoftInput() {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(commentContent.getWindowToken(), 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constant.PUBLISH_COMMENT:
				// ��¼���
				commentCommit.performClick();
				break;
			case Constant.SAVE_FAVOURITE:
				myFav.performClick();
				break;
			case Constant.GET_FAVOURITE:

				break;
			case Constant.GO_SETTINGS:
				userLogo.performClick();
				break;
			default:
				break;
			}
		}

	}

	/***
	 * ��̬����listview�ĸ߶� item �ܲ��ֱ�����linearLayout
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))
				+ 15;
		listView.setLayoutParams(params);
	}

}
