package com.example.dana.carmanagement.pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import com.example.dana.carmanagement.CarDetailsActivity;
import com.example.dana.carmanagement.NewCarActivity;

import java.util.Calendar;

public class YearPicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private String activityType;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog, this, year, 0, 0);

        dpd.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        dpd.getDatePicker().findViewById(Resources.getSystem().getIdentifier("month", "id", "android")).setVisibility(View.GONE);

        return dpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (activityType.equals("details")) {
            CarDetailsActivity.setYear(year);
        } else {
            NewCarActivity.setYear(year);
        }
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

}
