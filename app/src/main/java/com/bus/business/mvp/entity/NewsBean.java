package com.bus.business.mvp.entity;

import java.util.List;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/22
 */
public class NewsBean {
    private String seed;
    private List<LikeBean> likeList;

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public List<LikeBean> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<LikeBean> likeList) {
        this.likeList = likeList;
    }

    @Override
    public String toString() {
        return "LikeListModel{" +
                "seed='" + seed + '\'' +
                ", likeList=" + likeList +
                '}';
    }
}
