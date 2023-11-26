package com.com.student_management.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import androidx.appcompat.widget.SearchView;

import com.com.student_management.BroadcastReceiver;
import com.com.student_management.adapters.SearchStudentAdapter;
import com.com.student_management.constants.App;
import com.com.student_management.R;
import com.com.student_management.adapters.StudentAdapter;
import com.com.student_management.entities.Student;
import com.com.student_management.models.StudentModel;
import com.com.student_management.utils.StringUtil;

import java.util.ArrayList;

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
    private SearchStudentAdapter searchStudentAdapter;
    private ScrollView scrollView;
    private ConstraintLayout constraintLayout;
    private RecyclerView rvListStudent;
    private StudentAdapter studentAdapter;
    private SearchView searchView;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: " + action);
            if (action != null) {
                switch (action) {
                    case App.ACTION_UPDATE_STUDENT:
                    case App.ACTION_IMPORT_STUDENT_FROM_CSV:
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
        intentFilter.addAction(App.ACTION_IMPORT_STUDENT_FROM_CSV);
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
        searchStudentAdapter = new SearchStudentAdapter(getContext());
        getAllStudent();
        searchView.setVisibility(View.GONE);
        ivSearch.setOnClickListener(v -> {
            ivSearch.setVisibility(View.GONE);
            ivBack.setVisibility(View.GONE);
            ivSort.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);
            rvListStudent.setAdapter(searchStudentAdapter);
        });
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG, "onQueryTextSubmit: " + query);
                searchStudentAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(TAG, "onQueryTextSubmit: " + newText);
                searchStudentAdapter.getFilter().filter(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ivSearch.setVisibility(View.VISIBLE);
                ivBack.setVisibility(View.VISIBLE);
                ivSort.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
                rvListStudent.setAdapter(studentAdapter);
                studentAdapter.clearData();
                getAllStudent();
                return false;
            }
        });
        ivSort.setOnClickListener(v -> {
            Log.d(TAG, "onCreateView: sort");
            PopupMenu popup = new PopupMenu(getContext(), ivSort);
            //inflating menu from xml resource
            popup.inflate(R.menu.menu_sort);
            popup.setOnMenuItemClickListener(s -> {
                Log.d(TAG, "onMenuItemClick: " + s.getItemId());
                switch (s.getItemId()) {
                    case App.ACTION_SORT_A_TO_Z:
                        studentAdapter.sortStudentAZ();
                        return true;
                        case App.ACTION_SORT_Z_TO_A:
                        studentAdapter.sortStudentZA();
                    default:
                        return false;
                }
            });
            popup.show();
        });
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, getIntentFilter());
        return view;
    }

    private void init(View view) {
        ivBack = view.findViewById(R.id.iv_back);
        ivSort = view.findViewById(R.id.iv_sort);
        ivSearch = view.findViewById(R.id.searchStudent);
        searchView = view.findViewById(R.id.searchStudentView);
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
                    studentAdapter.setStudents(students);
                    searchStudentAdapter.setStudents(students);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}