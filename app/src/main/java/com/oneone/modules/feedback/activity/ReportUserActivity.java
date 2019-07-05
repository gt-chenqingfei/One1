package com.oneone.modules.feedback.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oneone.BaseActivity;
import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.dialog.SheetDialog;
import com.oneone.framework.ui.dialog.SheetItem;
import com.oneone.framework.ui.imagepicker.ImagePicker;
import com.oneone.framework.ui.imagepicker.bean.ImageItem;
import com.oneone.framework.ui.imagepicker.ui.ImageGridActivity;
import com.oneone.framework.ui.utils.PermissionsUtil;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.entry.beans.UploadTokenBean;
import com.oneone.modules.feedback.beans.Feedback;
import com.oneone.modules.feedback.beans.FeedbackListItem;
import com.oneone.modules.feedback.contract.FeedbackContract;
import com.oneone.modules.feedback.dialog.ReportBottomDialog;
import com.oneone.modules.feedback.presenter.FeedbackPresenter;
import com.oneone.modules.support.model.SupportModel;
import com.oneone.modules.support.qiniu.PhotoUploadListener;
import com.oneone.modules.support.qiniu.UploadParam;
import com.oneone.modules.timeline.activity.PermissionsWarnActivity;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.restful.ApiResult;
import com.oneone.support.qiniu.UploadImgUtil;
import com.oneone.support.qiniu.UploadObj;
import com.oneone.utils.ImageHelper;
import com.oneone.utils.ToastUtil;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by here on 18/6/8.
 */

