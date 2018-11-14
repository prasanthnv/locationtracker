package com.codemagos.locationtracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LinearLayout start_btn;
    TextView btn_txt;
    ImageView btn_image;
    LocationManager mLocationManager;
    boolean tracking = false;
    DatabaseHandler dbHadler;
    double start_time,end_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String user_mode = getResources().getString(R.string.flag);
        switch (user_mode){
            case "1":
                setContentView(R.layout.activity_main);
                break;
            case "2":
                setContentView(R.layout.activity_main2);
                break;
            case "3":
                setContentView(R.layout.activity_main3);
                break;
        }
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
        double lat = 0,lng = 0;
        double startTime = 0;
        double endTime = 0;
        double distance = 0;

        String s_time = "",e_time = "",s_lat = "",s_lng = "",e_lat= "",e_lng="";
        int flag = 0;
        Geocoder geocoder;

        geocoder = new Geocoder(this, Locale.getDefault());
        String start_address = "",end_address = "";
        while (locationsCursor.moveToNext()){

            double temp_lat = Double.parseDouble(locationsCursor.getString(locationsCursor.getColumnIndex("lat")));
            double temp_lng = Double.parseDouble(locationsCursor.getString(locationsCursor.getColumnIndex("lng")));
            if(flag > 0){
                distance += getDistanceFromLatLonInKm(lat,lng,temp_lat,temp_lng);
            }else {
                startTime =  Double.parseDouble(locationsCursor.getString(locationsCursor.getColumnIndex("time")));
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                Date resultdate = new Date(1000*Long.parseLong(locationsCursor.getString(locationsCursor.getColumnIndex("time"))));
                s_time = sdf.format(resultdate);


                s_lat =   locationsCursor.getString(locationsCursor.getColumnIndex("lat"));
                  s_lng = locationsCursor.getString(locationsCursor.getColumnIndex("lng"));


            }
            lat = temp_lat;
            lng = temp_lng;
            if(flag == (locationsCursor.getCount() -1)){
                endTime =  Double.parseDouble(locationsCursor.getString(locationsCursor.getColumnIndex("time")));
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                Date resultdate = new Date(1000*Long.parseLong(locationsCursor.getString(locationsCursor.getColumnIndex("time"))));
                e_time = sdf.format(resultdate);
                e_lat =   locationsCursor.getString(locationsCursor.getColumnIndex("lat"));
                e_lng = locationsCursor.getString(locationsCursor.getColumnIndex("lng"));
//                try {
//                    List<Address> addresses = geocoder.getFromLocation(
//                            Double.parseDouble(locationsCursor.getString(locationsCursor.getColumnIndex("lat"))),
//                            Double.parseDouble(locationsCursor.getString(locationsCursor.getColumnIndex("lng"))), 1);
//                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                    String city = addresses.get(0).getLocality();
//                    String state = addresses.get(0).getAdminArea();
//                    String country = addresses.get(0).getCountryName();
//                    String postalCode = addresses.get(0).getPostalCode();
//                    String knownName = addresses.get(0).getFeatureName();
//                    end_address = knownName + "," + city + "," + state + "";
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }

            flag++;
        }
        if(flag > 1){
            Log.e("==>",start_address + " =>|<= " + end_address);
            locationsCursor.close();
            double time_s = (endTime - startTime) / 1000.0;
            double speed_mps = distance / time_s;
            double speed_kph = (speed_mps * 3600.0) / 1000.0;
            distance = round(distance,2);
            speed_kph = round(speed_kph,2);
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);
            dbHadler.addHistory(distance,time_s,formattedDate,speed_kph);
            Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
            intent.putExtra("distance",distance+"");
            intent.putExtra("speed",speed_kph+"");
            intent.putExtra("start_time",s_time+"");
            intent.putExtra("end_time",e_time+"");
            intent.putExtra("start_lat",s_lat+"");
            intent.putExtra("start_lng",s_lng+"");
            intent.putExtra("end_lat",e_lat+"");
            intent.putExtra("end_lng",e_lng+"");
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
