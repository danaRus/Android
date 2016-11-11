package com.example.dana.carmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CarDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText makeEditText = (EditText) findViewById(R.id.makeDetail);
        final EditText modelEditText = (EditText) findViewById(R.id.modelDetail);
        final EditText yearEditText = (EditText) findViewById(R.id.yearDetail);

        Intent i = getIntent();
        String make = i.getExtras().getString("make");
        String model = i.getExtras().getString("model");
        String year = i.getExtras().getString("year");
        final Integer position = Integer.valueOf(i.getExtras().getString("position"));

        makeEditText.setText(make);
        modelEditText.setText(model);
        yearEditText.setText(year);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                MainActivity.cars.get(position).setMake(makeEditText.getText().toString());
                MainActivity.cars.get(position).setModel(modelEditText.getText().toString());
                MainActivity.cars.get(position).setYear(Integer.valueOf(yearEditText.getText().toString()));
                MainActivity.carAdapter.notifyDataSetChanged();

                Toast.makeText(view.getContext(), "The car's data has been modified", Toast.LENGTH_SHORT).show();
            }
        });

        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
