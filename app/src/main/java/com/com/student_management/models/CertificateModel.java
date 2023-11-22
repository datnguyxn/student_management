package com.com.student_management.models;

import com.com.student_management.entities.Certificate;
import com.google.firebase.firestore.FirebaseFirestore;

public class CertificateModel extends Model {
    private static final String TAG = "CertificateModel";
    private static final String COLLECTION_NAME = "certificates";

    public interface EventListener {
        void onGetCertificateSuccess(Certificate certificate);

        void onGetCertificateFailure();

        void onAddCertificateSuccess();

        void onAddCertificateFailure();

        void onUpdateCertificateSuccess(Certificate certificate);

        void onUpdateCertificateFailure();

        void onDeleteCertificateSuccess();

        void onDeleteCertificateFailure();
    }

    public CertificateModel() {
        super();
    }

    public CertificateModel(FirebaseFirestore firebaseFirestore) {
        super(firebaseFirestore);
    }

    public void create(Certificate certificate, EventListener listener) {
        firebaseFirestore.collection(COLLECTION_NAME)
                .add(certificate)
                .addOnSuccessListener(documentReference -> {
                    certificate.setId(documentReference.getId());
                    documentReference.set(certificate);
                    listener.onAddCertificateSuccess();
                })
                .addOnFailureListener(e -> listener.onAddCertificateFailure());
    }

}
