package com.example.taskapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.taskapp.models.MyTask;

import java.util.Calendar;

public class TimePickerDialog extends DialogFragment {

    private MyTask myTask;

    public TimePickerDialog(MyTask myTask){
        this.myTask=myTask;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour,minute;
        if (myTask.isTimeSet()==true){
            hour = myTask.getHour();
            minute = myTask.getMin();
        }
        else{
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }

        return new android.app.TimePickerDialog( getActivity(), (android.app.TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

}
