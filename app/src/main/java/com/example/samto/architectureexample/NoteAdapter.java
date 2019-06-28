package com.example.samto.architectureexample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<Note> notes = new ArrayList<>();
    private OnItemClickListener listener;


    public class NoteHolder extends RecyclerView.ViewHolder {
        public TextView tvPriority;
        public TextView tvTitle;
        public TextView tvDescription;

        public NoteHolder(View itemView) {
            super(itemView);
            tvPriority = itemView.findViewById(R.id.tv_priority);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int position = getAdapterPosition();
                   if ( listener != null && position != RecyclerView.NO_POSITION) {
                       listener.onItemClick(notes.get(position));
                   }
                }
            });
        }
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged(); // Tells adapter that the dataset has changed
    }
    @NonNull
    @Override // Called when a view is generated
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,
        parent, false);
        return new NoteHolder(itemView);
    }

    @Override // Called when view is changed/updated
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.tvTitle.setText(currentNote.getTitle());
        holder.tvDescription.setText(currentNote.getDescription());
        holder.tvPriority.setText(String.valueOf(currentNote.getPriority()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public Note getNoteAt(int position) {
        return notes.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setonItemClickListener(OnItemClickListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }
    }

