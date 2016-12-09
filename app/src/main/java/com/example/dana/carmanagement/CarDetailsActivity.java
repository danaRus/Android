package com.example.dana.carmanagement;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dana.carmanagement.contentprovider.CarContentProvider;
import com.example.dana.carmanagement.database.CarTable;
import com.example.dana.carmanagement.pickers.YearPicker;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

public class CarDetailsActivity extends AppCompatActivity {
    private Uri carUri;
    private EditText makeText;
    private EditText modelText;
    private static TextView yearText;
    private EditText priceText;
    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        makeText = (EditText) findViewById(R.id.makeDetail);
        modelText = (EditText) findViewById(R.id.modelDetail);
        yearText = (TextView) findViewById(R.id.yearDetail);
        priceText = (EditText) findViewById(R.id.priceDetail);

        Bundle extras = getIntent().getExtras();

        // check from the saved Instance
        carUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState
                .getParcelable(CarContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            carUri = extras
                    .getParcelable(CarContentProvider.CONTENT_ITEM_TYPE);

            fillData(carUri);
        }
        getChart();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(makeText.getText().toString()) || TextUtils.isEmpty(modelText.getText().toString())
                        || TextUtils.isEmpty(yearText.getText().toString()) || TextUtils.isEmpty(priceText.getText().toString())) {
                    Toast.makeText(view.getContext(), "No field can be empty.", Toast.LENGTH_SHORT).show();
                } else {
                    CarDetailsActivity.this.saveState();
                    Toast.makeText(view.getContext(), "The car's data has been modified", Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void fillData(Uri uri) {
        String[] projection = {CarTable.COLUMN_ID, CarTable.COLUMN_MAKE,
                CarTable.COLUMN_MODEL, CarTable.COLUMN_YEAR, CarTable.COLUMN_PRICE };
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            id = cursor.getLong(cursor.getColumnIndexOrThrow(CarTable.COLUMN_ID));
            makeText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(CarTable.COLUMN_MAKE)));
            modelText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(CarTable.COLUMN_MODEL)));
            yearText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(CarTable.COLUMN_YEAR)));
            priceText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(CarTable.COLUMN_PRICE)));
            // always close the cursor
            cursor.close();
        }
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

        // Update the car
        getContentResolver().update(carUri, values, null, null);
    }

    public void showYearDialog(View v) {
        YearPicker yearFragment = new YearPicker();
        yearFragment.setActivityType("details");
        yearFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static void setYear(int year) {
        yearText.setText(String.valueOf(year));
    }

    public void getChart() {
        XYSeries series = new XYSeries("");
        String[] projection = {CarTable.COLUMN_MAKE, CarTable.COLUMN_PRICE };
        Cursor cursor = getContentResolver().query(CarContentProvider.CONTENT_URI, projection, null, null,
                null);
        List<String> text = new ArrayList<>();
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount();i++) {
            text.add(cursor.getString(0));
            series.add(i, cursor.getInt(1));
            cursor.moveToNext();
        }
        cursor.close();

        // Now we create the renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);
        renderer.setColor(Color.BLACK);
        // Include low and max value
        renderer.setDisplayBoundingPoints(true);
        // we add point markers
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(3);

        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setLabelsTextSize(30);
        mRenderer.clearXTextLabels();

        int index = 0;
        for(String str : text) {
            mRenderer.addXTextLabel(index, str);
            mRenderer.setXLabels(0);
            index++;
        }
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setYAxisMax(35);
        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(true); // we show the grid
        mRenderer.setBarSpacing(2);
        mRenderer.setChartTitle("Car prices");
        mRenderer.setChartTitleTextSize(50);
        mRenderer.setLegendTextSize(50);

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        GraphicalView chartView = ChartFactory.getBarChartView(this, dataset, mRenderer, BarChart.Type.DEFAULT);

        LinearLayout chartLyt = (LinearLayout) findViewById(R.id.chartView);
        chartLyt.addView(chartView, 0);
    }

}
