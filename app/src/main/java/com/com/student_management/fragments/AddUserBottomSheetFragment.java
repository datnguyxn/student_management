package com.com.student_management.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.com.student_management.BroadcastReceiver;
import com.com.student_management.R;
import com.com.student_management.constants.App;
import com.com.student_management.constants.Roles;
import com.com.student_management.entities.User;
import com.com.student_management.models.UserModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;


public class AddUserBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String TAG = "AddUserBottomSheetFragment";
    private Context context;
    private TextInputEditText etName, etEmail, edAge, etPhone, etRole;
    private MaterialButton btnAddUser;
    private UserModel userModel = new UserModel();
    private FirebaseAuth mAuth;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(App.ACTION_ADD_USER)) {
                Log.d(TAG, "onReceive: " + App.ACTION_ADD_USER);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter(App.ACTION_ADD_USER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_user_bottom_sheet, container, false);
        Log.d(TAG, "onCreateView: ManagerFragment");
        init(view);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: create User");
                createUser();
                //close bottom sheet
                dismiss();
                //clear data
                etName.setText("");
                etEmail.setText("");
                edAge.setText("");
                etPhone.setText("");
                etRole.setText("");
            }
        });
        return view;
    }

    private void init(View view) {
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        edAge = view.findViewById(R.id.etAge);
        etPhone = view.findViewById(R.id.etPhone);
        etRole = view.findViewById(R.id.etRole);
        btnAddUser = view.findViewById(R.id.btnAddUser);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void createUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String age = edAge.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String role = etRole.getText().toString().trim().toUpperCase();
        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            edAge.setError("Age is required");
            edAge.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            etPhone.setError("Phone is required");
            etPhone.requestFocus();
            return;
        }
        if (phone.length() != 10) {
            etPhone.setError("Phone must be 10 digits");
            etPhone.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please provide valid email");
            etEmail.requestFocus();
            return;
        }
        if (role.isEmpty()) {
            etRole.setText(Roles.valueOf("MANAGER").toString());
            return;
        }

        userModel.isExistUser(email, new UserModel.CheckUserExistCallBacks() {
            @Override
            public void onExist() {
                Toast.makeText(context, "Email already exists", Toast.LENGTH_SHORT).show();
                dismiss();
            }

            @Override
            public void onNotFound() {
                userModel.create(email, name, phone, role, Long.parseLong(age), new UserModel.UserCallBacks() {
                    @Override
                    public void onCallback(User user) {
                        sendBroadcastToFragment(1);
                    }
                });
            }
        });

    }

    private void sendBroadcastToFragment(int action) {
        Intent intent = new Intent();
        intent.setAction(App.ACTION_ADD_USER);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}