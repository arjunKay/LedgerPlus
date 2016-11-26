package com.svco.ledgerplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
                Description: For inserting data into table 'CATEGORIES'

            -> Cursor getAllData(String tablename)
                Description : Self explanatory

            ->  boolean updateTxn(String id,String amount,String source,String category,String description,String day,String month,String year){
                Description : Update entry with ID=id in table 'TRANSACTIONS'

            ->  boolean updateCat(String id,String cat,String type)
                Description : Update entry with ID=id in table 'CATEGORIES'

            -> Integer deleteTxn(String id)
                Description : Delete entry with KEY_ID=id in table TRANSACTIONS

            -> Integer deleteCat(String id)
                Description : Delete entry with KEY_ID=id in table CATEGORIES

            -> Integer sumOfTxn(String cond)
                Description : If cond="ex", it returns sum of all expenditure.
                              If cond="in", it returns sum of all income

            -> Cursor getExCategory()
                Description : Get a cursor containing all Expense Categories

            -> Cursor getInCategory()
                Description : Get a cursor containing all Income Categories

            -> Cursor executeQuery(String query)
                Description : Execute query specified in 'query'

            ->Cursor filterData(String startDate, String endDate, String cashOrBank, String inOrEx, String category, String minAmt, String maxAmt)
                Description: Retrieve data based on filter conditions
 */

public class LedgerDBManager extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "LedgerPlusDB.db";

    public static final String TABLE_TRANSACTIONS = "TRANSACTIONS";
    public static final String TABLE_CATEGORIES = "CATEGORIES";
    public static final String TABLE_PROFILE = "PROFILE";


    public static final String KEY_ID = "_id";

    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";

    public static final String AMOUNT = "AMOUNT";
    public static final String SOURCE = "SOURCE";
    public static final String CATEGORY = "CATEGORY";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String DAY = "DAY";
    public static final String MONTH = "MONTH";
    public static final String YEAR = "YEAR";

    public static final String CATEGORY_NAME = "CATEGORY_NAME";
    public static final String TYPE = "TYPE";
    private final String[] EX_CATEGORIES = {"Food","Travel","Shopping","Entertainment"};
    private final String[] IN_CATEGORIES = {"Gift","Salary","Profit"};

    public LedgerDBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table for PROFILE
        db.execSQL("CREATE TABLE " + TABLE_PROFILE +" ( "+ KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ NAME +" TEXT, "+ EMAIL+" TEXT) ");
        //Create the tables TRANSACTIONS and CATEGORIES
        db.execSQL("CREATE TABLE "+TABLE_TRANSACTIONS+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+AMOUNT+" INTEGER,"+SOURCE+" TEXT,"+CATEGORY+" TEXT,"+DESCRIPTION+" TEXT,"+DAY+" INTEGER,"+MONTH+" INTEGER,"+YEAR+" INTEGER) ");
        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES +" ( "+ KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ CATEGORY_NAME +" TEXT, "+ TYPE +" TEXT )");

        //Populate CATEGORIES table with default categories when the database is created for the first time
        int x=0;
        ContentValues contentValues = new ContentValues();

        while(x<4){
            contentValues.put(CATEGORY_NAME, EX_CATEGORIES[x]);
            contentValues.put(TYPE, "E");
            db.insert(TABLE_CATEGORIES, null, contentValues);
            x++;
        }
        x=0;
        while(x<3){
            contentValues.put(CATEGORY_NAME, IN_CATEGORIES[x]);
            contentValues.put(TYPE, "I");
            db.insert(TABLE_CATEGORIES, null, contentValues);
            x++;
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TRANSACTIONS");
        db.execSQL("DROP TABLE IF EXISTS CATEGORIES");
        db.execSQL("DROP TABLE IF EXISTS PROFILE");
        onCreate(db);
    }
