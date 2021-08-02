package com.example.taskapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TaskProvider extends ContentProvider {

    public static final String DBNAME="TaskDatabase";
    public static final String TABLE_TASKS="TaskTable";

    public static final String COLUMN_TASK="Task";
    public static final String COLUMN_NOTE="Note";
    public static final String COLUMN_TASK_DATE="Date";
    public static final String COLUMN_DESTINATION_ADDRESS="DestinationAddress";
    public static final String COLUMN_NOTIFICATION_ID="NotificationID";
    public static final String COLUMN_LATITUDE="Latitude";
    public static final String COLUMN_LONGITUDE="Longitude";
    public static final String COLUMN_DAY="Day";
    public static final String COLUMN_HOUR="Hour";
    public static final String COLUMN_MIN="Minute";
    public static final String COLUMN_YEAR="Year";
    public static final String COLUMN_TOTAL_TIME="TotalTimeMillis";
    public static final String COLUMN_MONTH="Month";
    public static final String COLUMN_PLACE_NAME="PlaceName";
    public static final String COLUMN_ALERT_HOUR="AlertHour";
    public static final String COLUMN_ALERT_MIN="AlertMin";

    public static final String AUTHORITY="com.example.taskapp";
    public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/"+TABLE_TASKS);

    public static final String SQL_CREATE_TABLE="CREATE TABLE " + TABLE_TASKS + "("+" _ID INTEGER PRIMARY KEY, "+
            COLUMN_TASK+" TEXT," + COLUMN_NOTE+ " TEXT," + COLUMN_TASK_DATE+ " TEXT," + COLUMN_DESTINATION_ADDRESS + " TEXT,"+
            COLUMN_NOTIFICATION_ID + " TEXT,"+ COLUMN_LATITUDE+" REAL,"+COLUMN_LONGITUDE+" REAL," + COLUMN_DAY +
            " INTEGER," + COLUMN_HOUR + " INTEGER," + COLUMN_MIN + " INTEGER," + COLUMN_YEAR + " INTEGER,"
           + COLUMN_TOTAL_TIME + " INTEGER," + COLUMN_MONTH + " INTEGER," + COLUMN_PLACE_NAME + " TEXT," + COLUMN_ALERT_HOUR
            + " INTEGER," + COLUMN_ALERT_MIN + " INTEGER)";


    private DataBaseHelper dataBaseHelper;

    protected static final class DataBaseHelper extends SQLiteOpenHelper{

        DataBaseHelper(Context context){

            super(context,DBNAME,null,1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public TaskProvider(){};


    @Override
    public boolean onCreate() {
        dataBaseHelper=new DataBaseHelper(this.getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return dataBaseHelper.getReadableDatabase().query(TABLE_TASKS,projection,selection,selectionArgs,
                null,null,sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //never calling
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        long id=dataBaseHelper.getWritableDatabase().insert(TABLE_TASKS,null,values);
        Uri myuri= Uri.withAppendedPath(CONTENT_URI,""+id);
        return myuri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return dataBaseHelper.getWritableDatabase().delete(TABLE_TASKS,selection,selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return dataBaseHelper.getWritableDatabase().update(TABLE_TASKS,values,selection,selectionArgs);
    }
}
