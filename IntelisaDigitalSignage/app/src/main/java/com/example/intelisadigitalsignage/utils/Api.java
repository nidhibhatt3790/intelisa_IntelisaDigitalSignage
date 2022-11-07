package com.example.intelisadigitalsignage.utils;

import com.example.intelisadigitalsignage.Activity.MyRequest;
import com.example.intelisadigitalsignage.Activity.MyResponse;
import com.example.intelisadigitalsignage.Activity.NowPlayingClass;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {

    String URLPrefix = "https://login.intelisa.in/api/";
    String DevicePrefix = "https://login.intelisa.in/api/device/";

    @GET(URLPrefix + "getSerialNumber/")
    Call<ResponseBody> get_unique_key();


    @GET(URLPrefix + "getAppAPKVersion/")
    Call<ResponseBody> get_app_version();

//    @FormUrlEncoded
//    @POST("telnet/6003/index.php")
//    Call<ResponseBody> upload_location(@Field("data") String data);


    @FormUrlEncoded
    @POST(DevicePrefix + "updateDeviceIP/")
    Call<ResponseBody> get_deviceIP(
            @Field("deviceIMEI") String deviceIMEI


    );

    @FormUrlEncoded
    @POST(URLPrefix + "fetchSchedule/")
    Call<ResponseBody> get_scheduler(
            @Field("screenID") String screenID,
            @Field("scheduleDate") String scheduleDate);

    @FormUrlEncoded
    @POST(URLPrefix + "savePlayReport/")
    Call<ResponseBody> save_playVideo(
            @Field("screenID") String screenID,
            @Field("scheduleDate") String scheduleDate,
            @Field("nowPlaying") NowPlayingClass jsonObject);

    @POST(URLPrefix + "savePlayReport/")
    Call<MyResponse> postData(@Body MyRequest body);

//    }


}
