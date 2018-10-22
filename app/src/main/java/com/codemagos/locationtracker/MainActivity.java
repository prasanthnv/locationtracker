package com.codemagos.locationtracker;

import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codemagos.locationtracker.Database.DatabaseHandler;
import com.codemagos.locationtracker.Services.LocationService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    LinearLayout start_btn;
    TextView btn_txt;
    ImageView btn_image;
    LocationManager mLocationManager;
    boolean tracking = false;
    DatabaseHandler dbHadler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHadler = new DatabaseHandler(getApplicationContext());
        start_btn = findViewById(R.id.start_btn);
        btn_txt = start_btn.findViewWithTag("text");
        btn_image = start_btn.findViewWithTag("image");

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tracking){
                    startService(new Intent(getApplicationContext(), LocationService.class));
                    btn_txt.setText("Tracking");
                    btn_image.setImageResource(R.drawable.tracker_active);
                    tracking = true;
                }else{
                    stopService(new Intent(getApplicationContext(), LocationService.class));
                    btn_txt.setText("Start Tracking");
                    btn_image.setImageResource(R.drawable.tracker_1);
                    tracking = false;
                    findDistance();
                }

            }
        });
    }


    public void findDistance(){
        Cursor locationsCursor = dbHadler.getLocation();
        double lat = 0,lng = 0,startTime = 0,endTime = 0;
        double distance = 0;
        int flag = 0;

        while (locationsCursor.moveToNext()){

            double temp_lat = Double.parseDouble(locationsCursor.getString(locationsCursor.getColumnIndex("lat")));
            double temp_lng = Double.parseDouble(locationsCursor.getString(locationsCursor.getColumnIndex("lng")));
            if(flag > 0){
                distance += getDistanceFromLatLonInKm(lat,lng,temp_lat,temp_lng);
            }else {
                startTime =  Double.parseDouble(locationsCursor.getString(locationsCursor.getColumnIndex("time")));
            }
            lat = temp_lat;
            lng = temp_lng;
            if(flag == (locationsCursor.getCount() -1)){
                endTime =  Double.parseDouble(locationsCursor.getString(locationsCursor.getColumnIndex("time")));
            }
            flag++;
        }
        if(flag > 1){
            locationsCursor.close();
            double time_s = (endTime - startTime) / 1000.0;
            double speed_mps = distance / time_s;
            double speed_kph = (speed_mps * 3600.0) / 1000.0;
            distance = round(distance,2);
            speed_kph = round(speed_kph,2);
            Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
            intent.putExtra("distance",distance+"");
            intent.putExtra("speed",speed_kph+"");
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "No Enough Data to Process", Toast.LENGTH_LONG).show();

        }


    }


    public double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2)
    {
        final int R = 6371;
        // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);
        // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        // Distance in km
        return d;
    }
    private double deg2rad(double deg)
    {
        return deg * (Math.PI / 180);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
