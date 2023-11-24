package com.com.student_management.models;

import android.util.Log;

import com.com.student_management.entities.Certificate;
import com.com.student_management.utils.FormatDateTime;
import com.com.student_management.utils.HandID;
import com.com.student_management.utils.RandomID;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

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
            String hashId = HandID.encrypt(id);
            ArrayList<String> dateUpdated = new ArrayList<>();
            String dateCreated = FormatDateTime.formatDateTime();
            Certificate certificate = new Certificate(hashId, name, description, dateCreated, dateUpdated);
            firebaseFirestore.collection(CERTIFICATE_COLLECTION).document(hashId).set(certificate)
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
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Certificate certificate = documentSnapshot.toObject(Certificate.class);
                            callback.onCallback(certificate);
                        } else {
                            callback.onCallback(null);
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
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        ArrayList<Certificate> certificates = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Certificate certificate = documentSnapshot.toObject(Certificate.class);
                            certificates.add(certificate);
                        }
                        listener.onGetAllCertificatesSuccess(certificates);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "getAllCertificatesFailure: ", e);
                        listener.onGetAllCertificatesFailure();
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
