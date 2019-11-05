package com.example.android.evmap;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonToHashMapParser {
    private HashMap<String, String> getPlace(JSONObject googlePlaceJson)
    {
        String jsonStr = googlePlaceJson.toString();
        Log.d("JSON_string: ", jsonStr);
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String placeName = "--NA--";
        String vicinity= "--NA--";
        String latitude= "";
        String longitude="";
        //String reference="";
        String place_id = "";
        String photo_height="100";
        String photo_width="100";
        String photo_reference = "";
        String rating = "";
        Log.d("JSONdataParser","jsonobject ="+googlePlaceJson.toString());
        Object photo = null;
        JSONArray photos = null;
        //String photo_reference = "";


        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }

            latitude = Double.toString (googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
            longitude = Double.toString (googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
            //longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            //reference = googlePlaceJson.getString("reference");
            //place_id = googlePlaceJson.getString("place_id");


            //photo_height = googlePlaceJson.getJSONArray("photos").get(0).getInt("height");
            //photos = googlePlaceJson.getJSONArray("photos");
            //photo = photos.get(0);

            if (!googlePlaceJson.isNull("rating")) {
                rating = Double.toString(googlePlaceJson.getDouble("rating"));
            }
            if (!googlePlaceJson.isNull("place_id")) {
                place_id =googlePlaceJson.getString("place_id");
            }







            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            //googlePlaceMap.put("reference", reference);

            googlePlaceMap.put("rating", rating);
            googlePlaceMap.put("place_id", place_id);





        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap;

    }
    private List<HashMap<String, String>>getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String, String>> placelist = new ArrayList<>();
        HashMap<String, String> placeMap = null;

        for(int i = 0; i<count;i++)
        {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placelist.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placelist;
    }

    public List<HashMap<String, String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        Log.d("json data", jsonData);

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }
}

