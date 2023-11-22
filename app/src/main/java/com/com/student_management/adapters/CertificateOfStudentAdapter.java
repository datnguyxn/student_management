package com.com.student_management.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.com.student_management.R;
import com.com.student_management.entities.Certificate;
import com.com.student_management.entities.Student;
import com.com.student_management.models.CertificateModel;
import com.com.student_management.models.StudentModel;

import java.util.ArrayList;

public class CertificateOfStudentAdapter extends RecyclerView.Adapter<CertificateOfStudentAdapter.CertificateOfStudentViewHolder> {
    private Context context;
    private ArrayList<Certificate> certificates;
    private CertificateModel certificateModel = new CertificateModel();
    private Bundle bundle = new Bundle();
    private AlertDialog alertDialog;

    public CertificateOfStudentAdapter(Context context, ArrayList<Certificate> certificates) {
        this.context = context;
        this.certificates = certificates;
    }

    public CertificateOfStudentAdapter(Context context) {
        this.context = context;
        this.certificates = new ArrayList<>();
    }

    public void setCertificates(ArrayList<Certificate> certificates) {
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
        holder.tvCertificateName.setText(certificate.getName());
        holder.tvExpiredDate.setText(certificate.getExpirationDate());
        holder.menu_certificate_of_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class CertificateOfStudentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCertificateName, tvExpiredDate;
        private ImageView menu_certificate_of_student;
        public CertificateOfStudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCertificateName = itemView.findViewById(R.id.tvNameCertificate);
            tvExpiredDate = itemView.findViewById(R.id.expirationDate);
            menu_certificate_of_student = itemView.findViewById(R.id.menu_certificate_of_student);
        }
    }
}
