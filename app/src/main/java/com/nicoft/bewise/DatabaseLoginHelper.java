package com.nicoft.bewise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nicolas on 15/09/2015.
 */
public class DatabaseLoginHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DatabaseL.db";

    public static final String TABLE_NAME_1 = "Lugar";
    public static final String COL_1_1 = "ID";
    public static final String COL_2_1 = "NAME";
    public static final String COL_3_1 = "EMAIL";
    public static final String COL_4_1 = "PASS";
    public static final String COL_5_1 = "LOGGED";
    public static final String COL_6_1 = "NOTID";




    public DatabaseLoginHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME_1 + " (" + COL_1_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2_1 + " TEXT," + COL_3_1 + " TEXT," + COL_4_1 + " TEXT," + COL_5_1 + " TEXT," + COL_6_1 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        onCreate(db);
    }

    public boolean insertData(String name, String email, String password, String rid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
                contentValues.put(COL_2_1, name);
                contentValues.put(COL_3_1, email);
                contentValues.put(COL_4_1, password);
                contentValues.put(COL_5_1, "YES");
                contentValues.put(COL_6_1, rid);
        long rta = db.insert(TABLE_NAME_1,null,contentValues);

        if (rta == -1){
            return false;
        }
        else{
            return true;
        }

    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + TABLE_NAME_1,null);
        return res;
    }

    public boolean updateData(String id, String name, String email, String password, String rid ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
                contentValues.put(COL_1_1, Integer.parseInt(id));
                contentValues.put(COL_2_1, name);
                contentValues.put(COL_3_1, email);
                contentValues.put(COL_4_1, password);
                contentValues.put(COL_5_1, "YES");
                contentValues.put(COL_6_1, rid);


        long rta = db.update(TABLE_NAME_1,contentValues, "ID = ?",new String[] {id});

        if (rta == -1){
            return false;
        }
        else{
            return true;
        }
    }
    //TODO test this metod.
    public Integer deleteData (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_1, "ID = ?",new String[] {id});
    }
}
