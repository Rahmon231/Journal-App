package com.lemzeeyyy.journalapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lemzeeyyy.journalapp.model.Journal;

import java.util.List;

public class JournalListAdapter extends RecyclerView.Adapter<JournalListAdapter.JournalViewHolder> {
    private Context context;
    private List<Journal> journalList;

    public JournalListAdapter(Context context, List<Journal> journalList) {
        this.context = context;
        this.journalList = journalList;
    }

    @NonNull
    @Override
    public JournalListAdapter.JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull JournalListAdapter.JournalViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class JournalViewHolder extends RecyclerView.ViewHolder {
        private TextView title, thoughts, timeAdded,name;
        private ImageView image;
        public ImageView shareBtn;
        String userId;
        String userName;
        public JournalViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

        }
    }
}
