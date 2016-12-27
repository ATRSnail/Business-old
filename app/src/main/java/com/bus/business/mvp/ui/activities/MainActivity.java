package com.bus.business.mvp.ui.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;

import com.bus.business.R;
import com.bus.business.mvp.ui.activities.base.BaseActivity;
import com.bus.business.mvp.ui.fragment.MainPagerFragment;
import com.bus.business.mvp.ui.fragment.MeetingFragment;
import com.bus.business.mvp.ui.fragment.MineFragment;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static int currIndex = 0;
    @BindView(R.id.group)
    RadioGroup group;

    private ArrayList<String> fragmentTags;
    private FragmentManager fragmentManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initViews() {
        mToolbar.setNavigationIcon(null);
        mToolbar.setNavigationOnClickListener(null);
        fragmentManager = getSupportFragmentManager();
        initData();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.foot_bar_home:
                        chageIndex(0);
                        break;
                    case R.id.foot_bar_im:
                        chageIndex(1);
                        break;
                    case R.id.foot_bar_interest:
                        chageIndex(2);
                        break;
                    case R.id.main_footbar_user:
                        chageIndex(3);
                        break;
                    default:
                        break;
                }
                showFragment();
            }
        });

        showFragment();
    }

    private void initData() {
        currIndex = 0;
        fragmentTags = new ArrayList<>(Arrays.asList("HomeFragment", "ImFragment", "InterestFragment", "MemberFragment"));
    }

    private void chageIndex(int index) {
        setCustomTitle(index==0||index==1 ? "" : setTabSelection(index));
        showOrGoneSearchRl(index == 0||index == 1 ? View.VISIBLE : View.GONE);

        currIndex = index;
        showFragment();
    }

    @OnClick(R.id.rl_search)
    public void toSearch(View v){
        startActivity(new Intent(MainActivity.this,SearchActivity.class));
    }

    private String setTabSelection(int index){
        switch (index){
            case 2:
                return "通讯录";
            case 3:
                return "我的";
        }
        return "";
    }

    private void showFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags.get(currIndex));
        if (fragment == null) {
            fragment = instantFragment(currIndex);
        }
        for (int i = 0; i < fragmentTags.size(); i++) {
            Fragment f = fragmentManager.findFragmentByTag(fragmentTags.get(i));
            if (f != null && f.isAdded()) {
                fragmentTransaction.hide(f);
            }
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fragment_container, fragment, fragmentTags.get(currIndex));
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    private Fragment instantFragment(int currIndex) {
        switch (currIndex) {
            case 0:
                return new MainPagerFragment();
            case 1:
                return new MeetingFragment();
            case 2:
                return new MainPagerFragment();
            case 3:
                return new MineFragment();
            default:
                return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
