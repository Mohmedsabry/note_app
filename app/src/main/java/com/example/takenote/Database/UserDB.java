package com.example.takenote.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class UserDB extends SQLiteAssetHelper {
    private static final String NAME ="notes.db";
    private static final String TABLE ="user";
    private static final String USERNAME ="username";
    private static final String PASSWORD ="password";
    private static final int VERSION =1;

    public UserDB(Context context) {
        super(context, NAME, null, null, VERSION);
    }
    public Boolean insert(User user)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USERNAME,user.getUsername());
        cv.put(PASSWORD,user.getPassword());
        return database.insert(TABLE,null,cv)>-1;
    }
    @SuppressLint("Range")
    public ArrayList<User> getAll(){
        ArrayList<User>arrayList=new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery("select * from "+TABLE+"",null);
        if (c.moveToFirst()){
            do {
                arrayList.add(new User(c.getInt(c.getColumnIndex("id")),c.getString(c.getColumnIndex(USERNAME)),c.getString(c.getColumnIndex(PASSWORD))));
            }while (c.moveToNext());
        }
        return arrayList;
    }
}
