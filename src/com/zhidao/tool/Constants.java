package com.zhidao.tool;

import java.util.ArrayList;
import java.util.List;

import com.zhidao.bean.NewsClassify;

 
public class Constants {
	
	
		
	
		public static ArrayList<NewsClassify> getData() {
			ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
			NewsClassify classify = new NewsClassify();
			classify.setId(0);
			classify.setTitle("�Ƽ�");
			newsClassify.add(classify);
			classify = new NewsClassify();
			classify.setId(1);
			classify.setTitle("����");
			newsClassify.add(classify);
			classify = new NewsClassify();
			classify.setId(2);
			classify.setTitle("��װ");
			newsClassify.add(classify);
			classify = new NewsClassify();
			classify.setId(3);
			classify.setTitle("�ⷿ");
			newsClassify.add(classify);
			classify = new NewsClassify();
			classify.setId(4);
			classify.setTitle("����");
			newsClassify.add(classify);
			classify = new NewsClassify();
			classify.setId(5);
			classify.setTitle("ҩ��");
			newsClassify.add(classify);
			classify = new NewsClassify();
			classify.setId(6);
			classify.setTitle("���");
			newsClassify.add(classify);
			classify = new NewsClassify();
			classify.setId(7);
			classify.setTitle("����");
			newsClassify.add(classify);
			return newsClassify;
		}
		
	
	/** mark=0 ���Ƽ� */
public final static int mark_recom = 0;
	/** mark=1 ������ */
	public final static int mark_hot = 1;
	/** mark=2 ���׷� */
	public final static int mark_frist = 2;
	/** mark=3 ������ */
	public final static int mark_exclusive = 3;
	/** mark=4 ���ղ� */
	public final static int mark_favor = 4;
	
}
