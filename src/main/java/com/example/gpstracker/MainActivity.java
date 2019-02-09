package com.example.gpstracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hanks.htextview.typer.TyperTextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private List<gpsCoordinates> mdatalist = new ArrayList<>();
    private RecyclerView recyclerView;
    private gpsCoordinatesAdapter madapter;
    private Context context;
    SimpleDateFormat simpleDateFormat;

    LocationManager locationManager;
    Button btnClearLog;
    ToggleButton tbTracking;
    TextView tvLoc;

    int i = 0;
    boolean btn = true;
    String TAG;
    String timestamp;
    String latitude;
    String longitude;
    String mCoordinates;
    String strAddress;
    Address address;
    List<Address> addressList;
    boolean isCheckedVal;
    String finalAddres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        context = getApplicationContext();

        btnClearLog = findViewById(R.id.btnclearlog);
        tvLoc = findViewById(R.id.textview);
        tbTracking = findViewById(R.id.toggleButton);


        recyclerView = findViewById(R.id.recycler_view);
        madapter = new gpsCoordinatesAdapter(context, mdatalist);


        RecyclerView.LayoutManager mlayoutmanager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mlayoutmanager);
        recyclerView.setAdapter(madapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        tbTracking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    startTracking();
                    isCheckedVal = true;
                } else if (isChecked == false) {
                    stopTracking();
                    isCheckedVal = false;
                }
            }
        });

        btnClearLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearLog();
            }
        });
/*        btnTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTracking();

            }
        });*/

    }



    public void startTracking() {

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {

                getLocation();
                //handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 10);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isCheckedVal == true) {
            startTracking();
        }
    }


    public void stopTracking() {
        locationManager.removeUpdates(this);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, (LocationListener) this);

        } catch (Exception e) {
            Log.d(TAG, "getLocation: " + e);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        timestamp = "Last updated: " + simpleDateFormat.format(new Date());


        latitude = "Latitude: " + String.valueOf(location.getLatitude());
        longitude = " Longitude: " + String.valueOf(location.getLongitude());
        mCoordinates = latitude + longitude;

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addressList != null && addressList.size() > 0) {
                address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < address.getMaxAddressLineIndex(); j++) {
                    sb.append(address.getAddressLine(j)).append("\n");

                }
                sb.append(address.getSubLocality()).append("\n");
                sb.append("\t").append(address.getLocality()).append("\n");
                sb.append("\t").append(address.getPostalCode());
                strAddress = sb.toString();
                finalAddres = timestamp + "\n" + "Address : " + strAddress;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvLoc.setText("Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude() + "\nLast Updated : " + timestamp);
        prepareLocationData(mCoordinates, finalAddres);
        latitude = "";
        longitude = "";
        mCoordinates = "";
        timestamp = "";

    }

    public void prepareLocationData(String mCoordinates, String timestamp) {

        String _mcoordinate, _timestamp;

        _mcoordinate = mCoordinates;
        _timestamp = timestamp;

        gpsCoordinates mgpscoordinates = new gpsCoordinates(_mcoordinate, _timestamp);
        mdatalist.add(mgpscoordinates);
        //recyclerView.setAdapter(madapter);

/*        gpsCoordinates mgpscoordinates = new gpsCoordinates("lat : 10.456 long: 543260.65 ", "Last updated : 8:20");
        i++;
        mdatalist.add(mgpscoordinates);

        mgpscoordinates = new gpsCoordinates("lat : 20.456 long: 8.251 ", "8:30");
        i++;
        mdatalist.add(mgpscoordinates);

        mgpscoordinates = new gpsCoordinates("lat : 1.358 long: 7.61 ", "1:20");
        mdatalist.add(mgpscoordinates);*/

        //madapter.notifyItemInserted(i);
        madapter.notifyDataSetChanged();
        //madapter.notifyItemChanged(i);
    }

    private void clearLog() {
        mdatalist.clear();
        //mdatalist.removeAll(mdatalist);
        madapter.notifyDataSetChanged();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
