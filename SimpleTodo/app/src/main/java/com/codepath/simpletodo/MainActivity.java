package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items ;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    private static final String TODO_FILE_NAME = "todo.txt";
    private static final String LOG_TAG = "todoapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);

        readItems();

        itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);

        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText eNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = eNewItem.getText().toString();
        items.add(itemText);
        eNewItem.setText("");
        writeItems();
    }

    private void setupListViewListener() {

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,
                                           int pos, long id) {

                Log.v(LOG_TAG ,"long click position: " + pos);

                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();

                return true;
            }
        });

    }

    private void readItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, TODO_FILE_NAME);

        try {
            if (!todoFile.exists()) {
                Log.v(LOG_TAG, "file does not exist");
                todoFile.createNewFile();
            }

            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            Log.v(LOG_TAG , "file read failed : e : "+e);
            e.printStackTrace();
        }
    }

    private void writeItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir,TODO_FILE_NAME);
        try {
            FileUtils.writeLines(todoFile,items);
        } catch (IOException e) {
            Log.v(LOG_TAG , "file write failed : e : "+e);
            e.printStackTrace();
        }
    }

}
