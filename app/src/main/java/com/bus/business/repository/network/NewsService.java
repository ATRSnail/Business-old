package com.bus.business.repository.network;

import com.bus.business.mvp.entity.response.RspMeetingBean;
import com.bus.business.mvp.entity.response.RspNewsBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/22
 */
public interface NewsService {

    @FormUrlEncoded
    @POST("mblVf/youLike")
    Observable<RspNewsBean> getNewsList(@FieldMap Map<String,String>map);

    @FormUrlEncoded
    @POST("mblVf/youLike")
    Observable<RspMeetingBean> getMeetingsList(@FieldMap Map<String,String>map);
}
