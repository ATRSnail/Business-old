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
import com.bus.business.common.Constants;
import com.bus.business.mvp.entity.BannerBean;
import com.bus.business.mvp.entity.LikeBean;
import com.bus.business.mvp.entity.NewsBean;
import com.bus.business.mvp.presenter.impl.NewsPresenterImpl;
import com.bus.business.mvp.ui.activities.NewDetailActivity;
import com.bus.business.mvp.ui.adapter.NewsAdapter;
import com.bus.business.mvp.ui.fragment.base.BaseFragment;
import com.bus.business.mvp.view.NewsView;
import com.bus.business.utils.NetUtil;
import com.bus.business.widget.AutoSliderView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static android.view.View.VISIBLE;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/23
 */
public class NewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener
        , NewsView<NewsBean>, BaseQuickAdapter.RequestLoadMoreListener
        , BaseQuickAdapter.OnRecyclerViewItemClickListener
        , BaseSliderView.OnSliderClickListener {
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
        initHeadView();
        initSlider();
        initSwipeRefreshLayout();
        initRecyclerView();
        initPresenter();
    }

    private void initHeadView() {
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
        mNewsPresenter.setNewsTypeAndId(pageNum, numPerPage);
        mPresenter = mNewsPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
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
        mNewsListAdapter.addHeaderView(weatherView);
        mNewsListAdapter.addHeaderView(mSlideHeader);
        mNewsListAdapter.addHeaderView(mTitleHeader);
        mNewsListAdapter.setOnRecyclerViewItemClickListener(this);
        mNewsRV.setAdapter(mNewsListAdapter);

    }

    /**
     * 初始化导航图
     */
    private void initSlider() {
        if (sliderLayout == null || pagerIndicator == null) return;
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
        if (mList == null){
            mList = new ArrayList<>();
            mList.add(new BannerBean("AA"));
            mList.add(new BannerBean("AA"));
            mList.add(new BannerBean("AA"));
        }
        mSlideHeader.setVisibility(VISIBLE);
        for (BannerBean pageIconBean : mList) {
            AutoSliderView textSliderView = new AutoSliderView(this.getActivity(), pageIconBean);
            // initialize a SliderLayout
            textSliderView
                    .description(pageIconBean.getTypeName())
                    .image("http://111.206.135.50:8080"+pageIconBean.getImg())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            Bundle bundle = new Bundle();
            bundle.putString("typeId", pageIconBean.getVfType());
            bundle.putString("id", pageIconBean.getOutid());
            textSliderView.bundle(bundle);
            sliderLayout.addSlider(textSliderView);
        }
    }

    @Override
    public void setNewsList(NewsBean newsBean) {
//        checkIsEmpty(newsBean.getLikeList());
        KLog.a(newsBean.toString());
        initSlider(null);
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
        List<LikeBean> newsSummaryList = mNewsListAdapter.getData();
        Intent intent = new Intent(mActivity, NewDetailActivity.class);
        intent.putExtra(Constants.NEWS_POST_ID, newsSummaryList.get(position).getId());
        intent.putExtra(Constants.NEWS_IMG_RES, newsSummaryList.get(position).getCopyrightImg());
        return intent;
    }

    private void startActivity(View view, Intent intent) {
        ImageView newsSummaryPhotoIv = (ImageView) view.findViewById(R.id.item_img_bg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(mActivity, newsSummaryPhotoIv, Constants.TRANSITION_ANIMATION_NEWS_PHOTOS);
            startActivity(intent, options.toBundle());
        } else {
/*            ActivityOptionsCompat.makeCustomAnimation(this,
                    R.anim.slide_bottom_in, R.anim.slide_bottom_out);
            这个我感觉没什么用处，类似于
            overridePendingTransition(R.anim.slide_bottom_in, android.R.anim.fade_out);*/

/*            ActivityOptionsCompat.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY)
            这个方法可以用于4.x上，是将一个小块的Bitmpat进行拉伸的动画。*/

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
        String typeId = bundle.getString("typeId");
        String id = bundle.getString("id");
    }
}
