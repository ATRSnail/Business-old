package com.bus.business.mvp.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bus.business.R;
import com.bus.business.mvp.ui.activities.PlaceActivity;
import com.bus.business.mvp.ui.adapter.ViewPageAdapter;
import com.bus.business.mvp.ui.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/22
 */
public class MainPagerFragment extends BaseFragment {

    private static final String[] TITLE = {"讯息","协会+"};

    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.vp_view)
    ViewPager mViewPager;

    ViewPageAdapter mViewPageAdapter;
    private List<String> mTitles = new ArrayList<String>();
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    @Inject
    Activity mActivity;

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void initViews(View view) {
        mTitles.add(TITLE[0]);
        mTitles.add(TITLE[1]);
        mFragments.add(new NewsFragment());
        mFragments.add(new NewsFragment());
        mViewPageAdapter = new ViewPageAdapter(getActivity().getSupportFragmentManager(), mTitles, mFragments);
        mViewPager.setAdapter(mViewPageAdapter);
        //为TabLayout设置ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
        //使用ViewPager的适配器
        mTabLayout.setTabsFromPagerAdapter(mViewPageAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_page;
    }

    @OnClick(R.id.img_plus)
    public void choiceCity(View v){
      startActivity(new Intent(mActivity, PlaceActivity.class));
    }
}
