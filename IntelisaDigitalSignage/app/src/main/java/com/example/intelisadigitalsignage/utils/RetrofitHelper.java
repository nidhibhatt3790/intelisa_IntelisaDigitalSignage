package com.example.intelisadigitalsignage.utils;

import android.accounts.NetworkErrorException;
import android.util.Log;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.concurrent.TimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    public static String GREEN_URL = "https://login.intelisa.in";
    private Api gsonAPI,greenAPI;
    private ConnectionCallBack connectionCallBack;


    public RetrofitHelper() {

        Retrofit gsonretrofit = new Retrofit.Builder()
                .baseUrl(GREEN_URL)
               // .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gsonAPI = gsonretrofit.create(Api.class);

    }

    public RetrofitHelper(String url) {

        Retrofit gsonretrofit = new Retrofit.Builder()
                .baseUrl(url)
                //.client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        greenAPI = gsonretrofit.create(Api.class);

    }
    public Api api() {
        return gsonAPI;
    }
    public Api green_api() {
        return greenAPI;
    }

    public void callApi(Call<ResponseBody> call, ConnectionCallBack callBack) {
        connectionCallBack = callBack;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Utils.appendLog(response.raw().request().url() + "  receive response: " + response.raw().receivedResponseAtMillis() + " send= " + response.raw().sentRequestAtMillis());
//                Utils.appendLog(response.raw().request().url() + "  diff= " + (response.raw().receivedResponseAtMillis() - response.raw().sentRequestAtMillis()));
                if (response.code() == 200) {
                    if (connectionCallBack != null)
                        connectionCallBack.onSuccess(response);
                } else {
                    try {
                        String res = response.errorBody().string();
                        if (connectionCallBack != null)
                            connectionCallBack.onError(response.code(), res);
                    } catch (NullPointerException | IOException e) {
                        Log.i("TAG", "onResponse: " + call.request().url());
                        e.printStackTrace();
                        if (connectionCallBack != null)
                            connectionCallBack.onError(response.code(), e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable error) {
                String message = null;
                if (error instanceof NetworkErrorException) {
                    message = "Please check your internet connection";
                } else if (error instanceof ParseException) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof TimeoutException) {
                    message = "Connection TimeOut! Please check your internet connection.";
                } else if (error instanceof UnknownHostException) {
                    message = "Please check your internet connection and try later";
                } else if (error instanceof Exception) {
                    message = error.getMessage();
                }
                if (connectionCallBack != null)
                    connectionCallBack.onError(-1, message);
            }
        });
    }

    public interface ConnectionCallBack {
        public void onSuccess(Response<ResponseBody> body);

        public void onError(int code, String error);
    }
}
