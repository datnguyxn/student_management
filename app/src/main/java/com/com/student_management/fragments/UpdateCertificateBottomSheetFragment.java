package com.com.student_management.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.com.student_management.BroadcastReceiver;
import com.com.student_management.R;
import com.com.student_management.constants.App;
import com.com.student_management.entities.Certificate;
import com.com.student_management.models.CertificateModel;
import com.com.student_management.utils.FormatDateTime;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class UpdateCertificateBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String TAG = "UpdateCertificateBottomSheetFragment";
    private String idCertificate;
    private Context context = getContext();
    private TextInputEditText etIdCertificate, etNameCertificate, etDescriptionCertificate, edtDateCreatedCertificate;
    private CertificateModel certificateModel;
    private MaterialButton btnUpdateCertificateNew;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(App.ACTION_UPDATE_CERTIFICATE)) {
                Log.d(TAG, "onReceive: " + App.ACTION_UPDATE_CERTIFICATE);
            }

        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter(App.ACTION_UPDATE_CERTIFICATE));
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
        View view = inflater.inflate(R.layout.fragment_update_certificate_bottom_sheet, container, false);
        init(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            idCertificate = bundle.getString("idCertificate");
            etIdCertificate.setText(idCertificate);
            getCertificate(idCertificate);
        }
        btnUpdateCertificateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameCertificate = etNameCertificate.getText().toString().trim();
                String descriptionCertificate = etDescriptionCertificate.getText().toString().trim();
                String dateCreatedCertificate = edtDateCreatedCertificate.getText().toString().trim();
                ArrayList<String> dateUpdated = new ArrayList<>();
                dateUpdated.add(FormatDateTime.formatDateTime());
                Certificate certificate = new Certificate(nameCertificate, descriptionCertificate, dateUpdated);
                certificateModel.updateCertificate(idCertificate, certificate.updateCertificateToMap(), new CertificateModel.OnUpdateCertificateListener() {
                    @Override
                    public void onUpdateCertificateSuccess() {
                        Log.d(TAG, "onUpdateCertificateSuccess: ");
                        sendBroadcastToFragment(9);
                        dismiss();
                    }

                    @Override
                    public void onUpdateCertificateFailure() {
                        Log.d(TAG, "onUpdateCertificateFailure: ");
                        Toast.makeText(getContext(), "Update certificate failure", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
            }
        });
        return view;
    }

    private void init(View view) {
        etIdCertificate = view.findViewById(R.id.edtCertificateId);
        etNameCertificate = view.findViewById(R.id.edtCertificateNameUpdate);
        etDescriptionCertificate = view.findViewById(R.id.edtDescriptionCertificate);
        edtDateCreatedCertificate = view.findViewById(R.id.edtDateCreatedCertificate);
        btnUpdateCertificateNew = view.findViewById(R.id.btnUpdateCertificateNew);
        certificateModel = new CertificateModel();
    }

    private void getCertificate(String id) {
        certificateModel.getCertificate(id, new CertificateModel.CertificateCallback() {
            @Override
            public void onCallback(Certificate certificate) {
                if (certificate != null) {
                    Log.d(TAG, "onCallback: " + certificate.toString());
                    etNameCertificate.setText(certificate.getName());
                    etDescriptionCertificate.setText(certificate.getDescription());
                    edtDateCreatedCertificate.setText(certificate.getDateCreated());
                }
            }
        });
    }

    private void sendBroadcastToFragment(int action) {
        Intent intent = new Intent();
        intent.setAction(App.ACTION_UPDATE_CERTIFICATE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}