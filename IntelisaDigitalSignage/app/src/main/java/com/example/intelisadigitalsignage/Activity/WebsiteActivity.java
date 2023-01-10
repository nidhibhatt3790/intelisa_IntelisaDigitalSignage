package com.example.intelisadigitalsignage.Activity;

import static com.example.intelisadigitalsignage.MainActivity.urlList;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.UserManager;
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
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.intelisadigitalsignage.AppState;
import com.example.intelisadigitalsignage.DeviceAdmiReceiver;
import com.example.intelisadigitalsignage.LiveURL;
import com.example.intelisadigitalsignage.MainActivity;
import com.example.intelisadigitalsignage.MyDeviceAdminReceiver;
import com.example.intelisadigitalsignage.R;
import com.example.intelisadigitalsignage.data.Timer;
import com.example.intelisadigitalsignage.managers.SharePreferenceManager;
import com.example.intelisadigitalsignage.utils.RetrofitHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WebsiteActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_CODE_MANAGE_DEVICE_ADMINS = 999;
    WebView webview;
    ImageView imgScreenSaver;
    public static String formattedDate;
    public String _id, wesiteLiveUrl, adGroup, ownedby, screensaver, MSG_111, DATE, adDate, newVersion, currentVersion, kioskVal;
    Thread thread;
    private String file_url = "https://intelisaapk.s3.ap-south-1.amazonaws.com/Autoupdater.apk";
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private String flagDownload = "NotComplete";
    private String auto, KIOSK, ID, adName;
    private String DownloadFile;
    private Boolean onResumeFlag = false;
    private ArrayList<String> list;
    public boolean kioskdata;
    public static ArrayList<LiveURL> newURLlist = new ArrayList<LiveURL>();

    private int i;
    private int arrayIndex = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            new DownloadFileFromURL().execute(file_url);
        }

        if (requestCode == REQUEST_CODE_MANAGE_DEVICE_ADMINS) {
            // Check if the permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                // Do something here
            } else {
                // Permission was denied
                // Do something here
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        Log.d("ACTIVITY:", "oncreate");

        AppState.sContext = WebsiteActivity.this;

        setImmersive();


        imgScreenSaver = (ImageView) findViewById(R.id.imgScreenSaver);

        ID = SharePreferenceManager.getInstance().getSharePreference().getString("id", "");
        adGroup = SharePreferenceManager.getInstance().getSharePreference().getString("adGroup", "");
        adName = SharePreferenceManager.getInstance().getSharePreference().getString("adName", "");
        adDate = SharePreferenceManager.getInstance().getSharePreference().getString("adDate", "");

        Log.d("TAG:id is==", adGroup);
        Log.d("TAG:adgroup is==", adGroup);
        Log.d("TAG:adname is==", adName);
        Log.d("TAG:adDate is==", adDate);//2022-10-12T00:00:00.000Z



        KIOSK = SharePreferenceManager.getInstance().getSharePreference().getString("KIOSK", "");

        Log.d("WEB:kiosk", KIOSK);

        if (KIOSK != null)

            //KIOSK 1 = onJ

            if (KIOSK.equalsIgnoreCase("1") && (kioskdata == false)) {

                Log.d("TAG:","KIOSK IS 1");

                DevicePolicyManager dpm = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName deviceAdmin = new ComponentName(this, DeviceAdmiReceiver.class);


                if (dpm.isAdminActive(deviceAdmin)) {
                    // Device adm
                    //
                    //
                    //
                    // in is already active, skip activation.
                } else {
                    // Request device admin privileges.
                    Intent deviceAdminIntent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    deviceAdminIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin);
                    deviceAdminIntent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Device owner privileges are needed to perform certain operations.");
                    startActivityForResult(deviceAdminIntent, 999);
                    WebsiteActivity.this.startLockTask();

                }

                //dpm.addUserRestriction(deviceAdmin, UserManager.DISALLOW_MODIFY_ACCOUNTS);
                //dpm.addUserRestriction(deviceAdmin, UserManager.DISALLOW_CONFIG_TETHERING);
                // dpm.setLockTaskPackages(deviceAdmin, new String[]{getPackageName()});

                kioskdata = true;

            } else {

                Log.d("TAG:","KIOSK IS 0");

                WebsiteActivity.this.stopLockTask();
                kioskdata = false;


            }



        Intent intent = getIntent();

        MSG_111 = intent.getStringExtra("MSG_111");
        _id = intent.getStringExtra("_id");
        DATE = intent.getStringExtra("DATE");//FORMATTED DATE
        wesiteLiveUrl = intent.getStringExtra("websiteurl");
        ownedby = intent.getStringExtra("ownedby");
        screensaver = intent.getStringExtra("screensaver");

        //urllist = (ArrayList<LiveURL>) getIntent().getSerializableExtra("urllist");


        webview = (WebView) findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setBackgroundColor(Color.WHITE);


        Log.d("WEBSITE LIVE URL==", wesiteLiveUrl);

        Timer timer = new Timer(new Runnable() {
            @Override
            public void run() {

                // getDeviceIP(storedIMEI);
                //  Toast.makeText(MainActivity.this, "IN TIMER" + storedIMEI, Toast.LENGTH_LONG).show();

                getScheduler(_id, DATE, ownedby, screensaver, MSG_111, "", "");


            }
        }, 10000, true);


