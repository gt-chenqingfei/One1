package com.oneone.modules.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.oneone.BaseActivity;
import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.framework.android.utils.SoftKeyBoardUtil;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserManager;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.utils.StringUtil;
import com.oneone.utils.ToastUtil;
import com.oneone.widget.InputTextWatcher;

import butterknife.BindView;

/**
 * Created by here on 18/5/3.
 */

@ToolbarResource(title = R.string.str_modify_user_sign_page_title_text)
@LayoutResource(R.layout.activity_modify_single_user_sign)
public class ModifySingleUserSignActivity extends BaseActivity {
    @BindView(R.id.user_sign_et)
    EditText userSignEt;
    @BindView(R.id.et_limit_tv)
    TextView limitTv;

    TextView saveTv;
    public static final String EXTRA_SIGN = "extra_sign";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveTv = setRightTextMenu(R.string.str_app_title_right_save_text);
        initView();
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);
        final String inputStr = userSignEt.getText().toString().trim();

        if (!StringUtil.isNullOrEmpty(inputStr)) {
            UserManager manager = HereSingletonFactory.getInstance().getUserManager();
            UserProfileUpdateBean updateBean = new UserProfileUpdateBean();
            updateBean.setMonologue(inputStr);
            manager.updateUserInfo(new UserManager.UserUpdateListener() {
                @Override
                public void onUserUpdate(UserInfo userInfo, boolean isOk, String message) {
                    if (isOk) {
                        ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_app_save_request_ok));
                        Intent it = new Intent();
                        it.putExtra(EXTRA_SIGN, inputStr);
                        setResult(RESULT_OK, it);
                        SoftKeyBoardUtil.hideSoftInput(ModifySingleUserSignActivity.this);
                        ModifySingleUserSignActivity.this.finish();
                    } else {
                        ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_app_save_request_fail));
                    }
                }
            }, updateBean);
        } else {
            SoftKeyBoardUtil.hideSoftInput(ModifySingleUserSignActivity.this);
            ModifySingleUserSignActivity.this.finish();
        }
    }

    public void initView() {
        limitTv.setText("" + InputTextWatcher.CUSTOM_SIGN_LENGTH_MAX);
        userSignEt.addTextChangedListener(new InputTextWatcher(this, userSignEt, InputTextWatcher.CUSTOM_SIGN_LENGTH_MAX, 0, new InputTextWatcher.CurrentInputTextListener() {
            @Override
            public void onCurrentInputTextListener(int num) {
                limitTv.setText("" + (InputTextWatcher.CUSTOM_SIGN_LENGTH_MAX - num));
            }
        }));

        String signStr = HereUser.getInstance().getUserInfo().getMyMonologue();
        if (signStr != null && !signStr.equals("")) {
            userSignEt.setText(signStr);
            userSignEt.setSelection(userSignEt.getText().length());
        }
    }

    public static void startActivity4Rlt(Activity context, int reqCode) {
        Intent intent = new Intent(context, ModifySingleUserSignActivity.class);
        context.startActivityForResult(intent, reqCode);
    }
}
