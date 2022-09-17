package com.lemzeeyyy.journalapp.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lemzeeyyy.journalapp.R;
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
        View view = LayoutInflater.from(context).inflate(R.layout.journal_row,parent,false);
        return new JournalViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalListAdapter.JournalViewHolder holder, int position) {
        Journal journal = journalList.get(position);
        holder.title.setText(journal.getTitle());
        holder.thoughts.setText(journal.getThoughts());
        holder.name.setText(journal.getUserName());
        String imageUrl = journal.getImageUrl();
        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(
                journal.getTimeAdded()
                        .getSeconds()*1000
        );
        holder.timeAdded.setText(timeAgo);
        Glide.with(context)
                .load(imageUrl)
                .fitCenter()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return journalList.size();
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
            title = itemView.findViewById(R.id.journal_title_list);
            thoughts = itemView.findViewById(R.id.journal_thought_list);
            name = itemView.findViewById(R.id.journal_row_username);
            timeAdded = itemView.findViewById(R.id.journal_timestamp_list);
            image = itemView.findViewById(R.id.journal_image_list);
            shareBtn = itemView.findViewById(R.id.journal_share_btn);
            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }
}
