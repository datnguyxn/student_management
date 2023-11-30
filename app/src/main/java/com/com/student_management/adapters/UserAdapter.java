package com.com.student_management.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.com.student_management.BroadcastReceiver;

import com.com.student_management.R;
import com.com.student_management.constants.App;
import com.com.student_management.entities.User;
import com.com.student_management.fragments.UpdateUserBottomSheetFragment;
import com.com.student_management.fragments.ViewHistoryUserBottomSheetFragment;
import com.com.student_management.models.UserModel;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private static final String TAG = "UserAdapter";
    private Context context;
    private ArrayList<User> users;
    public UserModel userModel = new UserModel();
    private Bundle bundle = new Bundle();
    private AlertDialog alertDialog;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, android.content.Intent intent) {
            String action = intent.getAction();
            if (action.equals(App.ACTION_DELETE_USER)) {
                Log.d(TAG, "onReceive: " + App.ACTION_DELETE_USER);
            } else if (action.equals(App.ACTION_LOCK_USER)) {
                Log.d(TAG, "onReceive: " + App.ACTION_LOCK_USER);
            }
        }
    };

    public UserAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    public UserAdapter(Context context) {
        this.context = context;
        this.users = new ArrayList<>();
    }

    public void setData(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void clearData() {
        this.users.clear();
        notifyDataSetChanged();
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
                        if (item.getItemId() == R.id.update_user) {
                            bundle.putString("uuid", user.getUuid());
                            UpdateUserBottomSheetFragment bottomSheetDialogFragment = new UpdateUserBottomSheetFragment();
                            bottomSheetDialogFragment.setArguments(bundle);
                            bottomSheetDialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                            return true;
                        } else if (item.getItemId() == R.id.lock_user) {
                            Log.d("menu", "onMenuItemClick: " + item.getItemId());
                            bundle.putString("uuid", user.getUuid());
                            alertDialog = new AlertDialog.Builder(context)
                                    .setTitle("Lock User Information")
                                    .setMessage("Are you sure?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //set what would happen when positive button is clicked
                                            Log.d(TAG, "onClick: Lock user");
                                            if (user.getStatus().equals("active")) {
                                                userModel.setLockUser(user.getUuid(), "locked", new UserModel.IsLockedCallBacks() {
                                                            @Override
                                                            public boolean onLocked() {
                                                                sendBroadcastToFragment(4);
                                                                return true;
                                                            }
                                                        }
                                                );
                                            } else if (user.getStatus().equals("locked")) {
                                                userModel.setLockUser(user.getUuid(), "active", new UserModel.IsLockedCallBacks() {
                                                            @Override
                                                            public boolean onLocked() {
                                                                sendBroadcastToFragment(4);
                                                                return true;
                                                            }
                                                        }
                                                );
                                            }
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //set what should happen when negative button is clicked
                                            Log.d(TAG, "onClick: cancel delete user");
                                            alertDialog.dismiss();
                                        }
                                    })
                                    .show();
                            return true;
                        } else if (item.getItemId() == R.id.history_user) {
                            Log.d("menu", "onMenuItemClick: " + item.getItemId());
                            bundle.putString("uuid", user.getUuid());
                            ViewHistoryUserBottomSheetFragment bottomSheetDialogFragment1 = new ViewHistoryUserBottomSheetFragment();
                            bottomSheetDialogFragment1.setArguments(bundle);
                            bottomSheetDialogFragment1.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheetDialogFragment1.getTag());
                            return true;
                        } else if (item.getItemId() == R.id.delete_user) {
                            Log.d("menu", "onMenuItemClick: " + item.getItemId());
                            bundle.putString("uuid", user.getUuid());
                            alertDialog = new AlertDialog.Builder(context)
                                    .setTitle("Delete User Information")
                                    .setMessage("Are you sure?")
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //set what would happen when positive button is clicked
                                            Log.d(TAG, "onClick: delete user");
                                            userModel.deleteUser(user.getUuid(), new UserModel.IsDeletedCallBacks() {
                                                @Override
                                                public boolean onDeleted() {
                                                    users.remove(position);
                                                    notifyDataSetChanged();
                                                    return true;
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //set what should happen when negative button is clicked
                                            Log.d(TAG, "onClick: cancel delete user");
                                        }
                                    })
                                    .show();
                            return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
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

    private void sendBroadcastToFragment(int action) {
        Intent intent = new Intent();
        if (action == 3) {
            intent.setAction(App.ACTION_DELETE_USER);
        } else if (action == 4) {
            intent.setAction(App.ACTION_LOCK_USER);
        }

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}
