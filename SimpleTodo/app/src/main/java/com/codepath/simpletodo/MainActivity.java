package com.codepath.simpletodo;

import android.content.Intent;
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

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    private static final String TODO_FILE_NAME = "todo.txt";
    private static final String LOG_TAG = "todoapp:MainActivity";
    private static final String ACTIVITY_HEADER = "TODO List";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(ACTIVITY_HEADER);

        lvItems = (ListView) findViewById(R.id.lvItems);

        readItems();

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    /**
     * onClick handler for the Save Button
     * @param v View
     */
    public void onAddItem(View v) {
        EditText eNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = eNewItem.getText().toString();
        saveItem(items.size(), itemText);
        eNewItem.setText("");
        writeItems();
    }

    /**
     * Saves a todo item in the items list. If the value is empty string, ignores for new items, deletes existing items. Updates existing index, adds if index is >= items size
     * @param index
     * @param value
     */
    private void saveItem(int index , String value) {
        if (items == null) {
            return;
        }
        value = value.trim();
        if (value.isEmpty()) {
            if (index < items.size()) {
                items.remove(index);
            }
        } else if (items.size() <= index) {
            items.add(index, value);
        } else {
            items.set(index, value);
        }
    }

    /**
     * Implement onClick and onLongClick for each listItem
     */
    private void setupListViewListener() {

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.v(LOG_TAG, "click position: " + position);

                // Open new activity
                lauchEditActivity(position);

            }
        });


        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                Log.v(LOG_TAG, "long click position: " + position);

                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();

                return true;
            }
        });

    }

    /**
     * Reads from TODO_FILE_NAME, if file does not exist, then creates one
     */
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
            Log.v(LOG_TAG, "file read failed : e : " + e);
            e.printStackTrace();
        }
    }

    /**
     * Writes into TODO_FILE_NAME
     */
    private void writeItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, TODO_FILE_NAME);
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            Log.v(LOG_TAG, "file write failed : e : " + e);
            e.printStackTrace();
        }
    }

    /**
     * Launch edit activity. Pass bundle with context
     * @param position
     */
    private void lauchEditActivity(int position) {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("value", items.get(position));
        intent.putExtras(bundle);

        startActivityForResult(intent, 1);
    }


    /**
     * Handles launching MainActivity from EditActivity. Reads context from bundle and handles saving and writing to persist data
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String newText = bundle.getString("editedValue");
                int position = bundle.getInt("position");
                Log.v(LOG_TAG, "Edited Value in Main Activity:  "+newText +" for position : "+position);
                saveItem(position, newText);
                writeItems();
                itemsAdapter.notifyDataSetChanged();
            }
        }
    }
}