package com.credenceid.sample.fingerprint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.credenceid.biometrics.Biometrics;
import com.credenceid.biometrics.BiometricsManager;
import com.credenceid.biometrics.DeviceFamily;
import com.credenceid.biometrics.DeviceType;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static com.credenceid.biometrics.Biometrics.ResultCode.FAIL;
import static com.credenceid.biometrics.Biometrics.ResultCode.INTERMEDIATE;
import static com.credenceid.biometrics.Biometrics.ResultCode.OK;

@SuppressWarnings({"unused", "StaticFieldLeak", "StatementWithEmptyBody"})
public class LaunchActivity
        extends Activity {

    private static final String TAG = LaunchActivity.class.getSimpleName();

    /* CredenceSDK biometrics object, used to interface with APIs. */
    private static BiometricsManager mBiometricsManager;
    /* Stores which Credence family of device's this app is running on. */
    private static DeviceFamily mDeviceFamily = DeviceFamily.InvalidDevice;
    /* Stores which specific device this app is running on. */
    private static DeviceType mDeviceType = DeviceType.InvalidDevice;

    /* --------------------------------------------------------------------------------------------
     *
     * Public getter methods.
     *
     * --------------------------------------------------------------------------------------------
     */

    public static BiometricsManager
    getBiometricsManager() {

        return mBiometricsManager;
    }

    public static DeviceType
    getDeviceType() {

        return mDeviceType;
    }

    public static DeviceFamily
    getDeviceFamily() {

        return mDeviceFamily;
    }

    /* --------------------------------------------------------------------------------------------
     *
     * Android activity lifecycle event methods.
     *
     * --------------------------------------------------------------------------------------------
     */

    @Override
    protected void
    onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.initBiometrics();
    }

    /* --------------------------------------------------------------------------------------------
     *
     * Private helpers.
     *
     * --------------------------------------------------------------------------------------------
     */

    private void
    initBiometrics() {

        /*  Create new biometrics object. */
        mBiometricsManager = new BiometricsManager(this);

        /* Initialize object, meaning tell CredenceService to bind to this application. */
        mBiometricsManager.initializeBiometrics((Biometrics.ResultCode resultCode,
                                                 String minimumVersion,
                                                 String currentVersion) -> {

            if (OK == resultCode) {
                Toast.makeText(this, getString(R.string.biometrics_initialized), LENGTH_SHORT).show();

                mDeviceFamily = mBiometricsManager.getDeviceFamily();
                mDeviceType = mBiometricsManager.getDeviceType();

                /* Launch main activity. */
                Intent intent = new Intent(this, FingerprintActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                this.finish();

            } else if (INTERMEDIATE == resultCode) {
                /* This code is never returned here. */

            } else if (FAIL == resultCode) {
                Toast.makeText(this, getString(R.string.biometrics_fail_init), LENGTH_LONG).show();
            }
        });
    }
}
