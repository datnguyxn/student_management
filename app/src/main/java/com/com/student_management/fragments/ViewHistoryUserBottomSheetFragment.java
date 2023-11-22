package com.com.student_management.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.com.student_management.R;
import com.com.student_management.adapters.HistoryUserAdapter;
import com.com.student_management.entities.User;
import com.com.student_management.models.UserModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ViewHistoryUserBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String TAG = "ViewHistoryUserBottomSheetFragment";
    private TextView tvNameUser;
    private RecyclerView rvHistoryUser;
    private String uuid;
    private HistoryUserAdapter historyUserAdapter;
    private UserModel userModel;

    public ViewHistoryUserBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_history_user_bottom_sheet, container, false);
        init(view);
        Bundle mArgs = getArguments();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvHistoryUser.setLayoutManager(layoutManager);
        historyUserAdapter = new HistoryUserAdapter(getContext());
        rvHistoryUser.setAdapter(historyUserAdapter);
        if (mArgs != null) {
            uuid = mArgs.getString("uuid");
            Log.d(TAG, "onCreateView: " + uuid);
            getUserByUuid(uuid);
        }
        return view;
    }

    private void init(View view) {
        tvNameUser = view.findViewById(R.id.tvNameUser);
        rvHistoryUser = view.findViewById(R.id.rvHistoryUser);
        userModel = new UserModel();
    }

    private void getUserByUuid(String uuid) {
        userModel.getUser(uuid, new UserModel.UserCallBacks() {
            @Override
            public void onCallback(User user) {
                Log.d(TAG, "onCallback: " + user.toString());
                tvNameUser.setText(user.getName());
                historyUserAdapter.setHistories(user.getHistory());
            }
        });
    }
}