package com.example.a58010654.mobilephone;

public class FavoriteId {

    private int id;
    public static final String DATABASE_NAME = "favorite.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "favorite";

    public class Column {
        public static final String ID = "id";
    }

    public FavoriteId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
