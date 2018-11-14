package com.codemagos.locationtracker;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.codemagos.locationtracker.Database.DatabaseHandler;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    DatabaseHandler dbHadler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dbHadler = new DatabaseHandler(getApplicationContext());
        ListView list_histroy = findViewById(R.id.list_history);
        Cursor historyCursor = dbHadler.getHistory();
        ArrayList speed = new ArrayList();
        ArrayList time = new ArrayList();
        ArrayList distance = new ArrayList();
        ArrayList date = new ArrayList();
        while (historyCursor.moveToNext()){
            speed.add(historyCursor.getString(historyCursor.getColumnIndex("speed")));
            time.add(historyCursor.getString(historyCursor.getColumnIndex("time")));
            distance.add(historyCursor.getString(historyCursor.getColumnIndex("distance")));
            date.add(historyCursor.getString(historyCursor.getColumnIndex("date")));
        }

        HistoryAdaptor adaptor = new HistoryAdaptor(HistoryActivity.this,speed,date,distance);
        list_histroy.setAdapter(adaptor);
    }
}
