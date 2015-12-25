package com.assignment.gre.json;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.assignment.gre.adapters.RecyclerAdapter;
import com.assignment.gre.common.Constants;
import com.assignment.gre.common.DatabaseUtil;
import com.assignment.gre.network.VolleySingleton;

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

    public static void asyncRequest(Context context){

        final Context mcontext = context;

        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Constants.assignmentURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("ResponseFDS: ", response.toString());
                        ArrayList<HashMap<String,Object>> mdataset = JSONHelper.jsonParser(response);
                        DatabaseUtil.populateDatabase(mdataset,mcontext);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.v("ERROR", error.toString());

                    }
                });

        requestQueue.add(jsObjRequest);
    }
}
