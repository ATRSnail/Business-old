package com.bus.business.mvp.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.bus.business.R;
import com.bus.business.mvp.entity.MeetingBean;
import com.bus.business.mvp.entity.response.base.BaseRspObj;
import com.bus.business.repository.network.RetrofitManager;
import com.bus.business.utils.DateUtil;
import com.bus.business.utils.TransformUtils;
import com.bus.business.utils.UT;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import rx.Subscriber;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/24
 */
public class MeetingsAdapter extends BaseQuickAdapter<MeetingBean> {
    private String stateStr;
    public MeetingsAdapter(int layoutResId, List<MeetingBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MeetingBean likeBean) {
        baseViewHolder.setText(R.id.tv_name, likeBean.getMeetingName());
        baseViewHolder.setText(R.id.tv_address, "北京工商联");
        baseViewHolder.setText(R.id.tv_date, DateUtil.getCurGroupDay(likeBean.getMeetingTime()));

        TextView addText = baseViewHolder.getView(R.id.img_add);
        if (likeBean.getCheckType()){
            stateStr = "已签到";
        }else {
            stateStr = likeBean.getJoinType()?"已参会":"参会";
        }
        addText.setText(stateStr);
        addText.setBackgroundResource(likeBean.getJoinType() ? R.drawable.grey_circle_5 : R.drawable.blue_circle_5);
        addText.setOnClickListener(new addClickListener(addText, likeBean));
    }

    class addClickListener implements View.OnClickListener {
        private TextView tv;
        private MeetingBean meetingBean;

        public addClickListener(TextView tv, MeetingBean likeBean) {
            this.tv = tv;
            this.meetingBean = likeBean;
        }

        @Override
        public void onClick(View view) {
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
                                tv.setBackgroundResource(R.drawable.grey_circle_5);
                                tv.setText("已参会");
                                meetingBean.setJoinType(true);
                            }
                            UT.show(responseBody.getHead().getRspMsg());

                        }
                    });
        }
    }
}
