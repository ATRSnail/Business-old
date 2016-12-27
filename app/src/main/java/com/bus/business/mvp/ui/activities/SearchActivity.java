package com.bus.business.mvp.ui.activities;

import android.view.View;

import com.bus.business.R;
import com.bus.business.mvp.ui.activities.base.BaseActivity;

public class SearchActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initViews() {
        setCustomTitle("搜索");
        showOrGoneSearchRl(View.GONE);
    }

}
