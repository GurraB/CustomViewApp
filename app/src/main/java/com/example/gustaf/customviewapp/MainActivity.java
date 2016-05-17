package com.example.gustaf.customviewapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList<GraphEvent> events = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, start.get(Calendar.HOUR_OF_DAY) + 2);
        GraphEvent event = new GraphEvent(start.getTimeInMillis(), end.getTimeInMillis(), true);
        events.add(event);
        final VisualSchedule vs = (VisualSchedule) findViewById(R.id.schedule);
        vs.notifyDataChanged(events);
        vs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vs.notifyDataChanged(events);
            }
        });
    }
}
