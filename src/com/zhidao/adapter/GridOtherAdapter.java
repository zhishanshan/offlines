package com.zhidao.adapter;

import java.util.List;

import com.example.zhidao.R;
import com.zhidao.bean.GridChannelItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 

public class GridOtherAdapter extends BaseAdapter {
	private Context context;
	public List<GridChannelItem> channelList;
	private TextView item_text;
	/** �Ƿ�ɼ� */
	boolean isVisible = true;
	/** Ҫɾ����position */
	public int remove_position = -1;

	public GridOtherAdapter(Context context, List<GridChannelItem> channelList) {
		this.context = context;
		this.channelList = channelList;
	}

	@Override
	public int getCount() {
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public GridChannelItem getItem(int position) {
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.subscribe_category_item, null);
		item_text = (TextView) view.findViewById(R.id.text_item);
		GridChannelItem channel = getItem(position);
		item_text.setText(channel.getName());
		if (!isVisible && (position == -1 + channelList.size())){
			item_text.setText("");
		}
		if(remove_position == position){
			item_text.setText("");
		}
		return view;
	}
	
	/** ��ȡƵ���б� */
	public List<GridChannelItem> getChannnelLst() {
		return channelList;
	}
	
	/** ���Ƶ���б� */
	public void addItem(GridChannelItem channel) {
		channelList.add(channel);
		notifyDataSetChanged();
	}

	/** ����ɾ����position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
		// notifyDataSetChanged();
	}

	/** ɾ��Ƶ���б� */
	public void remove() {
		channelList.remove(remove_position);
		remove_position = -1;
		notifyDataSetChanged();
	}
	/** ����Ƶ���б� */
	public void setListDate(List<GridChannelItem> list) {
		channelList = list;
	}

	/** ��ȡ�Ƿ�ɼ� */
	public boolean isVisible() {
		return isVisible;
	}
	
	/** �����Ƿ�ɼ� */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
}