package com.assignment.gre.common;

import android.content.Context;

import com.assignment.gre.database.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rahul on 25/12/15.
 */
public class DatabaseUtil {

    // method to populate values into database
    public static void populateDatabase(ArrayList<HashMap<String,Object>> data, Context context){

        DBHelper database = new DBHelper(context);
        database.deleteAllData();

        int length = data.size();

        for(int i=0; i<length; i++){

            HashMap<String,Object> map = data.get(i);

            int id = (int)map.get("id");
            String word = (String)map.get("word");
            String meaning = (String)map.get("meaning");
            double ratio = (double)map.get("ratio");

            map = null;

            database.insertData(id,word,meaning,ratio);
        }

        //database.queryAllData();
    }

    // method to read all values from the database
    public static ArrayList<HashMap<String,Object>> readDatabase(Context context){

        DBHelper database = new DBHelper(context);
        return database.queryAllData();
    }

    // method to check if database is empty or not
    public static boolean isDBEmpty(Context context){

        DBHelper database = new DBHelper(context);
        return database.isDatabaseEmpty();
    }
}
