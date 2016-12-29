package com.bus.business.mvp.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bus.business.R;
import com.bus.business.common.UsrMgr;
import com.bus.business.mvp.entity.UserBean;
import com.bus.business.mvp.ui.activities.RetPasswordActivity;
import com.bus.business.mvp.ui.fragment.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

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


    @OnClick({R.id.tv_account_manager})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_account_manager:
                startActivity(new Intent(getActivity(), RetPasswordActivity.class));
                break;
        }
    }
}
