package com.oneone.framework.android.preference;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegerEditTextPreference extends EditTextPreference {

    private static final Logger logger = LoggerFactory.getLogger(IntegerEditTextPreference.class);

    public IntegerEditTextPreference(Context context) {
        this(context, null);
    }

    public IntegerEditTextPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IntegerEditTextPreference(Context context, AttributeSet attrs,
                                     int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected String getPersistedString(String defaultReturnValue) {
        int defaultValue = 0;

        try {
            defaultValue = Long.decode(defaultReturnValue).intValue();
        } catch (NumberFormatException e) {
            defaultValue = 0;
        } catch (NullPointerException e) {
            defaultValue = 0;
        }

        return String.valueOf(getPersistedInt(defaultValue));
    }

    @Override
    protected boolean persistString(String value) {
        try {
            return persistInt(Long.decode(value).intValue());
        } catch (NumberFormatException e) {
            logger.error("Invalid format", e);
            return false;
        }
    }

}