//       NN


    }


    private void setImmersive() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onBackPressed() {

//        Intent i = new Intent(WebsiteActivity.this, MainActivity.class);
//        startActivity(i);
    }


    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        return drawableResourceId;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveAds(String _id, String date, List<String> list) {

//        Log.d("TAG:LIST time", list.get(0));
//        Log.d("TAG:List adname", list.get(1));
//        Log.d("TAG:lIST ADGROUP", list.get(2));

        Date currentDate = Calendar.getInstance().getTime();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String time = dateFormat.format(new Date());

        Log.d("TAG::currentTimeData", time);

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = formatter.format(todayDate);


        Log.d("TAG::scheduleDate", todayString);

        RetrofitHelper retrofitHelper = new RetrofitHelper();

        MyRequest request = new MyRequest();

        request.setScreenID(_id);
        request.setScheduleDate(todayString);
        NowPlaying nowPlaying = new NowPlaying();
        nowPlaying.setStarttime(time);
        nowPlaying.setAdName(list.get(2));
        nowPlaying.setAdGroup(list.get(1));
        request.setNowPlaying(nowPlaying);

        Log.d("TAG:REQ PARAM IS::", "ID IS::" + request.getScreenID() + "\n" + "sdate:" + request.getScheduleDate() + "\n" + "startTime:" + request.getNowPlaying().getStarttime() + "\n" + "adname:" + request.getNowPlaying().getAdName() + "\n" + "adGroup:" + request.getNowPlaying().getAdGroup());

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
//    public class loadWebsiteView extends AsyncTask<String, String, String> {
//
//
//        /**
//         * Before starting background thread Show Progress Bar Dialog
//         */
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //showDialog(progress_bar_type);
//            imgScreenSaver.setVisibility(View.VISIBLE);
//        }
//
//        /**
//         * Downloading file in background thread
//         */
//        @Override
//        protected String doInBackground(String... f_url) {
//
//           return null;
//        }
//
//        /**
//         * Updating progress bar
//         */
//        protected void onProgressUpdate(String... progress) {
//            // setting progress percentage
//            //pDialog.setProgress(Integer.parseInt(progress[0]));
//
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog
//         **/
//        @Override
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog after the file was downloaded
//            //removeDialog(progress_bar_type);
//            imgScreenSaver.setVisibility(View.GONE);
//
//        }
//
//    }


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

        KIOSK = SharePreferenceManager.getInstance().getSharePreference().getString("KIOSK", "");

        Log.d("WEB:kiosk", KIOSK);

        if (KIOSK != null)

            //KIOSK 1 = onJ

            if (KIOSK.equalsIgnoreCase("1") && (kioskdata == false)) {

                Log.d("TAG:","KIOSK IS 1");

                DevicePolicyManager dpm = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName deviceAdmin = new ComponentName(this, DeviceAdmiReceiver.class);


                if (dpm.isAdminActive(deviceAdmin)) {
                    // Device adm
                    //
                    //
                    //
                    // in is already active, skip activation.
                } else {
                    // Request device admin privileges.
                    Intent deviceAdminIntent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    deviceAdminIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin);
                    deviceAdminIntent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Device owner privileges are needed to perform certain operations.");
                    startActivityForResult(deviceAdminIntent, 999);
                    WebsiteActivity.this.startLockTask();

                }

                //dpm.addUserRestriction(deviceAdmin, UserManager.DISALLOW_MODIFY_ACCOUNTS);
                //dpm.addUserRestriction(deviceAdmin, UserManager.DISALLOW_CONFIG_TETHERING);
                // dpm.setLockTaskPackages(deviceAdmin, new String[]{getPackageName()});

                kioskdata = true;

            } else {

                Log.d("TAG:","KIOSK IS 0");

                WebsiteActivity.this.stopLockTask();
                kioskdata = false;


            }

        //urllist = new ArrayList<>();
        Log.d("ACTIVITY", "ON RESUME");


        Intent intent = getIntent();

