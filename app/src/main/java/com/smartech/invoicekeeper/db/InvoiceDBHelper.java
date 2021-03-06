package com.smartech.invoicekeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;


public class InvoiceDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "invoice.db";

    private static final String SQL_CREATE_INVOICE =
            "CREATE TABLE " + InvoiceContract.Invoice.TABLE_NAME + " (" +
                    InvoiceContract.Invoice._ID + " INTEGER PRIMARY KEY," +
                    InvoiceContract.Invoice.COLUMN_NAME_TITLE + " TEXT, " +
                    InvoiceContract.Invoice.COLUMN_NAME_TYPE + " TEXT, " +
                    InvoiceContract.Invoice.COLUMN_NAME_WARRANTY_PERIOD + " TEXT, " +
                    InvoiceContract.Invoice.COLUMN_NAME_IMAGE_FILE + " TEXT, " +
                    InvoiceContract.Invoice.COLUMN_NAME_DATE + " TEXT);";

    private static final String SQL_DELETE_INVOICES =
            "DROP TABLE IF EXISTS " + InvoiceContract.Invoice.TABLE_NAME;

    public InvoiceDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_INVOICE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_INVOICES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertInvoice(String title, String type, String warranty, String imageFile, String date){
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(InvoiceContract.Invoice.COLUMN_NAME_TITLE, title);
        contentValues.put(InvoiceContract.Invoice.COLUMN_NAME_DATE, String.valueOf(date));
        contentValues.put(InvoiceContract.Invoice.COLUMN_NAME_IMAGE_FILE, imageFile);
        contentValues.put(InvoiceContract.Invoice.COLUMN_NAME_TYPE, type);
        contentValues.put(InvoiceContract.Invoice.COLUMN_NAME_WARRANTY_PERIOD, warranty);

        return writableDatabase.insert(InvoiceContract.Invoice.TABLE_NAME, null, contentValues);
    }

    public void updateInvoice(long id, String title, String type, String warranty, String imageFile, String date) {
        SQLiteDatabase writableDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(InvoiceContract.Invoice.COLUMN_NAME_TITLE, title);
        contentValues.put(InvoiceContract.Invoice.COLUMN_NAME_DATE, String.valueOf(date));
        contentValues.put(InvoiceContract.Invoice.COLUMN_NAME_IMAGE_FILE, imageFile);
        contentValues.put(InvoiceContract.Invoice.COLUMN_NAME_TYPE, type);
        contentValues.put(InvoiceContract.Invoice.COLUMN_NAME_WARRANTY_PERIOD, warranty);

        String selection = InvoiceContract.Invoice._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        writableDatabase.update(
                InvoiceContract.Invoice.TABLE_NAME,
                contentValues,
                selection,
                selectionArgs);
    }

    public InvoiceDAO getInvoiceById(long dbID) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                InvoiceContract.Invoice._ID,
                InvoiceContract.Invoice.COLUMN_NAME_TITLE,
                InvoiceContract.Invoice.COLUMN_NAME_DATE,
                InvoiceContract.Invoice.COLUMN_NAME_WARRANTY_PERIOD,
                InvoiceContract.Invoice.COLUMN_NAME_TYPE,
                InvoiceContract.Invoice.COLUMN_NAME_IMAGE_FILE
        };

        String selection = InvoiceContract.Invoice._ID + " = ?";
        String[] selectionArgs = {String.valueOf(dbID)};

        Cursor cursor = db.query(
                InvoiceContract.Invoice.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        cursor.moveToFirst();
        return new InvoiceDAO(cursor.getLong(0), cursor.getString(1),cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5));
    }

    public Cursor getAll(){
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {
                InvoiceContract.Invoice._ID,
                InvoiceContract.Invoice.COLUMN_NAME_TITLE,
                InvoiceContract.Invoice.COLUMN_NAME_DATE,
                InvoiceContract.Invoice.COLUMN_NAME_WARRANTY_PERIOD,
                InvoiceContract.Invoice.COLUMN_NAME_TYPE,
                InvoiceContract.Invoice.COLUMN_NAME_IMAGE_FILE
        };

        Cursor cursor = db.query(InvoiceContract.Invoice.TABLE_NAME, columns,
                null, null, null, null, InvoiceContract.Invoice._ID + " DESC");

        return cursor;
    }

    public boolean deleteById(long id) {
        SQLiteDatabase writableDatabase = getWritableDatabase();

        return writableDatabase.delete(InvoiceContract.Invoice.TABLE_NAME,
                InvoiceContract.Invoice._ID +  "=" + id, null) > 0;
    }
}