package com.example.intelisadigitalsignage;

import static com.example.intelisadigitalsignage.Constants.KEY_ALWAYS_ON;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intelisadigitalsignage.Activity.WebsiteActivity;
import com.example.intelisadigitalsignage.data.Timer;
import com.example.intelisadigitalsignage.managers.SharePreferenceManager;
import com.example.intelisadigitalsignage.utils.RetrofitHelper;
import com.google.gson.JsonSyntaxException;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvSerialNum;
    ProgressDialog dialog = null;
    long total = 0;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    public String msg;
    //public int flagAlwayson = 1;

    // File url to download
    private static String file_url = "https://intelisaapk.s3.ap-south-1.amazonaws.com/Autoupdater.apk";

    int length;
    long total_tmp = 0;
    public static TextView tvData;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    ArrayList<LiveURL> urlList = new ArrayList<LiveURL>();
    public String MSG_111, website, storedIMEI, _id, date, ownedBy, deviceIMEI, SCREENSAVER, GeneraredCode, ID, DATE, OWNEDBY, kiosk;
    private Switch alwaysOnSwitch;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppState.sContext = MainActivity.this;

        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.d("TAG CURRENT DATE IS", currentDate);

        Date currentTime = Calendar.getInstance().getTime();
        Log.d("TAG::currentTime", currentTime.toString());


        Log.d("TAG", "ONCREATE");
        tvSerialNum = (TextView) findViewById(R.id.tv_imeinum);


        GeneraredCode = SharePreferenceManager.getInstance().getSharePreference().getString("GeneratedCode", "");
        storedIMEI = SharePreferenceManager.getInstance().getSharePreference().getString("SERIALNUMBER", "");
        ID = SharePreferenceManager.getInstance().getSharePreference().getString("ID", "");
        DATE = SharePreferenceManager.getInstance().getSharePreference().getString("DATE", "");
        OWNEDBY = SharePreferenceManager.getInstance().getSharePreference().getString("OWNEDBY", "");
        SCREENSAVER = SharePreferenceManager.getInstance().getSharePreference().getString("SCREENSAVER", "");


        Log.d("GENERATED CODE Before==", GeneraredCode);
        Log.d("STORED IMEI ON CREATE==", storedIMEI);


        if (storedIMEI != "") {


            tvSerialNum.setText("Verification code for setup \n " + storedIMEI);

            Timer timer = new Timer(new Runnable() {
                @Override
                public void run() {


                    getScheduler(ID, DATE, OWNEDBY, SCREENSAVER, msg, "", "");


                }
            }, 60000, true);
        } else {
            if (GeneraredCode != "") {
                tvSerialNum.setText("Verification code for setup \n" + GeneraredCode);
                Timer timer = new Timer(new Runnable() {
                    @Override
                    public void run() {

                        getDeviceIP(GeneraredCode);


                    }
                }, 60000, true);

            } else {
                getSerialNumber();
            }
        }

        GeneraredCode = SharePreferenceManager.getInstance().getSharePreference().getString("GeneratedCode", "");

        Log.d("GENERATED CODE ==", GeneraredCode);


    }

    void saveSerialNumber(String serialnum, String id, String date, String ownedby, String screenSaver, String kiosk) {

        SharedPreferences.Editor editor = SharePreferenceManager.getInstance().getEditor();
        editor.putString("SERIALNUMBER", serialnum);
        editor.putString("ID", id);
        editor.putString("DATE", date);
        editor.putString("OWNEDBY", ownedby);
        editor.putString("SCREENSAVER", screenSaver);
        editor.putString("KIOSK", kiosk);

        editor.commit();

    }

    void clearPreference() {
        SharedPreferences preferences = getSharedPreferences("GetSerialNum", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }


    private void getSerialNumber() {
        //Utils.showProgress(activity);
        RetrofitHelper retrofitHelper = new RetrofitHelper();

        Call<ResponseBody> call = retrofitHelper.api().get_unique_key();
        retrofitHelper.callApi(call, new RetrofitHelper.ConnectionCallBack() {
            @Override
            public void onSuccess(Response<ResponseBody> body) {
                //Utils.dismissProgress();
                try {
                    String response = body.body().string();
                    Log.i("TAG", "onSuccess SERIAL NUMBER: " + response);

                    SharedPreferences.Editor editor = SharePreferenceManager.getInstance().getEditor();
                    editor.putString("GeneratedCode", response);
                    editor.commit();

                    tvSerialNum.setText("Verification code for setup \n " + response);


                    Timer timer = new Timer(new Runnable() {
                        @Override
                        public void run() {

                            getDeviceIP(response);


                        }
                    }, 60000, true);


                } catch (IOException | NullPointerException | JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String error) {
                //Utils.showAlert(activity, error);
                // Utils.dismissPro1gress();
                Log.d("Error in api call get serial num", error);
            }
        });
    }

    private void getDeviceIP(String resnum) {

        RetrofitHelper retrofitHelper = new RetrofitHelper();

        Call<ResponseBody> call = retrofitHelper.api().get_deviceIP(resnum);
        retrofitHelper.callApi(call, new RetrofitHelper.ConnectionCallBack() {
            @Override
            public void onSuccess(Response<ResponseBody> body) {
                //Utils.dismissProgress();
                try {
                    String response = body.body().string();

                    if (response.equals("")) {
                        Log.d("TAG", "EMPTY RESP");
                    }
                    Log.i("TAG", "onSuccess: " + response);

                    JSONObject json = new JSONObject(response);
                    _id = json.getString("_id");
                    date = json.getString("scheduleChangeDate");
                    ownedBy = json.getString("ownedBy");
                    deviceIMEI = json.getString("deviceIMEI");
                    kiosk = json.getString("kiosk");

                    Log.d("TAGKIOSKVALIS:", kiosk);

                    if (json.has("screenSaver")) {
                        String screenSaver = json.getString("screenSaver");
                        Log.d("SCREENSAVER::", screenSaver);
                        getAppVersion(_id, date, ownedBy, screenSaver);
                        saveSerialNumber(deviceIMEI, _id, date, ownedBy, screenSaver, kiosk);


                    } else {
                        String screenSaver = "@drawable/img_ss";
                        getAppVersion(_id, date, ownedBy, screenSaver);
                        saveSerialNumber(deviceIMEI, _id, date, ownedBy, screenSaver, kiosk);

                    }


                } catch (IOException | NullPointerException | JsonSyntaxException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String error) {
                //Utils.showAlert(activity, error);
                // Utils.dismissPro1gress();
                Log.d("Error in api call DEVICE IP", error.toString() + code);
            }
        });
    }

    private void getAppVersion(String _id, String date, String ownedby, String screenSaver) {

        RetrofitHelper retrofitHelper = new RetrofitHelper();

        Call<ResponseBody> call = retrofitHelper.api().get_app_version();
        retrofitHelper.callApi(call, new RetrofitHelper.ConnectionCallBack() {
            @Override
            public void onSuccess(Response<ResponseBody> body) {
                //Utils.dismissProgress();
                try {
                    String response = body.body().string();
                    Log.i("TAG", "onSuccess: " + response);

                    String newVersion = response;

                    PackageManager manager = MainActivity.this.getPackageManager();
                    PackageInfo info = manager.getPackageInfo(
                            MainActivity.this.getPackageName(), 0);
                    String Currentversion = info.versionName;

                    Double BuildVersion = 0.0;

                    try {
                        BuildVersion = Double.parseDouble(Currentversion);

                        Log.d("TAG" + " BuildVersion is==", "" + BuildVersion);
                        Log.d("TAG" + "currentversion is==", "" + Currentversion);

                        if (BuildVersion < Double.parseDouble(newVersion)) {

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage((R.string.app_version_dialog))
                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // START THE GAME!
                                            new DownloadFileFromURL().execute(file_url);
                                            SharedPreferences.Editor editor = SharePreferenceManager.getInstance().getEditor();
                                            editor.putString("MSG_111", "OK");
                                            editor.commit();

                                            getScheduler(_id, date, ownedby, screenSaver, "OK", newVersion, Currentversion);


                                        }
                                    })
                                    .setNegativeButton(R.
                                            string.cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog

                                            SharedPreferences.Editor editor = SharePreferenceManager.getInstance().getEditor();
                                            editor.putString("MSG_111", "CANCEL");
                                            editor.commit();

                                            getScheduler(_id, date, ownedby, screenSaver, "CANCEL", newVersion, Currentversion);

                                        }
                                    });
                            // Create the AlertDialog object and return it
                            builder.show();
                        } else {
                            Log.d("TAG:", "App new version not found");
                            getScheduler(_id, date, ownedby, screenSaver, "", "", "");

                        }
                        Log.i("TAG", "mynum: " + BuildVersion);

                    } catch (NumberFormatException nfe) {
                        // Handle parse error.
                        Log.i("TAG", "BuildVersion error: " + nfe.getMessage());

                    }


                } catch (IOException | NullPointerException | JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String error) {
                //Utils.showAlert(activity, error);
                // Utils.dismissPro1gress();
                Log.d("Error in api call get app version", error.toString() + code);

                if (code == 404) {

                } else {
                    Log.d("TAG:", "Ooops something went wrong");
                }
            }
        });

    }

    private void getScheduler(String _id, String date, String ownedby, String screenSaver, String msg, String AppNewVersion, String AppcurrentVersion) {

        RetrofitHelper retrofitHelper = new RetrofitHelper();

        Call<ResponseBody> call = retrofitHelper.api().get_scheduler(_id, date);
        retrofitHelper.callApi(call, new RetrofitHelper.ConnectionCallBack() {
            @Override
            public void onSuccess(Response<ResponseBody> body) {
                //Utils.dismissProgress();
                try {
                    String response = body.body().string();
                    String adName, adDate, todayDate, adGroup;

                    Log.d("TAGSCHEDULER", "onSuccess scheduler: " + response);

                    JSONObject json = new JSONObject(response);
                    JSONObject schedule = json.getJSONObject("schedule");
                    adDate = json.getString("scheduleDate");

                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = inputFormat.parse(adDate);
                    String formattedDate = outputFormat.format(date);

                    Log.d("TAG", " DATE: " + formattedDate);
                    if (currentDate.equalsIgnoreCase(formattedDate)) {
                        todayDate = adDate;
                    } else {
                        todayDate = currentDate;
                    }

                    JSONArray jsonArray = schedule.getJSONArray("roundRobin");

                    if (jsonArray.length() > 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject roundRobin = jsonArray.getJSONObject(i);
                            //Log.d("TAG", " roundRobin: " + roundRobin);

                            String iteration = roundRobin.getString("iteration");
                            adGroup = roundRobin.getString("adGroup");
                            Log.d("TAG:IN MAIN ACTIVITY:ADGROUP", adGroup);

                            adName = roundRobin.getString("adName");

                            SharedPreferences.Editor editor1 = SharePreferenceManager.getInstance().getEditor();
                            editor1.putString("id", _id);
                            editor1.putString("adName", adName);
                            editor1.putString("adGroup", adGroup);
                            editor1.putString("adDate", adDate);
                            editor1.commit();

                            if (adName != null) {

                                SharedPreferences sharedPref = getSharedPreferences("GetSerialNum", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("website", adName);
                                editor.commit();

                            }

                            if (adName.contains("https")) {

                                if (adName.contains(".mp3") || adName.contains("www.youtube.com")) {
                                    Log.d("TAG:not a live url",adName);
                                } else {
                                    urlList.add(new LiveURL(iteration, adName));
                                }
                            }


                            Intent intent = new Intent(MainActivity.this, WebsiteActivity.class);
                            intent.putExtra("MSG_111", msg);
                            intent.putExtra("_id", _id);
                            intent.putExtra("websiteurl", adName);
                            intent.putExtra("adGroup", adGroup);
                            intent.putExtra("DATE", todayDate);
                            intent.putExtra("DATENOFORMAT", adDate);
                            intent.putExtra("ownedby", ownedby);
                            intent.putExtra("screensaver", screenSaver);
                            intent.putExtra("newVersion", AppNewVersion);
                            intent.putExtra("currentVersion", AppcurrentVersion);
                            //intent.putStringArrayListExtra("urllist", urlList);
                            intent.putExtra("urllist", urlList);
                            startActivity(intent);

                        }


                    } else {
                        // Toast.makeText(MainActivity.this, "ROUND ROBIN BLANK" + file_url, Toast.LENGTH_LONG).show();
                        String screenSaver = "@drawable/img_ss";
                        Intent intent = new Intent(MainActivity.this, WebsiteActivity.class);
                        intent.putExtra("MSG_111", msg);
                        intent.putExtra("_id", _id);
                        intent.putExtra("websiteurl", "");
                        intent.putExtra("DATE", date);
                        intent.putExtra("ownedby", ownedby);
                        intent.putExtra("screensaver", screenSaver);
                        intent.putExtra("newVersion", AppNewVersion);
                        intent.putExtra("currentVersion", AppcurrentVersion);

                        startActivity(intent);
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG", "ON START");


    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.d("TAG", "ON RESUME");


        GeneraredCode = SharePreferenceManager.getInstance().getSharePreference().getString("GeneratedCode", "");
        storedIMEI = SharePreferenceManager.getInstance().getSharePreference().getString("SERIALNUMBER", "");
        website = SharePreferenceManager.getInstance().getSharePreference().getString("website", "");
        msg = SharePreferenceManager.getInstance().getSharePreference().getString("MSG_111", "");
        _id = SharePreferenceManager.getInstance().getSharePreference().getString("ID", "");
        date = SharePreferenceManager.getInstance().getSharePreference().getString("DATE", "");
        ownedBy = SharePreferenceManager.getInstance().getSharePreference().getString("OWNEDBY", "");
        SCREENSAVER = SharePreferenceManager.getInstance().getSharePreference().getString("SCREENSAVER", "");
        //SharedPreferences sharedPref = getSharedPreferences("GetSerialNum", Context.MODE_PRIVATE);
        //website = sharedPref.getString("website", "");
        // msg = sharedPref.getString("MSG_111", "");
        //storedIMEI = sharedPref.getString("SERIALNUMBER", "");
        Log.d("TAG", "RESUME storedIMEI" + storedIMEI);
        Log.d("TAG", "RESUME GENERATED CODE" + GeneraredCode);


        if (storedIMEI != "") {


            Intent intent = new Intent(this, WebsiteActivity.class);
            intent.putExtra("MSG_111", msg);
            intent.putExtra("_id", _id);
            intent.putExtra("websiteurl", website);
            intent.putExtra("DATE", date);
            intent.putExtra("ownedby", ownedBy);
            intent.putExtra("screensaver", SCREENSAVER);
            intent.putExtra("newVersion", "");
            intent.putExtra("currentVersion", "");
            // intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);

        } else {
            Log.d("TAG", "ON RESUME IMEI" + storedIMEI);
            tvSerialNum.setText("Verification code for setup \n" + GeneraredCode);

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("TAG", "ON RESTART");

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG", "ON PAUSE MAIN ACTIVITY");


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
                Toast.makeText(MainActivity.this, "Download error: " + file_url, Toast.LENGTH_LONG).show();
                //   = "NotComplete";
            } else {
                Toast.makeText(MainActivity.this, "File downloaded", Toast.LENGTH_SHORT).show();
                // = "complete";


            }
        }

    }


}

