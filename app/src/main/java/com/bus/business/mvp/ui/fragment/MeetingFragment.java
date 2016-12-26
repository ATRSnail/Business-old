package com.bus.business.mvp.ui.fragment;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bus.business.R;
import com.bus.business.mvp.entity.LikeBean;
import com.bus.business.mvp.entity.Meetingbean;
import com.bus.business.mvp.presenter.impl.MeetingPresenterImpl;
import com.bus.business.mvp.ui.adapter.MeetingsAdapter;
import com.bus.business.mvp.ui.fragment.base.BaseFragment;
import com.bus.business.mvp.view.NewsView;
import com.bus.business.utils.NetUtil;
import com.bus.business.widget.RecyclerViewDivider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/23
 */
public class MeetingFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener
        , NewsView<Meetingbean>, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.news_rv)
    RecyclerView mNewsRV;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.empty_view)
    TextView mEmptyView;

    @Inject
    Activity mActivity;
    @Inject
    MeetingPresenterImpl mNewsPresenter;
    private BaseQuickAdapter mNewsListAdapter;
    private List<LikeBean> likeBeanList;


    private int pageNum = 1;
    private int numPerPage = 20;

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void initViews(View view) {
        initSwipeRefreshLayout();
        initRecyclerView();
        initPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_news;
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getIntArray(R.array.gplus_colors)
        );
    }

    private void initPresenter() {
        mNewsPresenter.setNewsTypeAndId(pageNum, numPerPage);
        mPresenter = mNewsPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    private void initRecyclerView() {
        mNewsRV.setHasFixedSize(true);
        mNewsRV.addItemDecoration(new RecyclerViewDivider(mActivity,
                LinearLayoutManager.VERTICAL, 2, getResources().getColor(R.color.red)));
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
        mNewsListAdapter = new MeetingsAdapter(R.layout.layout_meeting_item, likeBeanList);
        mNewsListAdapter.setOnLoadMoreListener(this);
        mNewsRV.setAdapter(mNewsListAdapter);

    }

    @Override
    public void setNewsList(Meetingbean newsBean) {
//        checkIsEmpty(newsBean.getLikeList());
        KLog.a(newsBean.toString());
        mNewsListAdapter.addData(newsBean.getLikeList());
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
    public void onRefresh() {
        mNewsPresenter.refreshData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mNewsPresenter.onDestory();
    }

    @Override
    public void onLoadMoreRequested() {
        mNewsPresenter.loadMore();
    }

    private void checkIsEmpty(List<LikeBean> newsSummary) {
        if (newsSummary == null && mNewsListAdapter.getData() == null) {
            mNewsRV.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);

        } else {
            mNewsRV.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }
}
