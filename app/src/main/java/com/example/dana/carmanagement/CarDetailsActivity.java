package com.example.dana.carmanagement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dana.carmanagement.model.Car;
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
import java.util.Map;

public class CarDetailsActivity extends AppCompatActivity {
    private EditText makeText;
    private EditText modelText;
    private static TextView yearText;
    private EditText priceText;
    private List<String> models;
    private List<Integer> prices;
    private String uuid;

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

        Intent i = getIntent();
        String make = i.getExtras().getString("make");
        String model = i.getExtras().getString("model");
        String year = i.getExtras().getString("year");
        String price = i.getExtras().getString("price");
        uuid = i.getExtras().getString("uuid");
        models = i.getExtras().getStringArrayList("models");
        prices = i.getExtras().getIntegerArrayList("prices");

        makeText.setText(make);
        modelText.setText(model);
        yearText.setText(year);
        priceText.setText(price);

        getChart();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(makeText.getText().toString()) || TextUtils.isEmpty(modelText.getText().toString())
                        || TextUtils.isEmpty(yearText.getText().toString()) || TextUtils.isEmpty(priceText.getText().toString())) {
                    Toast.makeText(view.getContext(), "No field can be empty.", Toast.LENGTH_SHORT).show();
                } else {
                    final Car car = new Car();
                    car.setMake(makeText.getText().toString());
                    car.setModel(modelText.getText().toString());
                    car.setYear(Integer.valueOf(yearText.getText().toString()));
                    car.setPrice(Integer.valueOf(priceText.getText().toString()));
                    car.setUuid(uuid);

                    Map<String,Object> carMap = car.carToMap();
                    MainActivity.firebaseUtil.update(uuid, carMap);

                    Toast.makeText(view.getContext(), "The car's data has been modified", Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
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
        List<String> text = new ArrayList<>();

        for(int i = 0; i < models.size();i++) {
            text.add(models.get(i));
            series.add(i, prices.get(i));
        }

        // Create the renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);
        renderer.setColor(Color.BLACK);
        // Include low and max value
        renderer.setDisplayBoundingPoints(true);
        // Add point markers
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
        mRenderer.setShowGrid(true); // Show the grid
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
