package com.example.takenote.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;


import androidx.annotation.Nullable;

import com.example.takenote.Note;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Date;

public class Database extends SQLiteAssetHelper {

    private static String NAMA = "notes.db";
    private static int VERSION = 1;
    private String Title = "title";
    private String Type = "type";
    private String Desc = "desc";
    private String Id = "id";

    private String Tabel = "note";


    private String History = "History";
    private String USERNAME = "username";


    public Database(@Nullable Context context) {
        super(context, NAMA, null, VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Long Insert(Note note) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Type, note.getType());
        cv.put(Title, note.getTitle());
        cv.put(Desc, note.getDecscrption());
        cv.put(History, note.getHistory());
        cv.put(USERNAME, note.getUsername());
        return sqLiteDatabase.insert(Tabel, null, cv);//row
    }


    @SuppressLint("Range")
    public ArrayList<Note> getAll(String username) {
        ArrayList<Note> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * from " + Tabel + " where "+USERNAME+"=?", new String[]{username});
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(new Note(cursor.getString(cursor.getColumnIndex(Title)),cursor.getString(cursor.getColumnIndex(USERNAME)),
                                       cursor.getString(cursor.getColumnIndex(Type)),cursor.getString(cursor.getColumnIndex(History)),
                                    cursor.getString(cursor.getColumnIndex(Desc)),cursor.getInt(cursor.getColumnIndex(Id))));
            } while (cursor.moveToNext());
        }
        System.out.println("the all data is " + arrayList.size());
        return arrayList;
    }

    @SuppressLint("Range")
    public ArrayList<Note> getType(String type,String username) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery("select * from " + Tabel + " where " + Type + " like ? and "+USERNAME+"=?", new String[]{type,username});
        ArrayList<Note> arrayList = new ArrayList();
        if (c.moveToFirst()) {
            do {
                arrayList.add(new Note(c.getString(c.getColumnIndex(Title)),
                        c.getString(c.getColumnIndex(Type))
                        , c.getString(c.getColumnIndex(History))
                        , c.getString(c.getColumnIndex(Desc)),c.getInt(c.getColumnIndex(Id))));
            } while (c.moveToNext());
        }
        System.out.println(arrayList.size() + " this is type");
        return arrayList;
    }

    @SuppressLint("Range")
    public ArrayList<Note> SearchByTitle(String word,String username) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery("select * from " + Tabel + " where " + Title + " like ? and "+USERNAME+"=?", new String[]{word+"%",username});
        ArrayList<Note> arrayList = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                arrayList.add(new Note(c.getString(c.getColumnIndex(Title)),
                        c.getString(c.getColumnIndex(Type))
                        , c.getString(c.getColumnIndex(History))
                        , c.getString(c.getColumnIndex(Desc)),c.getInt(c.getColumnIndex(Id))));
            } while (c.moveToNext());
        }
        return arrayList;
    }

    @SuppressLint("Range")
   public Note SearchById(int id,String username){
        Note note=new Note();
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery("select * from "+Tabel+" where "+Id+" = "+id+" and "+USERNAME+"=? ",new String[]{username});
        if (c.moveToFirst()){
            do {
                note = new Note(c.getString(c.getColumnIndex(Title)),c.getString(c.getColumnIndex(Type)),
                                c.getString(c.getColumnIndex(History)),c.getString(c.getColumnIndex(Desc)),
                                c.getInt(c.getColumnIndex(Id)));
            }while (c.moveToNext());
        }
        return note;
    }
    public int DeleteById(int id){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        return sqLiteDatabase.delete(Tabel,""+Id+" = "+id+"",null);
    }
    public int ModifyById(Note note){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Type, note.getType());
        cv.put(Title, note.getTitle());
        cv.put(Desc, note.getDecscrption());
        note.setHistory(new Date().toString());
        cv.put(History, note.getHistory());
        cv.put(USERNAME, note.getUsername());
        return sqLiteDatabase.update(Tabel,cv,""+Id+" = "+note.getId()+"",null);
    }
}