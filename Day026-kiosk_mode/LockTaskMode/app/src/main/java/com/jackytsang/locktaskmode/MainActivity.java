package com.jackytsang.locktaskmode;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.UserManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.ActivityManager.LOCK_TASK_MODE_LOCKED;

public class MainActivity extends AppCompatActivity {


    private ComponentName deviceAdmin;
    private DevicePolicyManager mDpm;
    private PackageManager mPackageManager;
    private ActivityManager am;

    private static final String Battery_PLUGGED_ANY = Integer.toString(
            BatteryManager.BATTERY_PLUGGED_AC |
                    BatteryManager.BATTERY_PLUGGED_USB |
                    BatteryManager.BATTERY_PLUGGED_WIRELESS);

    private static final String DONT_STAY_ON = "0";

    private boolean mIsKioskEnabled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        deviceAdmin = new ComponentName(this, AdminReceiver.class);
        mDpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mPackageManager = getPackageManager();
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        setDefaultCosuPolicies(true);


        if (!mDpm.isAdminActive(deviceAdmin)) {
            Toast.makeText(this, getString(R.string.not_device_admin), Toast.LENGTH_SHORT).show();
        }

        if (mDpm.isDeviceOwnerApp(getPackageName())) {
            mDpm.setLockTaskPackages(deviceAdmin, new String[]{getPackageName()});
        } else {
            Toast.makeText(this, getString(R.string.not_device_owner), Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setDefaultCosuPolicies(boolean active) {
        // set user restrictions
        setUserRestriction(UserManager.DISALLOW_SAFE_BOOT, active);
        setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, active);
        setUserRestriction(UserManager.DISALLOW_ADD_USER, active);
        setUserRestriction(UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA, active);
        setUserRestriction(UserManager.DISALLOW_ADJUST_VOLUME, active);

//        // disable keyguard and status bar
//        mDpm.setKeyguardDisabled(deviceAdmin, active);
//        mDpm.setStatusBarDisabled(deviceAdmin, active);

        // enable STAY_ON_WHILE_PLUGGED_IN
        enableStayOnWhilePluggedIn(active);

        // set System Update policy

//        if (active) {
//            mDpm.setSystemUpdatePolicy(deviceAdmin,
//                    SystemUpdatePolicy.createWindowedInstallPolicy(60, 120));
//        } else {
//            DevicePolicyManager.setSystemUpdatePolicy(deviceAdmin, null);
//        }

        // set this Activity as a lock task package

        mDpm.setLockTaskPackages(deviceAdmin,
                active ? new String[]{getPackageName()} : new String[]{});

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
        intentFilter.addCategory(Intent.CATEGORY_HOME);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        if (active) {
            // set Cosu activity as home intent receiver so that it is started
            // on reboot
            mDpm.addPersistentPreferredActivity(
                    deviceAdmin, intentFilter, new ComponentName(
                            getPackageName(), MainActivity.class.getName()));
        } else {
            mDpm.clearPackagePersistentPreferredActivities(
                    deviceAdmin, getPackageName());
        }
    }

    private void setUserRestriction(String restriction, boolean disallow) {
        if (disallow) {
            mDpm.addUserRestriction(deviceAdmin,
                    restriction);
        } else {
            mDpm.clearUserRestriction(deviceAdmin,
                    restriction);
        }
    }

    private void enableStayOnWhilePluggedIn(boolean enabled) {
        if (enabled) {
            mDpm.setGlobalSetting(
                    deviceAdmin,
                    Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                    Battery_PLUGGED_ANY);
        } else {
            mDpm.setGlobalSetting(
                    deviceAdmin,
                    Settings.Global.STAY_ON_WHILE_PLUGGED_IN, DONT_STAY_ON);
        }
    }

    private boolean isInLockMode() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return am.isInLockTaskMode();
        else
            return am.getLockTaskModeState() == LOCK_TASK_MODE_LOCKED;
    }


    private void enableKioskMode(boolean enabled) {
        try {
            if (!enabled && isInLockMode()) {
                stopLockTask();
                mIsKioskEnabled = false;
                return;
            }

            if (mDpm.isLockTaskPermitted(this.getPackageName())) {
                if (!isInLockMode()) {
                    startLockTask();
                    mIsKioskEnabled = true;
                }
            } else {
                Toast.makeText(this, getString(R.string.kiosk_not_permitted), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO: Log and handle appropriately
        }
    }

    @OnClick(R.id.toggle_kiosk)
    void toggleKiosk() {
        enableKioskMode(!mIsKioskEnabled);
    }
}
