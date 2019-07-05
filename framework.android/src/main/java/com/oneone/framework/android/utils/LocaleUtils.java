package com.oneone.framework.android.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * @author qingfei.chen
 * @since 2018/4/8.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class LocaleUtils {

    public static int getCountryCode(Context context) {
        int countryCode = 86;
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String countryIso = manager.getNetworkCountryIso();
            if (TextUtils.isEmpty(countryIso)) {
                countryIso = manager.getSimCountryIso();
            }
            if (!TextUtils.isEmpty(countryIso)) {
                countryIso = countryIso.toUpperCase();
                Map<String, Integer> countryMap = getCountyCodeMap();
                if (countryMap != null && countryMap.containsKey(countryIso)) {
                    countryCode = countryMap.get(countryIso);
                }
            }
        } catch (Exception e) {

        }

        return countryCode;
    }

    private static String getTimeZone() {
        String timeZoneID = TimeZone.getDefault().getID();
        return timeZoneID;
    }

    public static Map<String, Integer> getCountyCodeMap() {
        Set<String> regions = PhoneNumberUtil.getInstance().getSupportedRegions();
        Map<String, Integer> countryMap = new HashMap<String, Integer>();
        for (String region : regions) {
            countryMap.put(region.toUpperCase(), PhoneNumberUtil.getInstance().getCountryCodeForRegion(region));
        }
        return countryMap;
    }

    public static String getCountryCodeByPhoneNum(String phoneNum) {

        if (TextUtils.isEmpty(phoneNum)) {
            throw new IllegalArgumentException("Phone number must be not null!");
        }

        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = PhoneNumberUtil.getInstance().parse(phoneNum, "CN");
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        return "+" + phoneNumber.getCountryCode();
    }

    public static boolean checkPhoneNumber(String phoneNumber, int countryCode) {
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            String regionCode = phoneNumberUtil.getRegionCodeForCountryCode(countryCode);
            final Phonenumber.PhoneNumber number =
                    phoneNumberUtil.parse(phoneNumber, regionCode);

            return phoneNumberUtil.isValidNumber(number);

        } catch (NumberParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkPhoneNumber(String phoneNumber) {
        try {
            final Phonenumber.PhoneNumber number =
                    PhoneNumberUtil.getInstance().parse(phoneNumber, "CN");

            return PhoneNumberUtil.getInstance().isValidNumber(number);

        } catch (NumberParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
