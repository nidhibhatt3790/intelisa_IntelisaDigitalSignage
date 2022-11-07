package com.example.intelisadigitalsignage.Activity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyResponse {

    @SerializedName("screenID")
    private String screenID;

    @SerializedName("deviceIMEI")
    private Object deviceIMEI;

    @SerializedName("__v")
    private int V;

    @SerializedName("scheduleDate")
    private String scheduleDate;

    @SerializedName("_id")
    private String id;

    @SerializedName("lastModified")
    private String lastModified;

    @SerializedName("nowPlaying")
    private NowPlaying nowPlaying;

    @SerializedName("playHistory")
    private List<Object> playHistory;

    public void setScreenID(String screenID){
        this.screenID = screenID;
    }

    public String getScreenID(){
        return screenID;
    }

    public void setDeviceIMEI(Object deviceIMEI){
        this.deviceIMEI = deviceIMEI;
    }

    public Object getDeviceIMEI(){
        return deviceIMEI;
    }

    public void setV(int V){
        this.V = V;
    }

    public int getV(){
        return V;
    }

    public void setScheduleDate(String scheduleDate){
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleDate(){
        return scheduleDate;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setLastModified(String lastModified){
        this.lastModified = lastModified;
    }

    public String getLastModified(){
        return lastModified;
    }

    public void setNowPlaying(NowPlaying nowPlaying){
        this.nowPlaying = nowPlaying;
    }

    public NowPlaying getNowPlaying(){
        return nowPlaying;
    }

    public void setPlayHistory(List<Object> playHistory){
        this.playHistory = playHistory;
    }

    public List<Object> getPlayHistory(){
        return playHistory;
    }

//    public class NowPlaying{
//
//        @SerializedName("adName")
//        private String adName;
//
//        @SerializedName("adGroup")
//        private String adGroup;
//
//        @SerializedName("starttime")
//        private String starttime;
//
//        public void setAdName(String adName){
//            this.adName = adName;
//        }
//
//        public String getAdName(){
//            return adName;
//        }
//
//        public void setAdGroup(String adGroup){
//            this.adGroup = adGroup;
//        }
//
//        public String getAdGroup(){
//            return adGroup;
//        }
//
//        public void setStarttime(String starttime){
//            this.starttime = starttime;
//        }
//
//        public String getStarttime(){
//            return starttime;
//        }
//    }

}
