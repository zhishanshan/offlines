package com.zhidao.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.pinyin4android.PinyinUtil;
import com.zhidao.dao.City;
 

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.util.Xml;

public class DBHelper extends SQLiteOpenHelper {
	
	/*-------------------------------*/
	private Context context;

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	/**
	 * 当数据库第一次被创建的时候调用
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		try {
			String create_table_city = "create table city(_id integer primary key autoincrement,"
					+ "cityid integer,"
					+ "cityname varchar(20),"
					+ "citypinyin text)";
			db.execSQL(create_table_city);
			// 插入数据
			List<City> citys = parserCitys();
			// foreach語句，可以遍歷列表，數組等等
			for (City city : citys) {
				// 方法一：寫sql
				String sql_insert = "insert into city values(null,?,?,?)";
				db.execSQL(
						sql_insert,
						new String[] { String.valueOf(city.getCityid()),
								city.getName(), city.getPinyi() });
			}
			db.setTransactionSuccessful();
			Log.i("iNFO", "數據庫第一次創建成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.endTransaction();
		}
	}

	/**
	 * 解析xml
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<City> parserCitys() throws Exception {
		List<City> citys = null;
		XmlPullParser parser = Xml.newPullParser();
		InputStream inputStream = context.getAssets().open("cityids.xml");
		parser.setInput(inputStream, "utf-8");
		int type = parser.getEventType();
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				if ("cityids".equals(name)) {
					citys = new ArrayList<City>();
				} else if ("city".equals(name)) {
					City city = new City();
					String id = parser.getAttributeValue(0);
					String cityName = parser.getAttributeValue(1);
					String pinyin = PinyinUtil.toPinyin(context, cityName);
					city.setCityid(Integer.valueOf(id));
					city.setName(cityName);
					city.setPinyi(pinyin);
					citys.add(city);

				}
				break;
			case XmlPullParser.END_TAG:

				break;
			default:
				break;
			}
			type = parser.next();
		}
		return citys;
	}

	/**
	 * 数据库升级的时候调用
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
