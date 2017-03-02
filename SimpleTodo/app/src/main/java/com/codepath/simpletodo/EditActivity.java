package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    private static final String LOG_TAG = "todoapp:EditActivity";
    private static final String ACTIVITY_HEADER = "Edit Item";

    private int position;
    private String currentText;
    private EditText newText;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(ACTIVITY_HEADER);
        setContentView(R.layout.activity_edit);

        bundle = getIntent().getExtras();

        if (bundle != null) {
            position = bundle.getInt("position");
            currentText = bundle.getString("value");
        }

        Log.v(LOG_TAG, "onCreate for position : "+position +" value : "+currentText);

        setUpCurrentText();

    }

    private void setUpCurrentText() {
        newText = (EditText)findViewById(R.id.newText);
        newText.setText(currentText);
        // cursor at the end
        newText.setSelection(currentText.length());
    }

    public void onEditItem(View v) {
        Log.v(LOG_TAG, "Edit On Save");

        String editedTask = newText.getText().toString();

        Log.v(LOG_TAG, "Edited task "+editedTask);

        Intent intent = new Intent();

        bundle.putString("editedValue", editedTask);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);

        this.finish();
    }

    public void onBackPressed() {
        Log.v(LOG_TAG, "Back Button");
        this.finish();
    }
}
