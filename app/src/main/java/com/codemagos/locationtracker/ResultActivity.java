package com.codemagos.locationtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.github.anastr.speedviewlib.SpeedView;
import com.github.anastr.speedviewlib.TubeSpeedometer;

public class ResultActivity extends AppCompatActivity {
LinearLayout result_wrapper;
TextView txt_result,txt_speed;
Intent incomingIntent;
String distance,speed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incomingIntent = getIntent();
        distance = incomingIntent.getStringExtra("distance");
        speed = incomingIntent.getStringExtra("speed");
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
                break;
            case "2":
                setContentView(R.layout.activity_result2);

                // speedometer library
                TubeSpeedometer speedometer2 = findViewById(R.id.speedView);
                if(speed_value > speedometer2.getMaxSpeed()){
                    speedometer2.setMaxSpeed(speed_value + 10);
                }
                speedometer2.speedTo(speed_value);
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
}
