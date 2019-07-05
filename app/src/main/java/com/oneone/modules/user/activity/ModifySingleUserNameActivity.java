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

@ToolbarResource(title = R.string.str_modify_user_name_page_title_text)
@LayoutResource(R.layout.activity_modify_single_user_name)
public class ModifySingleUserNameActivity extends BaseActivity {

    @BindView(R.id.user_name_et)
    EditText userNameEt;

    TextView saveTv;

    public static final String EXTRA_NICKNAME = "extra_nickname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveTv = setRightTextMenu(R.string.str_app_title_right_save_text);
        initView();
        userNameEt.addTextChangedListener(new InputTextWatcher(this, userNameEt, InputTextWatcher.USER_NAME_LENGTH_MAX, 1));
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);
        final String nameStr = userNameEt.getText().toString().trim();
        if (StringUtil.isNullOrEmpty(nameStr)) {
            ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_modify_user_name_page_title_text_null));
            return;
        }
        if (nameStr.length() < InputTextWatcher.USER_NAME_LENGTH_MIN) {
            ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_modify_user_name_page_title_text_two));
            return;
        }
        UserManager manager = HereSingletonFactory.getInstance().getUserManager();
        UserProfileUpdateBean updateBean = new UserProfileUpdateBean();
        updateBean.setNickname(nameStr);

        manager.updateUserInfo(new UserManager.UserUpdateListener() {
            @Override
            public void onUserUpdate(UserInfo userInfo, boolean isOk, String message) {
                if (isOk) {
                    ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_app_save_request_ok));
                    Intent it = new Intent();
                    it.putExtra(EXTRA_NICKNAME, nameStr);
                    setResult(RESULT_OK, it);
                    ModifySingleUserNameActivity.this.finish();
                    SoftKeyBoardUtil.hideSoftInput(ModifySingleUserNameActivity.this);
                } else {
                    ToastUtil.show(getActivityContext(), getResources().getString(R.string.str_app_save_request_fail));
                }
            }
        }, updateBean);
    }

    public void initView() {
        String nickName = HereUser.getInstance().getUserInfo().getMyNickname();
        if (nickName != null && !nickName.equals("")) {
            userNameEt.setText(nickName);
            userNameEt.setSelection(userNameEt.getText().length());
        }
    }

    public static void startActivity4Rlt(Activity context, int reqCode) {
        Intent intent = new Intent(context, ModifySingleUserNameActivity.class);
        context.startActivityForResult(intent, reqCode);
    }

}
