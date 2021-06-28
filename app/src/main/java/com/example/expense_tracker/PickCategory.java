package com.example.expense_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;

public class PickCategory extends AppCompatActivity {
    public static final String EDIT_NAME = "NAME";
    public static final String EDIT_CATEGORY = "CATEGORY";
    public static final String EDIT_DATE = "DATE";
    public static final String EDIT_AMOUNT = "AMOUNT";
    public static final String EDIT_NOTE = "NOTE";

    private ListView obj;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_category);
        mydb = new DBHelper(this);


        ArrayList<String> arrayList = mydb.getAllCategories();
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, arrayList);

        obj = (ListView)findViewById(R.id.listCategories);
        obj.setAdapter(arrayAdapter);

        obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String selectedFromList =(obj.getItemAtPosition(arg2).toString());
                Bundle extras = getIntent().getExtras();
                int Value = extras.getInt("id");
                Intent intent = new Intent(getApplicationContext(), EditExpense.class);
                if(extras !=null) {
                    if (Value > 0) {
                        Cursor rs = mydb.getExpenseData(Value);
                        rs.moveToFirst();

                        String name = rs.getString(rs.getColumnIndex(DBHelper.EXPENSE_COLUMN_NAME));
                        String date = rs.getString(rs.getColumnIndex(DBHelper.EXPENSE_COLUMN_DATE));
                        String amount = rs.getString(rs.getColumnIndex(DBHelper.EXPENSE_COLUMN_AMOUNT));
                        String note = rs.getString(rs.getColumnIndex(DBHelper.EXPENSE_COLUMN_NOTE));
                        mydb.updateExpense(Value, name, selectedFromList, date, Float.parseFloat(amount), note);
                        intent.putExtras(extras);
                        startActivity(intent);
                    } else {
                        String savedName = getIntent().getStringExtra(EditExpense.EDIT_NAME);
                        String savedDate = getIntent().getStringExtra(EditExpense.EDIT_DATE);
                        String savedAmount = getIntent().getStringExtra(EditExpense.EDIT_AMOUNT);
                        String savedNote = getIntent().getStringExtra(EditExpense.EDIT_NOTE);
                        intent.putExtra(EDIT_NAME, savedName);
                        intent.putExtra(EDIT_CATEGORY, selectedFromList);
                        intent.putExtra(EDIT_DATE, savedDate);
                        intent.putExtra(EDIT_AMOUNT, savedAmount);
                        intent.putExtra(EDIT_NOTE, savedNote);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_category:
                Intent intent = new Intent(getApplicationContext(), CreateCategory.class);
                Bundle extras = getIntent().getExtras();
                intent.putExtras(extras);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

