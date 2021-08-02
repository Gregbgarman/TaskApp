package com.example.taskapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taskapp.R;

import org.jetbrains.annotations.NotNull;


public class AdapterShowNoteDialog extends DialogFragment {

   private String InfoToShow;
   private TextView tvNote,tvOverview;
   private boolean showAlert;

    public AdapterShowNoteDialog() {
        // Required empty public constructor
    }

    public AdapterShowNoteDialog(String InfoToShow,boolean showAlert){
        this.InfoToShow=InfoToShow;
        this.showAlert=showAlert;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNote=view.findViewById(R.id.tvFASNote);
        tvOverview=view.findViewById(R.id.tvFASOverview);
        if (showAlert==false) {
            tvOverview.setText("NOTE");
        }
        else{
            tvOverview.setText("Alert Time");
        }
        tvNote.setText(InfoToShow);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adapter_show_note_dialog, container, false);
    }
}