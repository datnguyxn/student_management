package com.com.student_management.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.com.student_management.R;
import com.com.student_management.constants.App;
import com.com.student_management.entities.Certificate;
import com.com.student_management.entities.Student;
import com.com.student_management.models.CertificateModel;
import com.com.student_management.models.StudentModel;

import java.util.ArrayList;

public class CertificateOfStudentAdapter extends RecyclerView.Adapter<CertificateOfStudentAdapter.CertificateOfStudentViewHolder> {
    private Context context;
    private ArrayList<Certificate> certificates;
    private CertificateModel certificateModel = new CertificateModel();
    private StudentModel studentModel = new StudentModel();
    private Bundle bundle = new Bundle();
    private AlertDialog alertDialog;
    private String studentId, certificateId;

    public CertificateOfStudentAdapter(Context context, ArrayList<Certificate> certificates) {
        this.context = context;
        this.certificates = certificates;
    }

    public CertificateOfStudentAdapter(Context context) {
        this.context = context;
        this.certificates = new ArrayList<>();
    }

    public void setData(ArrayList<Certificate> certificates) {
        this.certificates = certificates;
        notifyDataSetChanged();
    }

    public void clearData() {
        this.certificates.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CertificateOfStudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_certificate_of_student, parent, false);
        return new CertificateOfStudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateOfStudentViewHolder holder, int position) {
        Certificate certificate = certificates.get(position);
        String id = certificate.getId();
        holder.tvCertificateName.setText(certificate.getName());
        holder.menu_certificate_of_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.menu_certificate_of_student);
                popupMenu.inflate(R.menu.certificate_of_student_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Log.d("TAG", "onMenuItemClick: " + menuItem.getItemId());
                        if (menuItem.getItemId() == App.DELETE_CERTIFICATE_OF_STUDENT) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Do you want to delete this certificate?");
                            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                                studentModel.deleteCertificateOfStudent(studentId, id, new StudentModel.OnUpdateCertificateListener() {
                                    @Override
                                    public void onCompleted() {
                                        certificates.remove(certificate);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure() {
                                        Log.e("TAG", "onFailure: ");
                                    }
                                });
                            });
                            builder.setNegativeButton("No", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            });
                            alertDialog = builder.create();
                            alertDialog.show();
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return certificates.size();
    }

    public void setArguments(Bundle bundle) {
        this.bundle = bundle;
        this.certificateId = bundle.getString("certificate_id");
        this.studentId = bundle.getString("student_id");
        Log.d("TAG", "setArguments: " + studentId);
        Log.d("TAG", "setArguments: " + certificateId);
    }

    public static class CertificateOfStudentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCertificateName;
        private ImageView menu_certificate_of_student;
        public CertificateOfStudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCertificateName = itemView.findViewById(R.id.tvNameCertificate);
            menu_certificate_of_student = itemView.findViewById(R.id.menu_certificate_of_student);
        }
    }
}
