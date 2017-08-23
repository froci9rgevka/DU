package com.pea.du.db.methods;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.pea.du.data.*;
import com.pea.du.data.StaticValue;
import com.pea.du.db.data.Contract;
import com.pea.du.db.data.DBHelper;

import java.util.ArrayList;

public class WriteMethods {

    private static SQLiteDatabase getDBfromContext(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        return dbHelper.getWritableDatabase();
    }

    public static ArrayList<Integer> setMeasures(Context context, ArrayList<StaticValue> staticValueArrayList){
        SQLiteDatabase db_write = getDBfromContext(context);
        ArrayList<Integer> result = new ArrayList<>();
        for (StaticValue staticValue : staticValueArrayList
                ) {
            ContentValues values = new ContentValues();
            values.put(Contract.GuestEntry.DEFECT_MEASURE, staticValue.getName());
            values.put(Contract.GuestEntry.SERVER_ID, staticValue.getServerId());
            result.add((int) db_write.insert(Contract.GuestEntry.DEFECT_MEASURE_TABLE_NAME, null, values));
        }
        return result;
    }

    public static ArrayList<Integer> setConstructiveElements(Context context, ArrayList<StaticValue> staticValueArrayList){
        SQLiteDatabase db_write = getDBfromContext(context);
        ArrayList<Integer> result = new ArrayList<>();
        for (StaticValue staticValue : staticValueArrayList
                ) {
            ContentValues values = new ContentValues();
            values.put(Contract.GuestEntry.DEFECT_CONSTRUCTIVE_ELEMENT, staticValue.getName());
            values.put(Contract.GuestEntry.SERVER_ID, staticValue.getServerId());
            result.add((int) db_write.insert(Contract.GuestEntry.DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, null, values));
        }
        return result;
    }

    public static ArrayList<Integer> setTypes(Context context, ArrayList<StaticValue> staticValueArrayList){
        SQLiteDatabase db_write = getDBfromContext(context);
        ArrayList<Integer> result = new ArrayList<>();
        for (StaticValue staticValue : staticValueArrayList
                ) {
            ContentValues values = new ContentValues();
            values.put(Contract.GuestEntry.DEFECT_TYPE, staticValue.getName());
            values.put(Contract.GuestEntry.SERVER_ID, staticValue.getServerId());
            result.add((int) db_write.insert(Contract.GuestEntry.DEFECT_TYPE_TABLE_NAME, null, values));
        }
        return result;
    }

    public static Integer setAddress(Context context, StaticValue address){
        ArrayList<StaticValue> staticValueArrayList = new ArrayList<>();
        staticValueArrayList.add(address);
        return setAddresses(context, staticValueArrayList).get(0);
    }

    public static ArrayList<Integer> setAddresses(Context context, ArrayList<StaticValue> staticValueArrayList){
        SQLiteDatabase db_write = getDBfromContext(context);
        ArrayList<Integer> result = new ArrayList<>();
        for (StaticValue staticValue : staticValueArrayList
             ) {
            ContentValues values = new ContentValues();
            values.put(Contract.GuestEntry.ADDRESS, staticValue.getName());
            values.put(Contract.GuestEntry.SERVER_ID, staticValue.getServerId());

            result.add((int) db_write.insert(Contract.GuestEntry.ADDRESS_TABLE_NAME, null, values));
        }
        return result;
    }

    public static Integer setUser(Context context, User user){
        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(user);
        return setUsers(context, userArrayList).get(0);
    }


    public static ArrayList<Integer> setUsers(Context context, ArrayList<User> userArrayList){
        SQLiteDatabase db_write = getDBfromContext(context);
        ArrayList<Integer> result = new ArrayList<>();
        for (User user : userArrayList
                ) {
            ContentValues values = new ContentValues();
            values.put(Contract.GuestEntry.SERVER_ID, user.getServerId());
            values.put(Contract.GuestEntry.NICKNAME, user.getNickname());
            values.put(Contract.GuestEntry.PASSWORD, user.getPassword());

            result.add((int) db_write.insert(Contract.GuestEntry.USER_TABLE_NAME, null, values));
        }
        return result;
    }

    public static Integer setAct(Context context, Act act){
        ArrayList<Act> actArrayList = new ArrayList<>();
        actArrayList.add(act);
        return setActs(context, actArrayList).get(0);
    }

    public static ArrayList<Integer> setActs(Context context, ArrayList<Act> actArrayList){
        SQLiteDatabase db_write = getDBfromContext(context);
        ArrayList<Integer> result = new ArrayList<>();
        for (Act act : actArrayList
                ) {
            ContentValues values = new ContentValues();
            values.put(Contract.GuestEntry.SERVER_ID, act.getServerId());
            values.put(Contract.GuestEntry.USER_ID, act.getUser().getServerId());
            values.put(Contract.GuestEntry.ADDRESS_ID, act.getAddress().getServerId());
            result.add((int) db_write.insert(Contract.GuestEntry.ACT_TABLE_NAME, null, values));
        }
        return result;
    }


    public static Integer setDefect(Context context, Defect defect){
        ArrayList<Defect> defectArrayList = new ArrayList<>();
        defectArrayList.add(defect);
        return setDefects(context, defectArrayList).get(0);
    }


    public static ArrayList<Integer> setDefects(Context context, ArrayList<Defect> defectArrayList){
        SQLiteDatabase db_write = getDBfromContext(context);
        ArrayList<Integer> result = new ArrayList<>();
        for (Defect defect : defectArrayList
                ) {
            ContentValues values = new ContentValues();
            values.put(Contract.GuestEntry.SERVER_ID, defect.getServerId());
            values.put(Contract.GuestEntry.ACT_ID, defect.getAct().getServerId());
            values.put(Contract.GuestEntry.DEFECT_CONSTRUCTIVE_ELEMENT_ID, defect.getConstructiveElement().getServerId());;
            values.put(Contract.GuestEntry.DEFECT_TYPE_ID, defect.getType().getServerId());
            values.put(Contract.GuestEntry.PORCH, defect.getPorch());
            values.put(Contract.GuestEntry.FLOOR, defect.getFloor());
            values.put(Contract.GuestEntry.FLAT, defect.getFlat());
            values.put(Contract.GuestEntry.DESCRIPTION, defect.getDescription());
            values.put(Contract.GuestEntry.DEFECT_MEASURE_ID, defect.getMeasure().getServerId());
            values.put(Contract.GuestEntry.CURRENCY, defect.getCurrency());

            result.add((int) db_write.insert(Contract.GuestEntry.DEFECT_TABLE_NAME, null, values));
        }
        return result;
    }

    public static Integer setDefectPhoto(Context context, Photo photo) {
        ArrayList<Photo> photoArrayList = new ArrayList<>();
        photoArrayList.add(photo);
        return setDefectPhotos(context, photoArrayList).get(0);
    }

    public static ArrayList<Integer> setDefectPhotos(Context context,  ArrayList<Photo> photoArrayList) {
        SQLiteDatabase db_write = getDBfromContext(context);
        ArrayList<Integer> result = new ArrayList<>();
        for (Photo photo : photoArrayList
                ) {
            ContentValues values = new ContentValues();
            //values.put(Contract.GuestEntry.SERVER_ID, photo.getId());
            values.put(Contract.GuestEntry.DEFECT_ID, photo.getDefect().getServerId());
            values.put(Contract.GuestEntry.PATH, photo.getPath());

            result.add((int) db_write.insert(Contract.GuestEntry.DEFECT_PHOTO_TABLE_NAME, null, values));
        }
        return result;
    }


}

