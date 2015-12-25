package com.assignment.gre.backgroundtasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.assignment.gre.common.Constants;
import com.assignment.gre.common.DatabaseUtil;
import com.assignment.gre.json.JSONHelper;
import com.assignment.gre.listeners.GREListListener;
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
public class DataFetcherTask extends AsyncTask<Void,Void,ArrayList<HashMap<String,Object>>> {

    private GREListListener mListener;
    private VolleySingleton mVolleySingleton;
    private RequestQueue mRequestQueue;
    private Context mContext;

    public DataFetcherTask(GREListListener listener, Context context) {

        this.mListener = listener;
        this.mVolleySingleton = VolleySingleton.getInstance(context);
        this.mRequestQueue = mVolleySingleton.getRequestQueue();
        this.mContext = mContext;
    }

    @Override
    protected ArrayList<HashMap<String, Object>> doInBackground(Void... params) {

        RequestQueue requestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        ArrayList<HashMap<String,Object>> data = JSONHelper.requestData(requestQueue, Constants.assignmentURL);
        Log.v("kramnik",data.toString());
        DatabaseUtil.populateDatabase(data,mContext);
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<HashMap<String, Object>> data) {
        super.onPostExecute(data);
        Log.v("gyu",data.toString());
        ArrayList<HashMap<String,Object>> wordlist= data;
        mListener.onWordListDownloaded(wordlist);
    }

}
