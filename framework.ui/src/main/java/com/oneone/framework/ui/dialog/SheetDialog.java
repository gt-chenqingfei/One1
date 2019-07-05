package com.oneone.framework.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oneone.framework.ui.BasePopDialog;
import com.oneone.framework.ui.R;
import com.oneone.framework.ui.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;


public class SheetDialog extends BasePopDialog {

    TextView mTvCancel;

    TextView mTitle;
    View mTitleContainer;

    LinearLayout mContainer;

    ScrollView mScrollView;

    private Context mContext;
    private List<SheetItem> mSheetItemList;
    private String title;
    private int id;
    private OnSheetItemClickListener listener;

    public SheetDialog(Context context, String title) {
        super(context);
        this.title = title;
        this.mContext = context;
    }

    public SheetDialog(Context context) {
        this(context, null);
    }

    public SheetDialog(Context context, int arrayResId, int id, OnSheetItemClickListener listener) {
        this(context, arrayResId, id, listener, 0);
    }

    public SheetDialog(Context context, int arrayResId, int id, OnSheetItemClickListener listener, int arg0) {
        this(context, null);
        this.id = id;
        this.listener = listener;
        String[] stringArray = context.getResources().getStringArray(arrayResId);
        int i = 0;
        for (String item : stringArray) {
            addSheetItem(item, i, arg0);
            i++;
        }
    }

    public SheetDialog(Context context, String[] array, int id, OnSheetItemClickListener listener, int arg0) {
        this(context, null);
        this.id = id;
        this.listener = listener;
        String[] stringArray = array;
        int i = 0;
        for (String item : stringArray) {
            addSheetItem(item, i, arg0);
            i++;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sheet);
        mTvCancel = findViewById(R.id.dialog_sheet_tv_cancel);
        mContainer = findViewById(R.id.dialog_sheet_container);
        mScrollView = findViewById(R.id.dialog_sheet_scrollview);
        mTitle = findViewById(R.id.dialog_sheet_title);
        mTitleContainer = findViewById(R.id.dialog_sheet_title_container);

        if (TextUtils.isEmpty(title)) {
            mTitleContainer.setVisibility(View.GONE);
        } else {
            mTitleContainer.setVisibility(View.VISIBLE);
            mTitle.setText(title);
        }
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public SheetDialog builder() {
        return this;
    }

    public SheetDialog setCanCancelable(boolean cancel) {
        this.setCancelable(cancel);
        return this;
    }

    public SheetDialog setOnTouchOutside(boolean touchOutside) {
        this.setCanceledOnTouchOutside(touchOutside);
        return this;
    }

    public SheetDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public SheetDialog setListener(OnSheetItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * @param strItem The dialog item
     * @return this
     */
    public SheetDialog addSheetItem(String strItem, int id, int arg0) {
        if (mSheetItemList == null) {
            mSheetItemList = new ArrayList<SheetItem>();
        }
        mSheetItemList.add(new SheetItem(strItem, id, arg0));
        return this;
    }

    @Override
    public void show() {
        super.show();
        fillSheetItems();
    }

    /**
     * 设置条目布局
     */
    private void fillSheetItems() {
        if (mSheetItemList == null || mSheetItemList.size() <= 0) {
            return;
        }

        int size = mSheetItemList.size();

        // 添加条目过多的时候控制高度
        if (size >= 7) {
            LayoutParams params = (LayoutParams) mScrollView
                    .getLayoutParams();
            params.height = ScreenUtil.getDisplayHeight() / 2;
            mScrollView.setLayoutParams(params);
        }

        // 循环添加条目
        for (int i = 1; i <= size; i++) {
            final int index = i;
            final SheetItem sheetItem = mSheetItemList.get(i - 1);

            TextView textView = new TextView(mContext);

            textView.setText(sheetItem.getValue());
            textView.setGravity(Gravity.CENTER);

            textView.setTextColor(getContext().getResources().getColor(R.color.color_black));

            int height = (int) (getContext().getResources().getDimension(R.dimen.dimen_dp_56));
            textView.setMinHeight(height);
            textView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            textView.setTextSize(16);
            textView.setMaxLines(1);

            // 点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    listener.onItemClick(sheetItem, id);
                }
            });

            int padding = ScreenUtil.dip2px(8);
            textView.setPadding(padding, 0, padding, 0);

            if (index + 1 <= size) {
                View view = new LinearLayout(mContext);
                view.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, ScreenUtil.dip2px(0.3f)));
                view.setBackgroundColor(mContext.getResources().getColor(R.color.color_CAD3E2));
                mContainer.addView(view);
            }
            mContainer.addView(textView);
        }
    }

    public interface OnSheetItemClickListener {
        void onItemClick(SheetItem item, int id);
    }


}
