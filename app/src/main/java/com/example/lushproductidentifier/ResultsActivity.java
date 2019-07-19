package com.example.lushproductidentifier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import static com.example.lushproductidentifier.MainActivity.dataModelArrayList;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ListView listView = findViewById(R.id.json_results);

        ListAdapter listAdapter = new ListAdapter(this, dataModelArrayList);
        listView.setAdapter(listAdapter);


    }
}
