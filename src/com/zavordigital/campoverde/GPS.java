package com.zavordigital.campoverde;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

public class GPS implements Runnable {

	private static LocationManager locManager;
	private static LocationListener locListener;
	private static Location locationMap;
	private Context context;

	public GPS(Context context) {
		this.context = context;
		locationMap = null;
	}

	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		try {
			locManager.removeUpdates(locListener);
		} catch (Exception e) {

		}
	}
	
	public synchronized boolean isEnable() {
		return locManager.isProviderEnabled (LocationManager.GPS_PROVIDER);
	}
	
	public synchronized String getAdress() {
		Geocoder gcd = new Geocoder(context, Locale.getDefault());
		List<Address> addresses;
		try {
			addresses = gcd.getFromLocation(locationMap.getLatitude(), locationMap.getLongitude(),100);
			if (addresses.size() > 0 && addresses != null) {
				String direcction = "";
				for(int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
					direcction += addresses.get(0).getAddressLine(i) + " ";
                return direcction;
			}
		} catch (IOException e) {
			return "";
		}
		return "";
	}

	public synchronized String[] getLocation() {
		if (locationMap != null) {
			String[] location = new String[2];
			location[0] = String.valueOf(locationMap.getLatitude());
			location[1] = String.valueOf(locationMap.getLongitude());
			return location;
		}
		return null;
	}

	@Override
	public void run() {
		locManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Looper.prepare();
			locListener = new MyLocationListener();
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
					0, locListener);
			Looper.loop();
			Looper.myLooper().quit();
		} else if(locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Looper.prepare();
			locListener = new MyLocationListener();
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
					0, locListener);
			Looper.loop();
			Looper.myLooper().quit();
		}
	}

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				locationMap = loc;
				locManager.removeUpdates(locListener);
			}

		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	}

}
