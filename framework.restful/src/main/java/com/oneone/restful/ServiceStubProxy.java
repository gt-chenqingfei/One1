package com.oneone.restful;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class ServiceStubProxy implements InvocationHandler {

    private final Class<?> iface;

    private final Map<Method, Invocation> invocations;

    ServiceStubProxy(final Class<?> iface, final Map<Method, Invocation> invocations) {
        this.iface = iface;
        this.invocations = invocations;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final Invocation invocation = this.invocations.get(method);
        if (null != invocation) {
            return invocation.invoke(args);
        }

        final String methodName = method.getName();
        if ("equals".equals(methodName) && null != args && 1 == args.length) {
            return this.equals(proxy);
        } else if ("hashCode".equals(methodName) && (null == args || 0 == args.length)) {
            return this.hashCode();
        } else if ("toString".equals(methodName) && (null == args || 0 == args.length)) {
            return this.toString();
        }

        throw new NoSuchMethodException(method.toGenericString());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;

        if (!(o instanceof ServiceStubProxy)) {
            return false;
        }

        if (((ServiceStubProxy) o).iface != this.iface) {
            return false;
        }

        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return this.iface.hashCode();
    }

    @Override
    public String toString() {
        return "Proxy for " + this.iface.getName();
    }

}
