package com.oneone.modules.find.adapter;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.oneone.R;
import com.oneone.event.EventMainTabSelection;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.find.activity.MatchPersonActivity;
import com.oneone.modules.find.beans.MatcherRecommendList;
import com.oneone.modules.find.dialog.NoFeelDialog;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;
import com.oneone.modules.find.model.LikeModel;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.profile.activity.IntersectionActivity;
import com.oneone.modules.profile.activity.MatchActivity;
import com.oneone.modules.profile.dialog.LikeAlreadyDialog;
import com.oneone.modules.qa.QaDataManager;
import com.oneone.modules.qa.activity.MyQaActivity;
import com.oneone.modules.qa.activity.MyQaMustActivity;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.QaAnswer;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserRoleSettingTagBean;
import com.oneone.restful.ApiResult;
import com.oneone.utils.ImageHelper;
import com.oneone.utils.MyTextUtil;
import com.oneone.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by here on 18/4/14.
 */

public class DiscoverSuggestUserAdapter extends PagerAdapter {

    private DiscoverSuggestUserAdapterListener listener;

    public interface DiscoverSuggestUserAdapterListener {
        void onRefreshAdapterClick();

        void onNoFeelBtnClick(FindPageUserInfoDTO findPageUserInfo);

        void onLikeBtnClick(FindPageUserInfoDTO findPageUserInfo);

        void onCancelLikeBtnClick(FindPageUserInfoDTO findPageUserInfo);

        void onGoQaBtnClick();

        void onReloadAdapterClick();

        void onItemClick(UserInfo userInfo);

        void onOpenConvertionClick(UserInfo mUserInfo);

        void reBuildAdapter(Fragment fragment, ArrayList<View> viewList, int expire);
    }

    private static final int MATCHER_SUGGEST_LIMIT = 3;
    private static final int TEXT_COUNT_LIMIT = 40;

    private Fragment fragment;
    private Context context;
    private List<FindPageUserInfoDTO> userList;
    private int expire;
    private int recommandSize;
    private LayoutInflater inflater;

    ArrayList<View> viewList = new ArrayList<View>();

    private Timer timer;
    private ViewHolder countDownPageHolder;
    private ViewHolder nobofyPageHolder;
    private Animator.AnimatorListener animatorListener;
    public static final String ANIMATION_LIKE = "like.json";

    private Button lastConfirmBtn;
    private boolean onlyOnePage = false;

    public boolean getOnlyOnePage() {
        return onlyOnePage;
    }

    public void setLastConfirmBtnVisiable(boolean isVisiable) {
        if (lastConfirmBtn != null) {
            boolean isAnswerQa = (boolean) lastConfirmBtn.getTag();
            if (isAnswerQa && !isVisiable) {
                lastConfirmBtn.setVisibility(View.GONE);
            } else {
                lastConfirmBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    private Handler refreshTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            expire = (int) msg.obj;
            if (countDownPageHolder != null) {
                String minStr = TimeUtil.secToMinute(expire);
                for (View v : viewList) {
                    ViewHolder holder = (ViewHolder) v.getTag();
                    holder.refreshTimeSignTv.setText(minStr);
                }
                countDownPageHolder.timeSurplusValTv.setText(TimeUtil.secToTime(expire));

                if (expire <= 0) {
                    RedDotManager.getInstance().notifyFindDotChanged(true);
                    setItemCountDown(countDownPageHolder);
                }
            }
        }
    };

    private Handler refreshAdapterHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            if (viewList.size() <= 1) {
//                onlyOnePage = true;
//
//                viewList.get(0).setBackgroundColor(Color.RED);
//                ViewHolder holder = (ViewHolder) viewList.get(0).getTag();
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.borderLayout.getLayoutParams();
//                params.width = borderWidth;
//
//                if (!onlyOnePage) {
//                    if (holder.pos == 0) {
//                        params.leftMargin = (int) (ScreenUtil.WIDTH_RATIO * 12);
//                    } else {
//                        params.leftMargin = (int) (ScreenUtil.WIDTH_RATIO * 8);
//                    }
//                    if (holder.type.equals("countDownTime")) {
//                        params.rightMargin = (int) (ScreenUtil.WIDTH_RATIO * 12);
//                    }
//                }
//            }
            DiscoverSuggestUserAdapter.this.notifyDataSetChanged();
        }
    };

