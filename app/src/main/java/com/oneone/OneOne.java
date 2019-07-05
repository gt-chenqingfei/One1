package com.oneone;


import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.alibaba.android.arouter.launcher.ARouter;
import com.mob.MobSDK;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.oneone.framework.android.ApplicationContext;
import com.oneone.framework.android.environment.StorageUtil;
import com.oneone.support.push.PushHandler;
import com.oneone.utils.ImagePickerHelper;
import com.oneone.widget.CustomGlobalFooter;
import com.oneone.widget.CustomGlobalHeader;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.beans.TalkBeans.attachment.CustomAttachParser;
import com.oneone.support.qiniu.QiNiuConfiguration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.bugly.crashreport.CrashReport;

import ch.qos.logback.classic.android.BasicLogcatConfigurator;

import static com.oneone.framework.android.utils.AppUtils.getCurProcessName;

public class OneOne extends ApplicationContext {
    public static OneOne mContext;

    static {
        // config logback
        BasicLogcatConfigurator.configureDefaultContext();

        /* 开启SVG图片的selector 和 drawableLeft 等属性 */
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new CustomGlobalHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new CustomGlobalFooter(context);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        IMManager.getInstance(mContext).init(mContext);
        PushHandler.init(this);
        if (!getApplicationInfo().packageName.equals(currentProcessName)) {
            return;
        }
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);
        AppInitializer.init(this);
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser()); // 监听的注册，必须在主进程中。
        registerActivityLifecycleCallbacks(this);
        StorageUtil.init(this, null);
        QiNiuConfiguration.init();
        MobSDK.init(this);

        ImagePickerHelper.init();
        CrashReport.initCrashReport(getApplicationContext(), "927234253f", true);
    }


}
