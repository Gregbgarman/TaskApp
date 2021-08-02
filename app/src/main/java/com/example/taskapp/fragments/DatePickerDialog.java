package com.example.taskapp.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.taskapp.models.MyTask;

import java.util.Calendar;

public class DatePickerDialog extends DialogFragment {

    private MyTask myTask;

    public DatePickerDialog(MyTask myTask){
        this.myTask=myTask;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year,month,day;

        if (myTask.isTimeSet()==true){
            year = myTask.getYear();
            month = myTask.getMonth();
            day = myTask.getDay();
        }
        else{
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }


        return new android.app.DatePickerDialog(getActivity(), (android.app.DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
    }


}
