package com.example.pinit.FindActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.pinit.FindActivity.DrawingTools.DrawingCanvas;
import com.example.pinit.FindActivity.DrawingTools.DrawingManager;
import com.example.pinit.R;
import com.example.pinit.SensorManager.SensorAngleResult;
import com.example.pinit.SensorManager.SensorConstants;
import com.example.pinit.SensorManager.SensorManager;

public class FindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //Hide action bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //Hide title bar
        setContentView(R.layout.activity_find);

        Intent intent=getIntent();
        int offsetAzimuth=intent.getIntExtra("azimuth",0); //Offset has 180 deg already added
        int offsetRoll=intent.getIntExtra("roll",0);

        DrawingCanvas canvas = findViewById(R.id.drawingCanvas);
        final DrawingManager manager = new DrawingManager(canvas,offsetAzimuth,offsetRoll);

        final TextView alertTextView = findViewById(R.id.alertTextView);

        new SensorManager(this, false, new SensorAngleResult() {
            @Override
            public void passResult(int azimuth, int roll,int pitch) {
                //Check pitch (Phone tilt)
                if(Math.abs(pitch)> SensorConstants.pitchLimit){
                    alertTextView.setText(R.string.alertTextViewInvalidPitch);
                    alertTextView.setVisibility(View.VISIBLE);
                    return;
                }
                //Check roll (Beyond hemisphere)
                else if(roll>SensorConstants.rollLimit){
                    alertTextView.setText(R.string.alertTextViewInvalidRoll);
                    alertTextView.setVisibility(View.VISIBLE);
                    return;
                }

                alertTextView.setVisibility(View.INVISIBLE);
                manager.drawCircle(azimuth+180,roll+180);
            }
        });
    }

}