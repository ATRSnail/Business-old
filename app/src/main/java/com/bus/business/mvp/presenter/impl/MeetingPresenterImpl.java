package com.bus.business.mvp.presenter.impl;

import com.bus.business.mvp.entity.Meetingbean;
import com.bus.business.mvp.entity.response.RspMeetingBean;
import com.bus.business.mvp.interactor.NewsInteractor;
import com.bus.business.mvp.interactor.impl.MeetingInteractorImpl;
import com.bus.business.mvp.presenter.NewsPresenter;
import com.bus.business.mvp.presenter.base.BasePresenterImpl;
import com.bus.business.mvp.view.NewsView;
import com.socks.library.KLog;

import javax.inject.Inject;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/24
 */
public class MeetingPresenterImpl extends BasePresenterImpl<NewsView<Meetingbean>,RspMeetingBean>
        implements NewsPresenter {

    private NewsInteractor<RspMeetingBean> mNewsInteractor;
    private int pageNum;
    private int numPerPage;
    private String typeId;
    private boolean mIsRefresh = true;
    private boolean misFirstLoad;

    @Inject
    public MeetingPresenterImpl(MeetingInteractorImpl newsInteractor) {
        this.mNewsInteractor = newsInteractor;
    }

    @Override
    public void onCreate() {
        if (mView != null) {
            loadNewsData();
        }
    }

    @Override
    public void beforeRequest() {
        if (!misFirstLoad) {
            mView.showProgress();
        }
    }

    @Override
    public void onError(String errorMsg) {
        super.onError(errorMsg);
        if (mView != null) {

        }
    }

    @Override
    public void success(RspMeetingBean data) {
        misFirstLoad = true;
        KLog.a("ddd----success");
        if (data == null || data.getBody().getLikeList() == null) return;
        pageNum ++;
        if (mView != null) {
            mView.setNewsList(data.getBody());
            mView.hideProgress();
        }
    }

    @Override
    public void setNewsTypeAndId(int pageNum, int numPerPage) {
        this.pageNum = pageNum;
        this.numPerPage = numPerPage;
    }

    @Override
    public void refreshData() {
        pageNum = 1;
        mIsRefresh = true;
        loadNewsData();
    }

    @Override
    public void loadMore() {
        loadNewsData();
    }

    private void loadNewsData() {
        mSubscription = mNewsInteractor.loadNews(this, pageNum, numPerPage);
    }
}
