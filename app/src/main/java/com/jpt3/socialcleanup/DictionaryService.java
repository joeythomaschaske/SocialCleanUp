package com.jpt3.socialcleanup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}
