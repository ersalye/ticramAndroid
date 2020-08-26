package com.turnpoint.ticram.tekram_driver.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.turnpoint.ticram.tekram_driver.Adapters.NotesAdapter;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.modules.NotesModel;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {
    RecyclerView note_recycler_view;
    ArrayList<NotesModel> notesModels;
    NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        note_recycler_view = findViewById(R.id.note_recycler_view);
        notesModels = new ArrayList<>();
        fillArrayModel();
        notesAdapter = new NotesAdapter(notesModels);
        note_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        note_recycler_view.setAdapter(notesAdapter);


    }

    private void fillArrayModel() {
        notesModels.add(new NotesModel("Hello"));
        notesModels.add(new NotesModel("hh"));
        notesModels.add(new NotesModel("ui"));

    }

    public void back(View view) {
        onBackPressed();
    }
}