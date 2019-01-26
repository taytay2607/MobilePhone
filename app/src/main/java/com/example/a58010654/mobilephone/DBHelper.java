package com.example.a58010654.mobilephone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase;

    public DBHelper(Context context) {
        super(context, FavoriteId.DATABASE_NAME, null, FavoriteId.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAVORITE_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY)",
                FavoriteId.TABLE,
                FavoriteId.Column.ID);

        db.execSQL(CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_FRIEND_TABLE = "DROP TABLE IF EXISTS " + FavoriteId.TABLE;
        db.execSQL(DROP_FRIEND_TABLE);
        onCreate(db);
    }

    public List<Integer> getFavoriteList() {
        List<Integer> favorite = new ArrayList<Integer>();

        sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query
                (FavoriteId.TABLE, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            favorite.add(cursor.getInt(0));
            cursor.moveToNext();
        }

        sqLiteDatabase.close();

        return favorite;
    }

    public void addFavorite(FavoriteId favoriteId) {

        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteId.Column.ID, favoriteId.getId());
        sqLiteDatabase.insert(FavoriteId.TABLE, null, values);
        sqLiteDatabase.close();
    }

    public void deleteFavorite(Integer id) {

        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(FavoriteId.TABLE, FavoriteId.Column.ID + " = " + id, null);
        sqLiteDatabase.close();
    }
}
