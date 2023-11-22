package com.com.student_management.fragments;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.com.student_management.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;

public class ChangeAvatarBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String TAG = "ChangeAvatarBottomSheetFragment";
    private static final int REQUEST_CODE = 123;
    private String uri;
    private MaterialButton btnGallery, btnRemoveAvatar;

    public interface BottomSheetListener {
        void onDataReceived(String data);
    }

    private BottomSheetListener mListener;

    public ChangeAvatarBottomSheetFragment() {
    }

    public void setBottomSheetListener(BottomSheetListener listener) {
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_avatar_bottom_sheet, container, false);
        init(view);
        openGallery();
        removeAvatar();
        return view;
    }

    private void init(View view) {
        btnGallery = view.findViewById(R.id.btnGallery);
        btnRemoveAvatar = view.findViewById(R.id.btnRemoveAvatar);
    }

    private void openGallery() {
        btnGallery.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, REQUEST_CODE);
        });
    }

    private void removeAvatar() {
        btnRemoveAvatar.setOnClickListener(v -> {
            uri = "";
            sendData();
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == getActivity().RESULT_OK && data != null) {
            Log.e(TAG, "onActivityResult2: " + data.getData().toString());
            Uri imageUri = data.getData();
            Log.e(TAG, "onActivityResult: " + imageUri.toString());
            uri = imageUri.toString();
            sendData();
        }
    }



    private void sendDataToParent(String data) {
        if (mListener != null) {
            mListener.onDataReceived(data);
            dismiss();
        }
    }

    // Trigger this method when you want to send data
    private void sendData() {
        sendDataToParent(uri);
    }
}