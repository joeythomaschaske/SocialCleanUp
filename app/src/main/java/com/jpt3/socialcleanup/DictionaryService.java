package com.jpt3.socialcleanup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DictionaryService extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DICTIONARY_TABLE_NAME = "dictionary";
    private static final String DATABASE_NAME = "dictionary.db";
    private static final String KEY_WORD = "word_id";
    private static final String KEY_DEFINITION = "word";
    private static final String DICTIONARY_TABLE_CREATE ="CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" + KEY_WORD + " TEXT, " +KEY_DEFINITION + " TEXT);";

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
}
