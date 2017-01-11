package com.example.dana.carmanagement;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dana.carmanagement.contentprovider.CarContentProvider;
import com.example.dana.carmanagement.database.CarTable;
import com.example.dana.carmanagement.model.Car;
import com.example.dana.carmanagement.pickers.YearPicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewCarActivity extends AppCompatActivity {

    private Uri carUri;
    private EditText makeText;
    private EditText modelText;
    private static TextView yearText;
    private EditText priceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        makeText = (EditText) findViewById(R.id.newMake);
        modelText = (EditText) findViewById(R.id.newModel);
        yearText = (TextView) findViewById(R.id.newYear);
        priceText = (EditText) findViewById(R.id.priceNew);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(makeText.getText().toString()) || TextUtils.isEmpty(modelText.getText().toString())
                        || TextUtils.isEmpty(yearText.getText().toString()) || TextUtils.isEmpty(priceText.getText().toString())) {
                    Toast.makeText(view.getContext(), "No field can be empty", Toast.LENGTH_SHORT).show();
                } else {
                    String make = makeText.getText().toString();
                    String model = modelText.getText().toString();
                    Integer year = Integer.valueOf(yearText.getText().toString());
                    Integer price = Integer.valueOf(priceText.getText().toString());
                    Car car = new Car();
                    car.setMake(make);
                    car.setModel(model);
                    car.setYear(year);
                    car.setPrice(price);
                    car.setUuid();

                    MainActivity.firebaseUtil.add(car);

                    sendEmail(car);

                    Toast.makeText(view.getContext(), "A new car has been inserted", Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    public void sendEmail(Car car) {
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
            // Toast.makeText(view.getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showYearDialog(View v) {
        YearPicker yearFragment = new YearPicker();
        yearFragment.setActivityType("newCar");
        yearFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static  void setYear(int year) {
        yearText.setText(String.valueOf(year));
    }

}
