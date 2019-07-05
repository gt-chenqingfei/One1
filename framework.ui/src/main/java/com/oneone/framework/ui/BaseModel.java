package com.oneone.framework.ui;

import android.content.Context;

import com.oneone.framework.ui.ibase.IBaseModel;

public class BaseModel implements IBaseModel {

    private Context mContext;

    public BaseModel(Context context) {
        this.mContext = context;
    }
}
