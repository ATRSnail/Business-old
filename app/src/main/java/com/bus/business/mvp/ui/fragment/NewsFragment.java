package com.bus.business.mvp.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bus.business.R;
import com.bus.business.common.ApiConstants;
import com.bus.business.common.Constants;
import com.bus.business.common.LoadNewsType;
import com.bus.business.common.NewsType;
import com.bus.business.mvp.entity.BannerBean;
import com.bus.business.mvp.entity.response.RspBannerBean;
import com.bus.business.mvp.entity.response.base.BaseNewBean;
import com.bus.business.mvp.presenter.impl.BusinessPresenterImpl;
import com.bus.business.mvp.presenter.impl.NewsPresenterImpl;
import com.bus.business.mvp.ui.activities.NewDetailActivity;
import com.bus.business.mvp.ui.adapter.NewsAdapter;
import com.bus.business.mvp.ui.fragment.base.BaseLazyFragment;
import com.bus.business.mvp.view.BusinessView;
import com.bus.business.mvp.view.NewsView;
import com.bus.business.repository.network.RetrofitManager;
import com.bus.business.utils.NetUtil;
import com.bus.business.utils.TransformUtils;
import com.bus.business.widget.AutoSliderView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscriber;

import static android.view.View.VISIBLE;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/23
 */
