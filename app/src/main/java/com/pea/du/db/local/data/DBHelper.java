package com.pea.du.db.local.data;

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
    private static final int DATABASE_VERSION = 34;


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

        String SQL_CREATE_WORK_TABLE = "CREATE TABLE " + Contract.GuestEntry.WORK_TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.SERVER_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.USER_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.ADDRESS_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.WORK_NAME_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.STAGE_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.CNT + " TEXT NOT NULL, "
                + Contract.GuestEntry.MEASURE_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.DESCR + " TEXT NOT NULL, "
                + Contract.GuestEntry.SUBCONTRACT + " BOOLEAN NOT NULL, "
                + Contract.GuestEntry.CONTRACTOR_ID + " INTEGER, "
                + Contract.GuestEntry.DOCDATE + " DATETIME NOT NULL);";

        String SQL_CREATE_PHOTO_TABLE = "CREATE TABLE " + Contract.GuestEntry.PHOTO_TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.SERVER_ID + " INTEGER, "
                + Contract.GuestEntry.WORKTYPE + " TEXT NOT NULL, "
                + Contract.GuestEntry.WORK_ID + " INTEGER NOT NULL, "
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

        String SQL_CREATE_DEFECT_SYNC_CE_T_TABLE = "CREATE TABLE " + Contract.GuestEntry.DEFECT_SYNC_CE_T_TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.SERVER_ID + " INTEGER, "
                + Contract.GuestEntry.DEFECT_CONSTRUCTIVE_ELEMENT_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.DEFECT_TYPE_ID + " INTEGER NOT NULL);";

        String SQL_CREATE_WORK_NAME_TABLE = "CREATE TABLE " + Contract.GuestEntry.WORK_NAME_TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.SERVER_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.WORK_NAME + " TEXT NOT NULL);";

        String SQL_CREATE_STAGE_TABLE = "CREATE TABLE " + Contract.GuestEntry.STAGE_TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.SERVER_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.STAGE + " TEXT NOT NULL);";

        String SQL_CREATE_CONTRACTOR_TYPE_TABLE = "CREATE TABLE " + Contract.GuestEntry.CONTRACTOR_TABLE_NAME + " ("
                + Contract.GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.GuestEntry.SERVER_ID + " INTEGER NOT NULL, "
                + Contract.GuestEntry.CONTRACTOR + " TEXT NOT NULL);";

        // Запускаем создание таблиц
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_ACT_TABLE);
        db.execSQL(SQL_CREATE_DEFECT_TABLE);
        db.execSQL(SQL_CREATE_WORK_TABLE);
        db.execSQL(SQL_CREATE_PHOTO_TABLE);
        db.execSQL(SQL_CREATE_ADDRESS_TABLE);
        db.execSQL(SQL_CREATE_DEFECT_MEASURE_TABLE);
        db.execSQL(SQL_CREATE_DEFECT_CONSTRUCTIVE_ELEMENT_TABLE);
        db.execSQL(SQL_CREATE_DEFECT_TYPE_TABLE);
        db.execSQL(SQL_CREATE_DEFECT_SYNC_CE_T_TABLE);
        db.execSQL(SQL_CREATE_WORK_NAME_TABLE);
        db.execSQL(SQL_CREATE_STAGE_TABLE);
        db.execSQL(SQL_CREATE_CONTRACTOR_TYPE_TABLE);
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
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.WORK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.PHOTO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.ADDRESS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.DEFECT_MEASURE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.DEFECT_TYPE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.DEFECT_SYNC_CE_T_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.WORK_NAME_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.STAGE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.GuestEntry.CONTRACTOR_TABLE_NAME);

        // Создаём новую таблицу
        onCreate(db);

    }
}
