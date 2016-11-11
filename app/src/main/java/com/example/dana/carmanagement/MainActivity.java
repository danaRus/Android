package com.example.dana.carmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dana.carmanagement.model.Car;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    public static List<Car> cars;
    public static CarAdapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Context context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent newCarIntent = new Intent(context, NewCarActivity.class);
                startActivity(newCarIntent);
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        generateCars();
        carAdapter = new CarAdapter(this, cars);
        listView.setAdapter(carAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Car car = cars.get(position);

                Intent detailIntent = new Intent(context, CarDetailsActivity.class);
                detailIntent.putExtra("make", car.getMake());
                detailIntent.putExtra("model", car.getModel());
                detailIntent.putExtra("year", String.valueOf(car.getYear()));
                // put the position in order to be able to modify the car when the save button is clicked
                detailIntent.putExtra("position", String.valueOf(position));

                startActivity(detailIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void generateCars() {
        cars = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            Car car = new Car("Make" + i, "Model" + i, 2000 + i);
            cars.add(car);
        }
    }
}
