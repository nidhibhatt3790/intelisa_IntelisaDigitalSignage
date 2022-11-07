package com.example.intelisadigitalsignage.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.intelisadigitalsignage.AppState;
import com.example.intelisadigitalsignage.LiveURL;
import com.example.intelisadigitalsignage.MainActivity;
import com.example.intelisadigitalsignage.R;
import com.example.intelisadigitalsignage.Tools;
import com.example.intelisadigitalsignage.data.Timer;
import com.example.intelisadigitalsignage.managers.SharePreferenceManager;
import com.example.intelisadigitalsignage.utils.RetrofitHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebsiteActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    WebView webview;
    ImageView imgScreenSaver;
    public static String formattedDate;
    public String _id, wesiteLiveUrl, adGroup, ownedby, screensaver, MSG_111, DATE, adDate, newVersion, currentVersion;
    Thread thread;
    private String file_url = "https://intelisaapk.s3.ap-south-1.amazonaws.com/Autoupdater.apk";
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private String flagDownload = "NotComplete";
    private String auto, KIOSK, ID, adName;
    private String DownloadFile;
    private Boolean onResumeFlag = false;
    private ArrayList<LiveURL> urllist;
    private ArrayList<String> list;
    private int i;
    private int arrayIndex = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            new DownloadFileFromURL().execute(file_url);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);


        AppState.sContext = WebsiteActivity.this;

        urllist = new ArrayList<>();

        imgScreenSaver = (ImageView) findViewById(R.id.imgScreenSaver);

        ID = SharePreferenceManager.getInstance().getSharePreference().getString("id", "");
        adGroup = SharePreferenceManager.getInstance().getSharePreference().getString("adGroup", "");
        adName = SharePreferenceManager.getInstance().getSharePreference().getString("adName", "");
        adDate = SharePreferenceManager.getInstance().getSharePreference().getString("adDate", "");

        Log.d("TAG:id is==", adGroup);
        Log.d("TAG:adgroup is==", adGroup);
        Log.d("TAG:adname is==", adName);
        Log.d("TAG:adDate is==", adDate);//2022-10-12T00:00:00.000Z


        Intent intent = getIntent();

        MSG_111 = intent.getStringExtra("MSG_111");
        _id = intent.getStringExtra("_id");
        DATE = intent.getStringExtra("DATE");//FORMATTED DATE
        //adDate = intent.getStringExtra("DATENOFORMAT");

        wesiteLiveUrl = intent.getStringExtra("websiteurl");
        // adGroup = intent.getStringExtra("adGroup");
        //adGroup = getIntent().getStringExtra("adGroup");

        ownedby = intent.getStringExtra("ownedby");
        screensaver = intent.getStringExtra("screensaver");
        //urllist = intent.getStringExtra("urllist");
        if(urllist.size()>0)
        {
            urllist.clear();
        }
        urllist = (ArrayList<LiveURL>) intent.getSerializableExtra("urllist");
        webview = (WebView) findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setBackgroundColor(Color.TRANSPARENT);


        Log.d("WEBSITE LIVE URL==", wesiteLiveUrl);


        KIOSK = SharePreferenceManager.getInstance().getSharePreference().getString("KIOSK", "");

        if (KIOSK != null)

            if (KIOSK.equalsIgnoreCase("1")) {
                WebsiteActivity.this.startLockTask();
            } else {
                WebsiteActivity.this.stopLockTask();

            }

