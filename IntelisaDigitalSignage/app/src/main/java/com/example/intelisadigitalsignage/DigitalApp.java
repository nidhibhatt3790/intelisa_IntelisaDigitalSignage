package com.example.intelisadigitalsignage;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.intelisadigitalsignage.Activity.WebsiteActivity;

public class DigitalApp extends Application implements Application.ActivityLifecycleCallbacks {

    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(this);

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            // App enters foreground

            Toast.makeText(this, "App is in foreground with activity::" + activity.getLocalClassName(), Toast.LENGTH_LONG).show();
            Log.d("Tracking Activity Started", activity.getLocalClassName());

            Log.d("TAG", "App is in foreground");
        }

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            // App enters background
            Toast.makeText(this, "App enters background with activity::" + activity.getLocalClassName(), Toast.LENGTH_LONG).show();

            Log.d("Tracking Activity Stopped", activity.getLocalClassName());
            Log.d("TAG", "App enters background");
            Intent i = null;


            startService(new Intent(getApplicationContext(), service.class));


        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
