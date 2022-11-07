package com.example.intelisadigitalsignage;


import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools  {
    //private String DownloadFile;
    //private static String file_url = "https://intelisaapk.s3.ap-south-1.amazonaws.com/Autoupdater.apk";

//    public void setDialog(Activity activity)
//    {
//        AlertDialog.Builder builder =
//                new AlertDialog.Builder(activity);
//        builder.setMessage((R.string.app_version_dialog))
//                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // START THE GAME!
//                        new activity.DownloadFileFromURL().execute(file_url);
//
//                        // DownloadFile = "OK";
//                        SharedPreferences.Editor editor = SharePreferenceManager.getInstance().getEditor();
//                        editor.putString("DownloadFile", "OK");
//                        editor.commit();
//
//                    }
//                })
//                .setNegativeButton(R.
//                        string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                        dialog.dismiss();
//                        DownloadFile = "CANCEL";
//                        SharedPreferences.Editor editor = SharePreferenceManager.getInstance().getEditor();
//                        editor.putString("DownloadFile", "CANCEL");
//                        editor.commit();
//
//
//                    }
//                });
//        // Create the AlertDialog object and return it
//        builder.show();
//    }
  @RequiresApi(api = Build.VERSION_CODES.N)
  public static String DateFormatter(String DateStr)
  {
      SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-DD");
      Date date = null;
      String formattedDate = null;
      try {
          date = inputFormat.parse(DateStr);
           formattedDate = outputFormat.format(date);
          Log.d("TAG", " DATE: " + formattedDate);


      } catch (ParseException e) {
          e.printStackTrace();
      }

      return formattedDate;
  }
}
