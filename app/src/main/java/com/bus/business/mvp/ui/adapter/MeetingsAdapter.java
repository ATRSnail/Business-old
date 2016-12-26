package com.bus.business.mvp.ui.adapter;

import com.bus.business.R;
import com.bus.business.mvp.entity.LikeBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/24
 */
public class MeetingsAdapter extends BaseQuickAdapter<LikeBean> {
    public MeetingsAdapter(int layoutResId, List<LikeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LikeBean likeBean) {
        baseViewHolder.setText(R.id.tv_name, likeBean.getIntroduction());
        baseViewHolder.setText(R.id.tv_address, likeBean.getHit());
        baseViewHolder.setText(R.id.tv_date, likeBean.getTypeName());
    }
}