//        if(urllist.size()>0) {
//
//            urllist.clear();
//        }

        //urllist = (ArrayList<LiveURL>) getIntent().getSerializableExtra("urllist");


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

//        if (urlList != null) {
//
////            Log.d("ARRAY OF URL", String.valueOf(MainActivity.urlList));
////            Log.d("LIVE URL IS RESUME==", MainActivity.urlList.get(i).getAdname());
////
//
//            Timer timer = new Timer(new Runnable() {
//                @RequiresApi(api = Build.VERSION_CODES.N)
//                @Override
//                public void run() {
//
//                    list = new ArrayList<String>(3);
//                    String dateStr = adDate;
//                    Log.d("TAG:RESUME", "date before parse" + dateStr);
//
//                    String DATE_FORMAT_I = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
//                    String DATE_FORMAT_O = "yyyy-MM-dd'T'HH:mm a";
//
//
//                    SimpleDateFormat formatInput = new SimpleDateFormat(DATE_FORMAT_I);
//                    SimpleDateFormat formatOutput = new SimpleDateFormat(DATE_FORMAT_O);
//                    formatOutput.setTimeZone(TimeZone.getTimeZone("UTC"));
//                    Date date = null;
//                    String dateString = null;
//                    try {
//                        if (adDate != null || adDate != "") {
//                            date = formatInput.parse(adDate);
//                            dateString = formatOutput.format(date);
//                            Log.d("TAG:::::::", " DATE new format: " + dateString);
//                        }
//
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//
//            }, 60000, true);
//
//
//            // getDeviceIP(storedIMEI);
//
//
//        }
//        else {
//            webview.setVisibility(View.GONE);
//            imgScreenSaver.setVisibility(View.VISIBLE);
//
//
//            if (screensaver.contains("@drawable/img_ss")) {
//                Glide.with(this).load(getImage("img_ss")).into(imgScreenSaver);
//
//
//            } else {
//                String path = "https://intelisa.s3.ap-south-1.amazonaws.com/" + ownedby + "/ads/" + screensaver;
//                Glide.with(WebsiteActivity.this).load(path).into(imgScreenSaver);
//            }
//
//
//        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ACTIVITY:", "ON START");


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("TAG", "ON RESTART");

        AppState.sContext = WebsiteActivity.this;

        //Intent intent = getIntent();
        // onResumeFlag = true;

        // Log.d("TAG ON RESUME MSG_111::", MSG_111);
        //  Log.d("DOWNLOAD FILE===", DownloadFile);


    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("ACTIVITY:", "onPause");


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


    public void getScheduler(String _id, String date, String ownedby, String screenSaver, String msg, String AppNewVersion, String AppcurrentVersion) {

        RetrofitHelper retrofitHelper = new RetrofitHelper();

        Call<ResponseBody> call = retrofitHelper.api().get_scheduler(_id, date);
        retrofitHelper.callApi(call, new RetrofitHelper.ConnectionCallBack() {
            @Override
            public void onSuccess(Response<ResponseBody> body) {
                //Utils.dismissProgress();
                try {
                    String response = body.body().string();
                    String adName, adDate, todayDate;
                    String adGroupApi = "";

                    Log.d("TAG:WEB:SCHEDULER", "onSuccess scheduler: " + response);

                    JSONObject json = new JSONObject(response);
                    JSONObject schedule = json.getJSONObject("schedule");
                    adDate = json.getString("scheduleDate");

                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = inputFormat.parse(adDate);
                    String formattedDate = outputFormat.format(date);

                    Log.d("TAG", " DATE: " + formattedDate);


                    JSONArray jsonArray = schedule.getJSONArray("roundRobin");

                    if (newURLlist.size() > 0) {
                        newURLlist.clear();
                    }

                    if (jsonArray.length() > 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject roundRobin = jsonArray.getJSONObject(i);
                            //Log.d("TAG", " roundRobin: " + roundRobin);

                            String iteration = roundRobin.getString("iteration");
                            adGroupApi = roundRobin.getString("adGroup");
                            adName = roundRobin.getString("adName");


                            Log.d("TAG:IN WEB ACTIVITY:ADGROUP", adGroup);

                            adName = roundRobin.getString("adName");


                            if (adName.contains("https")) {
                                newURLlist.add(new LiveURL(iteration, adName));
                            }

                            Log.d("WEB:URLLIST:AFTER SCHDULER", "" + newURLlist.size());

                        }
                        if (newURLlist != null) {

                            if (newURLlist.size() > 0 || (!(newURLlist.isEmpty()))) {

                                Log.d("TAG:ARRAY OF URL", String.valueOf(urlList));


                                // getDeviceIP(storedIMEI);

                                //webview.loadUrl(urllist.get(i).getAdname());
                                String finalAdGroupApi = adGroupApi;
                                Timer timer1 = new Timer(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void run() {


                                        Log.d("TAGintimer::", "" + newURLlist);

                                        Log.d("TAG:ARRAYINDEX", "" + arrayIndex);
                                        //Log.d("TAG:urllist size", "" + urllist.size());
                                        list = new ArrayList<String>(3);

                                        String dateStr = adDate;
                                        Log.d("TAG:", "date before parse" + dateStr);

                                        String DATE_FORMAT_I = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
                                        String DATE_FORMAT_O = "yyyy-MM-dd'T'HH:mm a";


                                        SimpleDateFormat formatInput = new SimpleDateFormat(DATE_FORMAT_I);
                                        SimpleDateFormat formatOutput = new SimpleDateFormat(DATE_FORMAT_O);
                                        formatOutput.setTimeZone(TimeZone.getTimeZone("UTC"));
                                        Date date = null;
                                        String dateString = null;

                                        try {
                                            if (adDate != null || adDate != "") {
                                                date = formatInput.parse(adDate);
                                                dateString = formatOutput.format(date);
                                                Log.d("TAG:::::::", " DATE new format: " + dateString);

                                            }

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        Log.d("TAG::ONCREATE", "ARRAYINDEX" + arrayIndex);

                                        if (newURLlist != null) {
                                            if (newURLlist.size() > 0 || (!newURLlist.isEmpty())) {

                                                if (newURLlist.size() == 1) {

                                                    Log.d("TAG::arrayindex 1", ":)");
                                                    webview.loadUrl(newURLlist.get(0).getAdname());//5-11-22
                                                    list.add(0, dateString);
                                                    list.add(1, finalAdGroupApi);
                                                    list.add(2, newURLlist.get(0).getAdname());//adName

                                                    saveAds(_id, DATE, list);

                                                    arrayIndex = 0;
                                                }


                                                if (arrayIndex == newURLlist.size()) {

                                                    arrayIndex = 0;

                                                    webview.loadUrl(newURLlist.get(arrayIndex).getAdname());
                                                    list.add(0, dateString);
                                                    list.add(1, finalAdGroupApi);
                                                    list.add(2, newURLlist.get(arrayIndex).getAdname());//adName

                                                    saveAds(_id, DATE, list);

                                                    arrayIndex++;

                                                } else {

                                                    Log.d("TAG::arrayindex else", ":)");
                                                    webview.loadUrl(newURLlist.get(arrayIndex).getAdname());//5-11-22
                                                    list.add(0, dateString);
                                                    list.add(1, finalAdGroupApi);
                                                    list.add(2, newURLlist.get(arrayIndex).getAdname());//adName

                                                    saveAds(_id, DATE, list);

                                                    arrayIndex++;


                                                }
                                            }
                                        }


                                        // getDeviceIP(storedIMEI);
                                        //  Toast.makeText(MainActivity.this, "IN TIMER" + storedIMEI, Toast.LENGTH_LONG).show();


                                    }
                                }, 30000, true);
                            } else {


                                webview.setVisibility(View.GONE);
                                imgScreenSaver.setVisibility(View.VISIBLE);

                                if (screensaver.contains("@drawable/img_ss")) {

                                    Glide.with(WebsiteActivity.this).load(getImage("img_ss")).into(imgScreenSaver);

                                } else {

                                    String path = "https://intelisa.s3.ap-south-1.amazonaws.com/" + ownedby + "/ads/" + screensaver;
                                    Glide.with(WebsiteActivity.this).load(path).into(imgScreenSaver);

                                }


                            }

                        } else {


                            webview.setVisibility(View.GONE);
                            imgScreenSaver.setVisibility(View.VISIBLE);

                            if (screensaver.contains("@drawable/img_ss")) {

                                Glide.with(WebsiteActivity.this).load(getImage("img_ss")).into(imgScreenSaver);

                            } else {

                                String path = "https://intelisa.s3.ap-south-1.amazonaws.com/" + ownedby + "/ads/" + screensaver;
                                Glide.with(WebsiteActivity.this).load(path).into(imgScreenSaver);

                            }


                        }

                    } else {
                        // Toast.makeText(MainActivity.this, "ROUND ROBIN BLANK" + file_url, Toast.LENGTH_LONG).show();
                    }

                } catch (IOException | JSONException | ParseException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                Log.d("TAG", "onError scheduler: " + error.toString());

            }
        });


    }


}




