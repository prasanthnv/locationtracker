package com.codemagos.locationtracker;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.github.anastr.speedviewlib.SpeedView;
import com.github.anastr.speedviewlib.TubeSpeedometer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {
LinearLayout result_wrapper;
TextView txt_result,txt_speed;
Intent incomingIntent;
String distance,speed;

String startTime = "";
String endTime = "";
String start_lat= "",start_lng = "",end_lat="",end_lng="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incomingIntent = getIntent();
        distance = incomingIntent.getStringExtra("distance");
        speed = incomingIntent.getStringExtra("speed");
        startTime =incomingIntent.getStringExtra("start_time");
        endTime = incomingIntent.getStringExtra("end_time");
        start_lat = incomingIntent.getStringExtra("start_lat");
        start_lng = incomingIntent.getStringExtra("start_lng");
        end_lat = incomingIntent.getStringExtra("end_lat");
        end_lng = incomingIntent.getStringExtra("end_lng");
        Toast.makeText(getApplicationContext(),startTime + "| " + endTime,Toast.LENGTH_SHORT).show();
        String user_mode = getResources().getString(R.string.flag);
        Float speed_value = Float.parseFloat(speed);

        switch (user_mode){
            case "1":
                setContentView(R.layout.activity_result);
                // speedometer library
                PointerSpeedometer speedometer = findViewById(R.id.speedView);
                if(speed_value > speedometer.getMaxSpeed()){
                    speedometer.setMaxSpeed(speed_value + 10);
                }
                speedometer.speedTo(speed_value);
                LinearLayout time_view  =findViewById(R.id.time_view);
                ((TextView)time_view.findViewWithTag("time_start")).setText(startTime);
                ((TextView)time_view.findViewWithTag("time_end")).setText(endTime);
//                try {
//                    ((TextView)time_view.findViewWithTag("time_taken")).setText(getDuration(startTime,endTime));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }





                break;
            case "2":
                setContentView(R.layout.activity_result2);
                LinearLayout location_view = findViewById(R.id.location_view);

                // speedometer library
                TubeSpeedometer speedometer2 = findViewById(R.id.speedView);
                if(speed_value > speedometer2.getMaxSpeed()){
                    speedometer2.setMaxSpeed(speed_value + 10);
                }
                speedometer2.speedTo(speed_value);

                Geocoder geocoder;

                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(
                            Double.parseDouble(start_lat),Double.parseDouble(start_lng), 1);
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    String start_address = knownName + "," + city + "," + state + "";
                    ((TextView)location_view.findViewWithTag("location_start")).setText(start_address+"\n"+start_lat+","+start_lng);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    List<Address> addresses = geocoder.getFromLocation(
                            Double.parseDouble(end_lat),Double.parseDouble(end_lng), 1);
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    String end_address = knownName + "," + city + "," + state + "";
                    ((TextView)location_view.findViewWithTag("location_end")).setText(end_address+"\n"+end_lat+","+end_lng);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case "3":
                setContentView(R.layout.activity_result3);
                // speedometer library
                SpeedView speedometer3 = findViewById(R.id.speedView);
                if(speed_value > speedometer3.getMaxSpeed()){
                    speedometer3.setMaxSpeed(speed_value + 10);
                }
                speedometer3.speedTo(speed_value);
                break;
        }

        result_wrapper = findViewById(R.id.result_wrapper);

        txt_result = result_wrapper.findViewWithTag("distance");
       // txt_speed = result_wrapper.findViewWithTag("speed");
        txt_result.setText(distance + " Km");
       // txt_speed.setText(speed + "km/h");


    }

    public void reset(View v){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    public String getDuration(String time1,String time2) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(time1);
        Date date2 = format.parse(time2);
        long difference = date2.getTime() - date1.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        Date date_diff = new Date(difference);
        return format1.format(date_diff);
    }


    public void showTopSpeed(View c){
        startActivity(new Intent(getApplicationContext(),HistoryActivity.class));
    }
}
