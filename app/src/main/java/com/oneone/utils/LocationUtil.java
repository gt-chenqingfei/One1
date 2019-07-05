package com.oneone.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.util.List;

/**
 * 用 Android 的 LocationManager 获取经纬度
 *
 * Created by ZhaiDongyang on 2018/7/5
 */
public class LocationUtil {

    public interface LocationHelper {
        void UpdateLastLocation(Location location);
    }

    private volatile static LocationUtil uniqueInstance;

    private LocationHelper mLocationHelper;

    private LocationManager mLocationManager;

    private Context mContext;

    private LocationUtil(Context context) {
        mContext = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public static LocationUtil getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (LocationUtil.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new LocationUtil(context);
                }
            }
        }
        return uniqueInstance;
    }

    public void initLocation(LocationHelper locationHelper) {
        Location location = null;
        mLocationHelper = locationHelper;
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        List<String> providers = mLocationManager.getProviders(true);
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                location = l;
            }
        }
        if (location != null) {
            locationHelper.UpdateLastLocation(location);
        }
    }

}
