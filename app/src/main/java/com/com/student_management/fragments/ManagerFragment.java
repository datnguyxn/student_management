package com.com.student_management.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.com.student_management.R;
import com.com.student_management.adapters.UserAdapter;
import com.com.student_management.entities.User;
import com.com.student_management.models.UserModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManagerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG = "ManagerFragment";
    private RecyclerView rvUser;
    private MaterialButton btnAddUser;
    private final UserModel userModel = new UserModel();
    private Context context = getContext();

    public ManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManagerFragment newInstance(String param1, String param2) {
        ManagerFragment fragment = new ManagerFragment();
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
        View view = inflater.inflate(R.layout.fragment_manager, container, false);
        init(view);
        setAllUser();
        return view;
    }

    private void init(View view) {
        rvUser = view.findViewById(R.id.rvUser);
        btnAddUser = view.findViewById(R.id.btnAddUser);
    }

    private void setAllUser() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvUser.setLayoutManager(layoutManager);
        userModel.getAllUsers(new UserModel.OnUserRetrievedListener() {
            @Override
            public void onCompleted(ArrayList<User> users) {
                Log.d(TAG, "onCompleted: " + users.toString());
                UserAdapter userAdapter = new UserAdapter(getContext(), users);
                rvUser.setAdapter(userAdapter);
            }
        });
    }


}