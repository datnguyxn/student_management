package com.com.student_management.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.com.student_management.BroadcastReceiver;
import com.com.student_management.constants.App;
import com.com.student_management.MainActivity;
import com.com.student_management.R;
import com.com.student_management.adapters.StudentAdapter;
import com.com.student_management.entities.Student;
import com.com.student_management.models.StudentModel;
import com.com.student_management.utils.HandID;
import com.com.student_management.utils.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListStudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListStudentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private StudentModel studentModel;
    private static final String TAG = "ListStudentFragment";
    private ImageView ivBack, ivSort, ivSearch;
    private ScrollView scrollView;
    private ConstraintLayout constraintLayout;
    private RecyclerView rvListStudent;
    private StudentAdapter studentAdapter;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: " + action);
            if (action != null) {
                switch (action) {
                    case App.ACTION_UPDATE_STUDENT:
                        if (studentAdapter != null) {
                            studentAdapter.clearData();
                            getAllStudent();
                        }
                }
            }

        }
    };

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(App.ACTION_UPDATE_STUDENT);
        return intentFilter;
    }

    public ListStudentFragment() {
        // Required empty public constructor
    }

    public static ListStudentFragment newInstance(String param1, String param2) {
        ListStudentFragment fragment = new ListStudentFragment();
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
        View view = inflater.inflate(R.layout.fragment_list_student, container, false);
        scrollView = new ScrollView(getContext());
        constraintLayout = new ConstraintLayout(getContext());
        scrollView.addView(constraintLayout);

        init(view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvListStudent.setLayoutManager(layoutManager);
        studentAdapter = new StudentAdapter(getContext());
        rvListStudent.setAdapter(studentAdapter);
        getAllStudent();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, getIntentFilter());
        return view;
    }

    private void init(View view) {
        ivBack = view.findViewById(R.id.iv_back);
        ivSort = view.findViewById(R.id.iv_sort);
        ivSearch = view.findViewById(R.id.iv_search);
        rvListStudent = view.findViewById(R.id.rvListStudent);
        studentModel = new StudentModel();
        ivBack.setOnClickListener(v -> {
            replaceFragment(new HomeFragment());
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void getAllStudent() {
        studentModel.getAllStudents(new StudentModel.OnGetAllStudentsListener() {
            @Override
            public void onCompleted(ArrayList<Student> students) {
                try {
                    Log.d(TAG, "onCompleted: " + students.toString());
                    ArrayList<String> majors = new ArrayList<>();
                    for (Student student : students) {
                        student.setId(HandID.decrypt(student.getId()));
                        majors.add(student.getMajor());
//                    StringBuilder[] stringBuilder = new StringBuilder[majors.size()];
//                    for (int i = 0; i < majors.size(); i++) {
//                        String[] majorWords = majors.get(i).split("\\s+");
//                        stringBuilder[i] = new StringBuilder();
//
//                        for (String major : majorWords) {
//                           if (!major.isEmpty()) {
//                                 stringBuilder[i].append(major.charAt(0));
//                           }
//                        }
//                        student.setMajor(stringBuilder[i].toString().toUpperCase());
                        for (int i = 0; i < majors.size(); i++) {
                            student.setMajor(StringUtil.convertString(majors)[i].toString().toUpperCase());
                        }
                    }
//                }
                    studentAdapter.setStudents(students);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}