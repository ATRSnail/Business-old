package com.bus.business.mvp.entity;

/**
 * @author xch
 * @version 1.0
 * @create_date 17/1/12
 */

public class WeathersBean {

    private WeatherBean weather;
    private String carNoLimit;

    public WeatherBean getWeather() {
        return weather;
    }

    public void setWeather(WeatherBean weather) {
        this.weather = weather;
    }

    public String getCarNoLimit() {
        return carNoLimit;
    }

    public void setCarNoLimit(String carNoLimit) {
        this.carNoLimit = carNoLimit;
    }

    @Override
    public String toString() {
        return "WeathersBean{" +
                "weather=" + weather +
                ", carNoLimit='" + carNoLimit + '\'' +
                '}';
    }
}
