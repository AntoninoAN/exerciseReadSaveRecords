package com.example.admin.w2d3_ex03;

import android.provider.BaseColumns;

/**
 * Created by Admin on 7/26/2017.
 */

public final class FeedReaderContract {
    private FeedReaderContract(){}
    public static class FeedEntry implements BaseColumns{
        public static final String TABLE_NAME="entry";
        public static final String COLUMN_NAME_TITLE="title";
        public static final String COLUMN_NAME_SUBTITLE="subtitle";

    }
}