//       NN
        if (this.urllist != null) {

            Log.d("ARRAY OF URL", String.valueOf(this.urllist));


            // getDeviceIP(storedIMEI);

            //webview.loadUrl(urllist.get(i).getAdname());
            Timer timer = new Timer(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {

                    Log.d("TAGintimer::", "" + urllist.size());

                    Log.d("TAG:ARRAYINDEX", "" + arrayIndex);
                    Log.d("TAG:urllist size", "" + urllist.size());
                    list = new ArrayList<String>(3);

                    String dateStr = adDate;
                    Log.d("TAG:", "date before parse" + dateStr);
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//                    df.setTimeZone(TimeZone.getTimeZone("UTC"));
//                    Date date = null;
//
//                    try {
//
//                        Log.d("TAG:","IN TRY");
//                        if (dateStr != null) {
//
//                            Log.d("TAG:","IN TRY IF");
//
//                            date = df.parse(dateStr);
//                            df.setTimeZone(TimeZone.getDefault());
//                            formattedDate = df.format(date);
//
//
//                            Log.d("TAGFORMATTEDTIME==", formattedDate);
//                            // list.add(0, formattedDate);//startTime
//                            //list.add(1, adGroup);
//
//                        }
//
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
                    String DATE_FORMAT_I = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
                    String DATE_FORMAT_O = "yyyy-MM-dd'T'HH:mm a";


                    SimpleDateFormat formatInput = new SimpleDateFormat(DATE_FORMAT_I);
                    SimpleDateFormat formatOutput = new SimpleDateFormat(DATE_FORMAT_O);
                    formatOutput.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date date = null;
                    String dateString = null;
                    try {
                        date = formatInput.parse(adDate);
                        dateString = formatOutput.format(date);
                        Log.d("TAG:::::::", " DATE new format: " + dateString);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Log.d("TAG::ONCREATE","ARRAYINDEX"+arrayIndex);
                    Log.d("TAG::ONCREATE","urllist size"+urllist.size());

                    if (arrayIndex == urllist.size()) {

                        arrayIndex = 1;

                        Log.d("TAG::ONCREATE IF","ARRAYINDEX"+arrayIndex);
                        Log.d("TAG::ONCREATE IF","urllist size"+urllist.size());

                        webview.loadUrl(urllist.get(arrayIndex-1).getAdname());//5-11-22
                        list.add(0, dateString);
                        list.add(1, adGroup);
                        list.add(2, urllist.get(arrayIndex-1).getAdname());//adName
                        Log.d("TAG:ONCREATE IF", "");
                        Log.d("TAG:ID", _id);
                        Log.d("TAG:DATE", DATE);


                        saveAds(_id, DATE, list);


                    }
                    else {
                        Log.d("TAG:LIVE URL IS==", urllist.get(arrayIndex).getAdname());

                        Log.d("TAG::ONCREATE ELSE","ARRAYINDEX"+arrayIndex);
                        Log.d("TAG::ONCREATE ELSE","urllist size"+urllist.size());

                        webview.loadUrl(urllist.get(arrayIndex).getAdname());

                        try {

                            Log.d("TAG:LIST SIZE", "" + list.size());

                            list.add(0, dateString);
                            list.add(1, adGroup);
                            list.add(2, urllist.get(arrayIndex).getAdname());//adName

                            Log.d("TAG:ONCREATE ELSE", "");
                            Log.d("TAG:ONCREATE", "");
                            Log.d("TAG:ONCREATE:ID", _id);
                            Log.d("TAG:ONCREATE:DATE", DATE);

                            saveAds(_id, DATE, list);

                            arrayIndex++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                    // getDeviceIP(storedIMEI);
                    //  Toast.makeText(MainActivity.this, "IN TIMER" + storedIMEI, Toast.LENGTH_LONG).show();


                }
            }, 60000, true);


        } else {


            webview.setVisibility(View.GONE);
            imgScreenSaver.setVisibility(View.VISIBLE);

            if (screensaver.contains("@drawable/img_ss")) {

                Glide.with(this).load(getImage("img_ss")).into(imgScreenSaver);

            } else {

                String path = "https://intelisa.s3.ap-south-1.amazonaws.com/" + ownedby + "/ads/" + screensaver;
                Glide.with(WebsiteActivity.this).load(path).into(imgScreenSaver);

            }


        }

//        // wesiteLiveUrl="";
//        if (wesiteLiveUrl != null) {
//            if (wesiteLiveUrl.contains("https")) {
//                webview.loadUrl(wesiteLiveUrl);
//            } else {
//
//                webview.setVisibility(View.GONE);
//                imgScreenSaver.setVisibility(View.VISIBLE);
//
//
//                if (screensaver.contains("@drawable/img_ss")) {
//                    Glide.with(this).load(getImage("img_ss")).into(imgScreenSaver);
//
//                } else {
//                    String path = "https://intelisa.s3.ap-south-1.amazonaws.com/" + ownedby + "/ads/" + screensaver;
//                    Glide.with(WebsiteActivity.this).load(path).into(imgScreenSaver);
//                }
//
//
//            }
//        }
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(WebsiteActivity.this, MainActivity.class);
        startActivity(i);
    }


    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        return drawableResourceId;
    }

