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

import com.example.dana.carmanagement.MainActivity;
import com.example.dana.carmanagement.R;
import com.example.dana.carmanagement.model.Car;

public class NewCarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText makeEditText = (EditText) findViewById(R.id.newMake);
        final EditText modelEditText = (EditText) findViewById(R.id.newModel);
        final EditText yearEditText = (EditText) findViewById(R.id.newYear);

        Intent i = getIntent();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Car car = new Car();
                car.setMake(makeEditText.getText().toString());
                car.setModel(modelEditText.getText().toString());
                car.setYear(Integer.valueOf(yearEditText.getText().toString()));
                MainActivity.cars.add(car);
                MainActivity.carAdapter.notifyDataSetChanged();

                // send email using an email client
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"anad_95@yahoo.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Car insertion");
                String emailBody = "The car : \n " + car.toString() + "\n has been inserted.";
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Choose an Email client:"));
                    finish();
                }catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(view.getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(view.getContext(), "A new car has been inserted", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
