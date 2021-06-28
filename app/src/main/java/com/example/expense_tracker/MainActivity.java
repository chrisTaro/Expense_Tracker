

/* CS 453 - Assignment #7
 * MainActivity.java
 * By: Christian Magpantay
 * Code Referenced: http://borg.csueastbay.edu/~bhecker/CS-453/Examples/ContentProvider.txt
 * Implements a custom content provider by taking an existing database
 * built by the user that holds all expenses and then
 * compares all expenses for most expensive and
 * least expensive. The user will be able to find this information
 * in the "Expense Stats" activity. The actitivity will have both
 * buttons for the infomation mentioned above which will be contained by a
 * custom content provider.
 * */

package com.example.expense_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView obj;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = new DBHelper(this);

        ArrayList<String> array_list = mydb.getAllExpenses();
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, array_list);

        obj = (ListView)findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);
        obj.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                int id_To_Search = arg2 + 1;
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);
                Intent intent = new Intent(getApplicationContext(), EditExpense.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    public void createExpense(View view){
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("id", 0);
        Intent intent = new Intent(getApplicationContext(), EditExpense.class);
        intent.putExtras(dataBundle);
        startActivity(intent);
    }

    public void statButton(View view){
        Intent intent = new Intent(getApplicationContext(), ProviderHandler.class);
        startActivity(intent);
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

}
