package com.example.session5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText word, definition;
    DictionaryDatabase mDB;
    ListView ListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDB = new DictionaryDatabase(this);
        word = findViewById(R.id.idWordEt);
        definition = findViewById(R.id.idDefinitionEt);
        Button buttonAddUpdate = findViewById(R.id.SaveBtn);
        buttonAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecord();
            }
        });

        ListView = findViewById(R.id.listView);
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, mDB.getDefinition(id), Toast.LENGTH_SHORT).show();
            }
        });
        ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,
                        "Records deleted = " + mDB.deleteRecord(id), Toast.LENGTH_SHORT).show();
                updateWordList();
                return true;
            }
        });
        updateWordList();
    }

    private void saveRecord() {
        mDB.saveRecord(word.getText().toString(), definition.getText().toString());
        word.setText("");
        definition.setText("");
        updateWordList();
    }

    private void updateWordList() {
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                mDB.getWordList(),
                new String[]{"word"},
                new int[]{android.R.id.text1},
                0);
        ListView.setAdapter(simpleCursorAdapter);
    }
}