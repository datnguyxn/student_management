package com.com.student_management.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.com.student_management.MainActivity;
import com.com.student_management.R;
import com.com.student_management.entities.Student;
import com.com.student_management.models.StudentModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewStudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewStudentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView imvBack;
    private TextInputEditText edtId, edtFullName, edtBirthday, edtPhoneNumber, edtAddAddress, edtMajor, edtCertificate;
    private MaterialButton btnAddStudent;
    private CheckBox checkBoxMale, checkBoxFemale;
    private static final String TAG = "NewStudentFragment";
    private StudentModel studentModel;

    public NewStudentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewStudentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewStudentFragment newInstance(String param1, String param2) {
        NewStudentFragment fragment = new NewStudentFragment();
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
        View newStudentView = inflater.inflate(R.layout.fragment_new_student, container, false);
        init(newStudentView);
        imvBack.setOnClickListener(v -> {
            replaceFragment(new HomeFragment());
        });
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + view.toString());
                addNewStudent();
            }
        });
        return newStudentView;
    }

    private void init(View newStudentView) {
        imvBack = newStudentView.findViewById(R.id.iv_back);
        edtId = newStudentView.findViewById(R.id.edtId);
        edtFullName = newStudentView.findViewById(R.id.edtFullName);
        edtBirthday = newStudentView.findViewById(R.id.edtBirthday);
        edtPhoneNumber = newStudentView.findViewById(R.id.edtPhoneNumber);
        edtAddAddress = newStudentView.findViewById(R.id.edtAddress);
        edtMajor = newStudentView.findViewById(R.id.edtMajor);
        edtCertificate = newStudentView.findViewById(R.id.edtCertificate);
        btnAddStudent = newStudentView.findViewById(R.id.btnAddNewStudent);
        checkBoxMale = newStudentView.findViewById(R.id.checkBoxMale);
        checkBoxFemale = newStudentView.findViewById(R.id.checkBoxFemale);
        studentModel = new StudentModel();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void addNewStudent() {
        String id = edtId.getText().toString().trim();
        String fullName = edtFullName.getText().toString().trim();
        String birthday = edtBirthday.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String address = edtAddAddress.getText().toString().trim();
        String major = edtMajor.getText().toString().trim();
        String certificate = edtCertificate.getText().toString().trim();
        // true is male, false is female
        boolean isGender = false;
        if (checkBoxMale.isChecked()) {
            isGender = true;
        }
        if (id.isEmpty() || fullName.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            Log.d(TAG, "addNewStudent: empty");
            Toast.makeText(getActivity(), "Please fill field is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean finalIsGender = isGender;
        studentModel.isExistStudent(id, new StudentModel.CheckStudentExistCallbacks() {
            @Override
            public void onExist(Student student) {
                Log.d(TAG, "onExist: " + student.toString());
                Toast.makeText(getActivity(), "Student is exist", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNotExist() {
                Log.d(TAG, "onNotExist: ");
                Log.d(TAG, "onNotExist: " + certificate);
                studentModel.create(id, fullName, finalIsGender, phoneNumber, birthday, address, major, certificate, new StudentModel.OnCreateStudentListener() {

                    @Override
                    public void onComplete(Student student) {
                        Log.d(TAG, "onComplete: " + student.toString());
                        Toast.makeText(getActivity(), "Add new student success", Toast.LENGTH_SHORT).show();
                        replaceFragment(new HomeFragment());
                    }

                    @Override
                    public void onFailure() {
                        Log.d(TAG, "onFailure: ");
                        Toast.makeText(getActivity(), "Add new student failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}