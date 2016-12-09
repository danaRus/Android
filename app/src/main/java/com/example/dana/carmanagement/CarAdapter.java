package com.example.dana.carmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dana.carmanagement.model.Car;

import java.util.List;

public class CarAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Car> cars;

    public CarAdapter(Context context, List<Car> cars) {
        this.cars = cars;
        this.context = context;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cars.size();
    }

    @Override
    public Object getItem(int position) {
        return cars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contextView, ViewGroup parent) {
        // Get the view for the row item
        View rowView = layoutInflater.inflate(R.layout.item, parent, false);

        TextView makeTextView = (TextView) rowView.findViewById(R.id.makeItem);
        //TextView modelTextView = (TextView) rowView.findViewById(R.id.model);

        Car car = (Car) getItem(position);
        makeTextView.setText(car.getMake());
        //modelTextView.setText(car.getModel());

        return rowView;
    }

    @Override
    public void notifyDataSetChanged() {
        // used when the data is changed in the details view
        super.notifyDataSetChanged();
    }
}
