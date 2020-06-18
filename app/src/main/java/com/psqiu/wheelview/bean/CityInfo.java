package com.psqiu.wheelview.bean;

import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.Map;

public class CityInfo {

	
	/**
	 * 数据库创建消息表
	 */
	public static final class Table implements BaseColumns {
		/** 表名 */
		public static final String TABLE_PROVINCE = "province_table"; 	//省
		public static final String TABLE_CITY = "city_table";			//市县
		public static final String TABLE_AREA = "area_table";			//市镇
		public static final String TABLE_WEATHER = "weather_phenomenon_table";	//天气现象表
		
		/**
		 * 字段名
		 */
		public static final String PROVINCE_ID = "PROVINCE_ID";
		public static final String PROVINCE = "PROVINCE";
		public static final String CITY_ID = "CITY_ID";
		public static final String CITY = "CITY";
		public static final String AREA_ID = "AREA_ID";
		public static final String AREA = "AREA";
		public static final String WEATHER_ID = "WEATHER_ID";
		
		public static final String CODE_ID = "CODE_ID";
		public static final String ZN_NAME = "ZN_NAME";
		
	}
	
	public String city_id;
	public String cityName;
	public String weather_id; //天气id
	
	/**
	 * 数据库查询结果转换成集合
	 * @param c
	 * @return
	 */
	public static Map<String, CityInfo> cursor2bean(Cursor c, String tableName) {
		Map<String, CityInfo> map = new HashMap<String, CityInfo>();
		if (c != null && c.getCount() > 0) {
			while (c.moveToNext()) {
				CityInfo info = new CityInfo();
				if (tableName.equals(Table.TABLE_PROVINCE)) {
					info.city_id = c.getString(c.getColumnIndexOrThrow(Table.PROVINCE_ID));
					info.cityName = c.getString(c.getColumnIndexOrThrow(Table.PROVINCE));
				}else if (tableName.equals(Table.TABLE_CITY)) {
					info.city_id = c.getString(c.getColumnIndexOrThrow(Table.CITY_ID));
					info.cityName = c.getString(c.getColumnIndexOrThrow(Table.CITY));
				}else if (tableName.equals(Table.TABLE_AREA)) {
					info.city_id = c.getString(c.getColumnIndexOrThrow(Table.AREA_ID));
					info.cityName = c.getString(c.getColumnIndexOrThrow(Table.AREA));
					if (c.getColumnIndex(Table.WEATHER_ID) != -1) {
						info.weather_id = c.getString(c.getColumnIndexOrThrow(Table.WEATHER_ID));
					}
				}
				map.put(info.cityName, info);
			}
			c.close();
		}
		return map;
	}
}
