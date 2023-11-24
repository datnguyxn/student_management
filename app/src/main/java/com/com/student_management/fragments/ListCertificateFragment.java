package com.com.student_management.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.com.student_management.BroadcastReceiver;
import com.com.student_management.MainActivity;
import com.com.student_management.R;
import com.com.student_management.adapters.CertificateAdapter;
import com.com.student_management.constants.App;
import com.com.student_management.entities.Certificate;
import com.com.student_management.models.CertificateModel;
import com.com.student_management.utils.HandID;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListCertificateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListCertificateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView imvBack;
    private static final String TAG = "ListCertificateFragment";
    private CertificateModel certificateModel;
    private RecyclerView rvListCertificate;
    private CertificateAdapter certificateAdapter;
    private ScrollView scrollView;
    private ConstraintLayout constraintLayout;
    private Context context = getContext();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(App.ACTION_UPDATE_CERTIFICATE)) {
                    Log.d(TAG, "onReceive: " + App.ACTION_UPDATE_CERTIFICATE);
                    if (certificateAdapter != null) {
                        certificateAdapter.clearData();
                        getAllCertificate();
                    }
                }
            }
        }
    };

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(App.ACTION_UPDATE_CERTIFICATE);
        return intentFilter;
    }

    public ListCertificateFragment() {
        // Required empty public constructor
    }

    public static ListCertificateFragment newInstance(String param1, String param2) {
        ListCertificateFragment fragment = new ListCertificateFragment();
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
        View listCertificateView = inflater.inflate(R.layout.fragment_list_certificate, container, false);
        scrollView = new ScrollView(getContext());
        constraintLayout = new ConstraintLayout(getContext());
        scrollView.addView(constraintLayout);
        init(listCertificateView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvListCertificate.setLayoutManager(layoutManager);
        certificateAdapter = new CertificateAdapter(getContext());
        rvListCertificate.setAdapter(certificateAdapter);
        getAllCertificate();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, getIntentFilter());
        imvBack.setOnClickListener(v -> {
            replaceFragment(new HomeFragment());
        });
        // Inflate the layout for this fragment
        return listCertificateView;
    }

    private void init(View listCertificateView) {
        imvBack = listCertificateView.findViewById(R.id.iv_back);
        rvListCertificate = listCertificateView.findViewById(R.id.rvCertificate);
        certificateModel = new CertificateModel();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public void onBackPressed() {
        // Create an Intent to navigate back to the previous activity
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);

        // Finish the current activity (fragment)
        requireActivity().finish();
    }

    private void getAllCertificate() {
        certificateModel.getAllCertificates(new CertificateModel.OnGetAllCertificatesListener() {
            @Override
            public void onGetAllCertificatesSuccess(ArrayList<Certificate> certificates) {
                try {
                    for (Certificate certificate : certificates) {
                        certificate.setId(HandID.decrypt(certificate.getId()));
                        Log.d(TAG, "onGetAllCertificatesSuccess: " + certificates.toString());
                    }
                    certificateAdapter.setData(certificates);
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