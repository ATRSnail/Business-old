package com.bus.business.mvp.ui.activities;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bus.business.R;
import com.bus.business.mvp.entity.MeetingBean;
import com.bus.business.mvp.entity.response.base.BaseRspObj;
import com.bus.business.mvp.event.JoinToMeetingEvent;
import com.bus.business.mvp.ui.activities.base.BaseActivity;
import com.bus.business.repository.network.RetrofitManager;
import com.bus.business.utils.DateUtil;
import com.bus.business.utils.TransformUtils;
import com.bus.business.utils.UT;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * @author xch
 * @version 1.0
 * @create_date 17/1/10
 */
public class MeetingDetailActivity extends BaseActivity{

    @Inject
    Activity mActivity;

    @BindView(R.id.news_detail_title_tv)
    TextView mTitle;
    @BindView(R.id.tv_publish_date)
    TextView mPubDate;
    @BindView(R.id.tv_join_address)
    TextView mJoinAddress;
    @BindView(R.id.tv_join_date)
    TextView mJoinDate;
    @BindView(R.id.news_detail_body_tv)
    TextView mNewsDetailBodyTv;
    @BindView(R.id.img_add)
    TextView mAddBtn;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private MeetingBean meetingBean;
    private int currentPos;


    @Override
    public int getLayoutId() {
        return R.layout.activity_meeting_detail;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        meetingBean = (MeetingBean) getIntent().getSerializableExtra(MeetingBean.MEETINGBEAN);
        currentPos = getIntent().getIntExtra(MeetingBean.MEETINGPOS,0);

        mProgressBar.setVisibility(View.GONE);
        setCustomTitle(meetingBean.getMeetingName());
        showOrGoneSearchRl(View.GONE);
        mAddBtn.setText(meetingBean.getJoinType()?"已参会":"参会");
        mTitle.setText(meetingBean.getMeetingName());

        mAddBtn.setBackgroundResource(meetingBean.getJoinType() ? R.drawable.grey_circle_5 : R.drawable.blue_circle_5);
        mJoinDate.setText("参会时间:"+DateUtil.getCurGroupDay(meetingBean.getMeetingTime()));
        mPubDate.setText("发表时间:"+DateUtil.getCurGroupDay(meetingBean.getCtime()));
        mJoinAddress.setText("参会地点:"+meetingBean.getMeetingLoc());
        mNewsDetailBodyTv.setText(meetingBean.getMeetingContent());
    }


    @OnClick(R.id.img_add)
    public void addToMeeting(View view){
        requestAddToMeeting();
    }

    public void requestAddToMeeting(){
        if (meetingBean.getJoinType()){
            UT.show("已参会");
            return;
        }
        RetrofitManager.getInstance(1).joinMeeting(meetingBean.getId())
                .compose(TransformUtils.<BaseRspObj>defaultSchedulers())
                .subscribe(new Subscriber<BaseRspObj>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseRspObj responseBody) {
                        if (responseBody.getHead().getRspCode().equals("0")) {
                            mAddBtn.setBackgroundResource(R.drawable.grey_circle_5);
                            mAddBtn.setText("已参会");
                            meetingBean.setJoinType(true);
                            EventBus.getDefault().post(new JoinToMeetingEvent(currentPos));
                        }
                        UT.show(responseBody.getHead().getRspMsg());

                    }
                });
    }
}
