package com.oneone.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * 监测 EditText 内容
 * <p>
 * Created by ZhaiDongyang on 2018/7/5
 */
public class InputTextWatcher implements TextWatcher {

    public static final String SPECIAL_CHAR = "[`~!@#$%^&*()_\\-+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
    public static final int USER_NAME_LENGTH_MIN = 2;// 用户名字最短
    public static final int USER_NAME_LENGTH_MAX = 13;// 用户名字最长
    public static final int CUSTOM_SIGN_LENGTH_MAX = 30;// 个性签名最长
    private static final int NOT_SPECIAL_CHAR = 1;// 1 不能输入特殊字符
    public static final int  STORY_MAX_LENGTH = 50;// 故事编辑最多输入字数

    private Context mContext;
    private EditText mEditText;
    private Pattern mPattern;
    private int mMaxNum;
    private int mSpecialChar = -1;

    private CurrentInputTextListener mListener;

    public interface CurrentInputTextListener {
        void onCurrentInputTextListener(int num);
    }

    public InputTextWatcher(Context context, EditText editText, int maxNum, int specialChar) {
        this.mContext = context;
        this.mEditText = editText;
        this.mMaxNum = maxNum;
        this.mSpecialChar = specialChar;
        initTextWatch(editText, maxNum);
    }

    public InputTextWatcher(Context context, EditText editText, int maxNum, int specialChar, CurrentInputTextListener listener) {
        this.mContext = context;
        this.mEditText = editText;
        this.mMaxNum = maxNum;
        this.mSpecialChar = specialChar;
        this.mListener = listener;
        initTextWatch(editText, maxNum);
    }

    private void initTextWatch(EditText editText, int maxNum) {
        mPattern = Pattern.compile(SPECIAL_CHAR);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxNum), new NoStartAndEndSpaceCharInputFilter()});
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mListener != null) {
            Editable editable = mEditText.getText();
            int len = editable.length();
            mListener.onCurrentInputTextListener(len);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mSpecialChar == NOT_SPECIAL_CHAR) {
            if (mPattern.matcher(s).find()) {
                int index = mEditText.getSelectionStart();
                s.delete(index - 1, index);
            }
        }
    }

    /**
     * 第一个字符不能是空格
     */
    private class NoStartAndEndSpaceCharInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null) {
                if (dest != null && dest.length() != 0) {
                    if (dest.toString().length() < 1) {
                        return source.toString().trim();
                    } else if(dstart == 0) {
                        return source.toString().trim();
                    }
                } else {
                    return source.toString().trim();
                }
            }
            return source;
        }
    }

}
