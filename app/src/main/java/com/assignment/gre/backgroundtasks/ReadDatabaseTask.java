package com.assignment.gre.backgroundtasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.assignment.gre.common.DatabaseUtil;
import com.assignment.gre.listeners.DatabaseListener;
import com.assignment.gre.listeners.GREListListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rahul on 25-12-2015.
 */
public class ReadDatabaseTask extends AsyncTask<Void,Void,ArrayList<HashMap<String,Object>>> {

    Context mContext;
    private DatabaseListener mListener;

    public ReadDatabaseTask(DatabaseListener listener, Context context){

        this.mListener = listener;
        this.mContext = context;
    }

    @Override
    protected ArrayList<HashMap<String, Object>> doInBackground(Void... voids) {

        ArrayList<HashMap<String,Object>> data = DatabaseUtil.readDatabase(mContext);

        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<HashMap<String, Object>> data) {
        super.onPostExecute(data);
        Log.v("MONGO", data.toString());
        mListener.onDatabaseQueried(data);
    }
}
