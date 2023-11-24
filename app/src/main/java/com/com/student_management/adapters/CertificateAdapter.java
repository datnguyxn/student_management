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

import com.com.student_management.constants.App;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.com.student_management.R;
import com.com.student_management.entities.Certificate;
import com.com.student_management.fragments.UpdateCertificateBottomSheetFragment;
import com.com.student_management.models.CertificateModel;
import com.com.student_management.models.StudentModel;

import java.util.ArrayList;

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.CertificateViewHolder> {
    private static final String TAG = "CertificateAdapter";

    public CertificateAdapter() {
    }

    private Context context;
    private ArrayList<Certificate> certificates;
    private Bundle bundle = new Bundle();
    private AlertDialog alertDialog;
    private CertificateModel certificateModel = new CertificateModel();
    private StudentModel studentModel = new StudentModel();

    public CertificateAdapter(Context context) {
        this.context = context;
        this.certificates = new ArrayList<>();
    }

    public CertificateAdapter(Context context, ArrayList<Certificate> certificates) {
        this.context = context;
        this.certificates = certificates;
    }

    public void setData(ArrayList<Certificate> certificates) {
        this.certificates.clear();
        this.certificates.addAll(certificates);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.certificates.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CertificateAdapter.CertificateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_certificate, parent, false);
        return new CertificateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateAdapter.CertificateViewHolder holder, int position) {
        Certificate certificate = certificates.get(position);
        if (certificate == null) {
            return;
        }
        holder.tvCertificateId.setText(certificate.getId());
        holder.tvCertificateName.setText(certificate.getName());
        holder.menuCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.menuCertificate);
                popupMenu.inflate(R.menu.certificate_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Log.d(TAG, "onMenuItemClick: " + menuItem.getItemId());
                        switch (menuItem.getItemId()) {
                            case App.UPDATE_CERTIFICATE:
                                bundle.putString("idCertificate", certificate.getId());
                                UpdateCertificateBottomSheetFragment updateCertificateBottomSheetFragment = new UpdateCertificateBottomSheetFragment();
                                updateCertificateBottomSheetFragment.setArguments(bundle);
                                updateCertificateBottomSheetFragment.show(((FragmentActivity) context).getSupportFragmentManager(), updateCertificateBottomSheetFragment.getTag());
                                return true;
                            case App.DELETE_CERTIFICATE:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Delete Certificate");
                                builder.setMessage("Do you want to delete this certificate?");
                                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                                    certificateModel.deleteCertificate(certificate.getId(), new CertificateModel.OnDeleteCertificateListener() {
                                        @Override
                                        public void onDeleteCertificateSuccess() {
                                            certificates.remove(certificate);
                                            notifyDataSetChanged();
                                            studentModel.deleteCertificateOfStudents(certificate.getId(), new StudentModel.OnUpdateCertificateListener() {
                                                @Override
                                                public void onCompleted() {
                                                    Log.d(TAG, "onCompleted: ");
                                                }

                                                @Override
                                                public void onFailure() {
                                                    Log.d(TAG, "onFailure: ");
                                                }
                                            });
                                        }

                                        @Override
                                        public void onDeleteCertificateFailure() {
                                            Log.d(TAG, "onDeleteCertificateFailure: ");
                                        }
                                    });
                                });
                                builder.setNegativeButton("No", (dialogInterface, i) -> {
                                            dialogInterface.dismiss();
                                        })
                                        .show();
                            default:
                                return false;
                        }
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

    public static class CertificateViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCertificateId, tvCertificateName;
        private ImageView menuCertificate;

        public CertificateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCertificateId = itemView.findViewById(R.id.tvIdCertificate);
            tvCertificateName = itemView.findViewById(R.id.tvCertificateName);
            menuCertificate = itemView.findViewById(R.id.menu_certificate);
        }
    }
}
