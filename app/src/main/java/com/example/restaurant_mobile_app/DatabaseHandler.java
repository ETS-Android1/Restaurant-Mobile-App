package com.example.restaurant_mobile_app;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "inquiry";
    private static final String TABLE_NAME = "inquiry";

    // Column names
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String SUBJECT = "subject";
    private static final String CONTENT = "content";

    public DatabaseHandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String TABLE_CREATE_QUERY = "CREATE TABLE "+TABLE_NAME+" " +
                "("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +NAME + " TEXT,"
                +EMAIL + " TEXT,"
                +SUBJECT+ " TEXT,"
                +CONTENT+" TEXT" +
                ");";

        db.execSQL(TABLE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS "+ TABLE_NAME;
        // Drop older table if existed
        db.execSQL(DROP_TABLE_QUERY);
        // Create tables again
        onCreate(db);
    }



    // Add a single inquiry

    public void addInquiry(Inquiry inquiry){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME,inquiry.getName());
        contentValues.put(EMAIL,inquiry.getEmail());
        contentValues.put(SUBJECT,inquiry.getSubject());
        contentValues.put(CONTENT,inquiry.getContent());

        //save to database
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        // close database
        sqLiteDatabase.close();
    }

    // Get all inquiries into a list
    public List<Inquiry> getAllInquiries(){

        List<Inquiry> inquiries = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do {
                // Create new Inquiry object
                Inquiry inquiry = new Inquiry();

                inquiry.setId(cursor.getInt(0));
                inquiry.setName(cursor.getString(1));
                inquiry.setEmail(cursor.getString(2));
                inquiry.setSubject(cursor.getString(3));
                inquiry.setContent(cursor.getString(4));


                inquiries.add(inquiry);
            }while (cursor.moveToNext());
        }
        return inquiries;
    }

    // Delete item
    public void deleteInquiry(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,"id =?",new String[]{String.valueOf(id)});
        db.close();
    }

    // Get a single inquiry
    public Inquiry getSingleInquiry(int id){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME,new String[]{ID,NAME,EMAIL,SUBJECT, CONTENT},
                ID + "= ?",new String[]{String.valueOf(id)}
                ,null,null,null);

        Inquiry inquiry;
        if(cursor != null){
            cursor.moveToFirst();
            inquiry = new Inquiry(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            return inquiry;
        }
        return null;
    }

    // Update a single Inquiry
    public int updateSingleInquiry(Inquiry inquiry){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME,inquiry.getName());
        contentValues.put(EMAIL,inquiry.getEmail());
        contentValues.put(SUBJECT,inquiry.getSubject());
        contentValues.put(CONTENT,inquiry.getContent());

        int status = db.update(TABLE_NAME,contentValues,ID +" =?",
                new String[]{String.valueOf(inquiry.getId())});

        db.close();
        return status;
    }

}

