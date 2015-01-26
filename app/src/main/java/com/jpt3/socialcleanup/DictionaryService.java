package com.jpt3.socialcleanup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class DictionaryService extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DICTIONARY_TABLE_NAME = "dictionary";
    public static final String DATABASE_NAME = "dictionary.db";
    public static final String KEY_WORD_COLUMN = "word_id";
    public static final String KEY_DEFINITION = "word";
    public static final String DICTIONARY_TABLE_CREATE ="CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" + KEY_WORD_COLUMN + " TEXT, " +KEY_DEFINITION + " TEXT);";

    DictionaryService(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DICTIONARY_TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addToDatabase(String item){
        ContentValues contentValues = null;

        try {
            contentValues = new ContentValues();
            contentValues.put(KEY_WORD_COLUMN, item);
            getWritableDatabase().insert(DICTIONARY_TABLE_NAME, null, contentValues);
        }
        catch (Exception e){
            Fabric.getLogger().e("Dictionary Service(addToDatabase) "  + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage(), e);
        }
    }

    public List<String> getAllItems(){
        String item = "";
        String query = "";
        List<String> contents = null;
        Cursor cursor = null;
        SQLiteDatabase db = null;

        try{
            query = "SELECT * FROM " + DICTIONARY_TABLE_NAME;
            db = getWritableDatabase();
            cursor = db.rawQuery(query, null);
            contents = new LinkedList<>();
            if(cursor.moveToFirst()) {
                do {
                    item = cursor.getString(0);
                    contents.add(item);
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            Fabric.getLogger().e("Dictionary Service(getAllItems) "  + Thread.currentThread().getStackTrace()[2].getLineNumber(), e.getMessage(), e);
        }
        return contents;
    }
}
