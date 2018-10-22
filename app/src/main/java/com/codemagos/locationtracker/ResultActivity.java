package com.codemagos.locationtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.github.anastr.speedviewlib.SpeedView;

public class ResultActivity extends AppCompatActivity {
LinearLayout result_wrapper;
TextView txt_result,txt_speed;
Intent incomingIntent;
String distance,speed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        result_wrapper = findViewById(R.id.result_wrapper);
        incomingIntent = getIntent();
        distance = incomingIntent.getStringExtra("distance");
        speed = incomingIntent.getStringExtra("speed");
        txt_result = result_wrapper.findViewWithTag("distance");
       // txt_speed = result_wrapper.findViewWithTag("speed");
        txt_result.setText(distance + " Km");
       // txt_speed.setText(speed + "km/h");
        Float speed_value = Float.parseFloat(speed);
        // speedometer library
        PointerSpeedometer speedometer = findViewById(R.id.speedView);
        if(speed_value > speedometer.getMaxSpeed()){
            speedometer.setMaxSpeed(speed_value + 10);
        }
        speedometer.speedTo(speed_value);

    }

    public void reset(View v){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
