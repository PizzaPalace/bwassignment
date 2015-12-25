package com.assignment.gre.json;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.assignment.gre.common.Constants;
import com.assignment.gre.common.DatabaseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by rahul on 25/12/15.
 */
public class JSONHelper {

    public static ArrayList<HashMap<String,Object>> requestData(RequestQueue requestQueue, String url) {
        ArrayList<HashMap<String,Object>> data = null;
        JSONObject response = null;
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                requestFuture,
                requestFuture);

        requestQueue.add(request);
        try {
            response = requestFuture.get(1000, TimeUnit.MILLISECONDS);
            data = jsonParser(response);

        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            //L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        }
        return data;
    }

    public static ArrayList<HashMap<String,Object>> jsonParser(JSONObject jsonObject) {

        try {

            ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

            JSONArray jsonArray = jsonObject.getJSONArray("words");

            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                HashMap<String,Object> map = new HashMap<String,Object>();
                JSONObject jObject = jsonArray.getJSONObject(i);

                map.put(Constants.ID, jObject.getInt("id"));
                map.put(Constants.WORD, jObject.getString("word"));
                map.put(Constants.MEANING, jObject.getString("meaning"));
                map.put(Constants.RATIO, jObject.getDouble("ratio"));

                list.add(map);
                map = null;

            }

            return list;
        }
        catch(JSONException exception){
            exception.printStackTrace();
            return null;
        }
    }



}
