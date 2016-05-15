package com.zhidao.adapter;

import java.util.List;

import com.example.zhidao.R;
import com.zhidao.entity.Comment;
import com.zzhidao.utils.LogUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

 
 

public class CommentAdapter extends BaseContentAdapter<Comment>{

	public CommentAdapter(Context context, List<Comment> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.comment_item, null);
			viewHolder.userName = (TextView)convertView.findViewById(R.id.userName_comment);
			viewHolder.commentContent = (TextView)convertView.findViewById(R.id.content_comment);
			viewHolder.index = (TextView)convertView.findViewById(R.id.index_comment);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		final Comment comment = dataList.get(position);
		if(comment.getUser()!=null){
			viewHolder.userName.setText(comment.getUser().getUsername());
			LogUtils.i("CommentActivity","NAME:"+comment.getUser().getUsername());
		}else{
			viewHolder.userName.setText("ǽ��");
		}
		viewHolder.index.setText((position+1)+"¥");
		viewHolder.commentContent.setText(comment.getCommentContent());
		
		return convertView;
	}

	public static class ViewHolder{
		public TextView userName;
		public TextView commentContent;
		public TextView index;
	}
}
