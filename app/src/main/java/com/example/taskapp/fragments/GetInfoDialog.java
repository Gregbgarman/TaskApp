package com.example.taskapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskapp.R;
import com.example.taskapp.models.MyTask;


public class GetInfoDialog extends DialogFragment {

    private String type,InfoEntered;
    private TextView tvMessage;
    private EditText etInfo;
    private GetTask getTask;
    private GetNote getNote;
    private MyTask myTask;

    public interface GetTask{
        public void GetTask(String task);
    }

    public interface GetNote{
        public void GetNote(String note);
    }


    public GetInfoDialog() {
        // Required empty public constructor
    }

    public GetInfoDialog(String type, MyTask myTask){

        this.type=type;
        this.myTask=myTask;
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvMessage=view.findViewById(R.id.tvDialogMsg);
        etInfo=view.findViewById(R.id.etInfo);

        view.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etInfo.getText().toString().trim().equals("")){
                    if (type.equals("Task")) {
                        Toast.makeText(getContext(), "Please Enter A Task", Toast.LENGTH_SHORT).show();
                    }

                    else if (type.equals("Note")){
                        Toast.makeText(getContext(), "Please Enter A Note", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    if (type.equals("Task")) {
                        getTask.GetTask(etInfo.getText().toString());

                    }

                    else if (type.equals("Note")){
                        getNote.GetNote(etInfo.getText().toString());
                    }
                    dismiss();
                }

            }
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (type.equals("Task")){
            tvMessage.setText("Enter Task");
            etInfo.setHint("Enter Task Here");
            if (myTask.isTaskSet()==true){              //set to true/false back in previous activity
                etInfo.setText(myTask.getTask());
            }

        }
        else if (type.equals("Note")){
            tvMessage.setText("Add Note");
            etInfo.setHint("Enter Note Here");
            if (myTask.isNoteSet()==true){
                etInfo.setText(myTask.getNote());
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {    //attaching interface
        super.onAttach(context);

        if(context instanceof GetInfoDialog.GetTask){
            getTask = (GetInfoDialog.GetTask) context;
        }

        if(context instanceof GetInfoDialog.GetNote){
            getNote = (GetInfoDialog.GetNote) context;
        }

        else{
            throw new RuntimeException(context.toString()+"must implement infodialog Interface");
        }
    }

}