package com.assignment.gre.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rahul on 25-12-2015.
 */
public class DBHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME ="DATABASE_NAME";
    private static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";

    private static final String COMMA_SEPARATOR = " ,";
    private static final String TABLE_CREATE_ENTRIES =
            "CREATE TABLE " +
                    DBContract.DBEntry.TABLE_NAME + " (" +
                    DBContract.DBEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEPARATOR +
                    DBContract.DBEntry.WORD_ID + INTEGER_TYPE + COMMA_SEPARATOR +
                    DBContract.DBEntry.WORD + TEXT_TYPE + COMMA_SEPARATOR +
                    //DBContract.DBEntry.VARIANT + TEXT_TYPE + COMMA_SEPARATOR +
                    DBContract.DBEntry.MEANING + TEXT_TYPE + COMMA_SEPARATOR +
                    DBContract.DBEntry.RATIO + REAL_TYPE + " )";

    //ArrayList<HashMap<String,Object>> mData;

    ArrayList<HashMap<String,Object>> mData;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS" + DBContract.DBEntry.TABLE_NAME);
        onCreate(db);
    }

    public long insertData(int id,String word,String meaning, double ratio){

        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBContract.DBEntry.WORD_ID,id);
        contentValues.put(DBContract.DBEntry.WORD, word);
        //contentValues.put(DBContract.DBEntry.VARIANT, variant);
        contentValues.put(DBContract.DBEntry.MEANING, meaning);
        contentValues.put(DBContract.DBEntry.RATIO, ratio);

        long row_index = database.insert(DBContract.DBEntry.TABLE_NAME, null,contentValues);

        return row_index;

    }// end of insert data


    public ArrayList<HashMap<String,Object>> queryAllData(){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(DBContract.DBEntry.TABLE_NAME, //Table Name
                null, //Table Number
                null,
                null,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()){
            mData = new ArrayList<HashMap<String,Object>>();
            do{
                int word_id = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.DBEntry.WORD_ID));
                String word = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.DBEntry.WORD));
                //String variant = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.DBEntry.VARIANT));
                String meaning = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.DBEntry.MEANING));
                double ratio = cursor.getDouble(cursor.getColumnIndexOrThrow(DBContract.DBEntry.RATIO));

                HashMap<String,Object> map = new HashMap<String,Object>();

                map.put(DBContract.DBEntry.WORD_ID,word_id);
                map.put(DBContract.DBEntry.WORD,word);
                //map.put(DBContract.DBEntry.VARIANT,variant);
                map.put(DBContract.DBEntry.MEANING,meaning);
                map.put(DBContract.DBEntry.RATIO,ratio);

                mData.add(map);
                map = null;
            }while(cursor.moveToNext());// end of while
        }// end of if
        if(mData != null){
            Log.v("DATA",mData.toString());
        }
        else{
            Log.v("RESULT", "SORRY BUT THIS LIST IS EMPTY");
        }

        return mData;
    }// end of query data function

    public int deleteAllData(){
        int result = -1;
        SQLiteDatabase database = getWritableDatabase();
        result = database.delete(DBContract.DBEntry.TABLE_NAME,null,null);
        return result;
    }

    public boolean isDatabaseEmpty(){

        boolean isEmpty;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(DBContract.DBEntry.TABLE_NAME, //Table Name
                null, //Table Number
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.getCount() == 0){
            isEmpty = true;
        }
        else{
            isEmpty = false;
        }

        return isEmpty;
    }
}
