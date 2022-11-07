package com.example.intelisadigitalsignage.Activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NowPlayingClass {

    @SerializedName("nowPlaying")
    @Expose
    private List<Nowplaying> data = null;


    public class Nowplaying {

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getAdName() {
            return adName;
        }

        public void setAdName(String adName) {
            this.adName = adName;
        }

        public String getAdGroup() {
            return adGroup;
        }

        public void setAdGroup(String adGroup) {
            this.adGroup = adGroup;
        }

        @SerializedName("starttime")
        @Expose
        private String starttime;
        @SerializedName("adName")
        @Expose
        private String adName;
        @SerializedName("adGroup")
        @Expose
        private String adGroup;

    }

}
