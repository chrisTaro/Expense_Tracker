package com.example.expense_tracker;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

public class EditExpense extends AppCompatActivity {
    public static final String EDIT_NAME = "NAME";
    public static final String EDIT_DATE = "DATE";
    public static final String EDIT_AMOUNT = "AMOUNT";
    public static final String EDIT_NOTE = "NOTE";

    DBHelper mydb;
    TextView name;
    TextView category;
    TextView date;
    TextView amount;
    TextView note;
    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        // textviews
        name = (TextView) findViewById(R.id.editName);
        category = (TextView) findViewById(R.id.editCategory);
        date = (TextView) findViewById(R.id.editDate);
        amount = (TextView) findViewById(R.id.editAmount);
        note = (TextView) findViewById(R.id.editNote);

        // db population
        mydb = new DBHelper(this);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            final int Value = extras.getInt("id");
            if (Value > 0) {
                Cursor rs = mydb.getExpenseData(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                String nam = rs.getString(rs.getColumnIndex(DBHelper.EXPENSE_COLUMN_NAME));
                String categor = rs.getString(rs.getColumnIndex(DBHelper.EXPENSE_COLUMN_CATEGORY));
                String dat = rs.getString(rs.getColumnIndex(DBHelper.EXPENSE_COLUMN_DATE));
                String amoun = rs.getString(rs.getColumnIndex(DBHelper.EXPENSE_COLUMN_AMOUNT));
                String not = rs.getString(rs.getColumnIndex(DBHelper.EXPENSE_COLUMN_NOTE));

                if (!rs.isClosed()) {
                    rs.close();
                }
                name.setText((CharSequence) nam);
                category.setText((CharSequence) categor);
                date.setText((CharSequence) dat);
                amount.setText((CharSequence) amoun);
                note.setText((CharSequence) not);

            } else {
                String savedName = getIntent().getStringExtra(PickCategory.EDIT_NAME);
                String savedCategory = getIntent().getStringExtra(PickCategory.EDIT_CATEGORY);
                String savedDate = getIntent().getStringExtra(PickCategory.EDIT_DATE);
                String savedAmount = getIntent().getStringExtra(PickCategory.EDIT_AMOUNT);
                String savedNote = getIntent().getStringExtra(PickCategory.EDIT_NOTE);
                name.setText((CharSequence) savedName);
                category.setText((CharSequence) savedCategory);
                date.setText((CharSequence) savedDate);
                amount.setText((CharSequence) savedAmount);
                note.setText((CharSequence) savedNote);
            }
        }
    }

    // options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_expense:
                Bundle extras = getIntent().getExtras();
                int Value = extras.getInt("id");
                mydb.deleteExpense(Value);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void pickCategory(View view) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (mydb.updateExpense(id_To_Update, name.getText().toString(),
                        category.getText().toString(), date.getText().toString(),
                        Float.parseFloat(amount.getText().toString()), note.getText().toString())) {
                    Intent intent = new Intent(getApplicationContext(), PickCategory.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else {
                Intent intent = new Intent(getApplicationContext(), PickCategory.class);
                intent.putExtra(EDIT_NAME, name.getText().toString());
                intent.putExtra(EDIT_DATE, date.getText().toString());
                intent.putExtra(EDIT_AMOUNT, amount.getText().toString());
                intent.putExtra(EDIT_NOTE, note.getText().toString());
                startActivity(intent);
            }
        }
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if (name.getText().toString().trim().equalsIgnoreCase("")) {
            name.setError("Please enter a name for expense");
        }
        if (category.getText().toString().trim().equalsIgnoreCase("")) {
            category.setError("Please pick a category");
        }
        if (date.getText().toString().trim().equalsIgnoreCase("")) {
            date.setError("Please enter a date");
        }
        if (amount.getText().toString().trim().equalsIgnoreCase("")) {
            amount.setError("Please enter an amount");
        }
        else if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (mydb.updateExpense(id_To_Update, name.getText().toString(),
                        category.getText().toString(), date.getText().toString(),
                        Float.parseFloat(amount.getText().toString()), note.getText().toString())) {
                    // to insert min & max
                    insertMin(Float.parseFloat(amount.getText().toString()), name.getText().toString());
                    insertMax(Float.parseFloat(amount.getText().toString()), name.getText().toString());

                    // when done, return back to main
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (mydb.insertExpense(name.getText().toString(), category.getText().toString(),
                        date.getText().toString(), Float.parseFloat(amount.getText().toString()),
                        note.getText().toString())) {

                    // to insert min & max
                    insertMin(Float.parseFloat(amount.getText().toString()),name.getText().toString());
                    insertMax(Float.parseFloat(amount.getText().toString()), name.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
                // return to main
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

    // min will be located in 1 slot
    public void insertMin(float num, String expenseName) {
        String URL = "content://com.example.assignment_7.MinMaxProvider";
        Uri minMax = Uri.parse(URL);

        String updateMin = Integer.toString(1);
        ContentValues values = new ContentValues();
        values.put(MinMaxProvider.NAME, expenseName);
        values.put(MinMaxProvider.AMOUNT, Float.toString(num));

        Cursor c = getContentResolver().query(minMax, null,MinMaxProvider._ID+"=?", new String[]{updateMin}, null);
        if(c.moveToFirst()) {
            float findIfMin = Float.parseFloat(c.getString(c.getColumnIndex(MinMaxProvider.AMOUNT)));
            if (num < findIfMin) {
                int id =
                        getContentResolver().update(MinMaxProvider.CONTENT_URI, values, MinMaxProvider._ID+"=?",new String[]{updateMin});
                Toast.makeText(getBaseContext(), "Min newly updated", Toast.LENGTH_LONG).show();
            }
        } else {
            Uri uri = getContentResolver().insert(MinMaxProvider.CONTENT_URI, values);
            Toast.makeText(getBaseContext(), "Min stats added", Toast.LENGTH_LONG).show();
        }
    }
    //  max will be located in 1 slot
    public void insertMax(float num, String expenseName) {
        String URL = "content://com.example.assignment_7.MinMaxProvider";
        Uri minMax = Uri.parse(URL);

        String updateMax = Integer.toString(2);
        ContentValues values = new ContentValues();
        values.put(MinMaxProvider.NAME, expenseName);
        values.put(MinMaxProvider.AMOUNT, Float.toString(num));

        Cursor c = getContentResolver().query(minMax, null,MinMaxProvider._ID+"=?", new String[]{updateMax}, null);
        if(c.moveToFirst()) {
            float findIfMax = Float.parseFloat(c.getString(c.getColumnIndex(MinMaxProvider.AMOUNT)));
            if (num > findIfMax) {
                int id =
                        getContentResolver().update(MinMaxProvider.CONTENT_URI, values, MinMaxProvider._ID+"=?",new String[]{updateMax});
                Toast.makeText(getBaseContext(), "Max newly updated", Toast.LENGTH_LONG).show();
            }
        } else {
            String updateMin = Integer.toString(1);
            c = getContentResolver().query(minMax, null,MinMaxProvider._ID+"=?", new String[]{updateMin}, null);
            if(c.moveToFirst()) {
                Uri uri = getContentResolver().insert(MinMaxProvider.CONTENT_URI, values);
                Toast.makeText(getBaseContext(), "Max stats added", Toast.LENGTH_LONG).show();
            }
        }
    }
}

