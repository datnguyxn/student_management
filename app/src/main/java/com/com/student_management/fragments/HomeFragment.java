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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.com.student_management.R;
import com.com.student_management.constants.App;
import com.com.student_management.entities.Certificate;
import com.com.student_management.entities.Student;
import com.com.student_management.middleware.RequireRole;
import com.com.student_management.models.CertificateModel;
import com.com.student_management.models.StudentModel;
import com.com.student_management.models.UserModel;
import com.com.student_management.utils.CSVFile;
import com.google.android.material.button.MaterialButton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

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
    private StudentModel studentModel;
    private CertificateModel certificateModel;
    private String uuid;
    private String role;
    private Uri fileUri;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);
        scrollView = new ScrollView(this.getContext());
        constraintLayout = new ConstraintLayout(this.getContext());
        scrollView.addView(constraintLayout);
        btnAddNewStudent.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: btnAddNewStudent clicked");
            if (RequireRole.checkRole(role, getContext())) {
                replaceFragment(new HomeFragment());
            } else {
                replaceFragment(new NewStudentFragment());
            }
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
            if (RequireRole.checkRole(role, getContext()) || RequireRole.checkManagerRole(role, getContext())) {
                replaceFragment(new HomeFragment());
            } else {
                replaceFragment(new NewCertificateFragment());
            }
        });

        btnImportStudent.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: btnImportStudent clicked");
            if (RequireRole.checkRole(role, getContext())) {
                replaceFragment(new HomeFragment());
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/csv");
                startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
            }
        });
        btnExportStudent.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: btnExportStudent clicked");
            if (RequireRole.checkRole(role, getContext())) {
                replaceFragment(new HomeFragment());
            } else {
                try {
                    getAllStudents();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnImportCertificate.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: btnImportCertificate clicked");
            if (RequireRole.checkRole(role, getContext()) || RequireRole.checkManagerRole(role, getContext())) {
                replaceFragment(new HomeFragment());
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/csv");
                startActivityForResult(intent, 1000);
            }
        });
        btnExportCertificate.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: btnExportCertificate clicked");
            if (RequireRole.checkRole(role, getContext()) || RequireRole.checkManagerRole(role, getContext())) {
                replaceFragment(new HomeFragment());
            } else {
                try {
                    getAllCertificates();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
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
        studentModel = new StudentModel();
        certificateModel = new CertificateModel();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(App.SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
        uuid = sharedPreferences.getString(App.SHARED_PREFERENCES_UUID, null);
        SharedPreferences sharedPreferences1 = activity.getSharedPreferences(App.SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
        role = sharedPreferences1.getString("role", null);
        tvRole.setText("For " + role);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE) {
            fileUri = data.getData();
            try {
                CSVFile csvFile = new CSVFile(getActivity().getContentResolver().openInputStream(fileUri));
                ArrayList<Student> studentArrayList = csvFile.readCSVForStudent();
                Log.d(TAG, "onActivityResult: " + studentArrayList.toString());
                setDataForStudent(studentArrayList);
                replaceFragment(new ListStudentFragment());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else if (requestCode == 1000) {
            fileUri = data.getData();
            try {
                CSVFile csvFile = new CSVFile(getActivity().getContentResolver().openInputStream(fileUri));
                ArrayList<Certificate> certificateArrayList = csvFile.readCSVForCertificate();
                Log.d(TAG, "onActivityResult: " + certificateArrayList.toString());
                setDataForCertificate(certificateArrayList);
                replaceFragment(new ListCertificateFragment());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setDataForCertificate(ArrayList<Certificate> certificateArrayList) {
        for (Certificate certificate : certificateArrayList) {
            certificateModel.create(certificate.getName(), certificate.getDescription(), new CertificateModel.OnCreateCertificateListener() {
                @Override
                public void onCreateCertificateSuccess(Certificate certificate) {
                    Log.d(TAG, "onCreateCertificateSuccess: " + certificate.toString());
                }

                @Override
                public void onCreateCertificateFailure() {
                    Log.d(TAG, "onCreateCertificateFailure: ");
                }
            });
        }
    }

    private void setDataForStudent(ArrayList<Student> students) {
        for (Student student : students) {
            studentModel.create(student.getFullName(), student.getGender(), student.getPhone(), student.getBirthday(), student.getAddress(), student.getMajor(), student.getIdCertificate(), new StudentModel.OnCreateStudentListener() {
                @Override
                public void onComplete(Student student) {
                    Log.d(TAG, "onComplete: " + student.toString());
                }

                @Override
                public void onFailure() {
                    Log.d(TAG, "onFailure: ");
                }
            });
        }
    }

    private void getAllStudents() {
        studentModel.getAllStudents(new StudentModel.OnGetAllStudentsListener() {
            @Override
            public void onCompleted(ArrayList<Student> students) {
                try {
                    File csvFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "students.csv");
                    Log.d(TAG, "onCreate: " + csvFile);
                    FileWriter writer = new FileWriter(csvFile.getAbsoluteFile());
                    if (!csvFile.exists()) {
                        csvFile.createNewFile();
                    }
                    BufferedWriter bw = new BufferedWriter(writer);
                    try {
                        bw.write("id,fullName,email,gender,birthday,phone,address,major,dateCreated,idCertificate");
                        bw.newLine();
                        for (Student student : students) {
                            if (student.getIdCertificate().size() > 0) {
                                Log.d(TAG, "onCompleted: " + student.getIdCertificate().toString().substring(1, student.getIdCertificate().toString().length() - 1).replaceAll("\\s+", ""));
                                bw.write(student.toStringForCSV() + "," + student.getIdCertificate().toString().substring(1, student.getIdCertificate().toString().length() - 1).replaceAll("\\s+", ""));
                                bw.newLine();
                            } else {
                                Log.d(TAG, "onCompleted: " + student.toStringForCSV());
                                bw.write(student.toStringForCSV());
                                bw.newLine();
                            }
                        }
                        bw.flush();
                        bw.close();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/csv");
                        Uri fileUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", csvFile);
                        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "Share CSV"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getAllCertificates() {
        certificateModel.getAllCertificates(new CertificateModel.OnGetAllCertificatesListener() {
            @Override
            public void onGetAllCertificatesSuccess(ArrayList<Certificate> certificates) {
                try {
                    File csvFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "certificates.csv");
                    Log.d(TAG, "onCreate: " + csvFile);
                    FileWriter writer = new FileWriter(csvFile.getAbsoluteFile());
                    if (!csvFile.exists()) {
                        csvFile.createNewFile();
                    }
                    BufferedWriter bw = new BufferedWriter(writer);
                    try {
                        bw.write("id,name,description,dateCreated,dateUpdated");
                        bw.newLine();
                        for (Certificate certificate : certificates) {
                            if (certificate.getDateUpdated().isEmpty()) {
                                Log.d(TAG, "onCompleted: " + certificate.toStringToCSVForCertificate());
                                bw.write(certificate.toStringToCSVForCertificate());
                                bw.newLine();
                            } else {
                                Log.d(TAG, "onCompleted: " + certificate.toStringToCSV());
                                bw.write(certificate.toStringToCSV());
                                bw.newLine();
                            }
                        }
                        bw.flush();
                        bw.close();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/csv");
                        Uri fileUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", csvFile);
                        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "Share CSV"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetAllCertificatesFailure() {
                Log.d(TAG, "onGetAllCertificatesFailure: ");
            }
        });
    }
}