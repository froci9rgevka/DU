package com.pea.du.web.stubAuthenticator.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.pea.du.db.data.DBHelper;
import com.pea.du.db.methods.ReadMethods;



/*
 * Define an implementation of ContentProvider that stubs out
 * all methods
 */
public class StubProvider extends ContentProvider {

    private static final String AUTHORITY = "com.pea.du.android.datasync.provider";
    private static final int ADRESS = 1;
    private static final int ADRESS_ID = 2;
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, "adress", ADRESS);
        sURIMatcher.addURI(AUTHORITY, "adress/#", ADRESS_ID);
    }

    public static final Uri ADRESS_URI = Uri.parse("content://" + AUTHORITY + "/" + "adress");

    private DBHelper dbHelper;
    private SQLiteDatabase db_write;


    /*
     * Always return true, indicating that the
     * provider loaded correctly.
     */
    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        db_write = dbHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (sURIMatcher.match(uri)) {
            case ADRESS:
                return ReadMethods.getCursorAddresses(getContext());
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    /*
     * Return no type for MIME type
     */
    @Override
    public String getType(Uri uri) {
    /*
     * query() always returns no results
     *
     */
        switch (sURIMatcher.match(uri)) {
            case ADRESS:
                return "vnd.Android.cursor.dir/vnd.com.example.restaurant.provider.menu";
            case ADRESS_ID:
                return "vnd.Android.cursor.dir/vnd.com.example.restaurant.provider.menu";
            default:
                throw new RuntimeException("getType No URI Match: " + uri);
        }
    }

    /*
     * insert() always returns null (no URI)
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
    /*
     * delete() always returns "no rows affected" (0)
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
    /*
     * update() always returns "no rows affected" (0)
     */
    public int update(
            Uri uri,
            ContentValues values,
            String selection,
            String[] selectionArgs) {
        return 0;
    }
}