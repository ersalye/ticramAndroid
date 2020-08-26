package com.turnpoint.ticram.tekram_driver.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.modules.NotesModel;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>{
    ArrayList<NotesModel> notesModels;

    public NotesAdapter(ArrayList<NotesModel> notesModels) {
        this.notesModels = notesModels;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_view,parent,false);
        NotesViewHolder notesViewHolder =  new NotesViewHolder(view);

        return notesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.notes_text.setText(notesModels.get(position).notes);

    }

    @Override
    public int getItemCount() {
        return notesModels.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder{
        TextView notes_text;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            notes_text = itemView.findViewById(R.id.notes_text);

        }
    }
}
