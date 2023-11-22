package com.com.student_management.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.com.student_management.R;
import com.com.student_management.models.UserModel;

import java.util.ArrayList;

public class HistoryUserAdapter extends RecyclerView.Adapter<HistoryUserAdapter.HistoryUserViewHolder> {
    private static final String TAG = "HistoryUserAdapter";
    private Context context;
    private ArrayList<String> histories;

    public HistoryUserAdapter(Context context, ArrayList<String> histories) {
        this.context = context;
        this.histories = histories;
    }

    public HistoryUserAdapter(Context context) {
        this.context = context;
        this.histories = new ArrayList<>();
    }

    public void setHistories(ArrayList<String> histories) {
        this.histories = histories;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HistoryUserAdapter.HistoryUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_history_user, parent, false);
         return new HistoryUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryUserAdapter.HistoryUserViewHolder holder, int position) {
        String history = histories.get(position);
        holder.tvDate.setText(history);
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public static class HistoryUserViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        public HistoryUserViewHolder(@NonNull View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
        }
    }
}