    public DiscoverSuggestUserAdapter(final Fragment fragment, ArrayList<View> viewList, int expire
            , final DiscoverSuggestUserAdapterListener listener) {
        this.fragment = fragment;
        this.context = fragment.getContext();
        inflater = LayoutInflater.from(context);
        this.userList = userList;
        this.expire = expire;
        this.listener = listener;
        this.userList = new ArrayList<>();
        this.viewList = viewList;

        onlyOnePage = true;
        timer = new Timer();
        timer.schedule(new RefreshTimeTask(expire, timer), 1000, 1000);
    }

    public DiscoverSuggestUserAdapter(final Fragment fragment, List<FindPageUserInfoDTO> userList, int expire
            , int recommandSize, final DiscoverSuggestUserAdapterListener listener) {
        this.fragment = fragment;
        this.context = fragment.getContext();
        inflater = LayoutInflater.from(context);
        this.userList = userList;
        this.expire = expire;
        this.recommandSize = recommandSize;
        this.listener = listener;

        int i = 0;
        if (userList.size() > 0) {
            onlyOnePage = false;
            for (; i < userList.size(); i++) {
                viewList.add(getView(i, userList.get(i), "normal"));
            }
            viewList.add(getView(i, null, "countDownTime"));
        } else {
            onlyOnePage = true;
            viewList.add(getView(i, null, "nobody"));
//            i++;
        }

        if (expire < 0) {
            return;
        }
        timer = new Timer();
        timer.schedule(new RefreshTimeTask(expire, timer), 1000, 1000);
    }

    public void setAnimation(final ViewHolder holder) {
        // 监听喜欢动画，完成后弹窗
        animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new AsyncTask<Object, Void, ApiResult>() {

                    @Override
                    protected ApiResult doInBackground(Object... voids) {
                        LikeModel likeModel = new LikeModel(context);
                        ApiResult result = likeModel.likeSetLike(holder.findPageUserInfo.getUserInfo().getUserId());

                        return result;
                    }

                    @Override
                    protected void onPostExecute(ApiResult result) {
                        super.onPostExecute(result);


                        if (result != null && result.getStatus() == 0) {
                            IMManager.getInstance().startConversationWithCallBack(context, holder.findPageUserInfo.getUserInfo().getUserId(), new IMManager.ConversationListener() {
                                @Override
                                public void onUserRelation(IMUserPrerelation imUserPrerelation) {
                                    if (!imUserPrerelation.isRelation()) {
                                        new LikeAlreadyDialog(context, listener, holder.findPageUserInfo.getUserInfo()).show();
                                    }
                                }
                            });
                        } else {
                            holder.likeIv.cancelAnimation();
                            holder.likeIv.clearAnimation();
                            holder.likeIv.destroyDrawingCache();
                            holder.likeIv.setTag(false);
                            holder.findPageUserInfo.getRelationInfo().setLikeStatus(0);
                            holder.findPageUserInfo.getUserInfo().setLikeStatus(0);
                            holder.likeIv.setImageResource(R.drawable.ic_like_normal);
                        }
                    }
                }.execute();

            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        };
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = viewList.get(position);
        container.addView(view);
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public float getPageWidth(int position) {
        float realWidth;
        if (!onlyOnePage) {
            realWidth = 330;
        } else {
            realWidth = 375;
        }
        return realWidth / 375f;
    }

