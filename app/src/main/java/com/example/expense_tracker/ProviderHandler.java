package com.example.expense_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ProviderHandler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_handler);
    }


    public void onClickRetrieveMin(View view) {

        String pullMin = Integer.toString(1);
        // Retrieve min max
        String URL = "content://com.example.assignment_7.MinMaxProvider";
        Uri minMax = Uri.parse(URL);
        Cursor c = getContentResolver().query(minMax, null,MinMaxProvider._ID+"=?", new String[]{pullMin}, null);

        String myStudents = "";

        if(c.moveToFirst()) {
            myStudents = myStudents +
                    "Expense: " + c.getString(c.getColumnIndex(MinMaxProvider.NAME)) + "\n"
                    + "Amount: $"  + c.getString(c.getColumnIndex(MinMaxProvider.AMOUNT));
        }
        Toast.makeText(this, myStudents, Toast.LENGTH_LONG).show();
    }

    public void onClickRetrieveMax(View view) {

        String pullMax = Integer.toString(2);
        // Retrieve min max
        String URL = "content://com.example.assignment_7.MinMaxProvider";
        Uri minMax = Uri.parse(URL);
        Cursor c = getContentResolver().query(minMax, null,MinMaxProvider._ID+"=?", new String[]{pullMax}, null);

        String myStudents = "";

        if(c.moveToFirst()) {
            myStudents = myStudents +
                    "Expense: " + c.getString(c.getColumnIndex(MinMaxProvider.NAME)) + "\n"
                    + "Amount: $"  + c.getString(c.getColumnIndex(MinMaxProvider.AMOUNT));
        }
        Toast.makeText(this, myStudents, Toast.LENGTH_LONG).show();
    }

    public void backHome(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
