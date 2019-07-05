package com.oneone.framework.android.tiny.tiny.common;

import com.oneone.framework.android.tiny.tiny.Tiny;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by zhengxiaoyong on 2017/3/12.
 */
public final class TinyUtil {

    public static void printExceptionMessage(Throwable t) {
        if (t != null && Tiny.getInstance().isDebug()) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.flush();

            String stackTrace = sw.toString();

            Logger.e("an exception appeared in the process of compression! exception information:" +
                    "\n" +
                    "thread-name:" + Thread.currentThread().getName() +
                    "\n" +
                    "exception-message:" + t.getMessage() +
                    "\n" +
                    "stacktrace:" + stackTrace
            );
        }
    }

}
