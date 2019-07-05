package com.oneone.modules.user.view;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.widget.TextViewCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oneone.Constants;
import com.oneone.R;
import com.oneone.api.constants.AccountStatus;
import com.oneone.api.constants.Role;
import com.oneone.event.EventProfileStoryUpdate;
import com.oneone.framework.ui.BasePresenterView;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.dialog.WarnDialog;
import com.oneone.framework.ui.imagepicker.preview.PhotoBrowserPagerActivity;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.mystory.bean.MyStoryPreviewBean;
import com.oneone.modules.mystory.bean.StoryImg;
import com.oneone.modules.mystory.contract.StoryContract;
import com.oneone.modules.mystory.dto.MyStoryImgDTO;
import com.oneone.modules.mystory.presenter.StoryPresenter;
import com.oneone.modules.profile.activity.StoryEditActivity;
import com.oneone.modules.profile.activity.StoryPhotoUploadActivity;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.utils.ImageHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author heyiwen
 * @since 2018/5/14.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.layout_story_view)
public class StoryView extends BasePresenterView<StoryPresenter, StoryContract.View> implements StoryContract.View, View.OnClickListener {

    @BindView(R.id.layout_story_view_container)
    LinearLayout mLLContainer;

    @BindView(R.id.layout_story_view_tv_1)
    TextView mTvMonologue;

    @BindView(R.id.layout_story_view_photo_upload_continue)
    TextView mTvPhotoUploadContinue;

    public StoryView(Context context) {
        super(context);
    }

    public StoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    UserInfo userInfo;

    MyStoryPreviewBean mStoryPreviewBean;


    @Override
    public void onViewCreated() {
        super.onViewCreated();
    }

    @Override
    public StoryPresenter onCreatePresenter() {
        return new StoryPresenter();
    }

    public void bindUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        this.userInfo = userInfo;
        if (!TextUtils.isEmpty(HereUser.getUserId()) && TextUtils.equals(HereUser.getUserId(), userInfo.getUserId())) {
            mTvPhotoUploadContinue.setVisibility(View.VISIBLE);
            mTvPhotoUploadContinue.setOnClickListener(this);
        }

