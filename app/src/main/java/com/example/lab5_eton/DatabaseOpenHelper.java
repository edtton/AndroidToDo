package com.example.lab5_eton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    final static String TABLE_NAME = "tasks";
    final static String TASK_NAME = "name";
    final static String _ID = "_id";
    final private static String NAME = "tasks_db";
    final private static Integer VERSION = 1;
    final private Context context;
    final static String[] allColumns = {_ID, TASK_NAME};

    public DatabaseOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CMD = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TASK_NAME + " TEXT NOT NULL)";
        db.execSQL(CREATE_CMD);

        ContentValues values = new ContentValues();

        values.put(TASK_NAME, "477 lab 5");
        db.insert(TABLE_NAME, null, values);
        values.clear();

        values.put(TASK_NAME, "485 project proposal");
        db.insert(TABLE_NAME, null, values);
        values.clear();

        values.put(TASK_NAME, "391 project proposal");
        db.insert(TABLE_NAME, null, values);
        values.clear();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(String task_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TASK_NAME, task_name);
        db.insert(TABLE_NAME, null, values);
        values.clear();
        db.close();
    }

    public void delete(String task_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = db.delete(TABLE_NAME, TASK_NAME + "=?", new String[] { task_name });
        db.close();
    }

    public Cursor readAll() {
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();
        c = db.query(TABLE_NAME, allColumns, null, new String[] {}, null, null, null);
        return c;
    }

    public void update(String oldName, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TASK_NAME, newName);
        // int status = db.delete(TABLE_NAME, TASK_NAME + "=?", new String[] { oldName });
        db.update(TABLE_NAME, values, TASK_NAME + "=?", new String[]{ oldName });
        db.close();
    }

    public void clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    void deleteDatabase() {
        context.deleteDatabase(NAME);
    }

}
