package com.example.intelisadigitalsignage;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.intelisadigitalsignage.Activity.WebsiteActivity;
import com.example.intelisadigitalsignage.managers.SharePreferenceManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

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

}
