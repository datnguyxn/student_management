package com.com.student_management.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.com.student_management.BroadcastReceiver;
import com.com.student_management.R;
import com.com.student_management.adapters.CertificateOfStudentAdapter;
import com.com.student_management.adapters.UserAdapter;
import com.com.student_management.constants.App;
import com.com.student_management.entities.Certificate;
import com.com.student_management.entities.Student;
import com.com.student_management.models.CertificateModel;
import com.com.student_management.models.StudentModel;
import com.com.student_management.utils.FormatDateTime;
import com.com.student_management.utils.HandID;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailAndUpdateStudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailAndUpdateStudentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG = "DetailAndUpdateStudentFragment";
    private Context context = getContext();
    private CertificateOfStudentAdapter certificateOfStudentAdapter;
    private StudentModel studentModel;
    private String studentId;
    private ImageView imvBack, ivMore;
    private TextInputEditText edtIdStudent, edtNameStudent, edtEmailStudent, edtBirthday, edtPhoneStudent, edtAddress, edtMajorStudent, edtDateCreated;
    private CheckBox checkBoxMale, checkBoxFemale;
    private MaterialButton btnUpdateStudent, btnCancel;
    private RecyclerView rvCertificateOfStudent;
    private ArrayList<String> listCertificateOfStudent = new ArrayList<>();
    private CertificateModel certificateModel;
    private AlertDialog alertDialog;
    private boolean isCheck = true;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            String action = intent.getAction();
            if (action.equals(App.ACTION_ADD_CERTIFICATE_FOR_STUDENT)) {
                Log.d(TAG, "onReceive: " + App.ACTION_ADD_CERTIFICATE_FOR_STUDENT);
                if (certificateOfStudentAdapter != null) {
                    certificateOfStudentAdapter.clearData();
                    getAllCertificateOfStudent(studentId);
                }
            }
        }
    };

    public DetailAndUpdateStudentFragment() {
        // Required empty public constructor
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(App.ACTION_ADD_CERTIFICATE_FOR_STUDENT);
        return intentFilter;
    }

    public static DetailAndUpdateStudentFragment newInstance(String param1, String param2) {
        DetailAndUpdateStudentFragment fragment = new DetailAndUpdateStudentFragment();
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
        View view = inflater.inflate(R.layout.fragment_detail_and_update_student, container, false);
        init(view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCertificateOfStudent.setLayoutManager(layoutManager);
        certificateOfStudentAdapter = new CertificateOfStudentAdapter(getContext());
        rvCertificateOfStudent.setAdapter(certificateOfStudentAdapter);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, getIntentFilter());
        Bundle bundle = getArguments();
        if (bundle != null) {
            Log.d(TAG, "onCreateView: " + bundle.getString("studentId"));
            studentId = bundle.getString("studentId");
        }
        getAllCertificateOfStudent(studentId);
        imvBack.setOnClickListener(v -> {
            replaceFragment(new ListStudentFragment());
        });
        ivMore.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), ivMore);
            popupMenu.inflate(R.menu.student_detail_item);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Log.d(TAG, "onMenuItemClick: " + menuItem.getItemId());
                    switch (menuItem.getItemId()) {
                        case App.UPDATE_STUDENT_INFO:
                            setEnableEditText(true);
                            edtDateCreated.setEnabled(false);
                            edtIdStudent.setEnabled(false);
                            edtEmailStudent.setEnabled(false);
                            btnUpdateStudent.setVisibility(View.VISIBLE);
                            btnCancel.setVisibility(View.VISIBLE);
                            if (checkBoxMale.isChecked()) {
                                isCheck = true;
                            } else if (checkBoxFemale.isChecked()) {
                                isCheck = false;
                            }
                            btnUpdateStudent.setOnClickListener(v -> {
                                String name = edtNameStudent.getText().toString().trim();
                                String email = edtEmailStudent.getText().toString().trim();
                                String birthday = edtBirthday.getText().toString().trim();
                                String phone = edtPhoneStudent.getText().toString().trim();
                                String address = edtAddress.getText().toString().trim();
                                String major = edtMajorStudent.getText().toString().trim();
                                ArrayList<String> dateUpdated = new ArrayList<>();
                                boolean isMale;
                                if (checkBoxMale.isChecked()) {
                                    isMale = true;
                                    checkBoxFemale.setChecked(false);
                                } else {
                                    isMale = false;
                                    checkBoxMale.setChecked(false);
                                }
                                String dateUpdate = FormatDateTime.formatDateTime();
                                dateUpdated.add(dateUpdate);
                                Student updateStudent = new Student(name, email, isMale, birthday, phone, address, major, dateUpdated);
                                Map<String, Object> studentMap = updateStudent.updateStudentToMap();
                                studentModel.updateStudent(studentId, studentMap, new StudentModel.OnStudentUpdatedLintener() {
                                    @Override
                                    public void onCompleted() {
                                        Log.d(TAG, "onCompleted: update student success");
                                        setEnableEditText(false);
                                        btnUpdateStudent.setVisibility(View.GONE);
                                        btnCancel.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure() {
                                        Log.e(TAG, "onFailure: update student failure");
                                        Toast.makeText(getContext(), "Update student failure", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            });
                            btnCancel.setOnClickListener(v -> {
                                setEnableEditText(false);
                                btnUpdateStudent.setVisibility(View.GONE);
                                btnCancel.setVisibility(View.GONE);
                            });
                            return true;
                        case App.DELETE_STUDENT_INFO:
                            alertDialog = new AlertDialog.Builder(getContext())
                                    .setTitle("Delete Student Information")
                                    .setMessage("Are you sure you want to delete this student information?")
                                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                                        studentModel.deleteStudent(studentId, new StudentModel.OnDeleteStudentListener() {
                                            @Override
                                            public void onCompleted(boolean isDelete) {
                                                Log.d(TAG, "onCompleted: " + isDelete);
                                                if (isDelete) {
                                                    replaceFragment(new ListStudentFragment());
                                                } else {
                                                    Toast.makeText(getContext(), "Delete student failure", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    })
                                    .setNegativeButton("No", (dialogInterface, i) -> {
                                        Log.d(TAG, "onClick: cancel delete user");
                                        alertDialog.dismiss();
                                    })
                                    .show();
                            return true;
                        case App.UPDATE_STUDENT_CERTIFICATE:
                            AddCertificateOfStudentBottomSheetFragment addCertificateOfStudentBottomSheetFragment = new AddCertificateOfStudentBottomSheetFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("studentId", studentId);
                            addCertificateOfStudentBottomSheetFragment.setArguments(bundle);
                            addCertificateOfStudentBottomSheetFragment.show(getActivity().getSupportFragmentManager(), addCertificateOfStudentBottomSheetFragment.getTag());
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popupMenu.show();

        });
        return view;
    }

    private void init(View view) {
        studentModel = new StudentModel();
        certificateModel = new CertificateModel();
        imvBack = view.findViewById(R.id.iv_back);
        ivMore = view.findViewById(R.id.ivMore);
        edtIdStudent = view.findViewById(R.id.edtIdStudent);
        edtNameStudent = view.findViewById(R.id.edtNameStudent);
        edtEmailStudent = view.findViewById(R.id.edtEmailStudent);
        edtBirthday = view.findViewById(R.id.edtBirthday);
        edtPhoneStudent = view.findViewById(R.id.edtPhoneStudent);
        edtAddress = view.findViewById(R.id.edtAddress);
        edtMajorStudent = view.findViewById(R.id.edtMajorStudent);
        edtDateCreated = view.findViewById(R.id.edtDateCreated);
        checkBoxMale = view.findViewById(R.id.checkBoxMale);
        checkBoxFemale = view.findViewById(R.id.checkBoxFemale);
        btnUpdateStudent = view.findViewById(R.id.btnUpdateStudent);
        rvCertificateOfStudent = view.findViewById(R.id.rvCertificateOfStudent);
        btnCancel = view.findViewById(R.id.btnCancel);
    }

    private void getAllCertificateOfStudent(String idStudent) {
        ArrayList<String> listCertificateOfStudent = new ArrayList<>();
        studentModel.isExistStudent(idStudent, new StudentModel.CheckStudentExistCallbacks() {
            @Override
            public void onExist(Student student) {
                try {
                    Log.d(TAG, "onExist: " + student.toString());
                    edtIdStudent.setText(HandID.decrypt(student.getId()));
                    edtNameStudent.setText(student.getFullName());
                    edtEmailStudent.setText(student.getEmail());
                    edtBirthday.setText(student.getBirthday());
                    edtPhoneStudent.setText(student.getPhone());
                    edtAddress.setText(student.getAddress());
                    edtMajorStudent.setText(student.getMajor());
                    edtDateCreated.setText(student.getDateCreated());
                    btnUpdateStudent.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                    Bundle bundle = new Bundle();
                    bundle.putString("student_id", studentId);
                    certificateOfStudentAdapter.setArguments(bundle);
                    if (student.getGender()) {
                        checkBoxMale.setChecked(true);
                    } else {
                        checkBoxFemale.setChecked(true);
                    }
                    setEnableEditText(false);
                    if (!student.getIdCertificate().isEmpty()) {
                        for (String id : student.getIdCertificate()) {
                            Log.d(TAG, "onExist: " + id);
                            listCertificateOfStudent.add(id);
                        }
                        setNameForCertificateOfStudent(listCertificateOfStudent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNotExist() {
                Log.e(TAG, "onNotExist: ");
            }
        });
    }


    private void setNameForCertificateOfStudent(ArrayList<String> listCertificateOfStudent) {
        ArrayList<Certificate> listNameCertificateOfStudent = new ArrayList<>();
        for (String id : listCertificateOfStudent) {
            certificateModel.getCertificate(id, new CertificateModel.CertificateCallback() {
                @Override
                public void onCallback(Certificate certificate) {
                    Log.d(TAG, "onCallback: " + certificate.toString());
                    listNameCertificateOfStudent.add(certificate);
                    certificateOfStudentAdapter.setData(listNameCertificateOfStudent);
                }
            });
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void setEnableEditText(boolean isEnable) {
        edtIdStudent.setEnabled(isEnable);
        edtNameStudent.setEnabled(isEnable);
        edtEmailStudent.setEnabled(isEnable);
        edtBirthday.setEnabled(isEnable);
        edtPhoneStudent.setEnabled(isEnable);
        edtAddress.setEnabled(isEnable);
        edtMajorStudent.setEnabled(isEnable);
        edtDateCreated.setEnabled(isEnable);
        checkBoxMale.setEnabled(isEnable);
        checkBoxFemale.setEnabled(isEnable);
    }
}