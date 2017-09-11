package com.pea.du.db.local.methods;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.pea.du.data.Photo;
import com.pea.du.db.local.data.Contract;
import com.pea.du.db.local.data.DBHelper;

public class DeleteMethods {


    private static SQLiteDatabase getDBfromContext(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        return dbHelper.getWritableDatabase();
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

    public static boolean deleteWorkNames(Context context)
    {
        SQLiteDatabase db_write = getDBfromContext(context);
        return db_write.delete(Contract.GuestEntry.WORK_NAME_TABLE_NAME, null, null) > 0;
    }

    public static boolean deleteStages(Context context)
    {
        SQLiteDatabase db_write = getDBfromContext(context);
        return db_write.delete(Contract.GuestEntry.STAGE_TABLE_NAME, null, null) > 0;
    }

    public static boolean deleteContractors(Context context)
    {
        SQLiteDatabase db_write = getDBfromContext(context);
        return db_write.delete(Contract.GuestEntry.CONTRACTOR_TABLE_NAME, null, null) > 0;
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

    public static boolean deleteWorks(Context context)
    {
        SQLiteDatabase db_write = getDBfromContext(context);
        return db_write.delete(Contract.GuestEntry.WORK_TABLE_NAME, null, null) > 0;
    }

    public static boolean deletePhotos(Context context)
    {
        SQLiteDatabase db_write = getDBfromContext(context);
        return db_write.delete(Contract.GuestEntry.DEFECT_PHOTO_TABLE_NAME, null, null) > 0;
    }

    public static boolean deletePhoto(Context context, Photo photo)
    {
        SQLiteDatabase db_write = getDBfromContext(context);
        return db_write.delete(Contract.GuestEntry.DEFECT_PHOTO_TABLE_NAME, Contract.GuestEntry.PATH + "='" + photo.getPath() + "'", null) > 0;
    }


    public static void deleteAnything(Context context){
        deleteAdresses(context);
        deleteMeasures(context);
        deleteConstructiveElements(context);
        deleteTypes(context);
        deleteActs(context);
        deleteDefects(context);
        deleteWorks(context);
        deletePhotos(context);
        deleteWorkNames(context);
        deleteStages(context);
        deleteContractors(context);
    }

}
