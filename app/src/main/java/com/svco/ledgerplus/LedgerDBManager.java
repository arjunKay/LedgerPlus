package com.svco.ledgerplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by user on 11/11/2016.
/*
        Database name   :  LedgerPlusDB
        Tables          :   1. TRANSACTIONS - Storing expenditure/income details
                            2. CATEGORIES - Storing names of categories coming under expenditure and income

        Functions implemented:
        ----------------------
            -> boolean insertTxn(String amount,String source,String category,String description,String day,String month,String year)
                Description: For inserting data into table 'TRANSACTIONS'

            -> boolean insertCat(String cat,String type)
                Description: For inserting data into table 'Categories'

            -> Cursor getAllData(String tablename)
                Description : Self explanatory

            ->  boolean updateTxn(String id,String amount,String source,String category,String description,String day,String month,String year){
                Description : Update entry with ID=id in table 'TRANSACTIONS'

            ->  boolean updateCat(String id,String cat,String type)
                Description : Update entry with ID=id in table 'CATEGORIES'

            -> Integer deleteData(String id, String tablename)
                Description : Delete entry with ID=id in table specified by 'tablename'

            -> Integer sumOfTxn(String cond)
                Description : If cond="ex", it returns sum of all expenditure.
                              If cond="in", it returns sum of all income
 */

public class LedgerDBManager extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "LedgerPlusDB.db";
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
        db.execSQL(" create table TRANSACTIONS (ID INTEGER PRIMARY KEY AUTOINCREMENT,AMOUNT INTEGER,SOURCE TEXT,CATEGORY TEXT,DESCRIPTION TEXT,DAY INTEGER,MONTH INTEGER,YEAR INTEGER) ");
        db.execSQL(" create table CATEGORIES (ID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORY_NAME TEXT,TYPE TEXT");
        //Type specifies whether Category is of expenditure or income. "e" for expenditure and "i" for income

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TRANSACTIONS");
        db.execSQL("DROP TABLE IF EXISTS CATEGORIES");
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
        long result = db.insert("Transactions",null,contentValues);
        if(result==-1)
            return false;
        else
            return true;

    }
    public boolean insertCat(String cat,String type){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Category_name",cat);
        contentValues.put("Type",type);

        long result = db.insert("Categories",null,contentValues);
        if(result==-1)
            return false;
        else
            return true;

    }
    public Cursor getAllData(String tablename){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+tablename,null);
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
        db.update("Transactions",contentValues,"ID = ?",new String[] {id});
        return true;
    }
    public boolean updateCat(String id,String cat,String type){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id",id);
        contentValues.put("Category_name",cat);
        contentValues.put("type",type);

        db.update("Categories",contentValues,"ID = ?",new String[] {id});
        return true;
    }


    public Integer deleteData(String id, String tablename){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(tablename,"ID = ?",new String[]{id });

    }
    public Integer sumOfTxn(String cond){
        String relOp="";
        if(cond.equalsIgnoreCase("ex"))
            relOp="<";
        if(cond.equalsIgnoreCase("in"))
            relOp=">";
        SQLiteDatabase db=this.getWritableDatabase();
        int sum;
        Cursor c = db.rawQuery("select sum(amount) from transactions where Amount "+relOp+" 0 ;", null);
        if(c.moveToFirst())
        {
            sum = c.getInt(0);
            if(cond.equals("ex"))
                sum*=-1;
        }

        else
            sum = -1;
        c.close();
        return sum;
    }
}