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
                    NewCarActivity.this.saveState();
                    Toast.makeText(view.getContext(), "A new car has been inserted", Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(CarContentProvider.CONTENT_ITEM_TYPE, carUri);
    }

    public void saveState() {
        String make = makeText.getText().toString();
        String model = modelText.getText().toString();
        Integer year = Integer.valueOf(yearText.getText().toString());
        Integer price = Integer.valueOf(priceText.getText().toString());

        // only save if either make or model is available
        if (make.length() == 0 && model.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(CarTable.COLUMN_MAKE, make);
        values.put(CarTable.COLUMN_MODEL, model);
        values.put(CarTable.COLUMN_YEAR, year);
        values.put(CarTable.COLUMN_PRICE, price);

        // New car
        carUri = getContentResolver().insert(
                CarContentProvider.CONTENT_URI, values);

        // send email using an email client
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"anad_95@yahoo.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Car insertion");
        String emailBody = "The car : \n " + new Car(make, model, year, price).toString() + "\n has been inserted.";
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
