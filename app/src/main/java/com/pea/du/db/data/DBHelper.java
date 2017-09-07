package com.pea.du.db.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{

    public static final String LOG_TAG =  DBHelper.class.getSimpleName();

    /**
     * Имя файла базы данных
     */
    private static final String DATABASE_NAME = "du.db";

    /**
     * Версия базы данных. При изменении схемы увеличить на единицу
     */
    private static final int DATABASE_VERSION = 29;


    /**
     * Конструктор {@link DBHelper}.
     *
     * @param context Контекст приложения
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Вызывается при создании базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строки для создания таблиц

        String SQL_CREATE_USER_TABLE = "CREATE TABLE " + Contract.GuestEntry.USER_TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.SERVER_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.NICKNAME + " TEXT NOT NULL, "
                + Contract.GuestEntry.PASSWORD + " TEXT NOT NULL);";

        String SQL_CREATE_ACT_TABLE = "CREATE TABLE " + Contract.GuestEntry.ACT_TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.SERVER_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.USER_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.ADDRESS_ID + " INTEGER NOT NULL);";

        String SQL_CREATE_DEFECT_TABLE = "CREATE TABLE " + Contract.GuestEntry.DEFECT_TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.SERVER_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.ACT_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.DEFECT_CONSTRUCTIVE_ELEMENT_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.DEFECT_TYPE_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.PORCH + " TEXT NOT NULL, "
                + Contract.GuestEntry.FLOOR + " TEXT NOT NULL, "
                + Contract.GuestEntry.FLAT + " TEXT NOT NULL, "
                + Contract.GuestEntry.DESCRIPTION + " TEXT NOT NULL, "
                + Contract.GuestEntry.DEFECT_MEASURE_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.CURRENCY + " TEXT NOT NULL);";

        String SQL_CREATE_DEFECT_PHOTO_TABLE = "CREATE TABLE " + Contract.GuestEntry.DEFECT_PHOTO_TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.SERVER_ID + " INTEGER, "
                + Contract.GuestEntry.DEFECT_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.URL + " TEXT, "
                + Contract.GuestEntry.PATH + " TEXT);";

        String SQL_CREATE_ADDRESS_TABLE = "CREATE TABLE " + Contract.GuestEntry.ADDRESS_TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.SERVER_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.ADDRESS + " TEXT NOT NULL);";

        String SQL_CREATE_DEFECT_MEASURE_TABLE = "CREATE TABLE " + Contract.GuestEntry.DEFECT_MEASURE_TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.SERVER_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.DEFECT_MEASURE + " TEXT NOT NULL);";

        String SQL_CREATE_DEFECT_CONSTRUCTIVE_ELEMENT_TABLE = "CREATE TABLE " + Contract.GuestEntry.DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.SERVER_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.DEFECT_CONSTRUCTIVE_ELEMENT + " TEXT NOT NULL);";

        String SQL_CREATE_DEFECT_TYPE_TABLE = "CREATE TABLE " + Contract.GuestEntry.DEFECT_TYPE_TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.SERVER_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.DEFECT_TYPE + " TEXT NOT NULL);";

        // Запускаем создание таблиц
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_ACT_TABLE);
        db.execSQL(SQL_CREATE_DEFECT_TABLE);
        db.execSQL(SQL_CREATE_DEFECT_PHOTO_TABLE);
        db.execSQL(SQL_CREATE_ADDRESS_TABLE);
        db.execSQL(SQL_CREATE_DEFECT_MEASURE_TABLE);
        db.execSQL(SQL_CREATE_DEFECT_CONSTRUCTIVE_ELEMENT_TABLE);
        db.execSQL(SQL_CREATE_DEFECT_TYPE_TABLE);
    }

    /**
     * Вызывается при обновлении схемы базы данных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.ACT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.DEFECT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.DEFECT_PHOTO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.ADDRESS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.DEFECT_MEASURE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.DEFECT_TYPE_TABLE_NAME);

        // Создаём новую таблицу
        onCreate(db);

    }
}
