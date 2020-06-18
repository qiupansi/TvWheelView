package com.psqiu.wheelview.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import java.util.Map;

public class CityManager {

	private CityDbHelper mCityDbHelper = null;
	private static Context mContext;
	public static CityManager mInstance = null;

	private CityManager(Context ctx) {
		mContext = ctx;
	}

	public static CityManager getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new CityManager(ctx);
		}
		return mInstance;
	}

	public static void release() {
		if (mContext != null) {
			mContext = null;
		}
		if (mInstance != null) {
			mInstance = null;
		}
	}

	public void setDefaultCityAreaid(String id){
		setString(CityInfo.Table.AREA_ID, id);
	}
	
	public String getDefaultCityAreaid(){
		return getString(CityInfo.Table.AREA_ID, "101270101");
	}

	public void setDefaultCityName(String name){
		setString(CityInfo.Table.CITY, name);
	}
	
	public String getDefaultCityName(){
		return getString(CityInfo.Table.CITY, "成都");
	}
	
	private SQLiteDatabase getCityDatabase() {
		if (mCityDbHelper == null) {
			mCityDbHelper = new CityDbHelper(mContext);
		}
		return mCityDbHelper.getReadableDatabase();
	}
	
	/**
	 * 获得省列表
	 * 
	 * @return
	 */
	public Map<String, CityInfo> getProvince() {
		SQLiteDatabase db = getCityDatabase();
		Cursor c = db.rawQuery("select * from " + CityInfo.Table.TABLE_PROVINCE, null);
		return CityInfo.cursor2bean(c, CityInfo.Table.TABLE_PROVINCE);
	}

	/**
	 * 根据省id获得市级列表
	 * 
	 * @param province_id
	 * @return
	 */
	public Map<String, CityInfo> getCity(String province_id) {
		SQLiteDatabase db = getCityDatabase();
		Cursor c = db.rawQuery("select * from " + CityInfo.Table.TABLE_CITY + " where " + CityInfo.Table.PROVINCE_ID + "='" + province_id + "'", null);
		return CityInfo.cursor2bean(c, CityInfo.Table.TABLE_CITY);
	}

	/**
	 * 根据县id获得区域列表
	 * 
	 * @param city_id
	 * @return
	 */
	public Map<String, CityInfo> getArea(String city_id) {
		SQLiteDatabase db = getCityDatabase();
		Cursor c = db.rawQuery("select * from " + CityInfo.Table.TABLE_AREA + " where " + CityInfo.Table.CITY_ID + "='" + city_id + "'", null);
		return CityInfo.cursor2bean(c, CityInfo.Table.TABLE_AREA);
	}

	public String getString(String key, String defValue) {
		SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		return mSharedPreferences.getString(key, defValue);
	}

	public void setString(String key, String value) {
		SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	
}
