package com.example.dana.carmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dana.carmanagement.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CarAdapter extends BaseAdapter implements Observer {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Car> cars;

    public CarAdapter(Context context){
        this.cars = new ArrayList<>();
        this.context = context;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(Car car) {
        cars.add(car);
    }

    public void update(Car car) {
        for(Car c : cars) {
            if(c.getUuid().equals(car.getUuid())) {
                c.setMake(car.getMake());
                c.setModel(car.getModel());
                c.setYear(car.getYear());
                c.setPrice(car.getPrice());
                return;
            }
        }
    }

    public void remove(Car car) {
        cars.remove(car);
    }

    @Override
    public void update(Observable o, Object arg) {
        notifyDataSetChanged();
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

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public List<String> getModels() {
        List<String> models= new ArrayList<>();
        for(Car car:cars) {
            models.add(car.getModel());
        }
        return models;
    }

    public List<Integer> getPrices() {
        List<Integer> prices= new ArrayList<>();
        for(Car car:cars) {
            prices.add(car.getPrice());
        }
        return prices;
    }

}
