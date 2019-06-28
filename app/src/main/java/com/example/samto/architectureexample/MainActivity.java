package com.example.samto.architectureexample;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private static final String EXTRA_TITLE =
            "com.example.samto.architectureexample.EXTRA_TITLE";

    private static final String EXTRA_DESCRIPTOR =
            "com.example.samto.architectureexample.EXTRA_DESCRIPTOR";

    private static final String EXTRA_PRIORITY =
            "com.example.samto.architectureexample.EXTRA_PRIORITY";
    private static final String EXTRA_ID =
            "com.example.samto.architectureexample.EXTRA_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST );
            }
        });
        // Set up recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        final NoteAdapter adapter = new NoteAdapter();
        adapter.setonItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                if (note != null) {
                    String title = note.getTitle();
                    String description = note.getDescription();
                    int priority = note.getPriority();
                    int id = note.getId();

                    intent.putExtra(EXTRA_TITLE, title);
                    intent.putExtra(EXTRA_DESCRIPTOR, description);
                    intent.putExtra(EXTRA_PRIORITY, priority);
                    intent.putExtra(EXTRA_ID, id);

                    startActivityForResult(intent, EDIT_NOTE_REQUEST );
                }
                else {
                    Toast.makeText(MainActivity.this, "Unable to find note",
                            Toast.LENGTH_SHORT);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        // Set up ViewModel
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        // Set up an observer for it
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(@Nullable List<Note> notes) {
                        adapter.setNotes(notes);
                    }
                }
        );

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                noteViewModel.delete(adapter.getNoteAt(position));
                CharSequence charSequence = "Note Deleted";
                Toast.makeText(getBaseContext(), charSequence, Toast.LENGTH_SHORT).show();

            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(EXTRA_TITLE);
            String description = data.getStringExtra(EXTRA_DESCRIPTOR);
            int priority = data.getIntExtra(EXTRA_PRIORITY, 1);
            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT);
        }

        else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(EXTRA_ID, -1);

            if (id != -1) {
                String title = data.getStringExtra(EXTRA_TITLE);
                String description = data.getStringExtra(EXTRA_DESCRIPTOR);
                int priority = data.getIntExtra(EXTRA_PRIORITY, 1);
                Note note = new Note(title, description, priority);
                note.setId(id);
                noteViewModel.update(note);
                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT);

            } else {
                 Toast.makeText(this, "Note not updated", Toast.LENGTH_SHORT);
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId()) {
            case R.id.delete_notes: {
                noteViewModel.deleteAllNotes();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
            }
        }
    }
