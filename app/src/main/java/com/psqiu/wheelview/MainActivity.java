package com.psqiu.wheelview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.psqiu.wheelview.bean.CityInfo;
import com.psqiu.wheelview.bean.CityManager;
import com.psqiu.wheelview.wheelview.ArrayWheelAdapter;
import com.psqiu.wheelview.wheelview.OnWheelChangedListener;
import com.psqiu.wheelview.wheelview.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//更多TV项目资源(如桌面，直播点播，教育，应用市场，文件管理器，设置，酒店应用等)，添加微信：qiupansi
//If you want more TV project resources,such as TvLauncher,TvLive,TvAppStore,TvSettings,TvFileManager,TvEducation,TvHotel,TvMusic,TvRemote and so on，Add me wechat：qiupansi
public class MainActivity extends AppCompatActivity implements OnWheelChangedListener {

    private WheelView mProvince;
    private WheelView mCity;
    private WheelView mArea;

    private FocusLinearLayout fll_locations;

    private Map<String, CityInfo> mProviceList = new HashMap<>();
    private Map<String, CityInfo> mCityList = new HashMap<>();
    private Map<String, CityInfo> mAreaList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        fll_locations = (FocusLinearLayout) findViewById(R.id.fll_locations);
        if (mProvince.getVisibleItems() != mCity.getVisibleItems() || mCity.getVisibleItems() != mArea.getVisibleItems()
                || mProvince.getVisibleItems() != mArea.getVisibleItems()) {
            throw new IllegalStateException(" visibleItems doesn't matched");//
        }
        int scaleY = mProvince.getVisibleItems();
        Log.d("jdk", "scaleY==" + scaleY);
        fll_locations.setScaleXY(1, scaleY);
    }

    private void initViews() {
        mProvince = (WheelView) findViewById(R.id.id_province);
        mCity = (WheelView) findViewById(R.id.id_city);
        mArea = (WheelView) findViewById(R.id.id_area);
        mProvince.requestFocus();
        mProviceList = CityManager.getInstance(this).getProvince();
        mProvince.setViewAdapter(new ArrayWheelAdapter(this, new ArrayList(mProviceList.keySet())));

        // 添加change事件
        mProvince.addChangingListener(this);
        mCity.addChangingListener(this);
        mArea.addChangingListener(this);
        updateCities();
    }


    @Override
    protected void onPause() {
        saveChoosedCity();
        super.onPause();
    }


    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        String currentProviceName = mProvince.getCurrentText().toString();
        if (!isEmpty(currentProviceName)) {
            CityInfo info = mProviceList.get(currentProviceName);
            mCityList.clear();
            mCityList = CityManager.getInstance(this).getCity(info.city_id);
            mCity.setViewAdapter(new ArrayWheelAdapter(this, new ArrayList(mCityList.keySet())));
            mCity.setCurrentItem(0);
            updateAreas();
        }
    }


    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        String currentCityName = mCity.getCurrentText().toString();
        if (!isEmpty(currentCityName)) {
            CityInfo info = mCityList.get(currentCityName);
            mAreaList.clear();
            mAreaList = CityManager.getInstance(this).getArea(info.city_id);
            mArea.setViewAdapter(new ArrayWheelAdapter(this, new ArrayList(mAreaList.keySet())));
            mArea.setCurrentItem(0);
        }
    }


    /**
     * change事件的处理
     */
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mProvince) {
            updateCities();
        } else if (wheel == mCity) {
            updateAreas();
        }
    }

    /**
     * 保存当前选择的城市信息
     */
    private void saveChoosedCity(){
        String currentAreaName = mArea.getCurrentText().toString();
        if (!isEmpty(currentAreaName)) {
            CityInfo info = mAreaList.get(currentAreaName);
            if (info != null) {
                CityManager.getInstance(this).setDefaultCityName(currentAreaName);
                CityManager.getInstance(this).setDefaultCityAreaid(info.weather_id);
            }
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
