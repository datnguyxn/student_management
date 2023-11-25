package com.com.student_management.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.com.student_management.entities.Certificate;
import com.com.student_management.utils.FormatDateTime;
import com.com.student_management.utils.RandomID;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CertificateModel extends Model {
    private static final String TAG = "CertificateModel";
    private static final String CERTIFICATE_COLLECTION = "certificates";

    public interface OnDeleteCertificateListener {
        void onDeleteCertificateSuccess();

        void onDeleteCertificateFailure();
    }

    public interface OnCreateCertificateListener {
        void onCreateCertificateSuccess(Certificate certificate);
        void onCreateCertificateFailure();
    }

    public interface CertificateCallback {
        void onCallback(Certificate certificate);
    }

    public interface OnGetAllCertificatesListener {
        void onGetAllCertificatesSuccess(ArrayList<Certificate> certificates);
        void onGetAllCertificatesFailure();
    }

    public interface OnUpdateCertificateListener {
        void onUpdateCertificateSuccess();
        void onUpdateCertificateFailure();
    }

    public interface IsExistCertificateListener {
        void onExist(Certificate certificate);
        void onNotExist();
    }

    public CertificateModel() {
        super();
    }

    public CertificateModel(FirebaseFirestore firebaseFirestore) {
        super(firebaseFirestore);
    }

    public void create(String name, String description, OnCreateCertificateListener listener) {
        try {
            String id = RandomID.generateIDCertificate();
            Log.d(TAG, "create: " + id);
            ArrayList<String> dateUpdated = new ArrayList<>();
            String dateCreated = FormatDateTime.formatDateTime();
            Certificate certificate = new Certificate(id, name, description, dateCreated, dateUpdated);
            firebaseFirestore.collection(CERTIFICATE_COLLECTION).document(id).set(certificate.toMap())
                    .addOnSuccessListener(unused -> {
                        Log.d(TAG, "createCertificateSuccess: ");
                        listener.onCreateCertificateSuccess(certificate);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "createCertificateFailure: ", e);
                        listener.onCreateCertificateFailure();
                    });
        } catch (Exception e) {
            Log.e(TAG, "createFailure: ", e);
            listener.onCreateCertificateFailure();
        }
    }

    public void isCertificateExists(String id, CertificateCallback callback) {
        try {
            firebaseFirestore.collection(CERTIFICATE_COLLECTION).document(id).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Certificate certificate = documentSnapshot.toObject(Certificate.class);
                            callback.onCallback(certificate);
                        } else {
                            callback.onCallback(null);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "isCertificateExistsFailure: ", e);
                        callback.onCallback(null);
                    });
        } catch (Exception e) {
            Log.e(TAG, "isCertificateExistsFailure: ", e);
            callback.onCallback(null);
        }
    }

    public void getCertificate(String id, CertificateCallback callback) {
        try {
            firebaseFirestore.collection(CERTIFICATE_COLLECTION).document(id).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Certificate certificate = task.getResult().toObject(Certificate.class);
                            Log.d(TAG, "getCertificateSuccess: " + certificate.toString());
                            if (certificate != null) {
                                callback.onCallback(certificate);
                            } else {
                                callback.onCallback(null);
                            }
                        } else {
                            Log.e(TAG, "getCertificateFailure: ", task.getException());
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "getCertificateFailure: ", e);
                        callback.onCallback(null);
                    });
        } catch (Exception e) {
            Log.e(TAG, "getCertificateFailure: ", e);
            callback.onCallback(null);
        }
    }

    public void getAllCertificates(OnGetAllCertificatesListener listener) {
        try {
            firebaseFirestore.collection(CERTIFICATE_COLLECTION).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        ArrayList<Certificate> certificates = new ArrayList<>();
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                                Certificate certificate = documentSnapshot.toObject(Certificate.class);
                                certificates.add(certificate);
                            }
                            listener.onGetAllCertificatesSuccess(certificates);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "getAllCertificatesFailure: ", e);
                            listener.onGetAllCertificatesFailure();
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "getAllCertificatesFailure: ", e);
            listener.onGetAllCertificatesFailure();
        }
    }

    public void updateCertificate(String id, Map<String, Object> data, OnUpdateCertificateListener listener) {
        try {
            firebaseFirestore.collection(CERTIFICATE_COLLECTION).document(id).update(data)
                    .addOnSuccessListener(unused -> {
                        Log.d(TAG, "updateCertificateSuccess: ");
                        listener.onUpdateCertificateSuccess();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "updateCertificateFailure: ", e);
                        listener.onUpdateCertificateFailure();
                    });
        } catch (Exception e) {
            Log.e(TAG, "updateCertificateFailure: ", e);
            listener.onUpdateCertificateFailure();
        }
    }

    public void deleteCertificate(String id, OnDeleteCertificateListener listener) {
        try {
            firebaseFirestore.collection(CERTIFICATE_COLLECTION).document(id).delete()
                    .addOnSuccessListener(unused -> {
                        Log.d(TAG, "deleteCertificateSuccess: ");
                        listener.onDeleteCertificateSuccess();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "deleteCertificateFailure: ", e);
                        listener.onDeleteCertificateFailure();
                    });
        } catch (Exception e) {
            Log.e(TAG, "deleteCertificateFailure: ", e);
            listener.onDeleteCertificateFailure();
        }
    }

    public void isExistCertificate(String name, IsExistCertificateListener listener) {
        try {
            firebaseFirestore.collection(CERTIFICATE_COLLECTION).whereEqualTo("name", name).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            listener.onNotExist();
                        } else {
                            Certificate certificate = queryDocumentSnapshots.getDocuments().get(0).toObject(Certificate.class);
                            listener.onExist(certificate);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "isExistCertificateFailure: ", e);
                        listener.onNotExist();
                    });
        } catch (Exception e) {
            Log.e(TAG, "isExistCertificateFailure: ", e);
            listener.onNotExist();
        }
    }
}
