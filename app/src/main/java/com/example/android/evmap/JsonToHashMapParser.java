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
        String placeName = "";
        String vicinity= "";
        String latitude= "";
        String longitude="";
        //String reference="";
        String place_id = "";
//        String photo_height="100";
//        String photo_width="100";
//        String photo_reference = "";
        String rating = "";
        Boolean open_now_bool = true;
        JSONObject opening_hours = null;
        String open_now_Str = "Open now.";
//
//        Object photo = null;
//        JSONArray photos = null;

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }

            latitude = Double.toString (googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
            longitude = Double.toString (googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

            if (!googlePlaceJson.isNull("rating")) {
                rating = Double.toString(googlePlaceJson.getDouble("rating"));
            }
            if (!googlePlaceJson.isNull("place_id")) {
                place_id =googlePlaceJson.getString("place_id");
            }
            if (!googlePlaceJson.isNull("opening_hours")) {
                opening_hours =googlePlaceJson.getJSONObject("opening_hours");
                if (!opening_hours.isNull("open_now")) {

                    try {
                        open_now_bool = opening_hours.getBoolean("open_now");
                        if (open_now_bool) {
                            open_now_Str = "Cpen now.";
                        } else {
                            open_now_Str = "Close now.";
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("rating", rating);
            googlePlaceMap.put("place_id", place_id);
            googlePlaceMap.put("open_now_Str", open_now_Str);

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

