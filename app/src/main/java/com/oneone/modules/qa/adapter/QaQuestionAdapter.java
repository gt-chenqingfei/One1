package com.oneone.modules.qa.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.qa.QaDataManager;
import com.oneone.modules.qa.beans.QuestionClassify;
import com.oneone.modules.qa.beans.QuestionData;
import com.oneone.modules.qa.beans.QuestionItem;
import com.oneone.utils.ImageHelper;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by here on 18/4/14.
 */

public class QaQuestionAdapter extends PagerAdapter {
    private Context context;
    List<QuestionData> list;
    private LayoutInflater inflater;
    ArrayList<View> viewList = new ArrayList<View>();

    private QaNotAnswerAdapterListener listener;

    public interface QaNotAnswerAdapterListener {
        void OnQuestionSelected (QuestionItem questionItem);
    }

    public QaQuestionAdapter(Context context, List<QuestionData> list, QaNotAnswerAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.list = list;
        inflater = LayoutInflater.from(context);

        for (int i = 0;i < list.size();i++) {
            viewList.add(getView(i));
        }

    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public int getItemPosition(Object object) {
//        return super.getItemPosition(object);
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == viewList.get((int)Integer.parseInt(object.toString()));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        System.out.println("instantiateItem");
        View view = viewList.get(position);
        container.addView(view);
        ViewGroup.LayoutParams params = container.getLayoutParams();
        params.width = (int) (ScreenUtil.WIDTH_RATIO * 280);
        params.height = (int) (ScreenUtil.WIDTH_RATIO * 463);
        return position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public float getPageWidth(int position) {
        return 1f;
    }

    public View getView(int pos) {
        QuestionData questionData = list.get(pos);
        View convertView = inflater.inflate(R.layout.item_qa_content, null);
        ViewHolder holder = new ViewHolder();
        holder.pos = pos;
        holder.questionData = questionData;
        holder.borderLayout = convertView.findViewById(R.id.border_layout);
        holder.pageCountTv = convertView.findViewById(R.id.page_count_tv);
        holder.qaThemeIv = convertView.findViewById(R.id.qa_theme_iv);
        holder.qaThemeTv = convertView.findViewById(R.id.qa_theme_tv);
        holder.qaTitleTv = convertView.findViewById(R.id.qa_title_tv);
        holder.qaChooseItemLayout = convertView.findViewById(R.id.qa_choose_item_layout);

        convertView.setTag(holder);

        initViews(holder);
        return convertView;
    }

    public void initViews (ViewHolder holder) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.borderLayout.getLayoutParams();
        params.width = (int) (ScreenUtil.WIDTH_RATIO * 280);
        params.height = (int) (ScreenUtil.WIDTH_RATIO * 463);

        params = (RelativeLayout.LayoutParams) holder.qaThemeIv.getLayoutParams();
        QuestionClassify classify = QaDataManager.getInstance(context).getClassifyById(holder.questionData.getQuestion().getClassifyId());

        if (classify != null) {
            holder.qaThemeTv.setText(classify.getClassifyName());
            ImageHelper.displayImage(context, holder.qaThemeIv, classify.getClassifyIcon());
        }

        holder.pageCountTv.setText(Html.fromHtml(holder.pos + 1 + "/" +  list.size()));

        holder.qaTitleTv.setText(holder.questionData.getQuestion().getContent());

        for (int i = 0;i < holder.questionData.getAnswerList().size();i++) {
            addChooseItem(holder.questionData.getAnswerList().get(i), holder.qaChooseItemLayout);
        }
    }

    public void addChooseItem (final QuestionItem questionItem, final LinearLayout parentLayout) {
        int childPos = parentLayout.getChildCount();

        TextView tv = new TextView(context);
        tv.setTextColor(context.getResources().getColor(R.color.text_blue_1));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setPadding(DensityUtil.dp2px(18),0,0,0);
        tv.setBackgroundResource(R.drawable.shap_green_blue_bg_2);
        char c = (char) (childPos + 65);
        tv.setText(c + " " + questionItem.getContent());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (ScreenUtil.WIDTH_RATIO * 46));
        if (childPos != 0)
            params.topMargin = DensityUtil.dp2px(8);
        tv.setLayoutParams(params);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetItemStyle(parentLayout);
                TextView tv = (TextView) view;
                tv.setTextColor(context.getResources().getColor(R.color.white));
                tv.setBackgroundResource(R.drawable.shap_blue_bg_3);
                listener.OnQuestionSelected(questionItem);
            }
        });
        parentLayout.addView(tv);
    }

    public void resetItemStyle (LinearLayout parentLayout) {
        for (int i = 0;i < parentLayout.getChildCount();i++) {
            TextView tv = (TextView) parentLayout.getChildAt(i);
            tv.setTextColor(context.getResources().getColor(R.color.text_blue_1));
            tv.setBackgroundResource(R.drawable.shap_green_blue_bg_2);
        }
    }

    public class ViewHolder {
        int pos;
        public QuestionData questionData;
        RelativeLayout borderLayout;

        TextView pageCountTv;
        ImageView qaThemeIv;
        TextView qaThemeTv;
        TextView qaTitleTv;

        LinearLayout qaChooseItemLayout;
    }
}
