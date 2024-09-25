package com.example.lab3_eton;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText elem;
    ListView listView;
    ArrayAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.mylist);

        myAdapter = new ArrayAdapter<String>(this, R.layout.line);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!((TextView) view).getText().toString().contains("DONE: ")) {
                    String newItem = "DONE: " + ((TextView) view).getText().toString();
                    myAdapter.add(newItem);
                    myAdapter.remove(myAdapter.getItem(position));
                }

                else {
                    myAdapter.remove(myAdapter.getItem(position));
                    String[] split = ((TextView) view).getText().toString().split(": ");
                    myAdapter.insert(split[1], 0);
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

        myAdapter.add("477 lab3");
        myAdapter.add("477 project1");
        myAdapter.add("485 project2");
        myAdapter.add("391 proposal");

    }

    public void clearEdit(View v) {
        int size = myAdapter.getCount() - 1;
        while (size >= 0 && myAdapter.getItem(size).toString().contains("DONE:")) {
            myAdapter.remove(myAdapter.getItem(size));
            size--;
        }
    }

    public void addElem(View v) {
        String input = elem.getText().toString();
        if (input.length() > 0) {
            myAdapter.add(input);
            elem.setText("");
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
}