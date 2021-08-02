package com.example.taskapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.example.taskapp.activities.CreateTaskActivity;
import com.example.taskapp.adapters.TaskAdapter;
import com.example.taskapp.fragments.LongClickDialog;
import com.example.taskapp.models.MyTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LongClickDialog.UpdateTaskList {

    private CardView cvCreateTask;
    private List<MyTask> TaskList;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window=getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.purple_200));
        cvCreateTask=findViewById(R.id.cvAddTaskMain);
        recyclerView=findViewById(R.id.rvTasks);
        cvCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateTaskActivity.class));
            }
        });

        TaskList=new ArrayList<>();
        taskAdapter=new TaskAdapter(this,TaskList,getSupportFragmentManager());
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {         //called when user comes back from creating a task
        super.onResume();
        TaskList.clear();
        QuerySQLTable();
        taskAdapter.notifyDataSetChanged();
    }

    private void QuerySQLTable(){           //Querying sql table and creating objects right away when load program/return from creating new

        final Cursor cursor=getContentResolver().query(TaskProvider.CONTENT_URI,null,null,null,null);
        if (cursor!=null){
            while (cursor.moveToNext()){
               TaskList.add(new MyTask(cursor));
            }
        }
    }

    @Override
    public void updateMyTaskList(int position) {        //when deleting a task
        TaskList.remove(position);
        taskAdapter.notifyItemRemoved(position);
    }
}