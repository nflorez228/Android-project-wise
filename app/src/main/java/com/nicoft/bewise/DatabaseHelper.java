package com.nicoft.bewise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nicolas on 15/09/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Database.db";

    public static final String TABLE_NAME_1 = "Lugar";
    public static final String COL_1_1 = "ID";
    public static final String COL_2_1 = "NOMBRE";
    public static final String COL_3_1 = "TIPO";
    public static final String COL_4_1 = "URL";

    public static final String TABLE_NAME_2 = "Habitacion";
    public static final String COL_1_2 = "ID";
    public static final String COL_2_2 = "NOMBRE";
    public static final String COL_3_2 = "TIPO";
    public static final String COL_4_2 = "PISO";
    public static final String COL_5_2 = "ID_LUGARES";

    public static final String TABLE_NAME_3 = "Elemento";
    public static final String COL_1_3 = "ID";
    public static final String COL_2_3 = "NOMBRE";
    public static final String COL_3_3 = "TIPO";
    public static final String COL_4_3 = "ESTADO";
    public static final String COL_5_3 = "TIME";
    public static final String COL_6_3 = "ID_HABITACION";

    public static final String TABLE_NAME_4 = "Tipo_Elemento";
    public static final String COL_1_4 = "ID";
    public static final String COL_2_4 = "NOMBRE";
    public static final String COL_3_4 = "CATEGORIA";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME_1 + " (" + COL_1_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2_1 + " TEXT," + COL_3_1 + " TEXT," + COL_4_1 +" TEXT)");
        db.execSQL("create table " + TABLE_NAME_2 + " (" + COL_1_2 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2_2 + " TEXT," + COL_3_2 + " TEXT," + COL_4_2 + " TEXT," + COL_5_2 +  " INTEGER)");
        db.execSQL("create table " + TABLE_NAME_3 + " (" + COL_1_3 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2_3 + " TEXT," + COL_3_3 + " INTEGER," + COL_4_3 + " TEXT," + COL_5_3 + " TEXT," + COL_6_3 + " INTEGER)");
        db.execSQL("create table " + TABLE_NAME_4 + " (" + COL_1_4 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2_4 + " TEXT," + COL_3_4 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_4);
        onCreate(db);
    }

    public boolean insertData(String table, String... args ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        switch(table)
        {
            case TABLE_NAME_1:
                contentValues.put(COL_2_1, args[0]);
                contentValues.put(COL_3_1, args[1]);
                contentValues.put(COL_4_1, args[2]);
                break;
            case TABLE_NAME_2:
                contentValues.put(COL_2_2, args[0]);
                contentValues.put(COL_3_2, args[1]);
                contentValues.put(COL_4_2, args[2]);
                contentValues.put(COL_5_2, Integer.parseInt(args[3]));
                break;
            case TABLE_NAME_3:
                contentValues.put(COL_2_3, args[0]);
                contentValues.put(COL_3_3, Integer.parseInt(args[1]));
                contentValues.put(COL_4_3, args[2]);
                contentValues.put(COL_5_3, args[3]);
                contentValues.put(COL_6_3, Integer.parseInt(args[4]));
                break;
            case TABLE_NAME_4:
                contentValues.put(COL_2_4, args[0]);
                contentValues.put(COL_3_4, args[1]);
                break;
        }
        long rta = db.insert(table,null,contentValues);

        if (rta == -1){
            return false;
        }
        else{
            return true;
        }

    }

    public Cursor getAllData(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + table,null);
        return res;
    }

    public boolean updateData(String table, String id, String... args ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        switch(table)
        {
            case TABLE_NAME_1:
                contentValues.put(COL_1_1, Integer.parseInt(id));
                contentValues.put(COL_2_1, args[0]);
                contentValues.put(COL_3_1, args[1]);
                contentValues.put(COL_4_1, args[2]);
                break;
            case TABLE_NAME_2:
                contentValues.put(COL_1_2, Integer.parseInt(id));
                contentValues.put(COL_2_2, args[0]);
                contentValues.put(COL_3_2, args[1]);
                contentValues.put(COL_4_2, args[2]);
                contentValues.put(COL_5_2, Integer.parseInt(args[3]));
                break;
            case TABLE_NAME_3:
                contentValues.put(COL_1_3, Integer.parseInt(id));
                contentValues.put(COL_2_3, args[0]);
                contentValues.put(COL_3_3, Integer.parseInt(args[1]));
                contentValues.put(COL_4_3, args[2]);
                contentValues.put(COL_5_3, args[3]);
                contentValues.put(COL_6_3, Integer.parseInt(args[4]));
                break;
            case TABLE_NAME_4:
                contentValues.put(COL_1_4, Integer.parseInt(id));
                contentValues.put(COL_2_4, args[0]);
                contentValues.put(COL_3_4, args[1]);
                break;
        }
        long rta = db.update(table,contentValues, "ID = ?",new String[] {id});

        if (rta == -1){
            return false;
        }
        else{
            return true;
        }
    }
    //TODO test this metod.
    public Integer deleteData (String table, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table, "ID = ?",new String[] {id});
    }
}
