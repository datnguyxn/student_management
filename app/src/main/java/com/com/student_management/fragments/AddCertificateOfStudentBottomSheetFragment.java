package com.com.student_management.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.com.student_management.BroadcastReceiver;
import com.com.student_management.R;
import com.com.student_management.constants.App;
import com.com.student_management.entities.Certificate;
import com.com.student_management.models.CertificateModel;
import com.com.student_management.models.StudentModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AddCertificateOfStudentBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String TAG = "AddCertificateOfStudentBottomSheetFragment";
    private TextInputEditText etNameCertificateOfStudent;
    private Context context;
    private MaterialButton btnAddCertificateOfStudent;
    private CertificateModel certificateModel;
    private StudentModel studentModel;
    private String studentId;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(App.ACTION_ADD_CERTIFICATE_FOR_STUDENT)) {
                Log.d(TAG, "onReceive: " + App.ACTION_ADD_CERTIFICATE_FOR_STUDENT);
            }
        }
    };
    public AddCertificateOfStudentBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter(App.ACTION_ADD_CERTIFICATE_FOR_STUDENT));
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
        View view =  inflater.inflate(R.layout.fragment_add_certificate_of_student_bottom_sheet, container, false);
        init(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            studentId = bundle.getString("studentId");
        }
        btnAddCertificateOfStudent.setOnClickListener(v -> {
            String nameCertificateOfStudent = etNameCertificateOfStudent.getText().toString().trim().toUpperCase();
            if (nameCertificateOfStudent.isEmpty()) {
                etNameCertificateOfStudent.setError("Please enter name certificate of student");
                etNameCertificateOfStudent.requestFocus();
                return;
            }
            addCertificateForStudent(nameCertificateOfStudent);
        });
        return view;
    }

    private void init(View view) {
        etNameCertificateOfStudent = view.findViewById(R.id.etNameCertificateOfStudent);
        btnAddCertificateOfStudent = view.findViewById(R.id.btnAddCertificateOfStudent);
        certificateModel = new CertificateModel();
        studentModel = new StudentModel();
    }

    private void addCertificateForStudent(String name) {
        certificateModel.isExistCertificate(name, new CertificateModel.IsExistCertificateListener() {
            @Override
            public void onExist(Certificate certificate) {
                Log.d(TAG, "onExist: " + certificate.toString());
                studentModel.updateCertificate(studentId, certificate.getId(), new StudentModel.OnUpdateCertificateListener() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                        Toast.makeText(getContext(), "Add certificate for student successfully", Toast.LENGTH_SHORT).show();
                        sendBroadcastToFragment(7);
                        dismiss();
                    }

                    @Override
                    public void onFailure() {
                        Log.d(TAG, "onFailure: ");
                        Toast.makeText(getContext(), "Add certificate for student failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNotExist() {
                Log.d(TAG, "onNotExist: ");
                Toast.makeText(getContext(), "Certificate is not exist", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendBroadcastToFragment(int action) {
        Intent intent = new Intent();
        intent.setAction(App.ACTION_ADD_CERTIFICATE_FOR_STUDENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}