    public View getView(int pos, FindPageUserInfoDTO findPageUserInfo, String itemType) {
        View convertView = inflater.inflate(R.layout.item_discover_suggest, null);
        ViewHolder holder = new ViewHolder();
        holder.convertView = convertView;
        holder.findPageUserInfo = findPageUserInfo;
        holder.pos = pos;
        holder.type = itemType;

        holder.borderLayout = convertView.findViewById(R.id.item_discover_suggest_border_layout);
        holder.pageCountTv = convertView.findViewById(R.id.discover_item_page_tv);
        holder.refreshTimeSignTv = convertView.findViewById(R.id.refresh_time_sign_tv_val);

        holder.suggestUserLayout = convertView.findViewById(R.id.suggest_user_layout);
        holder.photoIv = convertView.findViewById(R.id.discover_item_photo_iv);
        holder.noFeelingIv = convertView.findViewById(R.id.not_like_iv);
        holder.likeIv = convertView.findViewById(R.id.like_iv);
        holder.userNameTv = convertView.findViewById(R.id.user_name_tv);
        holder.userAgeAndPlaceTv = convertView.findViewById(R.id.user_age_and_place_tv);
        holder.samePercentLayout = convertView.findViewById(R.id.same_percent_layout);
        holder.userSamePercentTv = convertView.findViewById(R.id.same_percent_tv);
        holder.userSamePercentIv = convertView.findViewById(R.id.same_percent_iv);
        holder.userIntersectionTv = convertView.findViewById(R.id.intersection_tv);
        holder.userIntersectionIv = convertView.findViewById(R.id.intersection_iv);

        holder.whosSuggestLayout = convertView.findViewById(R.id.whos_suggest_layout);
        holder.matcherSaidTv = convertView.findViewById(R.id.matcher_said_tv);
        holder.whosSuggestTv = convertView.findViewById(R.id.whos_suggest_tv);

        holder.characterLayout = convertView.findViewById(R.id.character_layout);
        holder.characterTv = convertView.findViewById(R.id.character_tv);

        holder.userTagLayout = convertView.findViewById(R.id.user_tag_layout);
        holder.userTagTv = convertView.findViewById(R.id.user_tag_tv);

        holder.lastItemType1Layout = convertView.findViewById(R.id.last_item_type_1_layout);
        holder.timeSurplusValTv = convertView.findViewById(R.id.time_surplus_val_tv);
        holder.lastItemConfirmBtn = convertView.findViewById(R.id.last_item_type_1_confirm_btn);
        holder.lastItemType1BottomIv = convertView.findViewById(R.id.last_item_type_1_bottom_iv);
        holder.nextTimeSuggestDescTv = convertView.findViewById(R.id.next_time_suggest_desc_tv);

        holder.lastItemType2Layout = convertView.findViewById(R.id.last_item_type_2_layout);
        holder.lastItemType2BottomLayout = convertView.findViewById(R.id.last_item_type_2_bottom_layout);
        holder.suggestSearchBtn = convertView.findViewById(R.id.suggest_search_btn);
        holder.seeTimeLineBtn = convertView.findViewById(R.id.see_time_line_btn);

        convertView.setTag(holder);

        initViews(holder);
        return convertView;
    }

    private int borderWidth;