//adding profiles
public boolean addProfileName(String name,String email){
    SQLiteDatabase db=this.getWritableDatabase();
    ContentValues contentValues=new ContentValues();
    contentValues.put(NAME,name);
    contentValues.put(EMAIL,email);
    long result = db.insert(TABLE_PROFILE,null,contentValues);
    if(result==-1)
        return false;
    else
        return true;

}
    public Cursor getProfileName(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_PROFILE,null);
        return res;
    }

    // For inserting data into table 'TRANSACTIONS'
    public boolean insertTxn(String amount,String source,String category,String description,String day,String month,String year){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AMOUNT,amount);
        contentValues.put(SOURCE,source);
        contentValues.put(CATEGORY,category);
        contentValues.put(DESCRIPTION,description);
        contentValues.put(DAY,day);
        contentValues.put(MONTH,month);
        contentValues.put(YEAR,year);
        long result = db.insert("Transactions",null,contentValues);
        if(result==-1)
            return false;
        else
            return true;

    }

    //For inserting data into table 'CATEGORIES'
    public int insertCat(String cat,String type){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME, cat);
        contentValues.put(TYPE, type);
        long result = db.insert(TABLE_CATEGORIES, null, contentValues);
        Log.e("res",""+result);
        return (int)result;
    }


    public Cursor getAllData(String tablename){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+tablename,null);
        return res;
    }


    //Update entry with ID=id in table 'TRANSACTIONS'
    public boolean updateTxn(String id,String amount,String source,String category,String description,String day,String month,String year){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID,id);
        contentValues.put(AMOUNT,amount);
        contentValues.put(SOURCE,source);
        contentValues.put(CATEGORY,category);
        contentValues.put(DESCRIPTION,description);
        contentValues.put(DAY,day);
        contentValues.put(MONTH,month);
        contentValues.put(YEAR,year);
        db.update(TABLE_TRANSACTIONS,contentValues,KEY_ID+" = ?",new String[] {id});
        return true;
    }

    //Update the Category with KEY_ID=id in table 'CATEGORIES'
    public void updateCat(String id,String cat){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID,id);
        contentValues.put(CATEGORY_NAME,cat);
        db.update(TABLE_CATEGORIES,contentValues, KEY_ID +" = ?",new String[] {id});
    }

    //Delete entry with KEY_ID=id in table TRANSACTIONS
    public Integer deleteTxn(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_TRANSACTIONS,KEY_ID+" = ?",new String[]{id });

    }

    //Delete entry with KEY_ID=id in table CATEGORIES
    public Integer deleteCat(String id) {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_CATEGORIES, KEY_ID +" = ?",new String[]{id });
    }


    /* If cond="ex", it returns sum of all expenditure.
       If cond="in", it returns sum of all income
     */
    public Integer sumOfTxn(String cond){
        String relOp="";
        if(cond.equalsIgnoreCase("ex"))
            relOp="<";
        if(cond.equalsIgnoreCase("in"))
            relOp=">";
        SQLiteDatabase db=this.getWritableDatabase();
        int sum;
        Cursor c = db.rawQuery("SELECT SUM("+AMOUNT+") FROM "+TABLE_TRANSACTIONS+" where "+AMOUNT+" "+relOp+" 0 ;", null);
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
   public Integer sumOfExpToday(String currentYear,String currentMonth,String currentDay){
        String relOp="<";
        String eq ="=";
       int a= Integer.parseInt(currentDay);
       int d=Integer.parseInt(currentYear);
       int b=Integer.parseInt(currentMonth);
       SQLiteDatabase db=this.getWritableDatabase();
        int sum =0;
        Cursor c=db.rawQuery("SELECT SUM("+AMOUNT+") FROM "+TABLE_TRANSACTIONS+" WHERE "+AMOUNT+" "+relOp+" 0 AND  "+YEAR+"="+d+" AND "+MONTH+" = "+b+" AND "+DAY+"="+a+"; ",null);
       if(c.moveToFirst()){
           sum=c.getInt(0);
            sum=sum*-1;
        }c.close();
       
        return sum;
    }

    public Integer sumOfInToday(String currentYear,String currentMonth,String currentDay){
        String relOp=">";
        String eq ="=";
        int a= Integer.parseInt(currentDay);
        int d=Integer.parseInt(currentYear);
        int b=Integer.parseInt(currentMonth);
        SQLiteDatabase db=this.getWritableDatabase();
        int sum =0;
        Cursor c=db.rawQuery("SELECT SUM("+AMOUNT+") FROM "+TABLE_TRANSACTIONS+" WHERE "+AMOUNT+" "+relOp+" 0 AND  "+YEAR+"="+d+" AND "+MONTH+" = "+b+" AND "+DAY+"="+a+"; ",null);
        if(c.moveToFirst()){
            sum=c.getInt(0);
        }c.close();

        return sum;
    }


    public Integer sumOfInThisMonth(String currentYear,String currentMonth){
        String relOp=">";
        String eq ="=";
        int d=Integer.parseInt(currentYear);
        int b=Integer.parseInt(currentMonth);
        SQLiteDatabase db=this.getWritableDatabase();
        int sum =0;
        Cursor c=db.rawQuery("SELECT SUM("+AMOUNT+") FROM "+TABLE_TRANSACTIONS+" WHERE "+AMOUNT+" "+relOp+" 0 AND  "+YEAR+"="+d+" AND "+MONTH+" = "+b+"; ",null);
        if(c.moveToFirst()){
            sum=c.getInt(0);
        }c.close();

        return sum;
    }
    public Integer sumOfExpThisMonth(String currentYear,String currentMonth){
        String relOp="<";
        String eq ="=";
        int d=Integer.parseInt(currentYear);
        int b=Integer.parseInt(currentMonth);
        SQLiteDatabase db=this.getWritableDatabase();
        int sum =0;
        Cursor c=db.rawQuery("SELECT SUM("+AMOUNT+") FROM "+TABLE_TRANSACTIONS+" WHERE "+AMOUNT+" "+relOp+" 0 AND  "+YEAR+"="+d+" AND "+MONTH+" = "+b+"; ",null);
        if(c.moveToFirst()){
            sum=c.getInt(0);
            sum=sum*-1;
        }c.close();

        return sum;
    }

    public Integer sumOfExpThisYear(String currentYear){
        String relOp="<";
        String eq ="=";
        int d=Integer.parseInt(currentYear);
        SQLiteDatabase db=this.getWritableDatabase();
        int sum =0;
        Cursor c=db.rawQuery("SELECT SUM("+AMOUNT+") FROM "+TABLE_TRANSACTIONS+" WHERE "+AMOUNT+" "+relOp+" 0 AND  "+YEAR+"="+d+"; ",null);
        if(c.moveToFirst()){
            sum=c.getInt(0);
            sum=sum*-1;
        }c.close();

        return sum;
    }
    public Integer sumOfInThisYear(String currentYear){
        String relOp=">";
        String eq ="=";
        int d=Integer.parseInt(currentYear);
        SQLiteDatabase db=this.getWritableDatabase();
        int sum =0;
        Cursor c=db.rawQuery("SELECT SUM("+AMOUNT+") FROM "+TABLE_TRANSACTIONS+" WHERE "+AMOUNT+" "+relOp+" 0 AND  "+YEAR+"="+d+"; ",null);
        if(c.moveToFirst()){
            sum=c.getInt(0);
        }c.close();

        return sum;
    }



    public Cursor getExCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+ TABLE_CATEGORIES +" WHERE TYPE = 'E'",null);
        return res;
    }

    public Cursor getInCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+ TABLE_CATEGORIES +" WHERE TYPE = 'I'",null);
        return res;
    }

    //Execute query specified in 'query'
    public Cursor executeQuery(String query)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cur=db.rawQuery(query,null);
        return cur;
    }


    //Retrieve data based on filter conditions
    public Cursor filterData(String startDate, String endDate, String cashOrBank, String inOrEx, String category, String minAmt, String maxAmt)
    {
        String thequery="Select * from "+TABLE_TRANSACTIONS+" WHERE ";
        String temp=thequery;
        String startDateCond="", endDateCond="";
        Cursor c;
        if(startDate.equals("0")==false)
        {
            startDateCond="((Year*10000)+(Month*100)+Day)>="+startDate+" ";
            thequery=thequery+startDateCond+" AND ";
        }
        if(endDate.equals("0")==false)
        {
            endDateCond="((Year*10000)+(Month*100)+Day)<="+endDate+" ";
            thequery=thequery+endDateCond+" AND ";
        }

        String srcCond="";
        if(cashOrBank.equals("")==false)
        {
            srcCond="Source='"+cashOrBank+"' ";
            thequery=thequery+srcCond+" AND ";
        }


        String inOrExCond="";
        if(inOrEx.equals("e"))
        {
            inOrExCond=" Amount<0 ";
            thequery=thequery+inOrExCond+" AND ";
        }
        if(inOrEx.equals("i"))
        {
            inOrExCond=" Amount>0 ";
            thequery=thequery+inOrExCond+" AND ";
        }

        String catCond="";
        if(category.equals("")==false)
        {
            catCond="category='"+category+"'";
            thequery=thequery+catCond+" AND ";
        }

        String minAmtCond="";
        if(!minAmt.equals("0"))
        {
            minAmtCond=" ABS(Amount)>"+minAmt;
            thequery=thequery+minAmtCond+" AND ";
        }
        String maxAmtCond="";
        if(!maxAmt.equals("-1"))
        {
            maxAmtCond=" ABS(Amount)<"+maxAmt;
            thequery=thequery+maxAmtCond+" AND ";
        }

        if(thequery.equals(temp))
            return getAllData(TABLE_TRANSACTIONS);
        else
        {
            thequery=thequery.substring(0,thequery.lastIndexOf("AND"));
            SQLiteDatabase db=this.getWritableDatabase();
            c=db.rawQuery(thequery,null);
            return c;
        }

    }
}