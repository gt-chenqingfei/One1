package com.oneone.api;

import android.content.Context;


import com.oneone.restful.InvocationExpireListener;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.ServiceStubFactory;

import java.util.Collections;
import java.util.Map;

public class RestfulAPIFactory extends ServiceStubFactory implements InvocationExpireListener {

    public RestfulAPIFactory(Context context) {
        super(context);
    }

    public <T extends ServiceStub> T create(final Class<T> iface, final String baseUrl) {
        return super.create(iface, baseUrl, Collections.<String, String>emptyMap(), this);
    }

    public <T extends ServiceStub> T create(final Class<T> iface) {
        return super.create(iface, RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context), this);
    }

    public <T extends ServiceStub> T create(final Class<T> iface, final String baseUrl, final Map<String, String> headers) {

        return (T) super.create(iface, baseUrl, headers, this);
    }

    @Override
    public void onInvokeTokenExpire() {
        // TODO: 2018/3/26
    }
}