    public void initViews(final ViewHolder holder) {
        borderWidth = (int) (ScreenUtil.WIDTH_RATIO * 330);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.borderLayout.getLayoutParams();
        params.width = borderWidth;

        if (!onlyOnePage) {
            if (holder.pos == 0) {
                params.leftMargin = (int) (ScreenUtil.WIDTH_RATIO * 12);
            } else {
                params.leftMargin = (int) (ScreenUtil.WIDTH_RATIO * 8);
            }
            if (holder.type.equals("countDownTime")) {
                params.rightMargin = (int) (ScreenUtil.WIDTH_RATIO * 12);
            }
        } else {
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }


        holder.refreshTimeSignTv.setText(TimeUtil.secToMinute(expire));

        holder.borderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && holder.findPageUserInfo != null) {
                    listener.onItemClick(holder.findPageUserInfo.getUserInfo());
                }

//                MatchActivity.startActivity(context);
//                MyQaActivity.startActivity(context);
//                MyQaMustActivity.startActivity(context);
//                ImTalkActivity.startActivity(context);
//                NIMFunctional nimFunctional = new NIMFunctional(context);
//                TestInterfaceActivity.startActivity(context);
            }
        });

        if (holder.type.equals("normal")) {
            holder.pageCountTv.setText(Html.fromHtml("<font color=#3E4F6C>" + (holder.pos + 1) + "<font/>"
                    + "<font color=#C4CFE1>" + "/" + userList.size() + "<font/>"));

            holder.suggestUserLayout.setVisibility(View.VISIBLE);

            params = (RelativeLayout.LayoutParams) holder.photoIv.getLayoutParams();
            params.width = (int) (ScreenUtil.WIDTH_RATIO * 302);
            params.height = (int) (ScreenUtil.WIDTH_RATIO * 302);
            ImageHelper.displayAvatar(context, holder.photoIv, holder.findPageUserInfo.getUserInfo().getAvatar());

            holder.noFeelingIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    new NoFeelDialog(context, new NoFeelDialog.NoFeelDialogListener() {
                        @Override
                        public void onNoFeelSelected() {
                            viewList.remove(holder.convertView);
//                            if (viewList.size() <= 1) {
//                                onlyOnePage = true;
//                            }
                            listener.onNoFeelBtnClick(holder.findPageUserInfo);

                            if (viewList.size() <= 1) {
                                timer.cancel();
                                listener.reBuildAdapter(fragment, viewList, expire);
                            } else {
                                refreshAdapterHandler.sendEmptyMessage(0);
                            }
                        }
                    }).show();


//                    DiscoverSuggestUserAdapter.this.notifyDataSetChanged();
                }
            });
            if (holder.findPageUserInfo.getRelationInfo().getLikeStatus() == 0) {
                holder.likeIv.setTag(false);
                holder.likeIv.setImageResource(R.drawable.ic_like_normal);
            } else {
                holder.likeIv.setTag(true);
                holder.likeIv.setImageResource(R.drawable.ic_like_selected);
            }
            holder.likeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isLike = (boolean) view.getTag();
                    if (!isLike) {
                        holder.likeIv.clearAnimation();
                        holder.likeIv.removeAllAnimatorListeners();
                        setAnimation(holder);
                        holder.likeIv.setAnimation(ANIMATION_LIKE, LottieAnimationView.CacheStrategy.Weak);
                        holder.likeIv.addAnimatorListener(animatorListener);
                        holder.likeIv.playAnimation();
                        holder.likeIv.setMinAndMaxProgress(0f, 0.46f);
                        view.setTag(true);
                        holder.findPageUserInfo.getRelationInfo().setLikeStatus(1);
                        holder.findPageUserInfo.getUserInfo().setLikeStatus(1);


//                        listener.onLikeBtnClick(holder.findPageUserInfo);
                    } else {
                        holder.likeIv.cancelAnimation();
                        holder.likeIv.clearAnimation();
                        holder.likeIv.destroyDrawingCache();
                        view.setTag(false);
                        holder.findPageUserInfo.getRelationInfo().setLikeStatus(0);
                        holder.findPageUserInfo.getUserInfo().setLikeStatus(0);
                        holder.likeIv.setImageResource(R.drawable.ic_like_normal);
//                        listener.onCancelLikeBtnClick(holder.findPageUserInfo);

                        new AsyncTask<Object, Void, ApiResult>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                            }

                            @Override
                            protected ApiResult doInBackground(Object... voids) {
                                LikeModel likeModel = new LikeModel(context);
                                return likeModel.likeCancelLike(holder.findPageUserInfo.getUserInfo().getUserId());
                            }

                            @Override
                            protected void onPostExecute(ApiResult result) {
                                super.onPostExecute(result);
                                if (result.getStatus() != 0) {
                                    holder.likeIv.setTag(true);
                                    holder.findPageUserInfo.getRelationInfo().setLikeStatus(1);
                                    holder.findPageUserInfo.getUserInfo().setLikeStatus(1);
                                    holder.likeIv.setImageResource(R.drawable.ic_like_selected);
                                }
                            }
                        }.execute();
                    }
                }
            });
            holder.userNameTv.setText(MyTextUtil.getLimitEllipseText(holder.findPageUserInfo.getUserInfo().getNickname(), 6));
            String ageAndPlaceStr = holder.findPageUserInfo.getUserInfo().getAge() + "";
            if (holder.findPageUserInfo.getUserInfo().getCity() != null && !holder.findPageUserInfo.getUserInfo().getCity().equals(""))
                ageAndPlaceStr += "，" + holder.findPageUserInfo.getUserInfo().getDiscoverPlace();
            holder.userAgeAndPlaceTv.setText(ageAndPlaceStr);

            boolean isUserAnswered = false;
            boolean isMeAnswered = false;
            if (HereUser.getInstance().getUserInfo().getQaAnswer() != null) {
                isMeAnswered = HereUser.getInstance().getUserInfo().getQaAnswer().getAnswered() > 0;
            }
            if (holder.findPageUserInfo.getQaAnswer() != null) {
                isUserAnswered = holder.findPageUserInfo.getQaAnswer().getAnswered() > 0;
            }

            holder.userSamePercentTv.setVisibility(View.VISIBLE);
            holder.userSamePercentIv.setVisibility(View.VISIBLE);
            int matchValue = (int) holder.findPageUserInfo.getMatchValue();
            if (isUserAnswered && !isMeAnswered) {
                holder.userSamePercentIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (QaDataManager.getInstance(context).getQuestions() != null && QaDataManager.getInstance(context).getQuestions().size() > 0) {
                            MyQaMustActivity.startActivity(context);
                        } else {
                            MyQaActivity.startActivity(context, 0);
                        }
                    }
                });
                holder.userSamePercentTv.setText("??");
            } else if (!isUserAnswered || matchValue == 0) {
                holder.userSamePercentTv.setVisibility(View.GONE);
                holder.userSamePercentIv.setVisibility(View.GONE);
            } else {
                holder.userSamePercentTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MatchActivity.startActivity(context, holder.findPageUserInfo.getUserInfo());
                    }
                });
                holder.userSamePercentIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MatchActivity.startActivity(context, holder.findPageUserInfo.getUserInfo());
                    }
                });
                holder.userSamePercentTv.setText(matchValue + "%");
            }

            if (holder.findPageUserInfo.getIntersectionValue() > 0) {
                holder.userIntersectionTv.setText(holder.findPageUserInfo.getIntersectionValue() + "");
                holder.userIntersectionTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntersectionActivity.startActivity(context, holder.findPageUserInfo.getUserInfo().getUserId());
                    }
                });
                holder.userIntersectionIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntersectionActivity.startActivity(context, holder.findPageUserInfo.getUserInfo().getUserId());
                    }
                });
            } else {
                holder.userIntersectionIv.setVisibility(View.GONE);
                holder.userIntersectionTv.setVisibility(View.GONE);
            }
            if (holder.findPageUserInfo.getMatcherRecommendList() != null && holder.findPageUserInfo.getMatcherRecommendList().size() > 0) {
                String suggestNameStr = "";
                String suggestValStr = "";
                int j = 0;
                for (int i = 0; i < holder.findPageUserInfo.getMatcherRecommendList().size(); i++) {
                    if (j == MATCHER_SUGGEST_LIMIT)
                        break;
                    MatcherRecommendList recommend = holder.findPageUserInfo.getMatcherRecommendList().get(i);
                    if (recommend.getMatcherSaid() != null) {
                        j++;
                        if (i < MATCHER_SUGGEST_LIMIT - 1) {
                            suggestNameStr += recommend.getNickname() + "、";
                            if (!recommend.getMatcherSaid().equals(""))
                                suggestValStr += "\"" + recommend.getMatcherSaid() + "\"，";
                        } else {
                            suggestNameStr += recommend.getNickname();
                            if (!recommend.getMatcherSaid().equals(""))
                                suggestValStr += "\"" + recommend.getMatcherSaid() + "\"";
                        }
                    }
                }
//                for (MatcherRecommendList recommend : holder.findPageUserInfo.getMatcherRecommendList()) {
//                    suggestNameStr += recommend.getNickname() + "、";
//                    if (recommend.getMatcherSaid() != null) {
//                        suggestValStr += recommend.getMatcherSaid();
//                    }
//                }
                if (!suggestNameStr.equals("")) {
                    if (suggestNameStr.endsWith("、"))
                        suggestNameStr = suggestNameStr.substring(0, suggestNameStr.length() - 1);

                    suggestNameStr = MyTextUtil.getLimitEllipseText(suggestNameStr, 16);
                    holder.whosSuggestLayout.setVisibility(View.VISIBLE);
                    holder.whosSuggestTv.setText(Html.fromHtml("<font color=#6667FD>" + suggestNameStr + "<font/>"));
                    if (suggestValStr.endsWith("，"))
                        suggestValStr = suggestValStr.substring(0, suggestValStr.length() - 1);
                    String rltStr = MyTextUtil.getLimitEllipseText(suggestValStr, 40).replaceAll("\n", " ");
                    if (rltStr.endsWith("..."))
                        rltStr += "\"";
                    holder.matcherSaidTv.setText(rltStr);
                } else if (holder.findPageUserInfo.getUserInfo().getMonologue() != null && !holder.findPageUserInfo.getUserInfo().getMonologue().equals("")) {
                    holder.characterLayout.setVisibility(View.VISIBLE);
                    holder.characterTv.setText(MyTextUtil.getLimitEllipseText(holder.findPageUserInfo.getUserInfo().getMonologue(), 40));
                } else {
                    holder.userTagLayout.setVisibility(View.VISIBLE);
                    String tagStr = "";
                    UserRoleSettingTagBean tag1 = holder.findPageUserInfo.getUserInfo().getOccupationTags();
                    UserRoleSettingTagBean tag2 = holder.findPageUserInfo.getUserInfo().getSkillTags();
                    UserRoleSettingTagBean tag3 = holder.findPageUserInfo.getUserInfo().getCharacterTags();
                    UserRoleSettingTagBean tag4 = holder.findPageUserInfo.getUserInfo().getExperienceTags();
                    UserRoleSettingTagBean tag5 = holder.findPageUserInfo.getUserInfo().getSenseOfWorthTags();
                    UserRoleSettingTagBean tag6 = holder.findPageUserInfo.getUserInfo().getSpousePrefsTags();
                    if (tag1.getTagCount() > 0) {
                        String randomTagStr = tag1.randomTag();
                        if (randomTagStr != null && !randomTagStr.equals(""))
                            tagStr += randomTagStr + "、";
                    }
                    if (tag2.getTagCount() > 0) {
                        String randomTagStr = tag2.randomTag();
                        if (randomTagStr != null && !randomTagStr.equals(""))
                            tagStr += randomTagStr + "、";
                    }
                    if (tag3.getTagCount() > 0) {
                        String randomTagStr = tag3.randomTag();
                        if (randomTagStr != null && !randomTagStr.equals(""))
                            tagStr += randomTagStr + "、";
                    }
                    if (tag4.getTagCount() > 0) {
                        String randomTagStr = tag4.randomTag();
                        if (randomTagStr != null && !randomTagStr.equals(""))
                            tagStr += randomTagStr + "、";
                    }
                    if (tag5.getTagCount() > 0) {
                        String randomTagStr = tag5.randomTag();
                        if (randomTagStr != null && !randomTagStr.equals(""))
                            tagStr += randomTagStr + "、";
                    }
                    if (tag6.getTagCount() > 0) {
                        String randomTagStr = tag6.randomTag();
                        if (randomTagStr != null && !randomTagStr.equals(""))
                            tagStr += randomTagStr + "、";
                    }
                    if (tagStr.length() > 0)
                        tagStr = tagStr.substring(0, tagStr.length() - 1);

                    String rltStr = MyTextUtil.getLimitEllipseText(tagStr, 40);
//                    if (rltStr.endsWith("...")) {
//                        rltStr += "\"";
//                    }
                    holder.userTagTv.setText(rltStr);
                }
            } else if (holder.findPageUserInfo.getUserInfo().getMonologue() != null && !holder.findPageUserInfo.getUserInfo().getMonologue().equals("")) {
                holder.characterLayout.setVisibility(View.VISIBLE);
                holder.characterTv.setText(MyTextUtil.getLimitEllipseText(holder.findPageUserInfo.getUserInfo().getMonologue(), 40));
            } else {
                holder.userTagLayout.setVisibility(View.VISIBLE);
                String tagStr = "";
                UserRoleSettingTagBean tag1 = holder.findPageUserInfo.getUserInfo().getOccupationTags();
                UserRoleSettingTagBean tag2 = holder.findPageUserInfo.getUserInfo().getSkillTags();
                UserRoleSettingTagBean tag3 = holder.findPageUserInfo.getUserInfo().getCharacterTags();
                UserRoleSettingTagBean tag4 = holder.findPageUserInfo.getUserInfo().getExperienceTags();
                UserRoleSettingTagBean tag5 = holder.findPageUserInfo.getUserInfo().getSenseOfWorthTags();
                UserRoleSettingTagBean tag6 = holder.findPageUserInfo.getUserInfo().getSpousePrefsTags();
                if (tag1.getTagCount() > 0) {
                    String randomTagStr = tag1.randomTag();
                    if (randomTagStr != null && !randomTagStr.equals(""))
                        tagStr += randomTagStr + "、";
                }
                if (tag2.getTagCount() > 0) {
                    String randomTagStr = tag2.randomTag();
                    if (randomTagStr != null && !randomTagStr.equals(""))
                        tagStr += randomTagStr + "、";
                }
                if (tag3.getTagCount() > 0) {
                    String randomTagStr = tag3.randomTag();
                    if (randomTagStr != null && !randomTagStr.equals(""))
                        tagStr += randomTagStr + "、";
                }
                if (tag4.getTagCount() > 0) {
                    String randomTagStr = tag4.randomTag();
                    if (randomTagStr != null && !randomTagStr.equals(""))
                        tagStr += randomTagStr + "、";
                }
                if (tag5.getTagCount() > 0) {
                    String randomTagStr = tag5.randomTag();
                    if (randomTagStr != null && !randomTagStr.equals(""))
                        tagStr += randomTagStr + "、";
                }
                if (tag6.getTagCount() > 0) {
                    String randomTagStr = tag6.randomTag();
                    if (randomTagStr != null && !randomTagStr.equals(""))
                        tagStr += randomTagStr + "、";
                }
                if (tagStr.length() > 0)
                    tagStr = tagStr.substring(0, tagStr.length() - 1);

                String rltStr = MyTextUtil.getLimitEllipseText(tagStr, 40);
//                if (rltStr.endsWith("..."))
//                    rltStr += "\"";
                holder.userTagTv.setText(rltStr);
            }
        } else if (holder.type.equals("countDownTime")) {
            holder.pageCountTv.setVisibility(View.GONE);
            setItemCountDown(holder);
        } else if (holder.type.equals("nobody")) {
            holder.pageCountTv.setVisibility(View.GONE);
            setItemNobody(holder);
        }
    }

    private void setItemCountDown(ViewHolder holder) {
        holder.lastItemType1Layout.setVisibility(View.VISIBLE);
        holder.timeSurplusValTv.setText(TimeUtil.secToTime(expire));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.lastItemType1BottomIv.getLayoutParams();
        params.height = (int) (ScreenUtil.WIDTH_RATIO * 102);

        lastConfirmBtn = holder.lastItemConfirmBtn;
        if (expire > 0) {
            holder.lastItemConfirmBtn.setTag(true);
            holder.lastItemConfirmBtn.setText(R.string.str_discover_page_last_item_type_1_confirm_btn_1_text);
            holder.lastItemConfirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onGoQaBtnClick();
                }
            });

            QaAnswer answer = HereUser.getInstance().getUserInfo().getQaAnswer();
            if (answer.getAnswered() == answer.getCount()) {
                holder.lastItemConfirmBtn.setVisibility(View.GONE);
            } else {
                holder.lastItemConfirmBtn.setVisibility(View.VISIBLE);
            }

            holder.nextTimeSuggestDescTv.setText(context.getResources().getString(R.string.str_discover_page_next_time_suggest_desc_1_text_1) + recommandSize + context.getResources().getString(R.string.str_discover_page_next_time_suggest_desc_1_text_2));

            holder.lastItemType1BottomIv.setBackgroundResource(R.drawable.last_item_type_1_bottom_iv_1_bg);
        } else {
            holder.lastItemConfirmBtn.setTag(false);
            holder.lastItemConfirmBtn.setVisibility(View.VISIBLE);
            holder.lastItemConfirmBtn.setText(R.string.str_discover_page_last_item_type_1_confirm_btn_2_text);
            holder.lastItemConfirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onReloadAdapterClick();
                }
            });
            holder.nextTimeSuggestDescTv.setText(R.string.str_discover_page_next_time_suggest_desc_2_text);

            holder.lastItemType1BottomIv.setBackgroundResource(R.drawable.last_item_type_1_bottom_iv_2_bg);
        }
        countDownPageHolder = holder;
    }

    private void setItemNobody(ViewHolder holder) {
        holder.lastItemType2Layout.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.lastItemType2BottomLayout.getLayoutParams();
        params.height = (int) (ScreenUtil.WIDTH_RATIO * 278);
        holder.lastItemType2BottomLayout.setBackgroundResource(R.drawable.no_suggest_bottom_bg);
        holder.suggestSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MatchPersonActivity.class);
                fragment.startActivityForResult(intent, MatchPersonActivity.REQ_MATCH_PERSON);
            }
        });
        holder.seeTimeLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventMainTabSelection(EventMainTabSelection.TAB_TIMELINE));
            }
        });

        nobofyPageHolder = holder;
    }

    public class ViewHolder {
        View convertView;

        FindPageUserInfoDTO findPageUserInfo;

        int pos;
        RelativeLayout borderLayout;

        RelativeLayout suggestUserLayout;
        RelativeLayout lastItemType1Layout;
        RelativeLayout lastItemType2Layout;

        TextView pageCountTv;
        TextView refreshTimeSignTv;

        String type;

        //--------normal item
        ImageView photoIv;
        ImageView noFeelingIv;
        LottieAnimationView likeIv;

        TextView userNameTv;
        TextView userAgeAndPlaceTv;

        LinearLayout samePercentLayout;
        TextView userSamePercentTv;
        ImageView userSamePercentIv;
        TextView userIntersectionTv;
        ImageView userIntersectionIv;

        LinearLayout whosSuggestLayout;
        TextView matcherSaidTv;
        TextView whosSuggestTv;

        LinearLayout characterLayout;
        TextView characterTv;

        LinearLayout userTagLayout;
        TextView userTagTv;

        //--------last item type1
        TextView timeSurplusValTv;
        Button lastItemConfirmBtn;
        ImageView lastItemType1BottomIv;
        TextView nextTimeSuggestDescTv;

        //--------last item type2
        RelativeLayout lastItemType2BottomLayout;
        Button suggestSearchBtn;
        Button seeTimeLineBtn;
    }

    class RefreshTimeTask extends TimerTask {
        private Timer timer;
        private int sec;

        public RefreshTimeTask(int sec, Timer timer) {
            this.sec = sec;
            this.timer = timer;
        }

        @Override
        public void run() {
            if (sec < 0)
                this.timer.cancel();
            refreshTimeHandler.sendMessage(refreshTimeHandler.obtainMessage(0, sec--));
        }
    }
}
