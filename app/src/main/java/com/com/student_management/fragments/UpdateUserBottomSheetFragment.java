package com.com.student_management.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.com.student_management.BroadcastReceiver;
import com.com.student_management.R;
import com.com.student_management.constants.App;
import com.com.student_management.entities.User;
import com.com.student_management.models.UserModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateUserBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String TAG = "UpdateUserBottomSheetFragment";
    private Context context;
    private TextInputEditText etIdUser, etName, etEmail, edAge, etPhone, etRole, etActive;
    private MaterialButton btnUpdateUser;
    private UserModel userModel;
    private String uuid;
    private Map<String, Object> users = new HashMap<>();
    private String name, email, age, phone, role, active;
    private User user;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            String action = intent.getAction();
            if (action.equals(App.ACTION_UPDATE_USER)) {
                Log.d(TAG, "onReceive: " + App.ACTION_UPDATE_USER);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter(App.ACTION_UPDATE_USER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    public UpdateUserBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_user_bottom_sheet, container, false);
        init(view);
        Bundle mArgs = getArguments();
        if (mArgs != null) {
            uuid = mArgs.getString("uuid");
            setValueForUser(uuid);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This code runs after the data is filled successfully
                // Retrieve text from the EditText
                name = etName.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                age = edAge.getText().toString().trim();
                phone = etPhone.getText().toString().trim();
                role = etRole.getText().toString().trim();
                active = etActive.getText().toString().trim();
            }
        }, 1000);

        btnUpdateUser.setOnClickListener(v -> {
            name = etName.getText().toString().trim();
            email = etEmail.getText().toString().trim();
            age = edAge.getText().toString().trim();
            phone = etPhone.getText().toString().trim();
            role = etRole.getText().toString().trim();
            active = etActive.getText().toString().trim();
            user = new User(name, email, Long.parseLong(age), phone, role, active);
            users.putAll(user.updateUserToMap());
            userModel.updateUser(uuid, users, new UserModel.OnUserUpdatedListener() {

                @Override
                public void onCompleted() {
                    Log.d(TAG, "onCompleted: ");
                    sendBroadcastToFragment(2);
                    dismiss();
                }

                @Override
                public void onFailure() {
                    Log.d(TAG, "onFailure: ");
                }
            });
        });
        return view;
    }

    private void init(View view) {
        etIdUser = view.findViewById(R.id.edtIdUser);
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        edAge = view.findViewById(R.id.etAge);
        etPhone = view.findViewById(R.id.etPhone);
        etRole = view.findViewById(R.id.etRole);
        etActive = view.findViewById(R.id.etActive);
        btnUpdateUser = view.findViewById(R.id.btnUpdateUser);
        userModel = new UserModel();
    }

    private void setValueForUser(String uuid) {
        userModel.getUser(uuid, new UserModel.UserCallBacks() {
            @Override
            public void onCallback(User user) {
                etIdUser.setText(user.getUuid());
                etName.setText(user.getName());
                etEmail.setText(user.getEmail());
                edAge.setText(String.valueOf(user.getAge()));
                etPhone.setText(user.getPhone());
                etRole.setText(user.getRole());
                etActive.setText(user.getStatus());
            }
        });
    }

    private void sendBroadcastToFragment(int action) {
        Intent intent = new Intent();
        intent.setAction(App.ACTION_UPDATE_USER);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}