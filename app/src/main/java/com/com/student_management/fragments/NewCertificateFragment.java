package com.com.student_management.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.com.student_management.MainActivity;
import com.com.student_management.R;

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

    public NewCertificateFragment() {
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
            onBackPressed();
        });
        // Inflate the layout for this fragment
        return newCertificateView;
    }

    private void init(View newCertificateView) {
        imvBack = newCertificateView.findViewById(R.id.iv_back);
    }

    public void onBackPressed() {
        // Create an Intent to navigate back to the previous activity
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);

        // Finish the current activity (fragment)
        requireActivity().finish();
    }

}