package com.example.taskapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.taskapp.R;
import com.example.taskapp.models.MyTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.UUID;


public class SetAlertTimeFragment extends DialogFragment {

    private MyTask myTask;
    private TextView tvTaskTime;
    private Button btnSetAlert,btnCancel;
    private TimePicker timePicker;
    private SimpleDateFormat simpledateFormat;
    private String datestring;
    private GetAlertTimeInterface getAlertTimeInterface;

    public interface GetAlertTimeInterface{
        public void GetAlertTime(String datestring,int hour,int min);
    }

    public SetAlertTimeFragment() {
        // Required empty public constructor
    }

    public SetAlertTimeFragment(MyTask myTask){

        this.myTask=myTask;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTaskTime=view.findViewById(R.id.tvsetalerttasktime);
        tvTaskTime.setText(myTask.getDateString());


        btnSetAlert=view.findViewById(R.id.btnSetAlertTime);
        btnSetAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleNotificationEvent();              //Either schedules or cancels events accordingly
            }
        });

        btnCancel=view.findViewById(R.id.btnCancelAlert);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        timePicker=view.findViewById(R.id.ThetimePicker);
        if (myTask.isAlertSet()==true){
            timePicker.setHour(myTask.getAlerthour());
            timePicker.setMinute(myTask.getAlertmin());
            btnSetAlert.setText("Cancel Alert");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_alert_time, container, false);
    }

    private void CheckTimeSet(){            //creating string to display for date and time for set notification

        datestring="";

        if (timePicker.getHour()%12==0){
            datestring+="12";
        }
        else{
            datestring+=String.valueOf(timePicker.getHour()%12);
        }

        if (timePicker.getMinute()<10){
            datestring+=":0"+ timePicker.getMinute();
        }
        else{
            datestring+=":"+ timePicker.getMinute();
        }

        if (timePicker.getHour()>12){
            datestring+=" PM";
        }
        else{
            datestring+=" AM";
        }
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        if (context instanceof SetAlertTimeFragment.GetAlertTimeInterface){
            getAlertTimeInterface=(SetAlertTimeFragment.GetAlertTimeInterface)context;
        }
        else{
            throw new RuntimeException("error implementing alert interface");
        }
    }

    private void HandleNotificationEvent(){
        CheckTimeSet();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (myTask.isAlertSet() == true) {
            builder.setMessage("Cancel Notification at " + datestring + "?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    WorkManager.getInstance().cancelWorkById(UUID.fromString(myTask.getNotificationid()));
                    Toast.makeText(getContext(),"Notification cancelled",Toast.LENGTH_SHORT).show();
                    myTask.setAlertSet(false);
                    dismiss();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.create().show();

        }

        else {
            builder.setMessage("Receive Notification at " + datestring + "?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getAlertTimeInterface.GetAlertTime(datestring, timePicker.getHour(), timePicker.getMinute());
                    dismiss();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.create().show();
        }

    }
}