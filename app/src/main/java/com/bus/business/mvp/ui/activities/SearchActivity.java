package com.bus.business.mvp.ui.activities;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.bus.business.R;
import com.bus.business.common.LoadNewsType;
import com.bus.business.mvp.entity.LikeBean;
import com.bus.business.mvp.entity.response.base.BaseNewBean;
import com.bus.business.mvp.presenter.impl.NewsPresenterImpl;
import com.bus.business.mvp.ui.activities.base.BaseActivity;
import com.bus.business.mvp.ui.adapter.NewsAdapter;
import com.bus.business.mvp.view.NewsView;
import com.bus.business.utils.NetUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener
        , NewsView<List<LikeBean>> ,BaseQuickAdapter.RequestLoadMoreListener
        , BaseQuickAdapter.OnRecyclerViewItemClickListener{

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.news_rv)
    RecyclerView mNewsRV;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private BaseQuickAdapter mNewsListAdapter;
    private List<BaseNewBean> likeBeanList;
    private int pageNum = 1;
    private int numPerPage = 20;
    @Inject
    Activity mActivity;
    @Inject
    NewsPresenterImpl mNewsPresenter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        setCustomTitle("搜索");
        showOrGoneSearchRl(View.GONE);

        initSwipeRefreshLayout();
        initRecyclerView();
     //   initPresenter();
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(mActivity.getResources().getIntArray(R.array.gplus_colors));
    }

    private void initPresenter() {
        mNewsPresenter.setNewsTypeAndId(pageNum, numPerPage,"");
        mNewsPresenter.attachView(this);
        mPresenter = mNewsPresenter;
    }

    private void initRecyclerView() {
        mNewsRV.setHasFixedSize(true);
        mNewsRV.setLayoutManager(new LinearLayoutManager(mActivity,
                LinearLayoutManager.VERTICAL, false));
        mNewsRV.setItemAnimator(new DefaultItemAnimator());
        mNewsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

        });

        likeBeanList = new ArrayList<>();
        mNewsListAdapter = new NewsAdapter(R.layout.layout_new_item, likeBeanList);
        mNewsListAdapter.setOnLoadMoreListener(this);
        mNewsListAdapter.setOnRecyclerViewItemClickListener(this);
        mNewsRV.setAdapter(mNewsListAdapter);
    }


    @OnClick(R.id.search_cancel)
    public void touchSearch(View v) {
        initPresenter();
        if (mPresenter != null)
            mPresenter.onCreate();
    }

    @Override
    public void onRefresh() {
        mNewsPresenter.refreshData();
    }

    @Override
    public void setNewsList(List<LikeBean> newsBean, @LoadNewsType.checker int loadType) {
        mNewsListAdapter.addData(newsBean);
        mSwipeRefreshLayout.setRefreshing(false);
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
            Snackbar.make(mNewsRV, msg, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(View view, int i) {

    }

    @Override
    public void onLoadMoreRequested() {
        mNewsPresenter.loadMore();
    }
}
