package com.oneone.modules.entry.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.oneone.AppInitializer;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.api.constants.ApiStatus;
import com.oneone.event.EventFinishActivity;
import com.oneone.framework.android.utils.LocaleUtils;
import com.oneone.framework.android.utils.SoftKeyBoardUtil;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.dialog.WarnDialog;
import com.oneone.framework.ui.utils.Res;
import com.oneone.modules.entry.contract.AccountContract;
import com.oneone.modules.entry.presenter.AccountPresenter;
import com.oneone.modules.setting.ProtocolActivity;
import com.oneone.modules.upgrate.CheckVersionManager;
import com.oneone.support.share.Callback;
import com.oneone.support.share.PlatformUtil;
import com.oneone.support.share.ShareBase;
import com.oneone.support.share.ShareParams;
import com.oneone.support.share.Wechat;
import com.oneone.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;

@Route(path = "/entry/login")
@LayoutResource(R.layout.activity_login_page)
public class LoginActivity extends BaseActivity<AccountPresenter, AccountContract.View> implements
        AccountContract.View, OnClickListener, TextWatcher, Callback {

    @BindView(R.id.login_page_layout_1)
    RelativeLayout loginPageLayout1;
    @BindView(R.id.login_btn_layout)
    RelativeLayout phoneLoginLayout;
    @BindView(R.id.wx_login_btn_layout)
    RelativeLayout wxLoginLayout;
    @BindView(R.id.login_page_layout_2)
    RelativeLayout loginPageLayout2;
    @BindView(R.id.login_page_layout_2_input_phone_left_arrow_iv)
    ImageView loginPageLayoutLeftArrowIv;
    @BindView(R.id.login_page_layout_2_input_phone_inner_et)
    EditText loginPageLayout2PhoneEt;
    @BindView(R.id.login_page_layout_2_input_phone_inner_confirm_layout)
    RelativeLayout inputPhoneNumConfirmLayout;
    @BindView(R.id.one_one_login_page_confirm_phone_right_arrow_iv)
    ImageView inputPhoneNumConfirmRightArrowIv1;
    @BindView(R.id.one_one_login_page_confirm_phone_second_count_tv)
    TextView inputPhoneNumConfirmCountDownTv1;
    @BindView(R.id.user_protocol_tv)
    TextView userProtocolTv;
    @BindView(R.id.ll_protocol_oneone)
    LinearLayout userProtocolLl;
    @BindView(R.id.verify_code_outer_layout)
    RelativeLayout verifyCodeOuterLayout;
    @BindView(R.id.verify_code_layout)
    LinearLayout verifyCodeLayout;
    @BindView(R.id.verify_code_et)
    EditText verifyCodeEt;
    private static TextView inputPhoneNumConfirmCountDownTv;
    private static ImageView inputPhoneNumConfirmRightArrowIv;

    private static boolean getVeriBtnClickable = true;
    private LoginHandler countDownNumHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImmersionBar.statusBarDarkFont(true)
                .navigationBarColor(R.color.color_black)
                .keyboardEnable(true).init();
        inputPhoneNumConfirmCountDownTv = inputPhoneNumConfirmCountDownTv1;
        inputPhoneNumConfirmRightArrowIv = inputPhoneNumConfirmRightArrowIv1;
        countDownNumHandler = new LoginHandler(this);
        EventBus.getDefault().register(this);
        CheckVersionManager.getInstance().checkServerVersion(this);

        boolean booleanExtra = getIntent().getBooleanExtra("EXTRA_KICK_OUT", false);
        if (booleanExtra) {
            WarnDialog dialog = new WarnDialog(this, R.string.str_kick_out_tip, null);
            dialog.setNegativeButton(getString(R.string.str_known));
            dialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        countDownNumHandler.removeMessages(0);
        countDownNumHandler.removeMessages(1);
    }

    @Override
    public void onInitListener() {
        super.onInitListener();
        phoneLoginLayout.setOnClickListener(this);
        wxLoginLayout.setOnClickListener(this);
        userProtocolTv.setOnClickListener(this);
        loginPageLayoutLeftArrowIv.setOnClickListener(this);
        inputPhoneNumConfirmLayout.setOnClickListener(this);
        verifyCodeEt.addTextChangedListener(this);
    }

    @Override
    public AccountPresenter onCreatePresenter() {
        return new AccountPresenter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_layout:
                performPhoneLoginView();
                getVeriBtnClickable = true;
                break;

            case R.id.wx_login_btn_layout:
                getWXAuthInfo();
                break;

            case R.id.login_page_layout_2_input_phone_left_arrow_iv:
                performBack();
                break;

            case R.id.login_page_layout_2_input_phone_inner_confirm_layout:
                performPhoneLogin();
                break;

            case R.id.user_protocol_tv:
                ProtocolActivity.startActivity(this);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        final String validCode = s.toString();
        char[] c = new char[s.length()];
        if (c.length > 0) {
            s.toString().getChars(0, s.length(), c, 0);
            for (int i = 0; i < verifyCodeLayout.getChildCount(); i++) {
                TextView tv = (TextView) verifyCodeLayout.getChildAt(i);
                tv.setText("");
                if (c.length > i) {
                    tv.setText(c[i] + "");
                }
            }

            final String phoneStr = loginPageLayout2PhoneEt.getText().toString();

            if (c.length == 4 && !TextUtils.isEmpty(phoneStr)) {
                verifyCodeEt.setEnabled(false);
                mPresenter.loginByPhone(phoneStr, validCode);
            }
        } else {
            String[] defaultStrArr = {getString(R.string.str_login_page_input_veri_code_0), getString(R.string.str_login_page_input_veri_code_1), getString(R.string.str_login_page_input_veri_code_2), getString(R.string.str_login_page_input_veri_code_3)};
            for (int i = 0; i < verifyCodeLayout.getChildCount(); i++) {
                TextView tv = (TextView) verifyCodeLayout.getChildAt(i);
                if (defaultStrArr.length > i)
                    tv.setText(defaultStrArr[i]);
            }
        }
    }

    private void performPhoneLoginView() {
        loginPageLayout1.setVisibility(View.GONE);
        loginPageLayout2.setVisibility(View.VISIBLE);

        loginPageLayout2PhoneEt.setFocusable(true);
        loginPageLayout2PhoneEt.setFocusableInTouchMode(true);
        loginPageLayout2PhoneEt.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) loginPageLayout2PhoneEt.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(loginPageLayout2PhoneEt, 0);
    }

    private void performBack() {
        loginPageLayout2.setVisibility(View.GONE);
        loginPageLayout1.setVisibility(View.VISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(loginPageLayout2PhoneEt.getWindowToken(), 0);
    }

    private void performPhoneLogin() {
        final String phoneStr = loginPageLayout2PhoneEt.getText().toString();
        if (!getVeriBtnClickable) {
            return;
        }
        if (LocaleUtils.checkPhoneNumber(phoneStr)) {
            getVeriBtnClickable = false;
            mPresenter.getValidateCode(phoneStr);
        } else {
            ToastUtil.show(this, getString(R.string.str_app_notice_phone_num_err));
        }
    }

    @Override
    public void goHome() {
        AppInitializer.getInstance().startMainAndLoadPreData(this);
    }

    @Override
    public void goBindPhone(String platform, String platformId) {
        BindMobileActivity.startActivity(this, platform, platformId);
    }

    @Override
    public void goUserRoleSelect() {
        UserRoleSelectedActivity.startActivity(this);
        finish();
    }

    @Override
    public void goBindWXIfInstall() {
        if (!PlatformUtil.isWechatValid(this)) {
            goUserRoleSelect();
            return;
        }
        Intent it = new Intent(this, BindWXActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void validCodeEditViewEnable() {
        verifyCodeEt.setEnabled(true);
    }

    @Override
    public void onValidCodeGet(int status) {
        if (status != ApiStatus.OK) {
            getVeriBtnClickable = true;
            return;
        }

        userProtocolLl.setVisibility(View.GONE);
        verifyCodeOuterLayout.setVisibility(View.VISIBLE);

        inputPhoneNumConfirmCountDownTv.setText("60s");
        inputPhoneNumConfirmRightArrowIv.setVisibility(View.GONE);
        inputPhoneNumConfirmCountDownTv.setVisibility(View.VISIBLE);

        SoftKeyBoardUtil.toggleSoftInput();
        verifyCodeEt.requestFocus();

        new Thread() {
            public void run() {
                int i = 60;
                while (i >= 0) {
                    if (isFinishing()) {
                        return;
                    }
                    countDownNumHandler.sendMessage(countDownNumHandler.obtainMessage(0, i));
                    i--;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                countDownNumHandler.sendEmptyMessage(1);
            }

        }.start();
    }

    @Override
    public void alreadyBindWX(String platform, String platformId) {

    }

    static class LoginHandler extends Handler {

        WeakReference<Activity> weakReference;

        LoginHandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        public void handleMessage(android.os.Message msg) {
            Activity activity = weakReference.get();
            if (activity == null) return;
            switch (msg.what) {
                case 0:
                    int second = (int) msg.obj;
                    inputPhoneNumConfirmCountDownTv.setText(second + "s");
                    break;
                case 1:
                    inputPhoneNumConfirmCountDownTv.setVisibility(View.GONE);
                    inputPhoneNumConfirmCountDownTv.setText("60s");
                    inputPhoneNumConfirmRightArrowIv.setVisibility(View.VISIBLE);
                    getVeriBtnClickable = true;
                    break;

                default:
                    break;
            }
        }
    }

    ;

    private void getWXAuthInfo() {
        ShareBase shareBase = new Wechat(this, this);
        boolean ret = shareBase.getAuthorized();
        if (!ret) {
            showError(Res.getString(R.string.str_app_notice_not_install_wechat));
        }
        showLoading("");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinishActivity(EventFinishActivity event) {
        finish();
    }

    @Override
    public void onComplete(ShareParams shareParams, Platform platform, int i, HashMap<String, Object> hashMap) {
        loadingDismiss();
        if (platform.getName().equals(cn.sharesdk.wechat.friends.Wechat.NAME)) {
            //TODO TEST
//            mPresenter.loginByThirdPart(platform.getName().toLowerCase(), Constants.WX.TEST_WX_OPENID);
            mPresenter.loginByThirdPart(platform.getName().toLowerCase(), platform.getDb().getUserId());
        }
    }

    @Override
    public void onError(ShareParams shareParams, Platform platform, int i, Throwable throwable) {
        loadingDismiss();
    }

    @Override
    public void onCancel(ShareParams shareParams, Platform platform, int i) {
        loadingDismiss();
    }
}
