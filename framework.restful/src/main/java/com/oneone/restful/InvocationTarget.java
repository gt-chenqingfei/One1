package com.oneone.restful;

/**
 * Represent the target of RESTful service
 *
 * @author qingfei.chen
 * @since 1.0.0
 */
public class InvocationTarget {

    public final String url;

    public final String method;

    public final ApiResult apiResult;

    public InvocationTarget(final String url) {
        this(url, "GET");
    }

    public InvocationTarget(final String url, final String method) {
        this(url, method, null);
    }

    public InvocationTarget(final String url, final String method, ApiResult apiResult) {
        this.url = url;
        this.method = method;
        this.apiResult = apiResult;
    }

    @Override
    public String toString() {
        return this.method + " " + this.url;
    }

}
