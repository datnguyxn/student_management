package com.com.student_management.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.com.student_management.BroadcastReceiver;
import com.com.student_management.R;
import com.com.student_management.constants.App;
import com.com.student_management.entities.Student;
import com.com.student_management.models.StudentModel;
import com.com.student_management.utils.FormatDateTime;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateStudentBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String TAG = "UpdateStudentBottomSheetFragment";
    private TextInputEditText etId, etName, edtBirthday, etEmail, etPhone, etMajor, etAddress;
    private Context context;
    private CheckBox checkBoxMale, checkBoxFemale;
    private MaterialButton btnUpdateStudent;
    private StudentModel studentModel;
    private String id;
    private Student student;
    private Map<String, Object> students = new HashMap<>();
    private String studentId, name, birthday, email, phone, major, address;
    private boolean isMale = true;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(App.ACTION_UPDATE_STUDENT)) {
                Log.d(TAG, "onReceive: " + App.ACTION_UPDATE_STUDENT);
            }
        }
    };
    public UpdateStudentBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter(App.ACTION_UPDATE_STUDENT));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_student_bottom_sheet, container, false);
        init(view);
        Bundle mArgs = getArguments();
        if (mArgs != null) {
            id = mArgs.getString("id");
            Log.d(TAG, "onCreateView: " + id);
            setValueForStudent(id);
        }
        btnUpdateStudent.setOnClickListener(v -> {
            studentId = etId.getText().toString().trim();
            name = etName.getText().toString().trim();
            birthday = edtBirthday.getText().toString().trim();
            email = etEmail.getText().toString().trim();
            phone = etPhone.getText().toString().trim();
            major = etMajor.getText().toString().trim();
            address = etAddress.getText().toString().trim();
            if (checkBoxMale.isChecked()) {
                isMale = true;
                checkBoxFemale.setChecked(false);
            } else {
                isMale = false;
                checkBoxMale.setChecked(false);
            }
            ArrayList<String> dateUpdated = new ArrayList<>();
            String dateUpdate = FormatDateTime.formatDateTime();
            dateUpdated.add(dateUpdate);
            student = new Student(studentId, name, birthday, email, phone, major, address, isMale, dateUpdated);
//            students.putAll(student.upd());
            studentModel.updateStudent(id, students, new StudentModel.OnStudentUpdatedLintener() {
                @Override
                public void onCompleted() {
                    Log.d(TAG, "onCompleted: ");
                    sendBroadcastToFragment(5);
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
        etId = view.findViewById(R.id.etId);
        etName = view.findViewById(R.id.etNameStudent);
        edtBirthday = view.findViewById(R.id.edtBirthday);
        etEmail = view.findViewById(R.id.etEmailStudent);
        etPhone = view.findViewById(R.id.etPhoneStudent);
        etMajor = view.findViewById(R.id.etMajor);
        etAddress = view.findViewById(R.id.edtAddress);
        checkBoxMale = view.findViewById(R.id.checkBoxMale);
        checkBoxFemale = view.findViewById(R.id.checkBoxFemale);
        btnUpdateStudent = view.findViewById(R.id.btnUpdateUser);
        studentModel = new StudentModel();
    }

    private void setValueForStudent(String id) {
        studentModel.isExistStudent(id, new StudentModel.CheckStudentExistCallbacks() {
            @Override
            public void onExist(Student student) {
                etId.setText(student.getId());
                etName.setText(student.getFullName());
                edtBirthday.setText(student.getBirthday());
                etEmail.setText(student.getEmail());
                etPhone.setText(student.getPhone());
                etMajor.setText(student.getMajor());
                etAddress.setText(student.getAddress());
               if (student.getGender()) {
                   checkBoxMale.setChecked(true);
               } else {
                   checkBoxFemale.setChecked(true);
               }
            }
            @Override
            public void onNotExist() {
                Log.d(TAG, "onNotExist: ");
            }
        });
    }
    private void sendBroadcastToFragment(int action) {
        Intent intent = new Intent();
        intent.setAction(App.ACTION_UPDATE_STUDENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}