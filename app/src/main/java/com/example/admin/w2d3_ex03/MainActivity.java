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
    public EditText et_tittlevalue,et_subtitlevalue,et_updatevalue;
    public Button btnSave,btnRead,btnUpdate,btnDelete;
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
        et_updatevalue=(EditText)findViewById(R.id.et_updateValue);
        btnSave=(Button)findViewById(R.id.btn_SaveValue);
        btnSave.setOnClickListener(this);
        btnRead=(Button)findViewById(R.id.btn_ReadValue);
        btnRead.setOnClickListener(this);
        btnUpdate=(Button)findViewById(R.id.btn_UpdateValue);
        btnUpdate.setOnClickListener(this);
        btnDelete=(Button)findViewById(R.id.btn_DeleteValue);
        btnDelete.setOnClickListener(this);
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
            case R.id.btn_UpdateValue:
                break;
            case R.id.btn_DeleteValue:
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
    private void deleteRecord(){
        String selection= FeedEntry.COLUMN_NAME_TITLE+" LIKE ?";
        if(checkEmptyTitleValue()){
            Toast.makeText(MainActivity.this,"NOT ALLOW DELETE EMPTY VALUES",Toast.LENGTH_SHORT).show();
        }
        else {
            String[] selectionArgs = {et_tittlevalue.getText().toString()};
            int deleted = database.delete(
                    FeedEntry.TABLE_NAME,
                    selection,
                    selectionArgs
            );
            if (deleted > 0) {
                Log.d(TAG, "deletedRecord: record deleted");
                Toast.makeText(MainActivity.this, "RECORD DELETED " + et_tittlevalue.getText().toString(), Toast.LENGTH_SHORT).show();
                et_tittlevalue.setText("");
            } else {
                Log.d(TAG, "deletedRecord: record not deleted");
                Toast.makeText(MainActivity.this, "RECORD NOT DELETED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public Boolean checkEmptyTitleValue(){
        return et_tittlevalue.getText().toString().isEmpty();
    }
    private void updateRecord(){
        ContentValues values= new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, et_updatevalue.getText().toString().isEmpty()?
                "EMPTY VALUE":et_updatevalue.getText().toString());

        String selection= FeedEntry.COLUMN_NAME_TITLE+" LIKE ?";
        if(checkEmptyTitleValue()){
            Toast.makeText(MainActivity.this,"NOT ALLOW UPDATE EMPTY VALUES",Toast.LENGTH_SHORT).show();
            et_updatevalue.setText("");
        }
        else {
            String[] selectionArgs = {et_tittlevalue.getText().toString()};
            int count = database.update(FeedEntry.TABLE_NAME, values, selection, selectionArgs);
            if (count > 0) {
                tv_presentvalue.setText("");
                tv_presentvalue.setText("Updated "+et_tittlevalue+" for: "+et_updatevalue);
                Log.d(TAG, "updatedRecord: Records Updated");
                et_tittlevalue.setText("");
                et_updatevalue.setText("");
            } else
                Log.d(TAG, "updatedRecord: No Records Updated");
        }
    }
}
