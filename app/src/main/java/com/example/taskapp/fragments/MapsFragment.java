package com.example.taskapp.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.taskapp.R;
import com.example.taskapp.adapters.PlaceAdapter;
import com.example.taskapp.models.MyTask;
import com.example.taskapp.models.PlaceResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import android.view.inputmethod.InputMethodManager;

import com.example.taskapp.activities.CreateTaskActivity;

public class MapsFragment extends DialogFragment {

    private static final String TAG = "MapsFragment";
    private Button btnSave,btnCancel;
    private EditText etEntry;
    private SaveMapInterface saveMapInterface;
    private GoogleMap MAP;
    private PlaceResult FinalplaceResult;
    private List<PlaceResult> PlaceList;
    private PlaceAdapter placeAdapter;
    private RecyclerView recyclerView;
    private LinearLayout MapLayout;
    private TextView tvSaveMessage;
    private RelativeLayout PlacesListLayout;
    private MyTask myTask;

    public interface SaveMapInterface{
        public void SaveMap(PlaceResult placeResult);
    }

    public MapsFragment(MyTask myTask){
        this.myTask=myTask;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MAP=googleMap;
            if (myTask.isDestinationSet()==true){
                ShowSaveButton();
                MAP.addMarker(new MarkerOptions().position(new LatLng(myTask.getLat(),myTask.getLng())).title(myTask.getPlaceName())).showInfoWindow();
                MAP.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myTask.getLat(),myTask.getLng()),10));

            }
            else {
                MAP.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(39.0977, -94.56)));      //centering view over US
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.TheMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        PlaceList=new ArrayList();
        PlacesListLayout=view.findViewById(R.id.MyRelativeLayout);
        tvSaveMessage=view.findViewById(R.id.tvLocationmsg);
        btnSave=view.findViewById(R.id.btnSaveMap);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setMessage("Set " + FinalplaceResult.getAddress() + " As the Destination?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveMapInterface.SaveMap(FinalplaceResult);
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
        });

        btnCancel=view.findViewById(R.id.btnCancelMap);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        etEntry=view.findViewById(R.id.etMap);
        etEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //not using
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placeAdapter.notifyDataSetChanged();
                HideSaveButton();
                CheckLocations(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //not using
            }
        });

        PlaceAdapter.GetLocationInterface getLocationInterface=new PlaceAdapter.GetLocationInterface() {
            @Override
            public void GetLocation(PlaceResult placeResult) {

                etEntry.setText("");
                FinalplaceResult=placeResult;
                LatLng latLng=new LatLng(placeResult.getLat(),placeResult.getLng());
                MAP.addMarker(new MarkerOptions().position(latLng).title(placeResult.getName())).showInfoWindow();
                MAP.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                CloseKeyboard();
                ShowSaveButton();

            }
        };

        MapLayout=view.findViewById(R.id.LinearLayoutMap);
        recyclerView=view.findViewById(R.id.maprecyclerview);
        placeAdapter=new PlaceAdapter(getContext(),PlaceList,getLocationInterface);
        recyclerView.setAdapter(placeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void CheckLocations(CharSequence s){

        String searchstring="https://maps.googleapis.com/maps/api/place/textsearch/json?&query="+s+
                "&key="+getResources().getString(R.string.google_maps_key);

        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        asyncHttpClient.get(searchstring, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                JSONObject jsonObject= json.jsonObject;

                try {
                    PlaceList.clear();
                    JSONArray jsonArray= jsonObject.getJSONArray("results");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        if (j==10){
                            return;
                        }
                        PlaceList.add(new PlaceResult(jsonArray.getJSONObject(j)));

                    }
                    placeAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.e(TAG,"failed to query json data");
            }
        });

    }

    private void ShowSaveButton(){                      //after done typing
        PlacesListLayout.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        btnSave.setVisibility(View.VISIBLE);
        tvSaveMessage.setVisibility(View.VISIBLE);
    }

    private void HideSaveButton(){              //while typing location
        btnSave.setVisibility(View.INVISIBLE);
        tvSaveMessage.setVisibility(View.INVISIBLE);
        PlacesListLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void CloseKeyboard() {
       View v=getView().findFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        if(context instanceof MapsFragment.SaveMapInterface){
           saveMapInterface = (MapsFragment.SaveMapInterface) context;
        }

        else{
            throw new RuntimeException(context.toString()+"must implement UpdateAdapterInterface");
        }
    }
}