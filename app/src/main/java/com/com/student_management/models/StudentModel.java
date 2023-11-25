package com.com.student_management.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.com.student_management.entities.Student;
import com.com.student_management.utils.FormatDateTime;
import com.com.student_management.utils.RandomID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class StudentModel extends Model {
    private static final String TAG = "StudentModel";
    private static final String STUDENT_COLLECTION = "students";

    public interface OnCreateStudentListener {
        void onComplete(Student student);

        void onFailure();
    }

    public interface OnStudentUpdatedLintener {
        void onCompleted();

        void onFailure();
    }

    public interface StudentCallback {
        void onCallback(Student student);

    }

    public interface CheckStudentExistCallbacks {
        void onExist(Student student);

        void onNotExist();
    }

    public interface OnDeleteStudentListener {
        void onCompleted(boolean isDeleted);
    }

    public interface OnGetAllStudentsListener {
        void onCompleted(ArrayList<Student> students);
    }

    public interface OnUpdateCertificateListener {
        void onCompleted();

        void onFailure();
    }

    public interface OnGetAllCertificatesOfStudentListener {
        void onCompleted(ArrayList<String> certificates);
    }

    public StudentModel() {
        super();
    }

    public StudentModel(FirebaseFirestore firebaseFirestore) {
        super(firebaseFirestore);
    }

    public void create(String fullName, boolean gender, String phoneNumber, String birthday, String address, String major, ArrayList<String> certificates, OnCreateStudentListener listener) {
        try {
            String id = RandomID.generateIDStudent();
            String email = id + "@student.tdtu.edu.vn";
            String dateCreated = FormatDateTime.formatDateTime();
            ArrayList<String> dateUpdated = new ArrayList<>();
            if (isExistStudent(id)) {
                Log.d(TAG, "create: Student is exist");
                return;
            }
            Student newStudent = new Student(id, fullName, email, gender, birthday, phoneNumber, address, major, dateCreated, dateUpdated, certificates);
            firebaseFirestore.collection(STUDENT_COLLECTION).document(id).set(newStudent.toMap())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: create student success");
                            Log.d(TAG, "create: " + newStudent.toString());
                            listener.onComplete(newStudent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "create: " + e.toString());
                            listener.onFailure();
                        }
                    });

        } catch (Exception e) {
            Log.e(TAG, "Failure: ", e);
            listener.onFailure();
        }

    }

    public boolean isExistStudent(String id) {
        boolean[] isExist = {false};
        firebaseFirestore.collection(STUDENT_COLLECTION).document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Student student = task.getResult().toObject(Student.class);
                            if (student != null) {
                                Log.d(TAG, "onComplete: " + student.toString());
                                isExist[0] = true;
                            } else {
                                Log.d(TAG, "onComplete: student is null");
                                isExist[0] = false;
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
//                        callbacks.onNotExist();
                        isExist[0] = false;
                    }
                });
        return isExist[0];
    }

    public void isExistStudent(String id, CheckStudentExistCallbacks callbacks) {
        firebaseFirestore.collection(STUDENT_COLLECTION).document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Student student = task.getResult().toObject(Student.class);
                        if (student != null) {
                            Log.d(TAG, "onComplete: " + student.toString());
                            callbacks.onExist(student);
                        } else {
                            Log.d(TAG, "onComplete: student is null");
                            callbacks.onNotExist();
                        }

                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "onFailure: ", e);
                    callbacks.onNotExist();
                });
    }

    public void getAllStudents(OnGetAllStudentsListener allStudentsListener) {
        firebaseFirestore.collection(STUDENT_COLLECTION).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    ArrayList<Student> students = new ArrayList<>();

                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (DocumentSnapshot documentSnapshot : querySnapshot) {
                            Student student = documentSnapshot.toObject(Student.class);
                            students.add(student);
                        }
                        allStudentsListener.onCompleted(students);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                        allStudentsListener.onCompleted(null);
                    }
                });
    }

    public void deleteStudent(String id, OnDeleteStudentListener listener) {
        Log.d(TAG, "deleteStudent: " + id);
        firebaseFirestore.collection(STUDENT_COLLECTION).document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "deleteStudent: success");
                        listener.onCompleted(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onCompleted(false);
                        Log.e(TAG, "deleteStudent: ", e);
                    }
                });
    }

    public void updateStudent(String id, Map<String, Object> data, OnStudentUpdatedLintener listener) {
        ;
        firebaseFirestore.collection(STUDENT_COLLECTION).document(id).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: update student success");
                        listener.onCompleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure();
                        Log.e(TAG, "onFailure: " + e.toString());
                    }
                });
    }

    public void updateCertificate(String id, String idCertificate, OnUpdateCertificateListener listener) {
        firebaseFirestore.collection(STUDENT_COLLECTION).document(id).update("idCertificate", FieldValue.arrayUnion(idCertificate))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: update certificate success");
                        listener.onCompleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure();
                        Log.e(TAG, "onFailure: " + e.toString());
                    }
                });
    }

    public void deleteCertificateOfStudent(String id, String idCertificate, OnUpdateCertificateListener listener) {
        firebaseFirestore.collection(STUDENT_COLLECTION).document(id).update("idCertificate", FieldValue.arrayRemove(idCertificate))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: delete certificate success");
                        listener.onCompleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure();
                        Log.e(TAG, "onFailure: " + e.toString());
                    }
                });
    }

    public void deleteCertificateOfStudents(String idCertificate, OnUpdateCertificateListener listener) {
        firebaseFirestore.collection(STUDENT_COLLECTION).whereArrayContains("idCertificate", idCertificate).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        ArrayList<Student> students = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : querySnapshot) {
                            Student student = documentSnapshot.toObject(Student.class);
                            students.add(student);
                        }
                        for (Student student : students) {
                            deleteCertificateOfStudent(student.getId(), idCertificate, listener);
                        }
                        listener.onCompleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure();
                        Log.e(TAG, "onFailure: " + e.toString());
                    }
                });
    }
}
