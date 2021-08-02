package com.example.taskapp.models;

import android.database.Cursor;
import com.example.taskapp.TaskProvider;
import org.parceler.Parcel;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Parcel
public class MyTask {

    private String Task,Note,PlaceName,PlaceAddress,DateString,Notificationid;
    private boolean AlertSet,DestinationSet,TaskSet,NoteSet,TimeSet;
    private double lat,lng;
    private int year,month,day,hour,min;
    private int alerthour,alertmin;
    private long TotalTimeinMillis;

    public MyTask(){            //default constructor

        AlertSet=false;
        DestinationSet=false;
        TaskSet=false;
        NoteSet=false;
        TimeSet=false;
                                            //SQL stores null for TEXT and 0 for INTEGER/REAL so am not defaulting values
                                            //incase user doesn't fill in those fields
    }

    public MyTask(Cursor cursor){           //Passing in cursor when retrieving from SQL

        InitializeMemberData(cursor);
        InitializeBooleans();

    }

    public void setAlerthour(int alerthour) {
        this.alerthour = alerthour;
    }

    public void setAlertmin(int alertmin) {
        this.alertmin = alertmin;
    }

    public void setPlaceAddress(String placeAddress) {
        PlaceAddress = placeAddress;
        DestinationSet=true;
    }

    public String getPlaceAddress() {
        return PlaceAddress;
    }

    public void SetPlaceName(String placeName){
        this.PlaceName=placeName;
    }

    public void SetTask(String task){
        Task=task;
        TaskSet=true;
    }

    public void SetNote(String note){
        Note=note;
        NoteSet=true;
    }

    public void SetCalendar(Calendar calendar){
        SimpleDateFormat dateFormat;


        if (calendar.get(Calendar.HOUR_OF_DAY)<10){
            dateFormat = new SimpleDateFormat("EEE MMM dd h:mm");
        }

        else if (calendar.get(Calendar.HOUR_OF_DAY)>=10 && calendar.get(Calendar.HOUR_OF_DAY)<=12 ){
            dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm");
        }

        else if (calendar.get(Calendar.HOUR_OF_DAY)>=13 && calendar.get(Calendar.HOUR_OF_DAY)<=19 ){
            dateFormat = new SimpleDateFormat("EEE MMM dd h:mm");
        }

        else{
            dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm");
        }

        DateString=dateFormat.format(calendar.getTime());

        if (calendar.get(Calendar.HOUR_OF_DAY)>12){
            DateString+=" PM";
        }
        else{
            DateString+=" AM";
        }

        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        min=calendar.get(Calendar.MINUTE);

        TotalTimeinMillis=calendar.getTimeInMillis();
        TimeSet=true;
    }

    public void SetLatitude(double lat){
        this.lat=lat;
    }

    public void SetLongitude(double lng){
        this.lng=lng;
    }

    public void setNotificationid(String notificationid) {
        Notificationid = notificationid;
        AlertSet=true;
    }

    public String getNotificationid() {
        return Notificationid;
    }

    public String getDateString() {
        return DateString;
    }

    public String getTask() {
        return Task;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public boolean isDestinationSet() {         //needed for creating the right kind of notification

        return DestinationSet;
    }

    public boolean isAlertSet() {
        return AlertSet;
    }

    public boolean isNoteSet() {
        return NoteSet;
    }

    public boolean isTaskSet() {
        return TaskSet;
    }

    public boolean isTimeSet() {
        return TimeSet;
    }

    public String getNote() {
        return Note;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public int getAlerthour() {
        return alerthour;
    }

    public int getAlertmin() {
        return alertmin;
    }

    public void setAlertSet(boolean alertSet) {
        AlertSet = alertSet;
    }

    public long getTotalTimeinMillis() {
        return TotalTimeinMillis;
    }


    private void InitializeBooleans(){          //setting these important flags when getting sql data
        if (Task==null){
            TaskSet=false;
        }
        else{
            TaskSet=true;
        }

        if (Note==null){
            NoteSet=false;
        }
        else{
            NoteSet=true;
        }

        if (Notificationid==null){
            AlertSet=false;
        }
        else{
            AlertSet=true;
        }

        if (lat==0){
            DestinationSet=false;
        }
        else{
            DestinationSet=true;
        }

        if (year==0){
            TimeSet=false;
        }
        else{
            TimeSet=true;
        }
    }

    private void InitializeMemberData(Cursor cursor){       //creating object from SQL data

        this.Task=cursor.getString(cursor.getColumnIndex(TaskProvider.COLUMN_TASK));
        this.Note=cursor.getString(cursor.getColumnIndex(TaskProvider.COLUMN_NOTE));
        this.DateString=cursor.getString(cursor.getColumnIndex(TaskProvider.COLUMN_TASK_DATE));
        this.PlaceAddress=cursor.getString(cursor.getColumnIndex(TaskProvider.COLUMN_DESTINATION_ADDRESS));
        this.PlaceName=cursor.getString(cursor.getColumnIndex(TaskProvider.COLUMN_PLACE_NAME));
        this.Notificationid=cursor.getString(cursor.getColumnIndex(TaskProvider.COLUMN_NOTIFICATION_ID));
        this.lat=cursor.getDouble(cursor.getColumnIndex(TaskProvider.COLUMN_LATITUDE));
        this.lng=cursor.getDouble(cursor.getColumnIndex(TaskProvider.COLUMN_LONGITUDE));
        this.year=cursor.getInt(cursor.getColumnIndex(TaskProvider.COLUMN_YEAR));
        this.month=cursor.getInt(cursor.getColumnIndex(TaskProvider.COLUMN_MONTH));
        this.day=cursor.getInt(cursor.getColumnIndex(TaskProvider.COLUMN_DAY));
        this.hour=cursor.getInt(cursor.getColumnIndex(TaskProvider.COLUMN_HOUR));
        this.min=cursor.getInt(cursor.getColumnIndex(TaskProvider.COLUMN_MIN));
        this.TotalTimeinMillis=cursor.getLong(cursor.getColumnIndex(TaskProvider.COLUMN_TOTAL_TIME));
        this.alerthour=cursor.getInt(cursor.getColumnIndex(TaskProvider.COLUMN_ALERT_HOUR));
        this.alertmin=cursor.getInt(cursor.getColumnIndex(TaskProvider.COLUMN_ALERT_MIN));
    }
}
