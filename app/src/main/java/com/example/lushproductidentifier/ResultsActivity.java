package com.example.lushproductidentifier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        String data = getIntent().getStringExtra("data");

        try {
            JSONObject json = new JSONObject(data);
            Log.d("JSON contents: ", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
