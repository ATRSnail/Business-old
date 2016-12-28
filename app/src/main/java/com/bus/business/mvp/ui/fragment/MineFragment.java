package com.bus.business.mvp.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.bus.business.R;
import com.bus.business.common.UsrMgr;
import com.bus.business.mvp.entity.UserBean;
import com.bus.business.mvp.ui.fragment.base.BaseFragment;

import butterknife.BindView;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/24
 */
public class MineFragment extends BaseFragment{

    @BindView(R.id.tv_use_name)
    TextView mUseName;
    @BindView(R.id.tv_use_phone)
    TextView mUsePhone;

    private UserBean userBean;

    @Override
    public void initInjector() {

    }

    @Override
    public void initViews(View view) {
        userBean = UsrMgr.getUseInfo();
        mUseName.setText(userBean.getUserName());
        mUsePhone.setText(userBean.getPhoneModel());
    }


    @Override
    public int getLayoutId() {
        return R.layout.layout_mine;
    }
}
