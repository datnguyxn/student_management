package com.com.student_management.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.com.student_management.R;
import com.com.student_management.constants.App;
import com.com.student_management.models.UserModel;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.com.student_management.entities.User;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "HomeFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int FILE_PICKER_REQUEST_CODE = 1108;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MaterialButton btnAddNewStudent, btnStudentList, btnNewCertificate, btnCertificateList, btnImportStudent, btnExportStudent, btnImportCertificate, btnExportCertificate;
    private TextView tvRole;
    private Activity activity;
    private UserModel userModel;
    private ScrollView scrollView;
    private ConstraintLayout constraintLayout;
    private String uuid;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);
        setRole();
        scrollView = new ScrollView(this.getContext());
        constraintLayout = new ConstraintLayout(this.getContext());
        scrollView.addView(constraintLayout);
        btnAddNewStudent.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: btnAddNewStudent clicked");
            replaceFragment(new NewStudentFragment());
        });
        btnStudentList.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: btnStudentList clicked");
            replaceFragment(new ListStudentFragment());
        });
        btnCertificateList.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: btnCertificateList clicked");
            replaceFragment(new ListCertificateFragment());
        });
        btnNewCertificate.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: btnNewCertificate clicked");
            replaceFragment(new NewCertificateFragment());
        });

        btnImportStudent.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: btnImportStudent clicked");
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/csv");
            startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
        });
        return view;
    }

    @Override
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
        tvRole = (TextView) view.findViewById(R.id.role);
        btnAddNewStudent = (MaterialButton) view.findViewById(R.id.btnAddNewStudent);
        btnStudentList = (MaterialButton) view.findViewById(R.id.btnStudentList);
        btnNewCertificate = (MaterialButton) view.findViewById(R.id.btnNewCertificate);
        btnCertificateList = (MaterialButton) view.findViewById(R.id.btnCertificateList);
        btnImportStudent = (MaterialButton) view.findViewById(R.id.btnImportStudent);
        btnExportStudent = (MaterialButton) view.findViewById(R.id.btnExportStudent);
        btnImportCertificate = (MaterialButton) view.findViewById(R.id.btnImportCertificate);
        btnExportCertificate = (MaterialButton) view.findViewById(R.id.btnExportCertificate);
        userModel = new UserModel();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(App.SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
        uuid = sharedPreferences.getString(App.SHARED_PREFERENCES_UUID, null);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void setRole() {
        userModel.getRole(uuid, new UserModel.UserCallBacks() {
            @Override
            public void onCallback(User user) {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(App.SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("role", user.getRole());
                editor.apply();

                tvRole.setText("For " + user.getRole());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE) {
            // Get the selected file URI
            Uri uri = data.getData();
            Log.d(TAG, "onActivityResult: " + uri.toString());

            // Now you can read the CSV file using the URI
            readCSVFile(uri);
        }
    }

    private void readCSVFile(Uri uri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Process each line of the CSV file
                String[] values = line.split(",");
                // Do something with the values
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}