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
	 * �����ݿ��һ�α�������ʱ�����
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
			// ��������
			List<City> citys = parserCitys();
			// foreach�Z�䣬���Ա�v�б����M�ȵ�
			for (City city : citys) {
				// ����һ����sql
				String sql_insert = "insert into city values(null,?,?,?)";
				db.execSQL(
						sql_insert,
						new String[] { String.valueOf(city.getCityid()),
								city.getName(), city.getPinyi() });
			}
			db.setTransactionSuccessful();
			Log.i("iNFO", "�������һ�΄����ɹ���");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.endTransaction();
		}
	}

	/**
	 * ����xml
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
	 * ���ݿ�������ʱ�����
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
