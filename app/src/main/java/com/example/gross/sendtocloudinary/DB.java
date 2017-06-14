package com.example.gross.sendtocloudinary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "downloadedToCloudImgDb";
    public static final String TABLE_CLOUDINARY_IMG = "cloudImg";

    private static final String KEY_ID = "_id";
    static final String KEY_IMG_ID = "imageId";
    public static final String KEY_IMG_URL = "imageUrl";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CLOUDINARY_IMG + "(" + KEY_ID
                + " integer primary key," + KEY_IMG_ID + " text," + KEY_IMG_URL + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + TABLE_CLOUDINARY_IMG);
        onCreate(db);

    }
}
