package com.example.intelisadigitalsignage;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.List;

public class service extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable(){
            public void run() {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                i.setComponent(new ComponentName(getApplicationContext().getPackageName(),MainActivity.class.getName()));

            }}, 3000);


//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        this.startActivity(intent);



    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        Toast.makeText(this, "SERVICE ON START.", Toast.LENGTH_LONG).show();

//
//        Intent dialogIntent = new Intent(this, MainActivity.class);
//        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(dialogIntent);
//        Intent bringToForegroundIntent = new Intent(this, MainActivity.class);
//        bringToForegroundIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(bringToForegroundIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Toast.makeText(this, "SERVICE ON BIND.", Toast.LENGTH_LONG).show();

        return null;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "SERVICE STARTED IN INTELISA.", Toast.LENGTH_LONG).show();
//        ActivityManager activtyManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activtyManager.getRunningTasks(3);
//        for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskInfos)
//        {
//            if (this.getPackageName().equals(runningTaskInfo.topActivity.getPackageName()))
//            {
//                activtyManager.moveTaskToFront(runningTaskInfo.id, ActivityManager.MOVE_TASK_WITH_HOME);
//
//            }
//        }

//
//        ActivityManager am = (ActivityManager) service.this.getSystemService(ACTIVITY_SERVICE);
//// The first in the list of RunningTasks is always the foreground task.
//        ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);


       // Log.d("FOREGROUNDACTIVITY", foregroundTaskInfo.baseActivity.toString());
//         Intent i = new Intent();
//         i.setAction(Intent.ACTION_MAIN);
//         i.addCategory(Intent.CATEGORY_LAUNCHER);
//         i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
//         i.setComponent(new ComponentName(getApplicationContext().getPackageName(),MainActivity.class.getName()));

        return START_STICKY;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();

    }
}


