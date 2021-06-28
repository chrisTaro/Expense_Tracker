package com.example.expense_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateCategory extends AppCompatActivity {
    DBHelper mydb;
    EditText new_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
    }

    public void returnToPrevious(View view) {
        mydb = new DBHelper(this);
        new_category = (EditText) findViewById(R.id.editNewCategory);
        String categoryToAdd = new_category.getText().toString();
        if (mydb.insertNewCategory(categoryToAdd)) {
            Intent intent = new Intent(getApplicationContext(), PickCategory.class);
            Bundle extras = getIntent().getExtras();
            intent.putExtras(extras);
            startActivity(intent);
        }
    }
}
