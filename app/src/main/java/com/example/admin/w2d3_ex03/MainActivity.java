package com.example.admin.w2d3_ex03;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.admin.w2d3_ex03.FeedReaderContract.FeedEntry;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName()+"_TAG";
    private DBHelper helper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper=new DBHelper(MainActivity.this);
        database= helper.getWritableDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        saveRecord();
        readRecord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
    private void saveRecord(){
        String title= "Record Title";
        String subtitle= "Record Subtitle";
        ContentValues values= new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE,title);
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE,subtitle);
        long recordId= database.insert(FeedEntry.TABLE_NAME,null,values);
        if(recordId>0){
            Log.d(TAG,"saveRecord: Record Saved.");
        }
        else
            Log.d(TAG,"saveRecord: Record not Saved.");
    }
    private void readRecord(){
        String[] projection={FeedEntry._ID,
        FeedEntry.COLUMN_NAME_TITLE,
        FeedEntry.COLUMN_NAME_SUBTITLE};
        String selection= FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArg={
                "Record Title"
        };
        String sortOrder=FeedEntry.COLUMN_NAME_SUBTITLE+" DESC";
        Cursor cursor= database.query(
                FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder,
                null);
        while (cursor.moveToNext()) {
            long entryId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry._ID));
            String entryTitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE));
            String entrySubtitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_SUBTITLE));

            Log.d(TAG, "readRecord: id: " + entryId+" title: "+entryTitle+" subtitle: "+entrySubtitle);
        }
    }
}