@ToolbarResource(title = R.string.str_report_title)
@LayoutResource(R.layout.activity_report_user)
public class ReportUserActivity extends BaseActivity<FeedbackPresenter, FeedbackContract.View> implements FeedbackContract.View, View.OnClickListener {
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.report_classify_layout)
    RelativeLayout reportClassifyLayout;
    @BindView(R.id.report_classify_tv)
    TextView reportClassifyTv;
    @BindView(R.id.detail_desc_et)
    EditText reportDetailDescEt;
    @BindView(R.id.report_upload_pic_list_layout)
    LinearLayout reportPicListLayout;
    @BindView(R.id.report_upload_pic_select_pic_btn_iv)
    ImageView selectPicBtnIv;
    @BindView(R.id.connect_method_et)
    EditText reportConnectMethodEt;
    @BindView(R.id.commit_btn)
    Button commitBtn;

    public static final int REQ_PICKER_AVATAR = 10000;
    public static final int REQ_PICKER_PICTURE = 10001;
    public static final int REQ_PICKER_PICTURE_MODIFY = 10002;

    public static final int PIC_LIMIT = 9;

    private String defaultEntityType = "user";


    private UserInfo userInfo;

    private int  currentSelectReportReason;
    private List<ImageItem> mPictureSelected = new ArrayList<>();

    private ArrayList<UploadObj> uploadFinishObjList = new ArrayList<UploadObj>();

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gson = new Gson();

        userInfo = getIntent().getExtras().getParcelable("user_info");

        initView();
        initListener();
    }

    public void initView() {
        if (userInfo != null) {
            userNameTv.setText(getResources().getString(R.string.str_report_user_name) + userInfo.getNickname());
        }
    }

    private void initListener() {
        reportClassifyLayout.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
        selectPicBtnIv.setOnClickListener(this);
    }

    @Override
    public FeedbackPresenter onCreatePresenter() {
        return new FeedbackPresenter();
    }

    @Override
    public void onFeedbackSend(ApiResult result) {
        dismissLoading();
        if (result != null && result.getStatus() == 0) {
            if (result.getMessage() != null)
                ToastUtil.show(getActivityContext(), result.getMessage());
            ReportUserActivity.this.finish();
        } else {
            if (result != null && result.getMessage() != null)
                ToastUtil.show(getActivityContext(), result.getMessage());
        }
    }

    @Override
    public void onFeedbackList(int count, List<FeedbackListItem> feedbackList) {

    }

    public static void startActivity(Context context, UserInfo userInfo) {
        if (userInfo != null) {
            Intent it = new Intent(context, ReportUserActivity.class);
            it.putExtra("user_info", userInfo);
            context.startActivity(it);
        }
    }

    private void  showReportDialog(){
        SheetDialog dialog = new SheetDialog(this,getString(R.string.str_report_dialog_title));

        String[] reportStrArr = getResources().getStringArray(R.array.report_array);
        for (String reportStr : reportStrArr) {
            String reportText = reportStr.split(",")[0];
            int reportCode = Integer.valueOf(reportStr.split(",")[1]);
            dialog.addSheetItem(reportText,reportCode,reportCode);
        }
        dialog.setListener(new SheetDialog.OnSheetItemClickListener() {
            @Override
            public void onItemClick(SheetItem item, int id) {
                currentSelectReportReason = item.getId();
                reportClassifyTv.setText(item.getValue());
            }
        });
        dialog.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.report_classify_layout:

                showReportDialog();
                break;
            case R.id.commit_btn:
                if (checkCanSendFeedback()) {


                    if (mPictureSelected.size() > 0) {
                        uploadFinishObjList.clear();
                        showLoading("");

                        List<UploadParam> params = new ArrayList<UploadParam>();
                        for (ImageItem imageItem : mPictureSelected) {
                            UploadParam uploadParam = new UploadParam(0, imageItem.path, new PhotoUploadListener() {
                                @Override
                                public void onUploadCompleted(UploadParam param, List<UploadParam> group, boolean isAllEnd) {
                                    if (isAllEnd) {
                                        Feedback feedback = new Feedback();
                                        feedback.setFeedbackType(2);
                                        feedback.setTargetEntityId(userInfo.getUserId());
                                        feedback.setTargetEntityType(defaultEntityType);
                                        feedback.setFeedbackReason(currentSelectReportReason);
                                        feedback.setFeedback(reportDetailDescEt.getText().toString());
                                        feedback.setContactInfo(reportConnectMethodEt.getText().toString());

                                        ArrayList<String> uploadImgs = new ArrayList<String>();
                                        for (UploadParam groupParam : group) {
                                            uploadImgs.add(groupParam.getRemoteUrl());
                                        }
                                        feedback.setImgs(uploadImgs);

                                        String feedbackJsonStr = gson.toJson(feedback, Feedback.class);
                                        mPresenter.feedbackSend(feedbackJsonStr);
                                    }
                                }

                                @Override
                                public void onUploadError(UploadParam param, Throwable e) {

                                }

                                @Override
                                public void onUploadProgress(UploadParam param, double percent) {

                                }

                                @Override
                                public void onUploadStart(UploadParam param) {

                                }
                            });

                            params.add(uploadParam);
                        }
                        HereSingletonFactory.getInstance().getPhotoUploadManager().enqueueWithGroup(params);

                    } else {

                        Feedback feedback = new Feedback();
                        feedback.setFeedbackType(2);
                        feedback.setTargetEntityId(userInfo.getUserId());
                        feedback.setTargetEntityType(defaultEntityType);
                        feedback.setFeedbackReason(currentSelectReportReason);
                        feedback.setFeedback(reportDetailDescEt.getText().toString());
                        feedback.setContactInfo(reportConnectMethodEt.getText().toString());

                        String feedbackJsonStr = gson.toJson(feedback, Feedback.class);
                        mPresenter.feedbackSend(feedbackJsonStr);
                    }
                } else {
//                    ToastUtil.show(getActivityContext(), "");
                }
                break;
            case R.id.report_upload_pic_select_pic_btn_iv:
                if (mPictureSelected.size() < PIC_LIMIT)
                    if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.READ_EXTERNAL_STORAGE) &&
                            !PermissionsUtil.checkPermissions(mContext, PermissionsUtil.WRITE_EXTERNAL_STORAGE)) {
                        PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.EXTERNAL_STORAGE);
                    } else if (!PermissionsUtil.checkPermissions(mContext, PermissionsUtil.CAMERA)) {
                        PermissionsWarnActivity.startActivity(mContext, PermissionsWarnActivity.CAMERA);
                    } else {
                        startSelectImage(1, REQ_PICKER_PICTURE, false);
                    }
                else
                    ToastUtil.show(getActivityContext(), getResources().getString(R.string.pic_limit_text_1) + PIC_LIMIT + getResources().getString(R.string.pic_limit_text_2));
                break;
        }
    }

    public boolean checkCanSendFeedback() {
        if (userInfo != null && currentSelectReportReason != 0)
            return true;

        return false;
    }

    public void addNewPicItem(ArrayList<ImageItem> itemList) {
        for (ImageItem imageItem : itemList) {
            reportPicListLayout.addView(createPicItem(imageItem), reportPicListLayout.getChildCount());
        }
        mPictureSelected.addAll(itemList);
    }

    public View createPicItem(ImageItem imageItem) {
        RelativeLayout rl = new RelativeLayout(getActivityContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dp2px(70), DensityUtil.dp2px(70));
        params.leftMargin = DensityUtil.dp2px(8);
        rl.setPadding(DensityUtil.dp2px(2), DensityUtil.dp2px(2), DensityUtil.dp2px(2), DensityUtil.dp2px(2));
        rl.setLayoutParams(params);
        rl.setBackgroundResource(R.drawable.shap_blue_bg_2dp);

        ImageView iv = new ImageView(getActivityContext());
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rlParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        iv.setLayoutParams(rlParams);
        ImageHelper.displayImage(getActivityContext(), iv, imageItem.path);
        rl.addView(iv);
        return rl;
    }

    private void startSelectImage(int limit, int reqCode, boolean isCrop) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setShowCamera(true);
        imagePicker.setSelectLimit(limit);
        imagePicker.setMultiMode(limit > 1);
        imagePicker.setFocusWidth(ScreenUtil.screenWidth);
        imagePicker.setFocusHeight(ScreenUtil.screenHeight);
        imagePicker.setCrop(isCrop);
        imagePicker.setSaveRectangle(true);
        if (isCrop) {
            imagePicker.setSaveRectangle(false);
            imagePicker.setOutPutX(1000);
            imagePicker.setOutPutY(1000);
        }
        Intent intent1 = new Intent(getActivityContext(), ImageGridActivity.class);

        if (reqCode == REQ_PICKER_PICTURE) {
//        /* 设置已经选择的图片 */
            if (mPictureSelected != null && !mPictureSelected.isEmpty()) {
                intent1.putParcelableArrayListExtra(ImageGridActivity.EXTRAS_IMAGES, (ArrayList<? extends Parcelable>) mPictureSelected);
            }
        }
        startActivityForResult(intent1, reqCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_PICKER_PICTURE) {
            ArrayList<ImageItem> newItemList = null;
            if (data != null && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                newItemList = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            }

            if (newItemList == null || newItemList.isEmpty()) {
                return;
            }

            addNewPicItem(newItemList);
//            refreshStory();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
