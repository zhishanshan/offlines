package com.zhidao.tool;

import java.util.ArrayList;
import java.util.List;

import com.zhidao.bean.NewsClassify;

 
public class Constants {
	
	
		
	
		public static ArrayList<NewsClassify> getData() {
			ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
			NewsClassify classify = new NewsClassify();
			classify.setId(0);
			classify.setTitle("推荐");
			newsClassify.add(classify);
			classify = new NewsClassify();
			classify.setId(1);
			classify.setTitle("超市");
			newsClassify.add(classify);
			classify = new NewsClassify();
			classify.setId(2);
			classify.setTitle("服装");
			newsClassify.add(classify);
			classify = new NewsClassify();
			classify.setId(3);
			classify.setTitle("租房");
			newsClassify.add(classify);
			classify = new NewsClassify();
			classify.setId(4);
			classify.setTitle("电子");
			newsClassify.add(classify);
			classify = new NewsClassify();
			classify.setId(5);
			classify.setTitle("药店");
			newsClassify.add(classify);
			classify = new NewsClassify();
			classify.setId(6);
			classify.setTitle("书店");
			newsClassify.add(classify);
			classify = new NewsClassify();
			classify.setId(7);
			classify.setTitle("美容");
			newsClassify.add(classify);
			return newsClassify;
		}
		
	
	/** mark=0 ：推荐 */
public final static int mark_recom = 0;
	/** mark=1 ：热门 */
	public final static int mark_hot = 1;
	/** mark=2 ：首发 */
	public final static int mark_frist = 2;
	/** mark=3 ：独家 */
	public final static int mark_exclusive = 3;
	/** mark=4 ：收藏 */
	public final static int mark_favor = 4;
	
}
