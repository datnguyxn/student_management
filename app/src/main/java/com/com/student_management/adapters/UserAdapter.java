package com.com.student_management.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.com.student_management.R;
import com.com.student_management.constants.App;
import com.com.student_management.constants.Roles;
import com.com.student_management.entities.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private Context context;
    private ArrayList<User> users;
    public UserAdapter(Context context) {
        this.context = context;
    }

    public UserAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }
    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvName.setText(user.getName());
        holder.tvRole.setText(user.getRole());
        holder.tvStatus.setText(user.getStatus());
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.menu);
                //inflating menu from xml resource
                popup.inflate(R.menu.user_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Log.d("menu", "onMenuItemClick: " + item.getItemId());
                        switch (item.getItemId()) {
                            case App.UPDATE_USER:
                                //handle menu1 click

                                return true;
                            case App.DELETE_USER:
                                //handle menu2 click
                                Log.d("menu", "onMenuItemClick: " + item.getItemId());
                                return true;
                            case App.LOCK_USER:
                                //handle menu3 click
                                Log.d("menu", "onMenuItemClick: " + item.getItemId());
                                return true;
                            case App.HISTORY_USER:
                                Log.d("menu", "onMenuItemClick: " + item.getItemId());
                                return true;
                            default:
                                return false;
                        }
                    }

                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
       return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvRole, tvStatus;
        private ImageView menu;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvNameUser);
            tvRole = (TextView) itemView.findViewById(R.id.tvRole);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            menu = (ImageView) itemView.findViewById(R.id.menu);
        }
    }

}
