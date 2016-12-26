package com.bus.business.mvp.ui.activities;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bus.business.R;
import com.bus.business.mvp.entity.NewsBean;
import com.bus.business.mvp.presenter.impl.NewsPresenterImpl;
import com.bus.business.mvp.ui.activities.base.BaseActivity;
import com.bus.business.mvp.view.NewsView;
import com.bus.business.utils.NetUtil;

import javax.inject.Inject;

import butterknife.BindView;

public class NewDetailActivity extends BaseActivity implements NewsView<NewsBean> {

    private int pageNum = 1;
    private int numPerPage = 20;

    @Inject
    Activity mActivity;
    @Inject
    NewsPresenterImpl mNewsPresenter;

    @BindView(R.id.news_detail_title_tv)
    TextView mTitle;
    @BindView(R.id.news_detail_from_tv)
    TextView mFrom;
    @BindView(R.id.news_detail_body_tv)
    TextView mDetail;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

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
        mNewsPresenter.setNewsTypeAndId(pageNum, numPerPage);
        mPresenter = mNewsPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
        setCustomTitle("习近平绿色发展理念引领中国环境治理新实践");
        showOrGoneSearchRl(View.GONE);
    }

    @Override
    public void setNewsList(NewsBean newsBean) {
        mTitle.setText("习近平绿色发展理念引领中国环境治理新实践");
        mFrom.setText("澎湃新闻 06-06 17:24");
        mDetail.setText("最近，“民国无名女神”在微博上火了。讲真，这位无名女神是比现在那些整容脸、\n" +
                "            网红脸让人看的赏心悦目。但是，比起那些年轰动的民国美女和才女们，她还是略有逊色。\n" +
                "            要知道这些美女和才女们，不论是本尊还是是后世扮演她们的演员，真真是极美的。这些人里有你心中女神么？");
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMsg(String msg) {
        mProgressBar.setVisibility(View.GONE);
        // 网络不可用状态在此之前已经显示了提示信息
        if (NetUtil.isNetworkAvailable()) {
            Snackbar.make(mProgressBar, msg, Snackbar.LENGTH_LONG).show();
        }
    }
}
