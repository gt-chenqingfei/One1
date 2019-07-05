package com.oneone.restful;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.oneone.framework.android.utils.ConnectivityUtils;
import com.oneone.restful.annotation.BodyJsonParameter;
import com.oneone.restful.annotation.BodyParameter;
import com.oneone.restful.annotation.HttpDelete;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.HttpPost;
import com.oneone.restful.annotation.HttpPut;
import com.oneone.restful.annotation.MatrixParameter;
import com.oneone.restful.annotation.Path;
import com.oneone.restful.annotation.PathParameter;
import com.oneone.restful.annotation.QueryParameter;
import com.oneone.restful.cache.APICache;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpRequestBase;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
/**
 * @author qingfei.chen
 * @since 2018/3/31.
 * @version v1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class ServiceStubInvocation implements Invocation {

    public static final String ENCODING_GZIP = "gzip";

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("ServiceStubInvocation");

    final Context context;

    final Class<?> iface;

    final Method method;

    final String baseUrl;

    final Map<String, String> headers;

    final InvocationExpireListener expireListener;


    ServiceStubInvocation(final Context context, final Class<?> iface, final Method method,
                          final String baseUrl, Class<?> targetClass, InvocationExpireListener expireListener) {
        this(context, iface, method, baseUrl, Collections.<String, String>emptyMap(), expireListener);
    }

    ServiceStubInvocation(final Context context, final Class<?> iface, final Method method,
                          final String baseUrl, final Map<String, String> headers,
                          InvocationExpireListener expireListener) {
        this.context = context;
        this.iface = iface;
        this.method = method;
        this.baseUrl = baseUrl;
        this.headers = null == headers ? Collections.<String, String>emptyMap() : headers;
        this.expireListener = expireListener;
    }

    @Override
    public Object invoke(final Object... args) throws InvocationException {
        final HttpClient client = new RestfulHttpClient().getHttpClient();
        return this.invoke(client, args);
    }

    private Object invoke(final HttpClient client, final Object... args) throws InvocationException {

        logger.info("ServiceStubInvocation request Invoking " + this.iface.getName() + "#" + this.method.getName() + " ars:" + Arrays.toString(args));

        String svcPath = "";
        if (this.iface.isAnnotationPresent(Path.class)) {
            svcPath = this.iface.getAnnotation(Path.class).value();
        }

        final String httpMethod;
        final String servicePath;
        String httpGetEncoding = null;
        if (this.method.isAnnotationPresent(HttpPost.class)) {
            httpMethod = "POST";
            servicePath = this.method.getAnnotation(HttpPost.class).value();
        } else if (this.method.isAnnotationPresent(HttpPut.class)) {
            httpMethod = "PUT";
            servicePath = this.method.getAnnotation(HttpPut.class).value();
        } else if (this.method.isAnnotationPresent(HttpDelete.class)) {
            httpMethod = "DELETE";
            servicePath = this.method.getAnnotation(HttpDelete.class).value();
        } else if (this.method.isAnnotationPresent(HttpGet.class)) {
            httpMethod = "GET";
            servicePath = this.method.getAnnotation(HttpGet.class).value();

        } else {
            throw new UnsupportedOperationException(this.method.getName()
                    + " does not annotated by any HTTP method");
        }

        final List<NameValuePair> bodyParams = new ArrayList<NameValuePair>();
        String bodyJsonParams = null;
        final List<NameValuePair> matrixParams = new ArrayList<NameValuePair>();
        final List<NameValuePair> pathParams = new ArrayList<NameValuePair>();
        final List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();
        final Map<String, Object> bodyCacheParams = new TreeMap<String, Object>();
        if (null != paramAnnotations && paramAnnotations.length > 0) {
            for (int i = 0; i < paramAnnotations.length; i++) {
                final String value = String.valueOf(args[i]);
                final Annotation[] annotations = paramAnnotations[i];
                if (TextUtils.isEmpty(value) || "null".equals(value)) {
                    continue;
                }
                if (null != annotations && annotations.length > 0) {
                    for (final Annotation annotation : annotations) {
                        final Class<?> annotationType = annotation.annotationType();

                        if (QueryParameter.class.equals(annotationType)) {
                            final String name = ((QueryParameter) annotation).value();
                            queryParams.add(new BasicNameValuePair(name, value));
                        } else if (BodyParameter.class.equals(annotationType)) {
                            final String name = ((BodyParameter) annotation).value();
                            try {
                                bodyParams.add(new BasicNameValuePair(name,
                                        value));
                            } catch (Exception e) {

                            }
                            bodyCacheParams.put(name, value);
                        } else if (MatrixParameter.class.equals(annotationType)) {
                            final String name = ((MatrixParameter) annotation).value();
                            matrixParams.add(new BasicNameValuePair(name, value));
                        } else if (PathParameter.class.equals(annotationType)) {
                            final String name = ((PathParameter) annotation).value();
                            pathParams.add(new BasicNameValuePair(name, value));
                        } else if (BodyJsonParameter.class.equals(annotationType)) {
                            bodyJsonParams = value;
                        }
                    }
                }
            }
        }

        final StringBuilder queryString = new StringBuilder();
        if (queryParams.size() > 0) {
            try {
                queryString.append("?").append(
                        TextUtils.isEmpty(httpGetEncoding)
                                ? Utils.format(queryParams)
                                : EntityUtils.toString(
                                new UrlEncodedFormEntity(queryParams, httpGetEncoding)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final String apiPath;
        if (pathParams.size() > 0) {
            String path = servicePath;
            for (final NameValuePair nvp : pathParams) {
                path = path.replaceAll("\\{" + nvp.getName() + "\\}", nvp.getValue());
            }
            apiPath = path;
        } else {
            apiPath = servicePath;
        }

        final HttpRequestBase request;
        final String url = baseUrl + svcPath + apiPath + queryString.toString();
        final InvocationTarget target = new InvocationTarget(url, httpMethod);

        logger.debug(target.toString());

        try {
            final URI uri = new URI(url);

            if (this.method.isAnnotationPresent(HttpPost.class)) {
                request = new org.apache.http.client.methods.HttpPost(uri);
            } else if (this.method.isAnnotationPresent(HttpPut.class)) {
                request = new org.apache.http.client.methods.HttpPut(uri);
            } else if (this.method.isAnnotationPresent(HttpDelete.class)) {
                request = new org.apache.http.client.methods.HttpDelete(uri);
            } else {
                request = new org.apache.http.client.methods.HttpGet(uri);
            }
        } catch (final URISyntaxException uriSyntaxException) {
            throw new InvocationException(target, null, uriSyntaxException);
        }

        HttpResponse response = null;
        final NetworkInfo ni = ConnectivityUtils.getActiveNetwork(this.context);
        if (ni != null && ni.isConnected()) {
            try {
                request.setHeader("User-Agent", buildUserAgent(this.context));
                request.setHeader("Accept-Language", Locale.getDefault().getLanguage());
                request.setHeader("Accept-Encoding", ENCODING_GZIP);

                for (final Map.Entry<String, String> entry : this.headers.entrySet()) {
                    request.setHeader(entry.getKey(), entry.getValue());
                }

                if (request instanceof HttpEntityEnclosingRequest) {
                    JSONObject jsonParams = null;
                    if (!bodyParams.isEmpty()) {
                        jsonParams = new JSONObject();
                        for (int i = 0; i < bodyParams.size(); i++) {
                            jsonParams.put(bodyParams.get(i).getName(), bodyParams.get(i).getValue());
                        }
                    }

                    if (!TextUtils.isEmpty(bodyJsonParams)) {
                        jsonParams = new JSONObject(bodyJsonParams);
                    }

                    if (jsonParams != null) {
                        StringEntity se = new StringEntity(jsonParams.toString(), "UTF-8");
                        se.setContentType("application/json");
                        ((HttpEntityEnclosingRequest) request).setEntity(se);
                    }
                }


                if (null == (response = client.execute(request))) {
//                    throw new InvocationException(target);
                    return new ApiResult();//fixme add exception type
                }
            } catch (final Exception e) {
                logger.error("ServiceStubInvocation error",e);

                return new ApiResult();//fixme add exception type
//                throw new InvocationException(target, null, e);
            }

            final StatusLine status = response.getStatusLine();
            if (null == status) {
                throw new InvocationException(target);
            }

            switch (status.getStatusCode()) {
                case 200: {

                    HttpEntity entity = response.getEntity();

                    if (null == entity) {
                        throw new InvocationException(target, status);
                    }
                    final Header encoding = entity.getContentEncoding();
                    if (encoding != null) {
                        for (HeaderElement element : encoding.getElements()) {
                            if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
                                response.setEntity(new InflatingEntity(entity));
                                break;
                            }
                        }
                    }

                    entity = response.getEntity();

                    try {
                        String resultEntity = EntityUtils.toString(entity, "UTF-8");
                        APICache.getInstance(this.context).setCachedResult(apiPath,
                                bodyCacheParams, resultEntity);

                        return generateResponse(resultEntity);

                    } catch (final Exception e) {
                        logger.error("generateResponse error ", e);
                        throw new InvocationException(target, status, e);
                    }
                }
                default: {
                    HttpEntity entity = response.getEntity();
                    String resultEntity = null;
                    try {
                        resultEntity = EntityUtils.toString(entity, "UTF-8");
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.error("EntityUtils.toString error ", e);
                    }
                    ApiResult apiResult = new ApiResult();
                    JSONObject object = getJsonObject(resultEntity);
                    if (object == null) {
                        return apiResult;
                    }

                    apiResult.setStatus(object.optInt("status"));
                    apiResult.setMessage(object.optString("message"));

                    logger.debug(status.toString(), object.toString());
                    return apiResult;
                }
            }
        } else {
            JSONObject object = null;
            String entity = APICache.getInstance(this.context).
                    getCachedResult(apiPath, bodyCacheParams);
            try {

                if (!TextUtils.isEmpty(entity)) {
                    object = (JSONObject) new JSONTokener(entity).nextValue();
                } else {
                    object = new JSONObject();
                }

                ApiResult apiResult = generateResponse(object.toString());
                apiResult.setStatus(-1);
                apiResult.setMessage(context.getString(context.getResources().
                        getIdentifier("network_not_awesome", "string", context.getPackageName())));
                return apiResult;
            } catch (final Exception e) {
                throw new InvocationException(target);
            }
        }
    }

    private JSONObject getJsonObject(String resultEntity) {
        JSONObject result = null;

        Object object = null;
        try {
            object = new JSONTokener(resultEntity).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
            logger.error("getJsonObject error:", e);
        }

        if (object == null) {
            return null;
        }
        try {
            result = (JSONObject) object;
        }catch (ClassCastException e){
            return result;
        }
        return result;
    }

    private ApiResult generateResponse(String resultEntity) {
        JSONObject result = getJsonObject(resultEntity);
        ApiResult apiResult = new ApiResult();
        if (result == null) {
            return apiResult;
        }

        Type returnType = method.getGenericReturnType();

        logger.info("ServiceStubInvocation request response result = " + result.toString());
        apiResult.setStatus(result.optInt("status"));
        apiResult.setMessage(result.optString("message"));
        JSONObject data = result.optJSONObject("data");

        System.out.println("  " + returnType);
        if (!(returnType instanceof ParameterizedType)) {
            apiResult.setData(data);
            return apiResult;
        }
        Type[] types = ((ParameterizedType) returnType)
                .getActualTypeArguments();
        if (types == null || types.length <= 0) {
            apiResult.setData(data);
            return apiResult;
        }

        if (data == null) {
            return apiResult;
        }

        Type type = types[0];
        if (data.length() == 1) {
            String typeName = null;
            String key = "";
            if (data.keys().hasNext()) {
                key = data.keys().next();
            }
            if (type instanceof ParameterizedType) {
                typeName = ((ParameterizedType) type).getRawType().toString();
            } else {
                typeName = ((Class) type).getSimpleName();
                if (!TextUtils.isEmpty(typeName)) {
                    String firstChar = typeName.substring(0, 1);
                    typeName = typeName.replace(firstChar, firstChar.toLowerCase());
                }
            }
            if (TextUtils.isEmpty(typeName)) {
                apiResult.setData(new Gson().fromJson(data.toString(), type));
                return apiResult;
            }
            if (typeName.endsWith(List.class.getName())) {
                apiResult.setData(new Gson().fromJson(data.optJSONArray(key).toString(), type));
            } else if (JSONObject.class.getName().toLowerCase().endsWith(typeName.toLowerCase())) {
                apiResult.setData(data);
            } else {
                if (data.opt(key) != null) {
                    apiResult.setData(new Gson().fromJson(data.opt(key).toString(), type));
                } else {
                    apiResult.setData(null);
                }
            }
        } else {
            apiResult.setData(new Gson().fromJson(data.toString(), type));
        }

        return apiResult;
    }

    static String buildUserAgent(final Context context) {
        final String osVersion = "Android/" + Build.VERSION.RELEASE;
        final PackageManager pm = context.getPackageManager();
        final String packageName = context.getPackageName();

        try {
            final String versionName = pm.getPackageInfo(packageName, 0).versionName;
            return osVersion + " " + packageName + "/" + versionName;
        } catch (final Exception e) {
            return osVersion;
        }
    }

}