package com.bus.business.mvp.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bus.business.App;
import com.bus.business.R;
import com.bus.business.mvp.entity.response.base.BaseNewBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by xuchunhui on 16/8/12.
 */
public class NewsAdapter extends BaseQuickAdapter<BaseNewBean> {

    public NewsAdapter(int layoutResId, List<BaseNewBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BaseNewBean likeModel) {

        baseViewHolder.setText(R.id.item_title, likeModel.getTitle());
        baseViewHolder.setText(R.id.item_desc, likeModel.getFmImg());
        baseViewHolder.setText(R.id.item_type, likeModel.getCtime()+"");

        ImageView img = baseViewHolder.getView(R.id.item_img_bg);

        Glide.with(App.getAppContext()).load("http://172.16.10.15:9300/"+likeModel.getFmImg()).asBitmap() // gif格式有时会导致整体图片不显示，貌似有冲突
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.image_place_holder)
                .error(R.drawable.ic_load_fail)
                .into(img);
    }
}
