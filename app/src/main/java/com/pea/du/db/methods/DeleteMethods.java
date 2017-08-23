package com.pea.du.db.methods;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.pea.du.data.Photo;
import com.pea.du.db.data.Contract;
import com.pea.du.db.data.DBHelper;

public class DeleteMethods {


    private static SQLiteDatabase getDBfromContext(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        return dbHelper.getWritableDatabase();
    }

    public static boolean deletePhoto(Context context, Photo photo)
    {
        SQLiteDatabase db_write = getDBfromContext(context);
        return db_write.delete(Contract.GuestEntry.DEFECT_PHOTO_TABLE_NAME, Contract.GuestEntry.PATH + "='" + photo.getPath() + "'", null) > 0;
    }

    public static boolean deleteAdresses(Context context)
    {
        SQLiteDatabase db_write = getDBfromContext(context);
        return db_write.delete(Contract.GuestEntry.ADDRESS_TABLE_NAME, null, null) > 0;
    }

    public static boolean deleteMeasures(Context context)
    {
        SQLiteDatabase db_write = getDBfromContext(context);
        return db_write.delete(Contract.GuestEntry.DEFECT_MEASURE_TABLE_NAME, null, null) > 0;
    }

    public static boolean deleteConstructiveElements(Context context)
    {
        SQLiteDatabase db_write = getDBfromContext(context);
        return db_write.delete(Contract.GuestEntry.DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, null, null) > 0;
    }

    public static boolean deleteTypes(Context context)
    {
        SQLiteDatabase db_write = getDBfromContext(context);
        return db_write.delete(Contract.GuestEntry.DEFECT_TYPE_TABLE_NAME, null, null) > 0;
    }

    public static boolean deleteActs(Context context)
    {
        SQLiteDatabase db_write = getDBfromContext(context);
        return db_write.delete(Contract.GuestEntry.ACT_TABLE_NAME, null, null) > 0;
    }

    public static boolean deleteDefects(Context context)
    {
        SQLiteDatabase db_write = getDBfromContext(context);
        return db_write.delete(Contract.GuestEntry.DEFECT_TABLE_NAME, null, null) > 0;
    }
}