        if (!TextUtils.isEmpty(userInfo.getMonologue())) {
            mTvMonologue.setText(userInfo.getMonologue());
            mTvMonologue.setVisibility(View.VISIBLE);
        } else {
            mTvMonologue.setVisibility(View.GONE);
        }
    }

    public void bindData(MyStoryPreviewBean myStoryPreviewBean) {
        if (myStoryPreviewBean == null) {
            return;
        }

        mStoryPreviewBean = myStoryPreviewBean;

        ArrayList<StoryImg> storyImgs = myStoryPreviewBean.getImgs();
        if (storyImgs == null) {
            storyImgs = new ArrayList<>();
        }
        if (storyImgs.size() >= Constants.STORY.PHOTO_UPLOAD_MAX) {
            mTvPhotoUploadContinue.setVisibility(View.GONE);
        }

        mLLContainer.removeAllViews();

        mLLContainer.addView(createStoryParagraph(myStoryPreviewBean.getSummary()));
        if (storyImgs.size() > 0) {
            mLLContainer.addView(createPhotoLayout(storyImgs.get(0)));
        }
        if (storyImgs.size() > 6) {
            mLLContainer.addView(createPhotoLayout(storyImgs.get(6)));
        }
        mLLContainer.addView(createStoryParagraph(myStoryPreviewBean.getOccupation()));
        mLLContainer.addView(createStoryParagraph(myStoryPreviewBean.getCharacter()));
        if (storyImgs.size() > 1) {
            mLLContainer.addView(createPhotoLayout(storyImgs.get(1)));
        }
        mLLContainer.addView(createStoryParagraph(myStoryPreviewBean.getExperience()));
        if (storyImgs.size() > 3) {
            mLLContainer.addView(createPhotoLayout(storyImgs.get(3)));
        }
        mLLContainer.addView(createStoryParagraph(myStoryPreviewBean.getCouple()));
        if (storyImgs.size() > 2) {
            mLLContainer.addView(createPhotoLayout(storyImgs.get(2)));
        }
        if (storyImgs.size() > 5) {
            mLLContainer.addView(createPhotoLayout(storyImgs.get(5)));
        }
        if (storyImgs.size() > 8) {
            mLLContainer.addView(createPhotoLayout(storyImgs.get(8)));
        }
        mLLContainer.addView(createStoryParagraph(myStoryPreviewBean.getValues()));
        if (storyImgs.size() > 4) {
            mLLContainer.addView(createPhotoLayout(storyImgs.get(4)));
        }
        if (storyImgs.size() > 7) {
            mLLContainer.addView(createPhotoLayout(storyImgs.get(7)));
        }

    }

    private TextView createStoryParagraph(ArrayList<? extends MyStoryPreviewBean.Paragraph> paragraphList) {
        TextView tvParagraph = new TextView(getContext());

        if (paragraphList == null || paragraphList.isEmpty()) {
            return tvParagraph;
        }

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = ScreenUtil.dip2px(41);
        params.rightMargin = ScreenUtil.dip2px(41);
        params.topMargin = ScreenUtil.dip2px(26);
        tvParagraph.setLayoutParams(params);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvParagraph.setLetterSpacing(0.2f);
        }
        tvParagraph.setLineSpacing(1.2f, 1.3f);

        SpannableStringBuilder sb = new SpannableStringBuilder("");
        ForegroundColorSpan fcs;
        AbsoluteSizeSpan sizeSpan1;
        StyleSpan bss;

        int curTextIndex = 0;
        for (MyStoryPreviewBean.Paragraph paragraph : paragraphList) {
            sb.append(paragraph.getText());
            int textLength = paragraph.getText().length();
            fcs = new ForegroundColorSpan(getResources().getColor(R.color.single_flow_paragraph_text_color));
            sizeSpan1 = new AbsoluteSizeSpan(ScreenUtil.dip2px(15));

            sb.setSpan(fcs, curTextIndex, curTextIndex + textLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(sizeSpan1, curTextIndex, curTextIndex + textLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            if (paragraph.getType().equals("tag")) {
                bss = new StyleSpan(Typeface.BOLD);
                sb.setSpan(bss, curTextIndex, curTextIndex + textLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            } else if (paragraph.getType().equals("NORMAL")) {
                bss = new StyleSpan(Typeface.NORMAL);
                sb.setSpan(bss, curTextIndex, curTextIndex + textLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
            curTextIndex += textLength;
        }
        tvParagraph.setText(sb);
        return tvParagraph;


    }

    private View createPhotoLayout(StoryImg storyImg) {

        RelativeLayout container = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(params);
        ImageAndTextView imageAndText = createImageAndText(storyImg);

        container.addView(imageAndText.containerView);

        if (userInfo != null && TextUtils.equals(HereUser.getUserId(), userInfo.getUserId())) {
            View editView = createEditView(imageAndText.ivImage, storyImg);
            container.addView(editView);
        }
        return container;
    }

    private View createEditView(ImageView photoView, StoryImg storyImg) {

        LinearLayout viewGroup = new LinearLayout(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewGroup.setLayoutParams(params);
        viewGroup.setOrientation(HORIZONTAL);

        ImageView ivDelete = new ImageView(getContext());
        LayoutParams paramsDelete = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ivDelete.setImageResource(R.drawable.ic_story_delete);
        ivDelete.setLayoutParams(paramsDelete);
        ivDelete.setId(R.id.story_view_iv_photo_delete);
        ivDelete.setTag(storyImg.getGroupIndex());
        ivDelete.setOnClickListener(this);

        ImageView ivEdit = new ImageView(getContext());
        LinearLayout.LayoutParams paramsEdit = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsEdit.leftMargin = ScreenUtil.dip2px(13);
        ivEdit.setLayoutParams(paramsEdit);
        ivEdit.setImageResource(R.drawable.ic_story_edit);
        ivEdit.setId(R.id.story_view_iv_photo_edit);
        ivEdit.setTag(storyImg.getGroupIndex());
        ivEdit.setOnClickListener(this);

        viewGroup.addView(ivDelete);
        viewGroup.addView(ivEdit);

        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.topMargin = photoView.getLayoutParams().height - ScreenUtil.dip2px(13);
        params.rightMargin = ScreenUtil.dip2px(24 + 13);
        return viewGroup;
    }

    private ImageAndTextView createImageAndText(final StoryImg storyImg) {
        LinearLayout viewGroup = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewGroup.setOrientation(LinearLayout.VERTICAL);
        viewGroup.setLayoutParams(params);

        ImageView itemIv = new ImageView(getContext());
        itemIv.setScaleType(ImageView.ScaleType.FIT_XY);
        itemIv.setId(storyImg.getId());
        itemIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> thumbnailPaths = new ArrayList<>();
                ArrayList<String> normalPaths = new ArrayList<>();
                int position = 0;
                int i = 0;
                for (StoryImg image : mStoryPreviewBean.getImgs()) {
                    String url = ImageHelper.getImageUrl(image.getUrl());
                    normalPaths.add(url);
                    thumbnailPaths.add(url);
                    if (image.getId() == storyImg.getId()) {
                        position = i;
                    }
                    i++;
                }

                PhotoBrowserPagerActivity.launch(mContext, thumbnailPaths, normalPaths, position, view);
            }
        });

        float percent = (float) storyImg.getHeight() / (float) storyImg.getWidth();

        int width = ScreenUtil.getDisplayWidth() - ScreenUtil.dip2px(24) * 2;
        float height = width * percent;
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(width, (int) height);
        params2.topMargin = ScreenUtil.dip2px(26);
        params2.leftMargin = ScreenUtil.dip2px(24);
        params2.rightMargin = ScreenUtil.dip2px(24);

        itemIv.setLayoutParams(params2);
        viewGroup.addView(itemIv);
        ImageHelper.displayImage(getContext(), itemIv, storyImg.getUrl());

        TextView itemTv = new TextView(getContext());
        itemTv.setText(storyImg.getCaption());
        itemTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        itemTv.setTextColor(getResources().getColor(R.color.single_flow_story_img_text));
        itemTv.setGravity(Gravity.LEFT);
        params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.leftMargin = ScreenUtil.dip2px(53);
        params2.rightMargin = ScreenUtil.dip2px(53);
        params2.topMargin = ScreenUtil.dip2px(7);
        itemTv.setLayoutParams(params2);
        viewGroup.addView(itemTv);
        return new ImageAndTextView(viewGroup, itemIv, itemTv);
    }

    class ImageAndTextView {
        View containerView;
        ImageView ivImage;
        TextView tvText;

        public ImageAndTextView(View containerView, ImageView ivImage, TextView tvText) {
            this.containerView = containerView;
            this.ivImage = ivImage;
            this.tvText = tvText;
        }
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.layout_story_view_photo_upload_continue:
                StoryPhotoUploadActivity.startActivity(getContext(), mStoryPreviewBean);
                break;

            case R.id.story_view_iv_photo_delete:

                WarnDialog dialog = new WarnDialog(getContext(), R.string.str_profile_story_photo_delete,
                        new WarnDialog.OnPositiveClickListener() {
                            @Override
                            public void onPositiveClick() {
                                deletePhoto(Integer.parseInt(view.getTag().toString()));
                            }
                        }).setPositiveButton(getContext().getString(R.string.str_profile_story_btn_delete));

                dialog.show();
                break;

            case R.id.story_view_iv_photo_edit:
                int groupIndex = Integer.parseInt(view.getTag().toString());
                StoryEditActivity.startActivity(getContext(), mStoryPreviewBean, groupIndex);
                break;
        }
    }

    private void deletePhoto(int groupIndex) {
        StoryImg img = mPresenter.findStoryByGroupIndex(mStoryPreviewBean, groupIndex);
        if (img == null) {
            return;
        }
        mStoryPreviewBean.getImgs().remove(img);
        MyStoryImgDTO dto = new MyStoryImgDTO();
        dto.setPhotos(mStoryPreviewBean.getImgs());
        Gson gson = new Gson();
        mPresenter.updateStory(gson.toJson(dto), new StoryContract.OnEditStoryListener() {
            @Override
            public void onStoryUpdate(MyStoryPreviewBean bean) {
                EventBus.getDefault().post(new EventProfileStoryUpdate());
            }
        });
    }


}
