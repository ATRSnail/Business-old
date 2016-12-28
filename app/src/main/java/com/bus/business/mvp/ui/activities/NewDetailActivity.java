package com.bus.business.mvp.ui.activities;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bus.business.R;
import com.bus.business.common.Constants;
import com.bus.business.mvp.entity.BusDetailBean;
import com.bus.business.mvp.entity.response.RspBusDetailBean;
import com.bus.business.mvp.entity.response.RspNewDetailBean;
import com.bus.business.mvp.ui.activities.base.BaseActivity;
import com.bus.business.repository.network.RetrofitManager;
import com.bus.business.utils.TransformUtils;
import com.bus.business.widget.URLImageGetter;
import com.socks.library.KLog;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscriber;

public class NewDetailActivity extends BaseActivity {

    private int pageNum = 1;
    private int numPerPage = 20;
    private String newsId;
    private String newsType;

    @Inject
    Activity mActivity;

    @BindView(R.id.news_detail_title_tv)
    TextView mTitle;
    @BindView(R.id.news_detail_from_tv)
    TextView mFrom;
    @BindView(R.id.news_detail_body_tv)
    TextView mNewsDetailBodyTv;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private URLImageGetter mUrlImageGetter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_detail;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        newsId = getIntent().getStringExtra(Constants.NEWS_POST_ID);
        newsType = getIntent().getStringExtra(Constants.NEWS_TYPE);

        setCustomTitle(newsType.equals("1")?"新闻详情":"商讯详情");
        showOrGoneSearchRl(View.GONE);
        loadNewDetail();
    }

    private void loadNewDetail(){
        if (newsType.equals("1")){
            RetrofitManager.getInstance(1).getNewDetailObservable(newsId)
                    .compose(TransformUtils.<RspNewDetailBean>defaultSchedulers())
                    .subscribe(new Subscriber<RspNewDetailBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(RspNewDetailBean rspNewDetailBean) {
                            mProgressBar.setVisibility(View.GONE);
                            KLog.d(rspNewDetailBean.toString());
                            fillData(rspNewDetailBean.getBody().getNews());
                        }
                    });
        }
        else {
            RetrofitManager.getInstance(1).getBusDetailObservable(newsId)
                    .compose(TransformUtils.<RspBusDetailBean>defaultSchedulers())
                    .subscribe(new Subscriber<RspBusDetailBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(RspBusDetailBean rspNewDetailBean) {
                            mProgressBar.setVisibility(View.GONE);
                            KLog.d(rspNewDetailBean.toString());
                            fillData(rspNewDetailBean.getBody().getBusiness());
                        }
                    });
        }
    }

    private void fillData(BusDetailBean bean){
        mTitle.setText(bean.getTitle());
        mFrom.setText("工商联");
        mUrlImageGetter = new URLImageGetter(mNewsDetailBodyTv, bean.getContentS(), 1);
        mNewsDetailBodyTv.setText(Html.fromHtml(bean.getContentS(),mUrlImageGetter,null));
    }
}
