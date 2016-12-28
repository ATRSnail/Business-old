package com.bus.business.mvp.entity;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/28
 */
public class UserBean {

    /**
     * phoneNo : 15101659241
     * niceName : wo
     * id : xaQKecQ1p9ntyGIH39IRPsFoWpx4sfvT
     * utime : 1482742620000
     * systemeType : 2
     * imei : 1234567
     * state : 1
     * userName : y1234567
     * userEmail : 77826018@qq.com
     * ctime : 1482739541000
     * phoneModel : 1234567
     */

    private String phoneNo;
    private String niceName;
    private String id;
    private long utime;
    private String systemeType;
    private String imei;
    private String state;
    private String userName;
    private String userEmail;
    private long ctime;
    private String phoneModel;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getNiceName() {
        return niceName;
    }

    public void setNiceName(String niceName) {
        this.niceName = niceName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUtime() {
        return utime;
    }

    public void setUtime(long utime) {
        this.utime = utime;
    }

    public String getSystemeType() {
        return systemeType;
    }

    public void setSystemeType(String systemeType) {
        this.systemeType = systemeType;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "phoneNo='" + phoneNo + '\'' +
                ", niceName='" + niceName + '\'' +
                ", id='" + id + '\'' +
                ", utime=" + utime +
                ", systemeType='" + systemeType + '\'' +
                ", imei='" + imei + '\'' +
                ", state='" + state + '\'' +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", ctime=" + ctime +
                ", phoneModel='" + phoneModel + '\'' +
                '}';
    }
}
