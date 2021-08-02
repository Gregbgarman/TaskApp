package com.example.taskapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.taskapp.R;
import com.example.taskapp.TaskProvider;
import com.example.taskapp.activities.CreateTaskActivity;
import com.example.taskapp.models.MyTask;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;


public class LongClickDialog extends DialogFragment implements View.OnClickListener {

    //This is the dialog that pops up when user long clicks a task on home screen

    private static final String TAG = "LongClickDialog";
    private Button btnDelete,btnEdit;
    private MyTask myTask;
    private UpdateTaskList updateTaskList;
    private int TaskPosition;

    public interface UpdateTaskList{
        public void updateMyTaskList(int position);
    }

    public LongClickDialog() {
        // Required empty public constructor
    }

    public LongClickDialog(MyTask myTask, int TaskPosition){
        this.myTask=myTask;
        this.TaskPosition=TaskPosition;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDelete=view.findViewById(R.id.btnLCDDeleteTask);btnDelete.setOnClickListener(this);
        btnEdit=view.findViewById(R.id.btnLCDedit);btnEdit.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_long_click_dialog, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLCDedit:
                OpenCreateTask();
                break;

            case R.id.btnLCDDeleteTask:
                DeleteTask();
                updateTaskList.updateMyTaskList(TaskPosition);
                dismiss();
                break;
        }
    }

    private void DeleteTask(){
        String[] selectionargs=new String[]{myTask.getTask()};
        String SelectionClause= TaskProvider.COLUMN_TASK + "=?";

        try {
            getContext().getContentResolver().delete(TaskProvider.CONTENT_URI, SelectionClause, selectionargs);
            Toast.makeText(getContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e(TAG,"Task not deleted-error");
        }

    }


    private void OpenCreateTask(){
        Intent intent=new Intent(getContext(), CreateTaskActivity.class);
        intent.putExtra("TaskObject", Parcels.wrap(myTask));
        dismiss();
        getContext().startActivity(intent);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if (context instanceof LongClickDialog.UpdateTaskList){
            updateTaskList=(LongClickDialog.UpdateTaskList)context;
        }
        else{
            throw new RuntimeException("Long click dialog interface failure");
        }

    }
}