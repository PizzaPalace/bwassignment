package com.assignment.gre.listeners;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rahul on 25-12-2015.
 */
public interface DatabaseListener {

    public void onDatabaseQueried(ArrayList<HashMap<String,Object>> wordlist);
}
