package com.example.arshdeep.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.EventLogTags;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arshdeep on 6/28/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todoManager";

    private static final String TABLE_TODO = "todos";
    private static final String KEY_ID = "id";
    private static final String KEY_TASK = "name";
    private static final String KEY_DATE = "phone_number";
    private static final String KEY_TIME = "time";
    private static final String KEY_DESCRIPTION = "description";

    private static final String TABLE_DONE = "done";
    private static final String KEY_ID_DONE = "done_id";
    private static final String KEY_TASK_DONE = "done_name";
    private static final String KEY_DATE_DONE = "done_phone_number";
    private static final String KEY_TIME_DONE = "done_time";
    private static final String KEY_DESCRIPTION_DONE = "done_description";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE =  "CREATE TABLE " + TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TASK + " TEXT," + KEY_DATE + " TEXT," + KEY_TIME + " TEXT," + KEY_DESCRIPTION + " TEXT)";
        String CREATE_DONE_TABLE = "CREATE TABLE " + TABLE_DONE + "(" + KEY_ID_DONE + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TASK_DONE + " TEXT," + KEY_DATE_DONE + " TEXT," + KEY_TIME_DONE + " TEXT," + KEY_DESCRIPTION_DONE + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_DONE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(sqLiteDatabase);
    }

    long addDoneTodo(Todo todo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put(KEY_TASK_DONE , todo.getTask());
        values.put(KEY_DATE_DONE , todo.getDate());
        values.put(KEY_TIME_DONE , todo.getTime());
        values.put(KEY_DESCRIPTION_DONE , todo.getDescription());

        long id = db.insert(TABLE_DONE , null , values);
        db.close();
        return id;
    }


    long addTodo(Todo todo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put(KEY_TASK , todo.getTask());
        values.put(KEY_DATE , todo.getDate());
        values.put(KEY_TIME , todo.getTime());
        values.put(KEY_DESCRIPTION , todo.getDescription());

        long id = db.insert(TABLE_TODO , null , values);
        db.close();
        return id;
    }

    long addUndoTodo(Todo todo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put(KEY_ID , todo.getId());
        values.put(KEY_TASK , todo.getTask());
        values.put(KEY_DATE , todo.getDate());
        values.put(KEY_TIME , todo.getTime());
        values.put(KEY_DESCRIPTION , todo.getDescription());

        long id = db.insert(TABLE_TODO , null , values);
        db.close();
        return id;
    }



    Todo getTodo(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TODO , new String[]{KEY_ID , KEY_TASK , KEY_DATE , KEY_TIME ,KEY_DESCRIPTION}, KEY_ID + "=?", new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        Todo todo = new Todo(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
        return todo;
    }

    List<Todo> getAllDone(){
        List<Todo> doneList = new ArrayList<Todo>();
        String selectQuery = "SELECT * FROM " + TABLE_DONE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery , null);
        if(cursor.moveToFirst()){
            do{
                Todo todo = new Todo();
                todo.setId(Integer.parseInt(cursor.getString(0)));
                todo.setTask(cursor.getString(1));
                todo.setDate(cursor.getString(2));
                todo.setTime(cursor.getString(3));
                todo.setDescription(cursor.getString(4));
                doneList.add(todo);
            }while(cursor.moveToNext());
        }
        return doneList;
    }

    List<Todo> getAllTodos(){
        List<Todo> todoList = new ArrayList<Todo>();
        String selectQuery = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery , null);
        //Toast.makeText(DatabaseHandler.this, "" + cursor.getColumnCount(), Toast.LENGTH_SHORT).show();
        if(cursor.moveToFirst()){
            do{
                Todo todo = new Todo();
                todo.setId(Integer.parseInt(cursor.getString(0)));
                todo.setTask(cursor.getString(1));
                todo.setDate(cursor.getString(2));
                todo.setTime(cursor.getString(3));
                todo.setDescription(cursor.getString(4));
                todoList.add(todo);
            }while(cursor.moveToNext());
        }
        return todoList;
    }
    void deleteAllTodo(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO,null,null);
        db.close();
    }

    void deleteAllDone(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DONE,null,null);
        db.close();
    }
    int updateTodo(Todo todo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK , todo.getTask());
        values.put(KEY_DATE , todo.getDate());
        values.put(KEY_TIME , todo.getTime());
        values.put(KEY_DESCRIPTION , todo.getDescription());
        return db.update(TABLE_TODO , values , KEY_ID + " = ?" , new String[] {String.valueOf(todo.getId())});
    }
    long deleteTodo(Todo todo){
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.delete(TABLE_TODO , KEY_ID + " = ?" , new String[] {String.valueOf(todo.getId())});
        db.close();
        return id;
    }
    int getTodoCount(){
        String countQuery = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery , null);
        cursor.close();
        return cursor.getCount();
    }
}
