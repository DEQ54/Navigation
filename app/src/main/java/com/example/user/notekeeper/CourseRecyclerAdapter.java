package com.example.user.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder>{
    private final Context mContext;
    private final List<CourseInfo> mCourses;
    private final LayoutInflater MlayoutInflater;

    public CourseRecyclerAdapter(Context mContext, List<CourseInfo> mCourses) {
        this.mContext = mContext;
        MlayoutInflater = LayoutInflater.from(mContext);
        this.mCourses = mCourses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView=MlayoutInflater.inflate(R.layout.item_course_list,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CourseInfo course= mCourses.get(i);
        viewHolder.MtextCourse.setText(course.getTitle());
        viewHolder.mCurrentPosition=i;

    }

    @Override
    public int getItemCount() {

        return mCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public final TextView MtextCourse;
        public int mCurrentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            MtextCourse = (TextView)itemView.findViewById(R.id.text_course);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v,mCourses.get(mCurrentPosition).getTitle(),Snackbar.LENGTH_LONG).show();

                }
            });
        }
    }
}
