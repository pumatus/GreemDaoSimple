package com.example.chenlijin.greemdaosimple.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chenlijin.greemdaosimple.R;
import com.example.chenlijin.greemdaosimple.dao.Note;

import java.util.List;

/**
 * Created by chenlijin on 2016/3/27.
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {
    private List<Note> noteList;
    private Context context;

    public NoteListAdapter(List<Note> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note,null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Note note = noteList.get(position);
        holder.textViewContent.setText(note.getText());
        holder.textViewAddTime.setText(note.getComment());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList==null?0:noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContent;
        TextView textViewAddTime;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewContent = (TextView) itemView.findViewById(R.id.textview_content);
            textViewAddTime = (TextView) itemView.findViewById(R.id.textview_add_time);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
