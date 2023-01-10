package com.example.intelisadigitalsignage;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

public class DeviceAdmiReceiver extends DeviceAdminReceiver {

        // Called when the user enables the device admin.
        @Override
        public void onEnabled(Context context, Intent intent) {
            super.onEnabled(context, intent);
            Toast.makeText(context, "Device admin enabled", Toast.LENGTH_SHORT).show();

            DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

            // Set restrictions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dpm.setKeyguardDisabled(getWho(context), true);
                dpm.setStatusBarDisabled(getWho(context), true);

            }

        }

        // Called when the user disables the device admin.
        @Override
        public void onDisabled(Context context, Intent intent) {
            super.onDisabled(context, intent);
            Toast.makeText(context, "Device admin disabled", Toast.LENGTH_SHORT).show();
        }


}














