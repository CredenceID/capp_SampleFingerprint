package com.cid.sample.fingerprint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.credenceid.biometrics.Biometrics;
import com.credenceid.biometrics.BiometricsManager;

import static android.Manifest.permission.CAMERA;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static com.cid.sample.fingerprint.DeviceFamily.CONE;
import static com.cid.sample.fingerprint.DeviceFamily.CTAB;
import static com.cid.sample.fingerprint.DeviceFamily.TRIDENT;
import static com.cid.sample.fingerprint.DeviceFamily.TWIZZLER;
import static com.cid.sample.fingerprint.DeviceType.CONE_V1;
import static com.cid.sample.fingerprint.DeviceType.CONE_V2;
import static com.cid.sample.fingerprint.DeviceType.CONE_V3;
import static com.cid.sample.fingerprint.DeviceType.CTAB_V1;
import static com.cid.sample.fingerprint.DeviceType.CTAB_V2;
import static com.cid.sample.fingerprint.DeviceType.CTAB_V3;
import static com.cid.sample.fingerprint.DeviceType.CTAB_V4;
import static com.cid.sample.fingerprint.DeviceType.TRIDENT_1;
import static com.cid.sample.fingerprint.DeviceType.TRIDENT_2;
import static com.credenceid.biometrics.Biometrics.ResultCode.OK;

public class LaunchActivity
		extends Activity {
	@SuppressLint("StaticFieldLeak")
	public static BiometricsManager mBiometricsManager;
	public static DeviceFamily mDeviceFamily;
	public static DeviceType mDeviceType;

	@Override
	protected void
	onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create new instance of object to use biometric APIs.
		mBiometricsManager = new BiometricsManager(this);

		// Tell C-Service to bind to this application.
		mBiometricsManager.initializeBiometrics((Biometrics.ResultCode resultCode,
												 String minimumVersion,
												 String currentVersion) -> {
			if (resultCode == OK) {
				Toast.makeText(this, "Biometrics initialized.", LENGTH_SHORT).show();

				// Determine DeviceType/Family based on product name application is running on.
				mDeviceType = this.determineDeviceType(mBiometricsManager.getProductName());
				mDeviceFamily = this.determineDeviceFamily(mDeviceType);

				// Lunch main activity.
				Intent intent = new Intent(this, FingerprintActivity.class);
				startActivity(intent);
			} else Toast.makeText(this, "Biometrics FAILED to initialize.", LENGTH_LONG).show();
		});
	}

	private DeviceType
	determineDeviceType(String productName) {
		DeviceType deviceType = DeviceType.INVALID;

		//noinspection IfCanBeSwitch
		if (productName.equals("Twizzler"))
			deviceType = DeviceType.TWIZZLER;
		else if (productName.equals("Trident-1"))
			deviceType = TRIDENT_1;
		else if (productName.equals("Trident-2"))
			deviceType = TRIDENT_2;
		else if (productName.equals("Credence One V1"))
			deviceType = CONE_V1;
		else if (productName.equals("Credence One V2"))
			deviceType = CONE_V2;
		else if (productName.equals("Credence One V3"))
			deviceType = CONE_V3;
		else if (productName.equals("Credence Two V1"))
			deviceType = DeviceType.CTWO_V1;
		else if (productName.equals("Credence Two V2"))
			deviceType = DeviceType.CTWO_V2;
		else if (productName.equals("Credence TAB V1"))
			deviceType = CTAB_V1;
		else if (productName.equals("Credence TAB V2"))
			deviceType = CTAB_V2;
		else if (productName.equals("Credence TAB V3"))
			deviceType = CTAB_V3;
		else if (productName.equals("Credence TAB V4"))
			deviceType = CTAB_V4;

		return deviceType;
	}

	private DeviceFamily
	determineDeviceFamily(DeviceType deviceType) {
		DeviceFamily deviceFamily = DeviceFamily.INVALID;

		if (deviceType == DeviceType.TWIZZLER)
			deviceFamily = TWIZZLER;
		else if (deviceType == TRIDENT_1 || deviceType == TRIDENT_2)
			deviceFamily = TRIDENT;
		else if (deviceType == CONE_V1 || deviceType == CONE_V2 || deviceType == CONE_V3)
			deviceFamily = CONE;
		else if (deviceType == CTAB_V1 || deviceType == CTAB_V2
				|| deviceType == CTAB_V3 || deviceType == CTAB_V4)
			deviceFamily = CTAB;

		return deviceFamily;
	}
}
