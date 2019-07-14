package com.example.user.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    public static final String NOTE_POSITION ="com.example.user.notekeeper.NOTE_POSITION";
    public static final String ORIGINAL_NOTE_COURSE_ID ="com.example.user.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE="com.example.user.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT ="com.example.user.notekeeper.ORIGINAL_NOTE_TEXT";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo Mnote;
    private boolean isNewNote;
    private Spinner MspinnerCourses;
    private EditText MtextNoteTitle;
    private EditText MtextNoteText;
    private int MnotePosition;
    private boolean MisCancelling;
    private String MoriginalNoteCourseId;
    private String MoriginalNoteTitle;
    private String MoriginalNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MspinnerCourses = (Spinner) findViewById(R.id.spinner_courses);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MspinnerCourses.setAdapter(adapterCourses);
        readDisplayStateValues();
        if (savedInstanceState==null) {
            saveOriginalNoteValues();
        }else {
            restoreOriginalNoteValues(savedInstanceState);
        }

        MtextNoteTitle = (EditText)findViewById(R.id.text_note_title);
        MtextNoteText = (EditText)findViewById(R.id.text_note_text);

        if(!isNewNote)
        displayNote(MspinnerCourses, MtextNoteTitle, MtextNoteText);
    }

    private void restoreOriginalNoteValues(Bundle savedInstanceState) {
        MoriginalNoteCourseId=savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
        MoriginalNoteTitle=savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
        MoriginalNoteText=savedInstanceState.getString(ORIGINAL_NOTE_TEXT);
    }

    private void saveOriginalNoteValues() {
        if(isNewNote)
            return;
        MoriginalNoteCourseId = Mnote.getCourse().getCourseId();
        MoriginalNoteTitle = Mnote.getTitle();
        MoriginalNoteText = Mnote.getText();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(MisCancelling){
            if (isNewNote) {
                DataManager.getInstance().removeNote(MnotePosition);
            }else {
                storePreviousNoteValues();
            }
        }  else {
            saveNote();
        }
    }

    private void storePreviousNoteValues() {
        CourseInfo course=DataManager.getInstance().getCourse(MoriginalNoteCourseId);
        Mnote.setCourse(course);
        Mnote.setTitle(MoriginalNoteTitle);
        Mnote.setText(MoriginalNoteText);
    }

    @Override
    protected void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORIGINAL_NOTE_COURSE_ID,MoriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE,MoriginalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT,MoriginalNoteText);
    }

    private void saveNote() {
        Mnote.setCourse((CourseInfo)MspinnerCourses.getSelectedItem());
        Mnote.setTitle(MtextNoteTitle.getText().toString());
        Mnote.setText(MtextNoteText.getText().toString());

    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        List<CourseInfo>courses=DataManager.getInstance().getCourses();
        int courseIndex= courses.indexOf(Mnote.getCourse());
        spinnerCourses.setSelection(courseIndex);
        textNoteTitle.setText(Mnote.getTitle());
        textNoteText.setText(Mnote.getText());
    }

    private void readDisplayStateValues() {
        Intent intent=getIntent();
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        isNewNote = position==POSITION_NOT_SET;
        if(isNewNote){
          createNewNote();
        }
        else {
            Mnote = DataManager.getInstance().getNotes().get(position);
        }
    }

    private void createNewNote() {
        DataManager dm=DataManager.getInstance();
        MnotePosition = dm.createNewNote();
        Mnote=dm.getNotes().get(MnotePosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        } else if (id==R.id.action_cancel){
            MisCancelling = true;
            finish();
        } else if (id==R.id.action_next){
            moveNext();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item=menu.findItem(R.id.action_next);
        int lastNoteIndex=DataManager.getInstance().getNotes().size()-1;
        item.setEnabled(MnotePosition<lastNoteIndex);
        return super.onPrepareOptionsMenu(menu);
    }

    private void moveNext() {
        saveNote();
        ++MnotePosition;
        Mnote=DataManager.getInstance().getNotes().get(MnotePosition);

        saveOriginalNoteValues();
        displayNote(MspinnerCourses,MtextNoteTitle,MtextNoteText);
        invalidateOptionsMenu();
    }

    private void sendEmail() {
        CourseInfo course=(CourseInfo)MspinnerCourses.getSelectedItem();
        String subject=MtextNoteTitle.getText().toString();
        String text="Check out what I learned in the Pluralsight course\""+
                course.getTitle() +"\"\n"+ MtextNoteText.getText().toString();
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(intent);
    }
}