public class NewsFragment extends BaseLazyFragment implements SwipeRefreshLayout.OnRefreshListener
        , NewsView<List<BaseNewBean>>
        , BusinessView
        , BaseQuickAdapter.RequestLoadMoreListener
        , BaseQuickAdapter.OnRecyclerViewItemClickListener
        , BaseSliderView.OnSliderClickListener {

    public static final String NEW_TYPE = "new_type";
    @BindView(R.id.news_rv)
    RecyclerView mNewsRV;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.empty_view)
    TextView mEmptyView;

    private View weatherView;
    private View mTitleHeader;
    private View mSlideHeader;
    private SliderLayout sliderLayout;
    private PagerIndicator pagerIndicator;

    @Inject
    Activity mActivity;
    @Inject
    NewsPresenterImpl mNewsPresenter;
    @Inject
    BusinessPresenterImpl mBusinessPresenter;
    private BaseQuickAdapter mNewsListAdapter;
    private List<BaseNewBean> likeBeanList;

    private int pageNum = 1;
    private boolean isXunFrg;//true是讯息页,false是协会页

    public static NewsFragment getInstance(@NewsType.checker int checker) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(NEW_TYPE, checker);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void initViews(View view) {

    }

    @Override
    public void onFirstUserVisible() {
        initViews();
    }

    private void initViews() {
        initIntentData();
        initHeadView();
        initSlider();
        initSwipeRefreshLayout();
        initRecyclerView();
        initPresenter();
        loadBannerData();
    }

    private void initIntentData() {
        isXunFrg = getArguments() == null || getArguments().getInt(NEW_TYPE) == 1;
    }


    private void initHeadView() {
        if (!isXunFrg) return;
        weatherView = View.inflate(getContext(), R.layout.layout_weather, null);
        mTitleHeader = View.inflate(mActivity, R.layout.layout_head_text, null);
        mSlideHeader = View.inflate(mActivity, R.layout.layout_autoloop_viewpage, null);
        sliderLayout = (SliderLayout) mSlideHeader.findViewById(R.id.slider);
        pagerIndicator = (PagerIndicator) mSlideHeader.findViewById(R.id.custom_indicator);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_news;
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getIntArray(R.array.gplus_colors));
    }

    private void initPresenter() {
        if (isXunFrg){
            mNewsPresenter.setNewsTypeAndId(pageNum, Constants.numPerPage,"");
            mNewsPresenter.attachView(this);
            mPresenter = mNewsPresenter;
            mPresenter.onCreate();
        }else {
            mBusinessPresenter.setNewsTypeAndId(pageNum, Constants.numPerPage,"");
            mBusinessPresenter.attachView(this);
            mPresenter = mBusinessPresenter;
            mPresenter.onCreate();
        }

    }

    private void initRecyclerView() {
        mNewsRV.setHasFixedSize(true);
        mNewsRV.setLayoutManager(new LinearLayoutManager(mActivity,
                LinearLayoutManager.VERTICAL, false));
        mNewsRV.setItemAnimator(new DefaultItemAnimator());

        likeBeanList = new ArrayList<>();
        mNewsListAdapter = new NewsAdapter(R.layout.layout_new_item, likeBeanList);
        mNewsListAdapter.setOnLoadMoreListener(this);
        if (isXunFrg) {
            mNewsListAdapter.addHeaderView(weatherView);
            mNewsListAdapter.addHeaderView(mSlideHeader);
            mNewsListAdapter.addHeaderView(mTitleHeader);
        }
        mNewsListAdapter.setOnRecyclerViewItemClickListener(this);
        mNewsListAdapter.openLoadMore(Constants.numPerPage, true);
        mNewsRV.setAdapter(mNewsListAdapter);

    }

    /**
     * 初始化导航图
     */
    private void initSlider() {
        if (!isXunFrg || sliderLayout == null || pagerIndicator == null) return;
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setDuration(4000);
        sliderLayout.setCustomIndicator(pagerIndicator);
    }

    /**
     * 初始化轮播图
     *
     * @param mList
     */
    private void initSlider(List<BannerBean> mList) {
        if (!isXunFrg) return;
        mSlideHeader.setVisibility(VISIBLE);
        for (BannerBean pageIconBean : mList) {
            AutoSliderView textSliderView = new AutoSliderView(this.getActivity(), pageIconBean);
            // initialize a SliderLayout
            textSliderView
                    .description(pageIconBean.getTitle())
                    .image(ApiConstants.NETEAST_HOST + pageIconBean.getFmImg())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            Bundle bundle = new Bundle();
            bundle.putString("id", pageIconBean.getId()+"");
            textSliderView.bundle(bundle);
            sliderLayout.addSlider(textSliderView);
        }
    }

    private void loadBannerData() {
        if (!isXunFrg) return;
        RetrofitManager.getInstance(1).getBannersObservable()
                .compose(TransformUtils.<RspBannerBean>defaultSchedulers())
                .subscribe(new Subscriber<RspBannerBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RspBannerBean rspBannerBean) {
                        initSlider(rspBannerBean.getBody().getNewsBannerList());
                    }
                });
    }


    @Override
    public void setNewsList(List<BaseNewBean> newsBean, @LoadNewsType.checker int loadType) {
//        checkIsEmpty(newsBean.getLikeList());
        switch (loadType) {
            case LoadNewsType.TYPE_REFRESH_SUCCESS:
                mSwipeRefreshLayout.setRefreshing(false);
                mNewsListAdapter.setNewData(newsBean);
                checkIsEmpty(newsBean);
                break;
            case LoadNewsType.TYPE_REFRESH_ERROR:
                mSwipeRefreshLayout.setRefreshing(false);
                checkIsEmpty(newsBean);
                break;
            case LoadNewsType.TYPE_LOAD_MORE_SUCCESS:
                if (newsBean == null || newsBean.size() == 0) {
                    Snackbar.make(mNewsRV, getString(R.string.no_more), Snackbar.LENGTH_SHORT).show();
                } else {
                    mNewsListAdapter.notifyDataChangedAfterLoadMore(newsBean, true);
                }
                break;
            case LoadNewsType.TYPE_LOAD_MORE_ERROR:

                break;
        }
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
        if (isXunFrg){
            mNewsPresenter.refreshData();
        }else {
            mBusinessPresenter.refreshData();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isXunFrg){
            mNewsPresenter.onDestory();
        }else {
            mBusinessPresenter.onDestory();
        }

    }

    @Override
    public void onLoadMoreRequested() {
        if (isXunFrg){
            mNewsPresenter.loadMore();
        }else {
            mBusinessPresenter.loadMore();
        }

    }

    private void checkIsEmpty(List<BaseNewBean> newsSummary) {
        if (newsSummary == null && mNewsListAdapter.getData() == null) {
            mNewsRV.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mNewsRV.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        goToNewsDetailActivity(view, position);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void goToNewsDetailActivity(View view, int position) {
        Intent intent = setIntent(position);
        startActivity(view, intent);
    }

    @NonNull
    private Intent setIntent(int position) {
        List<BaseNewBean> newsSummaryList = mNewsListAdapter.getData();
        Intent intent = new Intent(mActivity, NewDetailActivity.class);
        intent.putExtra(Constants.NEWS_POST_ID, newsSummaryList.get(position).getId()+"");
        intent.putExtra(Constants.NEWS_TYPE,isXunFrg?"1":"2");
        return intent;
    }

    private void startActivity(View view, Intent intent) {
        ImageView newsSummaryPhotoIv = (ImageView) view.findViewById(R.id.item_img_bg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(mActivity, newsSummaryPhotoIv, Constants.TRANSITION_ANIMATION_NEWS_PHOTOS);
            startActivity(intent, options.toBundle());
        } else {

            //让新的Activity从一个小的范围扩大到全屏
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity(mActivity, intent, options.toBundle());
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Bundle bundle = slider.getBundle();
        if (bundle == null) {
            return;
        }
        Intent intent = new Intent(mActivity, NewDetailActivity.class);
        intent.putExtra(Constants.NEWS_POST_ID, bundle.getString("id"));
        intent.putExtra(Constants.NEWS_TYPE,"1");
        startActivity(sliderLayout,intent);

    }

    @Override
    public void setBusinessList(List<BaseNewBean> newsBean, @LoadNewsType.checker int loadType) {
        switch (loadType) {
            case LoadNewsType.TYPE_REFRESH_SUCCESS:
                mSwipeRefreshLayout.setRefreshing(false);
                mNewsListAdapter.setNewData(newsBean);
                checkIsEmpty(newsBean);
                break;
            case LoadNewsType.TYPE_REFRESH_ERROR:
                mSwipeRefreshLayout.setRefreshing(false);
                checkIsEmpty(newsBean);
                break;
            case LoadNewsType.TYPE_LOAD_MORE_SUCCESS:
                if (newsBean == null || newsBean.size() == 0) {
                    Snackbar.make(mNewsRV, getString(R.string.no_more), Snackbar.LENGTH_SHORT).show();
                } else {
                    mNewsListAdapter.notifyDataChangedAfterLoadMore(newsBean, true);
                }
                break;
            case LoadNewsType.TYPE_LOAD_MORE_ERROR:

                break;
        }
    }
}
