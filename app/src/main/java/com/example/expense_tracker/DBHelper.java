package com.example.expense_tracker;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String COLUMN_ID = "id";

    public static final String CATEGORIES_TABLE_NAME = "categories";
    public static final String CATEGORIES_COLUMN_LIST = "list";

    public static final String EXPENSE_TABLE_NAME = "expenses";
    public static final String EXPENSE_COLUMN_NAME = "name";
    public static final String EXPENSE_COLUMN_CATEGORY = "category";
    public static final String EXPENSE_COLUMN_DATE = "date";
    public static final String EXPENSE_COLUMN_AMOUNT = "amount";
    public static final String EXPENSE_COLUMN_NOTE = "note";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table expenses (id integer primary key, name text, category text, " +
                        "date text, amount real, note text)"
        );
        db.execSQL(
                "create table categories (id integer primary key, list text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS expenses");
        db.execSQL("DROP TABLE IF EXISTS categories");
        onCreate(db);
    }

    public boolean insertExpense(String name, String category, String date, float amount, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("category", category);
        contentValues.put("date", date);
        contentValues.put("amount", amount);
        contentValues.put("note", note);
        db.insert("expenses", null, contentValues);
        return true;
    }

    public boolean insertNewCategory(String newCate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("list", newCate);
        db.insert("categories", null, contentValues);
        return true;
    }

    public Cursor getExpenseData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from expenses where id="+id+"", null );
        return res;
    }

    public Cursor getCategoryData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from categories where id="+id+"", null );
        return res;
    }

    public boolean updateExpense(Integer id, String name, String category, String date, float amount,
                                 String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("category", category);
        contentValues.put("date", date);
        contentValues.put("amount", amount);
        contentValues.put("note", note);
        db.update("expenses", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateCategory(Integer id, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", category);
        db.update("expenses", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteExpense (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("expenses",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllExpenses() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from expenses", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(EXPENSE_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllCategories() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from categories", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CATEGORIES_COLUMN_LIST)));
            res.moveToNext();
        }
        return array_list;
    }

}