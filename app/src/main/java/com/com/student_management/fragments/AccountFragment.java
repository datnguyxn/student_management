package com.com.student_management.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.com.student_management.R;
import com.com.student_management.authentication.LoginActivity;
import com.com.student_management.constants.App;
import com.com.student_management.entities.User;
import com.com.student_management.models.UserModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements ChangeAvatarBottomSheetFragment.BottomSheetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG = "AccountFragment";
    private ShapeableImageView avatar;
    private FirebaseAuth mAuth;
    private MaterialButton btnLogout, btnChangeAvatar;
    private TextView tvName, tvRole;
    private UserModel userModel;
    private Activity activity;
    private String uuid;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_account, container, false);
        init(convertView);
        setAvatarAndName();
        setTvRole();
        ChangeAvatarBottomSheetFragment bottomSheetDialogFragment = new ChangeAvatarBottomSheetFragment();
        bottomSheetDialogFragment.setBottomSheetListener(this);
        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogFragment.show(getChildFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout();
            }
        });
        return convertView;
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    private void init(View view) {
        avatar = view.findViewById(R.id.imgAvatar);
        tvName = view.findViewById(R.id.tvName);
        tvRole = view.findViewById(R.id.tvRole);
        btnChangeAvatar = view.findViewById(R.id.btnChangeAvatar);
        btnLogout = view.findViewById(R.id.btnLogout);
        userModel = new UserModel();
        mAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(App.SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
        uuid = sharedPreferences.getString(App.SHARED_PREFERENCES_UUID, null);
    }

    private void setAvatarAndName() {
        userModel.getUser(uuid, new UserModel.UserCallBacks() {

            @Override
            public void onCallback(User user) {
                if (user != null) {
                    tvName.setText(user.getName());
                    Glide.with(activity).load(user.getAvatar()).into(avatar);
                }
            }
        });
    }

    private void handleLogout() {
        mAuth.signOut();
        SharedPreferences userRef = activity.getSharedPreferences(App.SHARED_PREFERENCES_USER, activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = userRef.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        activity.finish();
    }

    private void setTvRole() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(App.SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", null);
        if (role != null) {
            tvRole.setText(role);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setTvRole();
        setAvatarAndName();
    }

    @Override
    public void onDataReceived(String data) {
        Log.e(TAG, "onDataReceived: " + data);
        Glide.with(activity).load(data).into(avatar);
        userModel.updateAvatar(uuid, data);
    }
}