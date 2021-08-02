package com.example.taskapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.taskapp.activities.CreateTaskActivity;
import com.example.taskapp.models.MyTask;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TheWorker extends Worker {         //used with WorkManager

    private Context context;
    private NotificationCompat.Builder builder;

    public TheWorker(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {

        if (CreateTaskActivity.myTask.isDestinationSet()==true){
           SetUpWithDirections();
        }

        else{
            SetUpWithoutDirections();
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Random random=new Random();
        int notificationId = random.nextInt(10000000);

        notificationManager.notify(notificationId, builder.build());
        return Result.success();
    }

    private void SetUpWithDirections(){         //setting up notification so it will pop up with option to load google maps app
                                                //will only happen when user has set a destination when creating a task

        Uri uri=Uri.parse("geo:"+CreateTaskActivity.myTask.getLat() +"," + CreateTaskActivity.myTask.getLng()+"?q="+CreateTaskActivity.myTask.getPlaceAddress() );
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");

        PendingIntent pendingIntent=PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder = new NotificationCompat.Builder(context, "TASKAPP")
                .setSmallIcon(R.drawable.taskaction)
                .setContentTitle(CreateTaskActivity.myTask.getTask())
                .setContentText(CreateTaskActivity.myTask.getDateString())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setColor(Color.parseColor("#BB86FC"))
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.locationpin,"Get Directions",pendingIntent);
    }

    private void SetUpWithoutDirections(){          //notification will not have option to load google maps because no destination set

        builder = new NotificationCompat.Builder(context, "TASKAPP")
                .setSmallIcon(R.drawable.taskaction)
                .setContentTitle(CreateTaskActivity.myTask.getTask())
                .setContentText(CreateTaskActivity.myTask.getDateString())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setColor(Color.parseColor("#BB86FC"));
    }
}
