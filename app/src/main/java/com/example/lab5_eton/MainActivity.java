package com.example.lab5_eton;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText elem;
    ListView listView;
    ArrayAdapter myAdapter;

    private SQLiteDatabase db = null;
    private DatabaseOpenHelper dbHelper = null;
    SimpleCursorAdapter scAdapter;
    Cursor mCursor;
    final static String _ID = "_id";
    final static String TASK_NAME = "name";
    final static String[] columns = { _ID, TASK_NAME };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseOpenHelper(this);

        listView = (ListView) findViewById(R.id.mylist);

        // myAdapter = new ArrayAdapter<String>(this, R.layout.line);

        scAdapter = new SimpleCursorAdapter(this, R.layout.line, null, new String[]{TASK_NAME}, new int[]{R.id.task}, 0);

        // listView.setAdapter(myAdapter);

        listView.setAdapter(scAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                if (!((TextView) view).getText().toString().contains("DONE: ")) {
//                    String newItem = "DONE: " + ((TextView) view).getText().toString();
//                    myAdapter.add(newItem);
//                    myAdapter.remove(myAdapter.getItem(position));
//                }
//
//                else {
//                    myAdapter.remove(myAdapter.getItem(position));
//                    String[] split = ((TextView) view).getText().toString().split(": ");
//                    myAdapter.insert(split[1], 0);
//                }
                if (mCursor.moveToPosition(position)) {
                    String task = mCursor.getString(mCursor.getColumnIndex(TASK_NAME));
                    if(!task.contains("DONE")) {
                        String done_task = "DONE: " + task;
                        dbHelper.update(task, done_task);
                        mCursor = dbHelper.readAll();
                        scAdapter.swapCursor(mCursor);
                    }
                    else {
                        dbHelper.update(task, task.replace("DONE: ", ""));
                        mCursor = dbHelper.readAll();
                        scAdapter.swapCursor(mCursor);
                    }
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    alertView("Single Item Deletion",position);
                    return true;
                }
            }
        );
        elem =  (EditText)findViewById(R.id.input);

        // myAdapter.add("item from previous adapter");

        dbHelper = new DatabaseOpenHelper(this);
    }

    public void onResume() {
        super.onResume();
        db = dbHelper.getWritableDatabase();
        mCursor = dbHelper.readAll();
        scAdapter.swapCursor(mCursor);
    }

    public void onPause() {
        super.onPause();
        if (db != null) {
            db.close();
        }
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
    }

    public void clearEdit(View v) {
//        int size = myAdapter.getCount() - 1;
//        while (size >= 0 && myAdapter.getItem(size).toString().contains("DONE:")) {
//            myAdapter.remove(myAdapter.getItem(size));
//            size--;
//        }
        int size = scAdapter.getCount() - 1;

        if (size < 0 || mCursor == null || !mCursor.moveToFirst()) {
            Toast.makeText(getApplicationContext(),"No done items to delete!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> done = new ArrayList<>();

        do {
            String task = mCursor.getString(mCursor.getColumnIndex(TASK_NAME));
            if (task.contains("DONE")) {
                done.add(task);
            }
        } while (mCursor.moveToNext());

        for (String task : done) {
            dbHelper.delete(task);
        }

//        while (size >= 0 && mCursor.moveToPosition(size) && mCursor.getString(mCursor.getColumnIndex(TASK_NAME)).contains("DONE")) {
//            String task = mCursor.getString(mCursor.getColumnIndex(TASK_NAME));
//            dbHelper.delete(task);
//            size--;
//        }

//        if (mCursor != null && mCursor.moveToFirst()) {
//             do {
//                String task = mCursor.getString(mCursor.getColumnIndex(TASK_NAME));
//                if(task.contains("DONE")) {
//                    dbHelper.delete(task);
//                    mCursor = dbHelper.readAll();
//                    scAdapter.swapCursor(mCursor);
//                }
//             } while (mCursor.moveToNext());
//        }

//        mCursor.moveToFirst();
//        size = mCursor.getCount();
//
//        for (int i = 0; i < size; i++) {
//            String task = mCursor.getString(mCursor.getColumnIndex(TASK_NAME));
//            if (task.contains("DONE")) {
//                dbHelper.delete(task);
//                mCursor.moveToFirst();
//            }
//            else {
//                mCursor.moveToNext();
//            }
//        }

        mCursor.close();
        mCursor = dbHelper.readAll();
        scAdapter.swapCursor(mCursor);
    }

    public void addElem(View v) {
        String input = elem.getText().toString();
        if (input.length() > 0) {
            // myAdapter.add(input);
            dbHelper.insert(input);
            elem.setText("");
            mCursor = dbHelper.readAll();
            scAdapter.swapCursor(mCursor);
        } else
            Toast.makeText(getApplicationContext(),"Cannot add an empty item!", Toast.LENGTH_SHORT).show();

    }

    private void alertView(String message ,final int position ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle( message )
                .setIcon(R.drawable.ic_launcher_background)
                .setMessage("Are you sure you want to delete this item?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }})
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        myAdapter.remove(myAdapter.getItem(position));
                    }
                }).show();
    }

    private final class LoadDB extends AsyncTask<String, Void, Cursor> {
        // runs on the UI thread
        @Override protected void onPostExecute(Cursor data) {
//            myAdapter = new SimpleCursorAdapter(getApplicationContext(),
//                    android.R.layout.simple_list_item_1,
//                    data,
//                    new String[] { "item" },
//                    new int[] { android.R.id.text1 },0);
            scAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.line, null, new String[]{TASK_NAME}, new int[]{R.id.task}, 0);
            mCursor = data;
            // mlist.setAdapter(myAdapter);
            listView.setAdapter(scAdapter);
        }

        // runs on its own thread
        @Override
        protected Cursor doInBackground(String... args) {
            db = dbHelper.getWritableDatabase();
            return db.query(DatabaseOpenHelper.TABLE_NAME, DatabaseOpenHelper.allColumns, null, null,
                    null, null, null);
        }
    }
}