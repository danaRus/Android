package com.example.dana.carmanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dana.carmanagement.contentprovider.CarContentProvider;
import com.example.dana.carmanagement.firebaseutil.FirebaseUtil;
import com.example.dana.carmanagement.model.Car;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    public static CarAdapter adapter;

    public static FirebaseUtil firebaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUtil = new FirebaseUtil();

        setContentView(R.layout.activity_main);
        final Context context = this;

        if (firebaseUtil.getmFirebaseUser() == null) {
            // Not logged in ==> launch the Log In activity
            loadLogInView();
        } else {
            firebaseUtil.setmUserId(firebaseUtil.getmFirebaseUser().getUid());

            listView = (ListView) findViewById(R.id.listView);
            adapter = new CarAdapter(this);
            firebaseUtil.addObserver(adapter);

            listView.setAdapter(adapter);

            fillData();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Car car = adapter.getCars().get(position);

                    Intent detailIntent = new Intent(context, CarDetailsActivity.class);
                    detailIntent.putExtra("make", car.getMake());
                    detailIntent.putExtra("model", car.getModel());
                    detailIntent.putExtra("year", String.valueOf(car.getYear()));
                    detailIntent.putExtra("price", String.valueOf(car.getPrice()));
                    detailIntent.putExtra("uuid", car.getUuid());

                    detailIntent.putExtra("models", (ArrayList<String>)adapter.getModels());
                    detailIntent.putExtra("prices", (ArrayList<Integer>)adapter.getPrices());

                    startActivity(detailIntent);
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final Car car = adapter.getCars().get(position);
                    final String uuid = car.getUuid();
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Delete")
                            .setMessage("This car will be deleted.")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    firebaseUtil.remove(uuid, car);
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                    return true;
                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newCarIntent = new Intent(context, NewCarActivity.class);
                startActivity(newCarIntent);
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

        if (id == R.id.action_logout) {
            firebaseUtil.getmFirebaseAuth().signOut();
            loadLogInView();
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillData() {
        firebaseUtil.fillData();
    }

    // for firebase
    private void loadLogInView() {
        Intent intent = new Intent(this, LogInActivity.class);
        // this activity will become the start of a new task on this history stack
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // this flag will cause any existing task that would be associated with the activity to be
        // cleared before the activity is started
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
