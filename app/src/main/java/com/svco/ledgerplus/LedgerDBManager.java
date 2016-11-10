package com.svco.ledgerplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by user on 11/11/2016.
 */

public class LedgerDBManager extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "LedgerPlusDB.db";
    public static final String TABLE_NAME = "transaction";
    public static final String COL1 = "ID";
    public static final String COL2 = "AMOUNT";
    public static final String COL3 = "SOURCE";
    public static final String COL4 = "CATEGORY";
    public static final String COL5 = "DESCRIPTION";
    public static final String COL6 = "DAY";
    public static final String COL7 = "MONTH";
    public static final String COL8 = "YEAR";

    public LedgerDBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,AMOUNT INTEGER,SOURCE TEXT,CATEGORY TEXT,DESCRIPTION TEXT,DAY INTEGER,MONTH INTEGER,YEAR INTEGER) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertTxn(String amount,String source,String category,String description,String day,String month,String year){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,amount);
        contentValues.put(COL3,source);
        contentValues.put(COL4,category);
        contentValues.put(COL5,description);
        contentValues.put(COL6,day);
        contentValues.put(COL7,month);
        contentValues.put(COL8,year);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
            return false;
        else
            return true;

    }
    public Cursor getAllTxn(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
    public boolean updateTxn(String id,String amount,String source,String category,String description,String day,String month,String year){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,id);
        contentValues.put(COL2,amount);
        contentValues.put(COL3,source);
        contentValues.put(COL4,category);
        contentValues.put(COL5,description);
        contentValues.put(COL6,day);
        contentValues.put(COL7,month);
        contentValues.put(COL8,year);
        db.update(TABLE_NAME,contentValues,"ID = ?",new String[] {id});
        return true;
    }
    public Integer deleteTxn(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String[]{id });

    }
    public Integer sumOfTxn(String id){
        String relOp="";
        if(id.equalsIgnoreCase("ex"))
            relOp="<";
        if(id.equalsIgnoreCase("in"))
            relOp=">";
        SQLiteDatabase db=this.getWritableDatabase();
        int sum;
        Cursor c = db.rawQuery("select sum(amount) from "+TABLE_NAME+" where Amount "+relOp+" 0 ;", null);
        if(c.moveToFirst())
            sum = c.getInt(0);
        else
            sum = -1;
        c.close();
        return sum;
    }




}
