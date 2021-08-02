package com.example.taskapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaceResult {

    private String name,address;
    private double lat,lng;
    private boolean IsResidence;

    public PlaceResult(JSONObject jsonObject) throws JSONException {
        name=jsonObject.getString("name");
        address=jsonObject.getString("formatted_address");
        lat=jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        lng=jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        JSONArray jsonArray=jsonObject.getJSONArray("types");
        CheckIfPremise(jsonArray);
        FinalizeName();
    }

    private void CheckIfPremise(JSONArray jsonArray) throws JSONException{

        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.get(i).equals("premise")){
                IsResidence=true;
                return;
            }
        }
        IsResidence=false;
    }

    private void FinalizeName(){        //if location is a residence-name will be same as address in JSON, so only showing city for the name
        if (IsResidence==true) {
            String temp="";
            boolean HitComma=false;

            for (int i = 0; i < address.length(); i++) {

                if (HitComma==true){
                    temp+=address.charAt(i);
                }

                if (address.charAt(i)==',' && HitComma==false){
                    HitComma=true;
                }
            }
            name=new String(temp);
        }
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
