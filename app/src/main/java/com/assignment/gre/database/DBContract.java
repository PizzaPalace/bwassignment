package com.assignment.gre.database;

import android.provider.BaseColumns;

/**
 * Created by rahul on 25-12-2015.
 */
public final class DBContract {

    public DBContract() {}

    /* Inner class that defines the table contents */
    public final class DBEntry implements BaseColumns {
        public static final String TABLE_NAME = "WORDS_TABLE";
        public static final String WORD_ID = "id";
        public static final String WORD = "word";
        public static final String VARIANT = "variant";
        public static final String MEANING = "meaning";
        public static final String RATIO = "ratio";
    }
}
