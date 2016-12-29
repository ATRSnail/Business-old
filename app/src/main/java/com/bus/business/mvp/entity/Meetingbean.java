package com.bus.business.mvp.entity;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/24
 */
public class MeetingBean {

    /**
     * id : 1
     * utime : 1482723070000
     * meetingLoc : ioc
     * duration : 1
     * meetingName : meetName
     * status : 1
     * meetingTime : 1470627036000
     * qrImg :
     * meetingContent : context
     * ctime : 1482722354000
     * areaId : 2
     */

    private int id;
    private long utime;
    private String meetingLoc;
    private int duration;
    private String meetingName;
    private String status;
    private long meetingTime;
    private String qrImg;
    private String meetingContent;
    private long ctime;
    private int areaId;
    private String checkType;// 签到状态 0未签到 1已签到
    private String joinType;// 参会状态 0未 1已

    public boolean getJoinType() {
        return joinType != null && joinType.equals("1");
    }

    public void setJoinType(boolean joinType) {
        this.joinType = joinType ? "1" : "0";
    }

    public boolean getCheckType() {
        return checkType != null && checkType.equals("1");
    }

    public void setCheckType(boolean checkType) {
        this.checkType = checkType ? "1" : "0";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUtime() {
        return utime;
    }

    public void setUtime(long utime) {
        this.utime = utime;
    }

    public String getMeetingLoc() {
        return meetingLoc;
    }

    public void setMeetingLoc(String meetingLoc) {
        this.meetingLoc = meetingLoc;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(long meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getQrImg() {
        return qrImg;
    }

    public void setQrImg(String qrImg) {
        this.qrImg = qrImg;
    }

    public String getMeetingContent() {
        return meetingContent;
    }

    public void setMeetingContent(String meetingContent) {
        this.meetingContent = meetingContent;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    @Override
    public String toString() {
        return "MeetingBean{" +
                "id=" + id +
                ", utime=" + utime +
                ", meetingLoc='" + meetingLoc + '\'' +
                ", duration=" + duration +
                ", meetingName='" + meetingName + '\'' +
                ", status='" + status + '\'' +
                ", meetingTime=" + meetingTime +
                ", qrImg='" + qrImg + '\'' +
                ", meetingContent='" + meetingContent + '\'' +
                ", ctime=" + ctime +
                ", areaId=" + areaId +
                ", checkType='" + checkType + '\'' +
                ", joinType='" + joinType + '\'' +
                '}';
    }
}
