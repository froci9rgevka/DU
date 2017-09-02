package com.pea.du.db.methods;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pea.du.data.*;
import com.pea.du.data.StaticValue;
import com.pea.du.db.data.Contract;
import com.pea.du.db.data.DBHelper;

import java.util.ArrayList;

import static com.pea.du.data.User.getUserById;
import static com.pea.du.db.data.Contract.GuestEntry.*;

public class ReadMethods {

    private static SQLiteDatabase getDBFromContext(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        return dbHelper.getReadableDatabase();
    }

    /*
    query имеет 7 аргументов
    если null, значит выбырается всё
    1-таблица
    2-столбцы
    3-столбцы для условия WHERE
    4-значения для условия WHERE
    5-группировка по столбцам
    6-фильтрация по группам
    7-порядок сортировки
     */

    public static ArrayList<StaticValue> getStaticValues(Context context, String tableName, String selection, String[] selectionArgs){
        SQLiteDatabase db = getDBFromContext(context);
        ArrayList result = new ArrayList();

        Cursor cursor = db.query(
                tableName, null, selection, selectionArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            StaticValue staticValue = new StaticValue();
            staticValue.setId(cursor.getInt(0));
            staticValue.setServerId(cursor.getInt(1));
            staticValue.setName(cursor.getString(2));
            result.add(staticValue);
        }

        return result;
    }

    public static Cursor getCursorAddresses(Context context){
        SQLiteDatabase db = getDBFromContext(context);

        Cursor cursor = db.query(
                ADDRESS_TABLE_NAME, null, null, null, null, null, null, null);

        cursor.moveToFirst();

        return cursor;
    }



    public static ArrayList<User> getUsers(Context context, String selection, String[] selectionArgs){
        SQLiteDatabase db = getDBFromContext(context);
        ArrayList result = new ArrayList();


        Cursor cursor = db.query(
                Contract.GuestEntry.USER_TABLE_NAME, null, selection, selectionArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            User user = new User();

            user.setId(cursor.getInt(0));
            user.setServerId(cursor.getInt(1));
            user.setNickname(cursor.getString(2));
            user.setPassword(cursor.getString(3));

            result.add(user);
        }

        return result;
    }



    public static ArrayList<Act> getActs(Context context, String selection, String[] selectionArgs){
        SQLiteDatabase db = getDBFromContext(context);
        ArrayList result = new ArrayList();

        Cursor cursor = db.query(
                Contract.GuestEntry.ACT_TABLE_NAME, null, selection, selectionArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            User user;
            Act act = new Act();

            act.setId(cursor.getInt(0));
            act.setServerId(cursor.getInt(1));

            user = getUserById(context,(cursor.getInt(2)));
            act.setUser(user);

            StaticValue address = new StaticValue(ADDRESS_TABLE_NAME, ADDRESS);
            address.setServerId(cursor.getInt(3));
            address.getStaticById(context);
            act.setAddress(address);
            result.add(act);
        }

        return result;
    }

    public static ArrayList<Defect> getDefects(Context context, String selection, String[] selectionArgs){
        SQLiteDatabase db = getDBFromContext(context);
        ArrayList result = new ArrayList();


        Cursor cursor = db.query(
                Contract.GuestEntry.DEFECT_TABLE_NAME, null, selection, selectionArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            Defect defect = new Defect();
            Act act = new Act();
            StaticValue constructiveElement = new StaticValue(DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, DEFECT_CONSTRUCTIVE_ELEMENT);
            StaticValue type = new StaticValue(DEFECT_TYPE_TABLE_NAME, DEFECT_TYPE);
            StaticValue measure = new StaticValue(DEFECT_MEASURE_TABLE_NAME, DEFECT_MEASURE);

            defect.setId(cursor.getInt(0));
            defect.setServerId(cursor.getInt(1));

            act.setServerId(cursor.getInt(2));
            defect.setAct(act);

            constructiveElement.setServerId(cursor.getInt(3));
            constructiveElement.getStaticById(context);
            defect.setConstructiveElement(constructiveElement);

            type.setServerId(cursor.getInt(4));
            type.getStaticById(context);
            defect.setType(type);

            defect.setPorch(cursor.getString(5));
            defect.setFloor(cursor.getString(6));
            defect.setFlat(cursor.getString(7));
            defect.setDescription(cursor.getString(8));

            measure.setServerId(cursor.getInt(9));
            measure.getStaticById(context);
            defect.setMeasure(measure);

            defect.setCurrency(cursor.getString(10));

            result.add(defect);
        }

        return result;
    }

    public static ArrayList<Photo> getDefectPhotos(Context context, String selection, String[] selectionArgs){
        SQLiteDatabase db = getDBFromContext(context);

        ArrayList result = new ArrayList();

        Cursor cursor = db.query(
                Contract.GuestEntry.DEFECT_PHOTO_TABLE_NAME, null, selection, selectionArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            Photo photo = new Photo();
            Defect defect = new Defect();

            photo.setId(cursor.getInt(0));
            //photo.setServerId(cursor.getInt(1));

            defect.setServerId(cursor.getInt(2));
            photo.setDefect(defect);

            photo.setUrl(cursor.getString(3));
            photo.setPath(cursor.getString(4));

            result.add(photo);
        }

        return result;
    }

}