//    private void getData(String _id, String date, String ownedby, String screenSaver) {
//
//        RetrofitHelper retrofitHelper = new RetrofitHelper();
//
//        Call<ResponseBody> call = retrofitHelper.api().get_app_version();
//        retrofitHelper.callApi(call, new RetrofitHelper.ConnectionCallBack() {
//            @Override
//            public void onSuccess(Response<ResponseBody> body) {
//                //Utils.dismissProgress();
//                try {
//                    String response = body.body().string();
//                    Log.i("TAG", "onSuccess WEBSITE: " + response);
//
//                    String newVersion = response;
//
//                    PackageManager manager = WebsiteActivity.this.getPackageManager();
//                    PackageInfo info = manager.getPackageInfo(
//                            WebsiteActivity.this.getPackageName(), 0);
//                    String Currentversion = info.versionName;
//
//                    Double BuildVersion = 0.0;
//
//                    try {
//                        //  BuildVersion = Double.parseDouble(Currentversion);
//
//                        if (BuildVersion < Double.parseDouble(newVersion)) {
//
//                            AlertDialog.Builder builder =
//                                    new AlertDialog.Builder(WebsiteActivity.this);
//                            builder.setMessage(R.string.app_version_dialog)
//                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            // START THE GAME!
//
//
//                                            new DownloadFileFromURL().execute(file_url);
//                                            flagDownload = "updated";
//                                            dialog.dismiss();
//                                            // requestPermission();
//                                        }
//                                    })
//                                    .setNegativeButton(R.
//                                            string.cancel, new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            // User cancelled the dialog
//                                            flagDownload = "notupdated";
//
//                                            dialog.dismiss();
//
//
//                                        }
//                                    });
//                            // Create the AlertDialog object and return it
//                            builder.show();
//                        } else {
//                            Log.d("TAG:", "App new version not found");
//
//                        }
//                        Log.i("TAG", "mynum: " + BuildVersion);
//
//                    } catch (NumberFormatException nfe) {
//                        // Handle parse error.
//                        Log.i("TAG", "BuildVersion error: " + nfe.getMessage());
//
//                    }
//
//
//                } catch (IOException | NullPointerException | JsonSyntaxException e) {
//                    e.printStackTrace();
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(int code, String error) {
//                //Utils.showAlert(activity, error);
//                // Utils.dismissPro1gress();
//                Log.d("Error in api call", error.toString() + code);
//
//                if (code == 404) {
//
//                } else {
//                    Log.d("TAG:", "Ooops something went wrong");
//                }
//            }
//        });
//
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveAds(String _id, String date, List<String> list) {

        Log.d("TAG:LIST time", list.get(0));
        Log.d("TAG:List adname", list.get(1));
        Log.d("TAG:lIST ADGROUP", list.get(2));

        Date currentDate = Calendar.getInstance().getTime();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String time = dateFormat.format(new Date());

        Log.d("TAG::currentTimeData",time);

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = formatter.format(todayDate);


        Log.d("TAG::scheduleDate",todayString);

        RetrofitHelper retrofitHelper = new RetrofitHelper();

        MyRequest request = new MyRequest();

        request.setScreenID(_id);
        request.setScheduleDate(todayString);
        NowPlaying nowPlaying = new NowPlaying();
        nowPlaying.setStarttime(time);
        nowPlaying.setAdName(list.get(2));
        nowPlaying.setAdGroup(list.get(1));
        request.setNowPlaying(nowPlaying);

        Log.d("TAG:REQ PARAM IS::","ID IS::"+request.getScreenID()+"\n"+"sdate:"+request.getScheduleDate()+"\n"+"startTime:"+request.getNowPlaying().getStarttime()+"\n"+"adname:"+request.getNowPlaying().getAdName()+"\n"+"adGroup:"+request.getNowPlaying().getAdGroup());

        Call<MyResponse> call = retrofitHelper.api().postData(request);

        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                Log.d("TAG:SUCCESS", response.toString());
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable throwable) {
                Log.e("TAG:FAILURE", "onFailure: ", throwable);
            }
        });
    }


    public class DownloadFileFromURL extends AsyncTask<String, String, String> {


        /**
         * Before starting background thread Show Progress Bar Dialog
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                File path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS);
                File file = new File(path, "/" + "Autoupdater.apk");
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);


                }


                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {


                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            removeDialog(progress_bar_type);
            if (file_url != null) {
                Toast.makeText(WebsiteActivity.this, "Download error: " + file_url, Toast.LENGTH_LONG).show();
                flagDownload = "NotComplete";
            } else {
                Toast.makeText(WebsiteActivity.this, "File downloaded", Toast.LENGTH_SHORT).show();
                flagDownload = "complete";


            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        AppState.sContext = WebsiteActivity.this;

        urllist = new ArrayList<>();
        Log.d("TAG", "ON RESUME");

        Intent intent = getIntent();

        if(urllist.size()>0) {

            urllist.clear();
        }

        urllist = (ArrayList<LiveURL>) intent.getSerializableExtra("urllist");


        MSG_111 = SharePreferenceManager.getInstance().getSharePreference().getString("MSG_111", "");
        DownloadFile = SharePreferenceManager.getInstance().getSharePreference().getString("DownloadFile", "");
        ID = SharePreferenceManager.getInstance().getSharePreference().getString("id", "");
        adGroup = SharePreferenceManager.getInstance().getSharePreference().getString("adGroup", "");
        adName = SharePreferenceManager.getInstance().getSharePreference().getString("adName", "");
        adDate = SharePreferenceManager.getInstance().getSharePreference().getString("adDate", "");

        _id = intent.getStringExtra("_id");
        DATE = intent.getStringExtra("DATE");//FORMATTED DATE
        //adDate = intent.getStringExtra("DATENOFORMAT");
        wesiteLiveUrl = intent.getStringExtra("websiteurl");
        //adGroup = intent.getStringExtra("adGroup");
        ownedby = intent.getStringExtra("ownedby");
        screensaver = intent.getStringExtra("screensaver");


        Log.d("TAG ON RESUME MSG_111::", MSG_111);
        Log.d("DOWNLOAD FILE===", DownloadFile);


        Log.d("WEBSITE LIVE URL RESUME==", wesiteLiveUrl);

        if (MSG_111.equals("CANCEL") && DownloadFile.equals("CANCEL")) {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(WebsiteActivity.this);
            builder.setMessage((R.string.app_version_dialog))
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // START THE GAME!
                            new DownloadFileFromURL().execute(file_url);

                            // DownloadFile = "OK";
                            SharedPreferences.Editor editor = SharePreferenceManager.getInstance().getEditor();
                            editor.putString("DownloadFile", "OK");
                            editor.commit();

                        }
                    })
                    .setNegativeButton(R.
                            string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.dismiss();
                            DownloadFile = "CANCEL";
                            SharedPreferences.Editor editor = SharePreferenceManager.getInstance().getEditor();
                            editor.putString("DownloadFile", "CANCEL");
                            editor.commit();


                        }
                    });
            // Create the AlertDialog object and return it
            builder.show();
        } else if (MSG_111.equals("CANCEL") && DownloadFile.equals("")) {
            AlertDialog.Builder builder =

                    new AlertDialog.Builder(WebsiteActivity.this);
            builder.setMessage((R.string.app_version_dialog))
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // START THE GAME!
                            new DownloadFileFromURL().execute(file_url);

                            // DownloadFile = "OK";
                            SharedPreferences.Editor editor = SharePreferenceManager.getInstance().getEditor();
                            editor.putString("DownloadFile", "OK");
                            editor.commit();


                        }
                    })
                    .setNegativeButton(R.
                            string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.dismiss();
                            DownloadFile = "CANCEL";
                            SharedPreferences.Editor editor = SharePreferenceManager.getInstance().getEditor();
                            editor.putString("DownloadFile", "CANCEL");
                            editor.commit();


                        }
                    });
            // Create the AlertDialog object and return it
            builder.show();
        }

        if (!onResumeFlag.booleanValue()) {
            // Toast.makeText(WebsiteActivity.this,"ONRESUMEFLAG"+onResumeFlag,Toast.LENGTH_LONG).show();
        }
        onResumeFlag = true;

        if (this.urllist != null) {

            Log.d("ARRAY OF URL", String.valueOf(this.urllist));
            Log.d("LIVE URL IS RESUME==", this.urllist.get(i).getAdname());


            Timer timer = new Timer(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {

                    list = new ArrayList<String>(3);
                    String dateStr = adDate;
                    Log.d("TAG:RESUME", "date before parse" + dateStr);

                    String DATE_FORMAT_I = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
                    String DATE_FORMAT_O = "yyyy-MM-dd'T'HH:mm a";


                    SimpleDateFormat formatInput = new SimpleDateFormat(DATE_FORMAT_I);
                    SimpleDateFormat formatOutput = new SimpleDateFormat(DATE_FORMAT_O);
                    formatOutput.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date date = null;
                    String dateString = null;
                    try {
                        date = formatInput.parse(adDate);
                        dateString = formatOutput.format(date);
                        Log.d("TAG:::::::", " DATE new format: " + dateString);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


//                    Log.d("TAG::ONRESUME ","ARRAYINDEX"+arrayIndex);
//                    Log.d("TAG::ONRESUME ","urllist size"+urllist.size());
//
//                    if (arrayIndex == urllist.size()) {
//
//                        Log.d("TAG::ONRESUME IF","ARRAYINDEX"+arrayIndex);
//                        Log.d("TAG::ONRESUME IF","urllist size"+urllist.size());
//
//                        webview.loadUrl(urllist.get(arrayIndex - 1).getAdname());
//                        list.add(0, dateString);
//                        list.add(1, adGroup);
//                        list.add(2, wesiteLiveUrl);//adName
//                        Log.d("TAG:ON RESUME IF", "");
//                        Log.d("TAG:ID", _id);
//                        Log.d("TAG:DATE", DATE);
//
//                        saveAds(_id, DATE, list);
//                        arrayIndex = 1;
//
//                    }
//                    else {
//
//                        Log.d("TAG::ONRESUME ELSE","ARRAYINDEX"+arrayIndex);
//                        Log.d("TAG::ONRESUME ELSE","urllist size"+urllist.size());
//
//                        webview.loadUrl(urllist.get(arrayIndex).getAdname());
//                        list.add(0, dateString);
//                        list.add(1, adGroup);
//                        list.add(2, wesiteLiveUrl);//adName
//                        Log.d("TAG:ONRESUME ELSE", "");
//                        Log.d("TAG:ID", _id);
//                        Log.d("TAG:DATE", DATE);
//
//                        saveAds(_id, DATE, list);
//                        arrayIndex++;
//                    }
//                    Log.d("TAG:ONRESUME", "");
//                    Log.d("TAG:ONCREATE:ID", _id);
//                    Log.d("TAG:ONCREATE:DATE", DATE);


                }

            }, 60000, true);


            // getDeviceIP(storedIMEI);


        } else {
            webview.setVisibility(View.GONE);
            imgScreenSaver.setVisibility(View.VISIBLE);


            if (screensaver.contains("@drawable/img_ss")) {
                Glide.with(this).load(getImage("img_ss")).into(imgScreenSaver);


            } else {
                String path = "https://intelisa.s3.ap-south-1.amazonaws.com/" + ownedby + "/ads/" + screensaver;
                Glide.with(WebsiteActivity.this).load(path).into(imgScreenSaver);
            }


        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG", "ON START");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("TAG", "ON RESTART");

        AppState.sContext = WebsiteActivity.this;

        Intent intent = getIntent();
        onResumeFlag = true;

        Log.d("TAG ON RESUME MSG_111::", MSG_111);
        Log.d("DOWNLOAD FILE===", DownloadFile);


    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {

            new DownloadFileFromURL().execute(file_url);
        }
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }


}




