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
import android.widget.ImageView;
import android.widget.Toast;

import com.com.student_management.MainActivity;
import com.com.student_management.R;
import com.com.student_management.entities.Certificate;
import com.com.student_management.models.CertificateModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewCertificateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCertificateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView imvBack;
    private TextInputEditText edtCertificateName, edtCertificateDescription;
    private MaterialButton btnAddCertificate;
    private static final String TAG = "NewCertificateFragment";
    private CertificateModel certificateModel;

    public NewCertificateFragment() {
        // Required empty public constructor
    }


    public static NewCertificateFragment newInstance(String param1, String param2) {
        NewCertificateFragment fragment = new NewCertificateFragment();
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
        View newCertificateView = inflater.inflate(R.layout.fragment_new_certificate, container, false);
        init(newCertificateView);
        imvBack.setOnClickListener(v -> {
            replaceFragment(new HomeFragment());
        });
        btnAddCertificate.setOnClickListener(v -> {
            createNewCertificate();
        });
        return newCertificateView;
    }

    private void init(View newCertificateView) {
        imvBack = newCertificateView.findViewById(R.id.iv_back);
        edtCertificateName = newCertificateView.findViewById(R.id.edtCertificateName);
        edtCertificateDescription = newCertificateView.findViewById(R.id.edtDescription);
        btnAddCertificate = newCertificateView.findViewById(R.id.btnAddNewCertificate);
        certificateModel = new CertificateModel();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void createNewCertificate() {
        String certificateName = edtCertificateName.getText().toString().trim().toUpperCase();
        String certificateDescription = edtCertificateDescription.getText().toString().trim();
        if (certificateName.isEmpty() || certificateDescription.isEmpty()) {
            return;
        }


        certificateModel.isCertificateExists(certificateName, new CertificateModel.CertificateCallback() {
            @Override
            public void onCallback(Certificate certificate) {
                if (certificate != null) {
                    Toast.makeText(getContext(), "Certificate is already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                certificateModel.create(certificateName, certificateDescription, new CertificateModel.OnCreateCertificateListener() {
                    @Override
                    public void onCreateCertificateSuccess(Certificate certificate) {
                        Log.d(TAG, "onCreateCertificateSuccess: " + certificate.toString());
                        Toast.makeText(getContext(), "Create certificate successfully", Toast.LENGTH_SHORT).show();
                        replaceFragment(new ListCertificateFragment());
                    }

                    @Override
                    public void onCreateCertificateFailure() {
                        Log.e(TAG, "onCreateCertificateFailure: ");
                        Toast.makeText(getContext(), "Create certificate failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}