package com.assignment.gre.listeners;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rahul on 25/12/15.
 */
public interface GREListListener {

    public void onWordListDownloaded(ArrayList<HashMap<String,Object>> wordlist);
}
