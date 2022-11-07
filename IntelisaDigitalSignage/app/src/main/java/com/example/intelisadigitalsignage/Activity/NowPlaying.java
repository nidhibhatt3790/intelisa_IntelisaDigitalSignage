package com.example.intelisadigitalsignage.Activity;

import com.google.gson.annotations.SerializedName;

public class NowPlaying {


        @SerializedName("adName")
        private String adName;

        @SerializedName("adGroup")
        private String adGroup;

        @SerializedName("starttime")
        private String starttime;

        public void setAdName(String adName){
            this.adName = adName;
        }

        public String getAdName(){
            return adName;
        }

        public void setAdGroup(String adGroup){
            this.adGroup = adGroup;
        }

        public String getAdGroup(){
            return adGroup;
        }

        public void setStarttime(String starttime){
            this.starttime = starttime;
        }

        public String getStarttime(){
            return starttime;
        }

}
