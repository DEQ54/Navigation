package com.example.user.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>{
    private final Context mContext;
    private final List<NoteInfo>mNotes;
    private final LayoutInflater MlayoutInflater;

    public NoteRecyclerAdapter(Context mContext, List<NoteInfo> mNotes) {
        this.mContext = mContext;
        MlayoutInflater = LayoutInflater.from(mContext);
        this.mNotes = mNotes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView=MlayoutInflater.inflate(R.layout.item_note_list,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        NoteInfo note=mNotes.get(i);
        viewHolder.MtextCourse.setText(note.getCourse().getTitle());
        viewHolder.MtextTitle.setText(note.getTitle());
        viewHolder.mCurrentPosition=i;

    }

    @Override
    public int getItemCount() {

        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public final TextView MtextCourse;
        public final TextView MtextTitle;
        public int mCurrentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            MtextCourse = (TextView)itemView.findViewById(R.id.text_course);
            MtextTitle = (TextView)itemView.findViewById(R.id.text_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_POSITION,mCurrentPosition);
                    mContext.startActivity(intent);

                }
            });
        }
    }
}
