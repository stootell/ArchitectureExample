package com.example.samto.architectureexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    private Intent intent;


    private static final String EXTRA_ID =
            "com.example.samto.architectureexample.EXTRA_ID";
    private static final String EXTRA_TITLE =
            "com.example.samto.architectureexample.EXTRA_TITLE";

    private static final String EXTRA_DESCRIPTOR =
            "com.example.samto.architectureexample.EXTRA_DESCRIPTOR";

    private static final String EXTRA_PRIORITY =
            "com.example.samto.architectureexample.EXTRA_PRIORITY";

    private void SaveNote() {
        int id = intent.getIntExtra(EXTRA_ID, -1);
        int priority = numberPickerPriority.getValue();
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        Intent data = new Intent();

        // Check if values are empty. trim() eliminates spaces if it's all spaces
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Text fields can not be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        if (id != -1) {
           data.putExtra(EXTRA_ID, id);
        }

        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTOR, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        setResult(RESULT_OK, data);
        finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);


        editTextTitle = findViewById(R.id.et_title);
        editTextDescription = findViewById(R.id.et_entry);
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(100);

        //Put our icon in the menu bar
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTOR));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else {
            setTitle("Add Note");
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note: {
                SaveNote();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
