package com.oneone.restful;

import org.apache.http.StatusLine;

/**
 * @author qingfei.chen
 */
public class InvocationException extends Exception {

    private final InvocationTarget target;

    private final StatusLine status;

    public InvocationException(final InvocationTarget target) {
        this(target, null, null, null);
    }

    public InvocationException(final InvocationTarget target, final StatusLine status) {
        this(target, status, null, null);
    }

    public InvocationException(final InvocationTarget target, final StatusLine status, final Throwable t) {
        this(target, status, null, null);
    }

    public InvocationException(final InvocationTarget target, final StatusLine status, final String detailMessage, final Throwable t) {
        super(detailMessage, t);
        this.target = target;
        this.status = status;
    }

}
