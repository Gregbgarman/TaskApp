package com.example.taskapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.taskapp.R;
import com.example.taskapp.TaskProvider;
import com.example.taskapp.TheWorker;
import com.example.taskapp.fragments.GetInfoDialog;
import com.example.taskapp.fragments.MapsFragment;
import com.example.taskapp.fragments.SetAlertTimeFragment;
import com.example.taskapp.models.MyTask;
import com.example.taskapp.models.PlaceResult;
import org.parceler.Parcels;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CreateTaskActivity extends AppCompatActivity implements View.OnClickListener,
        GetInfoDialog.GetNote,GetInfoDialog.GetTask, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,MapsFragment.SaveMapInterface,SetAlertTimeFragment.GetAlertTimeInterface {

    private static final String TAG = "CreateTaskActivity";
    private TextView tvTaskOverview,tvDestinationOverview,tvDateOverview,tvNoteOverview,tvAlertOverview;
    private CardView cvTask,cvNotes,cvDateandTime,cvDestination,cvAlert,cvSubmit;
    private Calendar MyCalendar;
    private ImageView ivTaskComplete,ivDateSet,ivNoteComplete,ivDestinationSet,ivTask,
            ivCVtask,ivCValert,ivCVDate,ivCVDestination,ivCVNote;
    public static MyTask myTask;
    private boolean DateSet,EditingOldTask;
    private String[] selectionargs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        getSupportActionBar().hide();
        Window window=getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.purple_200));


        if(Parcels.unwrap(getIntent().getParcelableExtra("TaskObject"))!=null){
            myTask=Parcels.unwrap(getIntent().getParcelableExtra("TaskObject"));
            EditingOldTask=true;
            selectionargs=new String[]{myTask.getTask()};       //getting task name now to delete it when user makes new one
        }                                                       // new task created when user makes edit to old task

        else {
            myTask = new MyTask();
            EditingOldTask=false;
        }

        InitializeViews();
        CheckFilledInformation();       //used for checking what has been entered already
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cvTask:
               ShowDialog("Task");
                break;
            case R.id.cvNote:
                if (myTask.isTaskSet()==true) {
                    ShowDialog("Note");
                }
                else{
                    Toast.makeText(this, "Please Enter Task", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cvDate:
                if (myTask.isTaskSet()==true) {
                    GetDateandTime();
                }
                else{
                    Toast.makeText(this, "Please Enter Task", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cvMap:
                if (myTask.isTaskSet()==true){
                    ShowMap();
                }
                else{
                    Toast.makeText(this, "Please Enter Task", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.cvAlert:
                if (myTask.isTaskSet()==true && myTask.isTimeSet()==true){
                    SetAlertTime();
                }
                else{
                    Toast.makeText(this, "Please Enter Task and Time", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.CVSUBMIT:
                if (myTask.isTaskSet()==true) {
                    ShowFinishMessage();
                }
                else{
                    Toast.makeText(this,"Please Enter A Task",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void ShowDialog(String type){           //used for setting task name and note-called with button click

        GetInfoDialog getInfoDialog=new GetInfoDialog(type,myTask);
        getInfoDialog.show(getSupportFragmentManager(),"getinfo");

    }

    private void SetAlertTime(){            //used for setting alert time-called with button click

        SetAlertTimeFragment setAlertTimeFragment=new SetAlertTimeFragment(myTask);
        setAlertTimeFragment.show(getSupportFragmentManager(),"alert fragment");

    }

    private void GetDateandTime(){      //called from button click to get date and time of task

        DialogFragment timePicker = new com.example.taskapp.fragments.TimePickerDialog(myTask);
        timePicker.show(getSupportFragmentManager(), "time picker");

        DialogFragment datePicker = new com.example.taskapp.fragments.DatePickerDialog(myTask);
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    private void ShowMap(){
        MapsFragment mapsFragment=new MapsFragment(myTask);
        mapsFragment.show(getSupportFragmentManager(),"maps");
    }

    @Override
    public void GetTask(String task) {          //setting task-interface method
        myTask.SetTask(task);
        CheckFilledInformation();
    }

    @Override
    public void GetNote(String note) {          //note set-interface method
        myTask.SetNote(note);
        CheckFilledInformation();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {       //called after date dialog ends
        MyCalendar.set(Calendar.YEAR, year);
        MyCalendar.set(Calendar.MONTH, month);
        MyCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        DateSet=true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {         //time and date saved when time dialog ends
        if (DateSet==true) {                                                    //this ensures saving only happens when both date and time get entered-not messed up with cancelling
            MyCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            MyCalendar.set(Calendar.MINUTE, minute);
            myTask.SetCalendar(MyCalendar);
            CheckFilledInformation();
        }
    }

    @Override
    public void SaveMap(PlaceResult placeResult) {          //destination saved-interface method
        myTask.SetLatitude(placeResult.getLat());
        myTask.SetLongitude(placeResult.getLng());
        myTask.SetPlaceName(placeResult.getName());
        myTask.setPlaceAddress(placeResult.getAddress());
        CheckFilledInformation();
    }

    @Override
    public void GetAlertTime(String datestring, int hour, int min) {        //interface method
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,MyCalendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH,MyCalendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH,MyCalendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,min);

        myTask.setAlerthour(hour);              //storing these so it is restored when opening the dialog fragment
        myTask.setAlertmin(min);                //can figure out am/pm from what the hour is

        createNotificationChannel();
        ScheduleNotification(calendar.getTimeInMillis());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "GregTaskApp";
            String description = "WorkManager";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("TASKAPP", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

   private void ScheduleNotification(long ScheduledTime){
       long currenttime=Calendar.getInstance().getTimeInMillis();
       long schedule=(ScheduledTime-currenttime);

       OneTimeWorkRequest oneTimeWorkRequest=new OneTimeWorkRequest.Builder(TheWorker.class)        //notification only scheduled once
               .setInitialDelay(schedule, TimeUnit.MILLISECONDS)        //notification delay determined by subtracting real time from desired notification time
               .build();

       UUID id=oneTimeWorkRequest.getId();
       WorkManager.getInstance().enqueue(oneTimeWorkRequest);

       myTask.setNotificationid(id.toString());
       CheckFilledInformation();
   }

   private void CheckFilledInformation(){           //updating views accordingly

        if (myTask.isTimeSet()==true){
            ivDateSet.setVisibility(View.VISIBLE);
            tvDateOverview.setVisibility(View.INVISIBLE);
            ivCVDate.setImageResource(R.drawable.pen);
        }
        else{
            ivDateSet.setVisibility(View.INVISIBLE);
            tvDateOverview.setVisibility(View.VISIBLE);
            tvDateOverview.setText("N/A");
        }

        if (myTask.isTaskSet()==true){
            ivTaskComplete.setVisibility(View.VISIBLE);
            tvTaskOverview.setVisibility(View.INVISIBLE);
            ivCVtask.setImageResource(R.drawable.pen);

        }
        else{
            ivTaskComplete.setVisibility(View.INVISIBLE);
            tvTaskOverview.setVisibility(View.VISIBLE);
            tvTaskOverview.setText("N/A");
        }

        if (myTask.isDestinationSet()==true){
            ivDestinationSet.setVisibility(View.VISIBLE);
            tvDestinationOverview.setVisibility(View.INVISIBLE);
            ivCVDestination.setImageResource(R.drawable.pen);
        }
        else{
            ivDestinationSet.setVisibility(View.INVISIBLE);
            tvDestinationOverview.setVisibility(View.VISIBLE);
            tvDestinationOverview.setText("N/A");
        }

        if (myTask.isNoteSet()==true){
            ivNoteComplete.setVisibility(View.VISIBLE);
            tvNoteOverview.setVisibility(View.INVISIBLE);
            ivCVNote.setImageResource(R.drawable.pen);
        }
        else{
            ivNoteComplete.setVisibility(View.INVISIBLE);
            tvNoteOverview.setVisibility(View.VISIBLE);
            tvNoteOverview.setText("N/A");
        }

        if (myTask.isAlertSet()==true){
            tvAlertOverview.setText("ON");
            ivCValert.setImageResource(R.drawable.pen);
        }
        else{
            tvAlertOverview.setText("OFF");
        }
   }

   private void ShowFinishMessage(){            //pops up when user clicks button to save task

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Save this Task?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveTask();
                if (myTask.isTimeSet()==true) {
                    GoogleCalendarMessage();
                }
                else{
                    finish();
                }

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
   }

   private void GoogleCalendarMessage(){
       AlertDialog.Builder builder=new AlertDialog.Builder(this);
       builder.setMessage("Would you like to add this task to Google Calendar?");
       builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               AddtoGoogleCalendar();
               finish();
           }
       });
       builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               finish();
           }
       });
       builder.create().show();

   }

   private void AddtoGoogleCalendar(){          //accesses calendar app on phone

       Intent intent=new Intent(Intent.ACTION_INSERT)
               .setData(CalendarContract.Events.CONTENT_URI)
               .putExtra(CalendarContract.Events.TITLE,myTask.getTask())
               .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,MyCalendar.getTimeInMillis())
               .putExtra(CalendarContract.Events.EVENT_LOCATION,myTask.getPlaceAddress())
               .putExtra(CalendarContract.Events.DESCRIPTION,myTask.getNote());

       if (intent.resolveActivity(getPackageManager())!=null){
           startActivity(intent);
       }
       else{
           Toast.makeText(CreateTaskActivity.this, "couldnt save to google calendar", Toast.LENGTH_SHORT).show();
       }
   }

    private void SaveTask(){            //saving with sqlite

        if (EditingOldTask==true){      //if editing old task, not truly doing an overwrite, deleting old one and making new one
            DeleteTask();
        }

        ContentValues contentValues=new ContentValues();
        contentValues.put(TaskProvider.COLUMN_TASK,myTask.getTask());
        contentValues.put(TaskProvider.COLUMN_NOTE,myTask.getNote());
        contentValues.put(TaskProvider.COLUMN_TASK_DATE,myTask.getDateString());
        contentValues.put(TaskProvider.COLUMN_DESTINATION_ADDRESS,myTask.getPlaceAddress());
        contentValues.put(TaskProvider.COLUMN_NOTIFICATION_ID,myTask.getNotificationid());
        contentValues.put(TaskProvider.COLUMN_LATITUDE,myTask.getLat());
        contentValues.put(TaskProvider.COLUMN_LONGITUDE,myTask.getLng());
        contentValues.put(TaskProvider.COLUMN_DAY,myTask.getDay());
        contentValues.put(TaskProvider.COLUMN_HOUR,myTask.getHour());
        contentValues.put(TaskProvider.COLUMN_MIN,myTask.getMin());
        contentValues.put(TaskProvider.COLUMN_YEAR,myTask.getYear());
        contentValues.put(TaskProvider.COLUMN_TOTAL_TIME,myTask.getTotalTimeinMillis());
        contentValues.put(TaskProvider.COLUMN_MONTH,myTask.getMonth());
        contentValues.put(TaskProvider.COLUMN_PLACE_NAME,myTask.getPlaceName());
        contentValues.put(TaskProvider.COLUMN_ALERT_HOUR,myTask.getAlerthour());
        contentValues.put(TaskProvider.COLUMN_ALERT_MIN,myTask.getAlertmin());

        try {
            getContentResolver().insert(TaskProvider.CONTENT_URI, contentValues);
            Toast.makeText(this,"Task Saved",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e(TAG,"Task not saved");
        }
    }

    private void DeleteTask(){

        String SelectionClause= TaskProvider.COLUMN_TASK + "=?";
        try {
           getContentResolver().delete(TaskProvider.CONTENT_URI, SelectionClause, selectionargs);
        }catch (Exception e){
            Log.e(TAG,"Old Task not deleted-error");
        }
    }

    private void InitializeViews(){         //boilerplate code of setting views

        tvTaskOverview=findViewById(R.id.tvTaskOverview);
        tvDestinationOverview=findViewById(R.id.tvDestinationOverview);
        tvDateOverview=findViewById(R.id.tvDateOverview);
        tvNoteOverview=findViewById(R.id.tvNoteOverview);
        tvAlertOverview=findViewById(R.id.tvAlertOverview);
        cvTask=findViewById(R.id.cvTask);cvTask.setOnClickListener(this);
        cvNotes=findViewById(R.id.cvNote);cvNotes.setOnClickListener(this);
        cvDateandTime=findViewById(R.id.cvDate);cvDateandTime.setOnClickListener(this);
        cvDestination=findViewById(R.id.cvMap);cvDestination.setOnClickListener(this);
        cvAlert=findViewById(R.id.cvAlert);cvAlert.setOnClickListener(this);
        MyCalendar=Calendar.getInstance();
        ivTaskComplete=findViewById(R.id.ivTaskComplete);
        ivDateSet=findViewById(R.id.ivDateSet);
        ivDestinationSet=findViewById(R.id.ivDestinationSet);
        ivNoteComplete=findViewById(R.id.ivNoteComplete);
        ivTask=findViewById(R.id.ivivivTask);
        Glide.with(this).load(R.drawable.taskaction).into(ivTask);
        ivCVtask=findViewById(R.id.ivCVTASK);
        ivCValert=findViewById(R.id.ivCVALERT);
        ivCVDestination=findViewById(R.id.ivCVDESTINATION);
        ivCVNote=findViewById(R.id.ivCVNOTE);
        ivCVDate=findViewById(R.id.ivCVDATE);
        cvSubmit=findViewById(R.id.CVSUBMIT);cvSubmit.setOnClickListener(this);
        DateSet=false;
    }
}