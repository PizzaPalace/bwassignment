package com.assignment.gre.activities;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.assignment.gre.R;
import com.assignment.gre.common.AlarmUtil;
import com.assignment.gre.common.Constants;
import com.assignment.gre.common.DatabaseUtil;
import com.assignment.gre.database.DBHelper;
import com.assignment.gre.fragments.ContentFragment;
import com.assignment.gre.fragments.NavigationDrawerFragment;
import com.assignment.gre.network.VolleySingleton;
import com.assignment.gre.services.SyncService;
import com.assignment.gre.views.SlidingTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.OnFragmentInteractionListener,
        ContentFragment.OnFragmentInteractionListener{

    // for use with Lollipop and Marshmallow
    JobScheduler mJobScheduler;



    private static int count = 3;

    private Toolbar mToolbar;
    private NavigationDrawerFragment mDrawerFragment;
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private SlidingTabLayout mTabs;

    private static final long POLL_FREQUENCY = 28800000;
    private static final int JOB_ID = 100;

    // Datastructure to hold key-value pairs
    ArrayList<HashMap<String,Object>> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        initializeDrawerLayout();
        intializeViewPager();

        mData = new ArrayList<HashMap<String, Object>>();

        //RequestQueue requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        //String url = "http://appsculture.com/vocab/words.json";
        //requestMoviesJSON(requestQueue,Constants.assignmentURL);

        /*JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("Response: ", response.toString());
                        jsonParser(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.v("ERROR", error.toString());

                    }
                });

        requestQueue.add(jsObjRequest);*/

        if(Build.VERSION.SDK_INT < 22)
            scheduleAlarm();

        else if(Build.VERSION.SDK_INT >= 22)
            setupJob();
    }

    private void initializeDrawerLayout(){

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.intializeDrawer(R.id.fragment_navigation_drawer, mDrawerLayout, mToolbar);
    }

    private void intializeViewPager(){

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new SlidingPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout) findViewById(R.id.sliding_tab_layout);
        mTabs.setCustomTabView(R.layout.tab_view, R.id.tabText);
        //make sure all tabs take the full horizontal screen space and divide it equally amongst themselves
        mTabs.setDistributeEvenly(true);
        mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //color of the tab indicator
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mTabs.setViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onContentFragmentInteraction(Uri uri) {

    }

    private class SlidingPagerAdapter extends FragmentStatePagerAdapter{

        private String[] tabText;

        public SlidingPagerAdapter(FragmentManager fm) {
            super(fm);
            tabText = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {

            ContentFragment fragment = ContentFragment.newInstance("","");
            return fragment;
        }

        @Override
        public int getCount() {
            return count;
        }
    }

    // method to parse JSON obtained from network
    private void jsonParser(JSONObject jsonObject) {

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("words");

            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                HashMap<String,Object> map = new HashMap<String,Object>();
                JSONObject jObject = jsonArray.getJSONObject(i);

                map.put(Constants.ID, jObject.getInt("id"));
                map.put(Constants.WORD, jObject.getString("word"));
                map.put(Constants.MEANING, jObject.getString("meaning"));
                map.put(Constants.RATIO, jObject.getDouble("ratio"));

                mData.add(map);
                map = null;

            }

            Log.v("data",mData.toString());
            populateDatabase();
        }
        catch(JSONException exception){
            exception.printStackTrace();
        }
    }

    private void populateDatabase(){

        DBHelper database = new DBHelper(this);
        database.deleteAllData();

        int length = mData.size();

        for(int i=0; i<length; i++){

            HashMap<String,Object> map = mData.get(i);

            int id = (int)map.get("id");
            String word = (String)map.get("word");
            String meaning = (String)map.get("meaning");
            double ratio = (double)map.get("ratio");

            map = null;

            database.insertData(id,word,meaning,ratio);
        }

        database.queryAllData();
    }

    private void setupJob() {
        mJobScheduler = (JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);
        //set an initial delay with a Handler so that the data loading by the JobScheduler does not clash with the loading inside the Fragment
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //schedule the job after the delay has been elapsed
                buildJob();
            }
        }, 10000);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void buildJob() {

        ComponentName serviceName = new ComponentName(this, SyncService.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(true)
                .build();
        int result = mJobScheduler.schedule(jobInfo);
        if (result == JobScheduler.RESULT_SUCCESS) Log.d("TAG", "Job scheduled successfully!");
    }


    private void scheduleAlarm(){

        AlarmUtil util = new AlarmUtil();
        util.setAlarm(this);
    }

}
