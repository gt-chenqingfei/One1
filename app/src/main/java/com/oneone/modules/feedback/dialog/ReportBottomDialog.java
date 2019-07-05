package com.oneone.modules.feedback.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BasePopDialog;
import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * Created by here on 18/6/8.
 */

public class ReportBottomDialog extends BasePopDialog {

    public interface ReportBottomDialogListener {
        void onItemClick (ReportReason reportReason);
    }
    public ReportBottomDialog(@NonNull Context context, ReportBottomDialogListener listener) {
        super(context);
        this.listener = listener; 
    }

    private ReportBottomDialogListener listener;
    private LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_report_bottom);

        rootLayout = findViewById(R.id.root_layout);

        initView();
    }

    public void initView () {
        String[] reportStrArr = getContext().getResources().getStringArray(R.array.report_array);
        for (String reportStr : reportStrArr) {
            String reportText = reportStr.split(",")[0];
            int reportCode = Integer.valueOf(reportStr.split(",")[1]);

            final ReportReason reportReason = new ReportReason();
            reportReason.setReasonCode(reportCode);
            reportReason.setReasonStr(reportText);

            View lineView = new View(getContext());
            lineView.setBackgroundColor(getContext().getResources().getColor(R.color.gray_line));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(0.5f));
            rootLayout.addView(lineView, params);

            TextView tv = new TextView(getContext());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tv.setTextColor(getContext().getResources().getColor(R.color.color_4D566B));
            tv.setText(reportText);
            tv.setGravity(Gravity.CENTER);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(reportReason);
                }
            });

            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(60));
            rootLayout.addView(tv, params);
        }
    }

    public class ReportReason {
        private int reasonCode;
        private String reasonStr;

        public int getReasonCode() {
            return reasonCode;
        }

        public void setReasonCode(int reasonCode) {
            this.reasonCode = reasonCode;
        }

        public String getReasonStr() {
            return reasonStr;
        }

        public void setReasonStr(String reasonStr) {
            this.reasonStr = reasonStr;
        }
    }
}
