package com.example.admin.w2d3_ex03;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.w2d3_ex03.FeedReaderContract.FeedEntry;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName()+"_TAG";
    private DBHelper helper;
    private SQLiteDatabase database;
    public EditText et_tittlevalue,et_subtitlevalue;
    public Button btnSave,btnRead;
    public TextView tv_presentvalue;
    StringBuilder stringBuilder= new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper=new DBHelper(MainActivity.this);
        database= helper.getWritableDatabase();
        et_tittlevalue=(EditText)findViewById(R.id.et_Title);
        et_subtitlevalue=(EditText)findViewById(R.id.et_Subtitle);
        tv_presentvalue=(TextView)findViewById(R.id.tv_PresentValues);
        btnSave=(Button)findViewById(R.id.btn_SaveValue);
        btnSave.setOnClickListener(this);
        btnRead=(Button)findViewById(R.id.btn_ReadValue);
        btnRead.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_SaveValue:
                saveRecord();
                break;
            case R.id.btn_ReadValue:
                readRecord();
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //saveRecord();
        //readRecord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
    private void saveRecord(){
        String title= et_tittlevalue.getText().toString();
        String subtitle= et_subtitlevalue.getText().toString();
        ContentValues values= new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE,title);
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE,subtitle);
        long recordId= database.insert(FeedEntry.TABLE_NAME,null,values);
        if(recordId>0){
            Toast.makeText(MainActivity.this,"RECORD SAVED",Toast.LENGTH_SHORT).show();
            Log.d(TAG,"saveRecord: Record Saved.");
            et_tittlevalue.setText("");
            et_subtitlevalue.setText("");
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
        String sortOrder=FeedEntry._ID+" ASC";
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
            stringBuilder.append("ID: "+entryId+" TITLE: "+entryTitle+" SUBTITLE: "+entrySubtitle);
            stringBuilder.append("\n");
            tv_presentvalue.setText("");
            tv_presentvalue.setText(stringBuilder);
            Log.d(TAG, "readRecord: id: " + entryId+" title: "+entryTitle+" subtitle: "+entrySubtitle);
        }
    }
}
