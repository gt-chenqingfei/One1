package com.oneone.modules.entry.activity;

import com.oneone.AppInitializer;
import com.oneone.BaseActivity;
import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.api.constants.ApiStatus;
import com.oneone.event.EventFinishActivity;
import com.oneone.framework.android.utils.LocaleUtils;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.modules.entry.contract.AccountContract;
import com.oneone.modules.entry.presenter.AccountPresenter;
import com.oneone.modules.setting.ProtocolActivity;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserManager;
import com.oneone.support.share.PlatformUtil;
import com.oneone.utils.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

@LayoutResource(R.layout.activity_wx_bind_phone_page)
@ToolbarResource(title = R.string.str_wx_bind_phone_page_bind_phone_title, navigationIcon = -1)
public class BindMobileActivity extends BaseActivity<AccountPresenter, AccountContract.View>
        implements AccountContract.View {

    public static void startActivity(Context context, String platform, String platformId) {
        Intent it = new Intent(context, BindMobileActivity.class);
        it.putExtra(EXTRA_PLATFORM, platform);
        it.putExtra(EXTRA_PLATFORM_ID, platformId);
        context.startActivity(it);
    }

    public static final String EXTRA_PLATFORM = "mPlatform";
    public static final String EXTRA_PLATFORM_ID = "mPlatformId";

    @BindView(R.id.content_layout_input_phone)
    RelativeLayout wxBindPhoneLayout;
    @BindView(R.id.bind_phone_et)
    EditText phoneEt;
    @BindView(R.id.bind_phone_confirm_phone_num_iv)
    Button phoneConfirmBtn;
    @BindView(R.id.content_layout_input_vericode)
    RelativeLayout inputVeriCodeLayout;
    @BindView(R.id.verify_code_layout)
    LinearLayout verifyCodeLayout;
    @BindView(R.id.verify_code_et)
    EditText verifyCodeEt;
    @BindView(R.id.second_count_tv)
    TextView secondCountTv;
    @BindView(R.id.send_vericode_btn)
    Button sendVericodeBtn;
    @BindView(R.id.change_phone_tv)
    TextView changePhoneTv;

    @OnClick(R.id.user_protocol_tv)
    public void click() {
        ProtocolActivity.startActivity(this);
    }

    private boolean getVeriBtnClickable = true;
    private String platform;
    private String platformId;
    private boolean stop = false;

    @Override
    public AccountPresenter onCreatePresenter() {
        return new AccountPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        platform = intent.getStringExtra(EXTRA_PLATFORM);
        platformId = intent.getStringExtra(EXTRA_PLATFORM_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        countDownNumHandler.removeMessages(0);
        countDownNumHandler.removeMessages(1);
    }


    private Handler countDownNumHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (secondCountTv == null) {
                removeMessages(msg.what);
                return;
            }
            switch (msg.what) {
                case 0:
                    int second = (int) msg.obj;
                    secondCountTv.setText(second + getString(R.string.str_wx_bind_phone_page_send_veri_code_after_second));
                    break;
                case 1:
                    getVeriBtnClickable = true;
                    secondCountTv.setText("");
                    sendVericodeBtn.setText(R.string.str_wx_bind_phone_page_send_veri_code_again);
                    break;
                case 2:
                    secondCountTv.setText("");
                    sendVericodeBtn.setText(R.string.str_wx_bind_phone_page_enter_oneone);
                    getVeriBtnClickable = true;
                    break;

                default:
                    break;
            }
        }

        ;
    };


    public void initView() {
        changePhoneTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                wxBindPhoneLayout.setVisibility(View.VISIBLE);
                inputVeriCodeLayout.setVisibility(View.GONE);
                clearVeriCode();
                phoneEt.requestFocus();
            }
        });

        phoneConfirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (getVeriBtnClickable) {
                    getVeriCode();
                }
            }
        });

        sendVericodeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (getVeriBtnClickable) {
                    getVeriCode();
                } else {
                    if (verifyCodeEt.getText().length() == 4) {
                        String phoneNumber = phoneEt.getText().toString();
                        String countryCode = LocaleUtils.getCountryCodeByPhoneNum(phoneNumber);
                        HereSingletonFactory.getInstance().getUserManager().setCountryCode(countryCode);
                        String validCode = verifyCodeEt.getText().toString();
                        mPresenter.bindMobile(countryCode, phoneNumber, validCode, platform, platformId);
                    }
                }
            }
        });
        verifyCodeEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                } else {
                    String[] defaultStrArr = {getString(R.string.str_login_page_input_veri_code_0), getString(R.string.str_login_page_input_veri_code_1), getString(R.string.str_login_page_input_veri_code_2), getString(R.string.str_login_page_input_veri_code_3)};
                    for (int i = 0; i < verifyCodeLayout.getChildCount(); i++) {
                        TextView tv = (TextView) verifyCodeLayout.getChildAt(i);
                        if (defaultStrArr.length > i)
                            tv.setText(defaultStrArr[i]);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void clearVeriCode() {
        stop = true;
        verifyCodeEt.setText("");

        String[] defaultStrArr = {getString(R.string.str_login_page_input_veri_code_0), getString(R.string.str_login_page_input_veri_code_1), getString(R.string.str_login_page_input_veri_code_2), getString(R.string.str_login_page_input_veri_code_3)};
        for (int i = 0; i < verifyCodeLayout.getChildCount(); i++) {
            TextView tv = (TextView) verifyCodeLayout.getChildAt(i);
            tv.setText(defaultStrArr[i]);
        }
    }

    public void getVeriCode() {
        sendVericodeBtn.setText(R.string.str_wx_bind_phone_page_enter_oneone);

        final String phoneStr = phoneEt.getText().toString();

        if (LocaleUtils.checkPhoneNumber(phoneStr)) {
            getVeriBtnClickable = false;
            mPresenter.getValidateCode(phoneStr);
            wxBindPhoneLayout.setVisibility(android.view.View.GONE);
            inputVeriCodeLayout.setVisibility(android.view.View.VISIBLE);
            verifyCodeEt.requestFocus();
            new Thread() {
                public void run() {
                    int i = 60;
                    while (i >= 0 && !stop) {
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
                    if (stop) {
                        stop = false;
                        countDownNumHandler.sendEmptyMessage(2);
                    } else {
                        countDownNumHandler.sendEmptyMessage(1);
                    }
                }
            }.start();
        } else {
            ToastUtil.show(BindMobileActivity.this, getString(R.string.str_wx_bind_phone_page_wrong_phone_num));
        }
    }

    @Override
    public void validCodeEditViewEnable() {
        getVeriBtnClickable = true;
    }

    @Override
    public void onValidCodeGet(int status) {
        if (status == ApiStatus.OK) {
            ToastUtil.show(BindMobileActivity.this,
                    getString(R.string.str_wx_bind_phone_page_send));
        }
    }

    @Override
    public void alreadyBindWX(String platform, String platformId) {

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
    }

    @Override
    public void goBindWXIfInstall() {
        if (!PlatformUtil.isWechatValid(this)) {
            ToastUtil.show(this,
                    getString(R.string.str_app_notice_not_install_wechat));
            return;
        }
        Intent it = new Intent(this, BindWXActivity.class);
        startActivity(it);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinishActivity(EventFinishActivity event) {
        finish();
    }

}
