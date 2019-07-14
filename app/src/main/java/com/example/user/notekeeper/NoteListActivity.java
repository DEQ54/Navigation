package com.example.user.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {
    private NoteRecyclerAdapter MnoteRecyclerAdapter;

    // private ArrayAdapter<NoteInfo> MadapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteListActivity.this,NoteActivity.class));
            }
        });
        initializeDisplayContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  MadapterNotes.notifyDataSetChanged();
        MnoteRecyclerAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {
      // final ListView listNotes=(ListView) findViewById(R.id.list_notes);
      //  List<NoteInfo> notes=DataManager.getInstance().getNotes();
      //  MadapterNotes = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,notes);
     //   listNotes.setAdapter(MadapterNotes);
     //   listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      //      @Override
        //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       //           Intent intent=new Intent(NoteListActivity.this,NoteActivity.class);
    //              NoteInfo note=(NoteInfo) listNotes.getItemAtPosition(position);
     //             intent.putExtra(NoteActivity.NOTE_POSITION,position);
     //             startActivity(intent);
     //       }
     //   });
     final RecyclerView recyclerNotes=(RecyclerView) findViewById(R.id.list_notes);
     final LinearLayoutManager notesLayoutManager=new LinearLayoutManager(this);
     recyclerNotes.setLayoutManager(notesLayoutManager);
     List<NoteInfo> note=DataManager.getInstance().getNotes();
        MnoteRecyclerAdapter = new NoteRecyclerAdapter(this,note);
     recyclerNotes.setAdapter(MnoteRecyclerAdapter);


    }

}
