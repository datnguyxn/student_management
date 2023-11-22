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
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.com.student_management.R;
import com.com.student_management.constants.App;
import com.com.student_management.entities.Student;
import com.com.student_management.fragments.UpdateStudentBottomSheetFragment;
import com.com.student_management.models.StudentModel;
import com.com.student_management.fragments.ViewAndUpdateCertificateOfStudentFragment;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private static final String TAG = "StudentAdapter";
    private Context context;
    private ArrayList<Student> students;
    private StudentModel studentModel = new StudentModel();
    private Bundle bundle = new Bundle();
    private AlertDialog alertDialog;

    public StudentAdapter(Context context, ArrayList<Student> students) {
        this.context = context;
        this.students = students;
    }

    public StudentAdapter(Context context) {
        this.context = context;
        this.students = new ArrayList<>();
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
        notifyDataSetChanged();
    }

    public void clearData() {
        this.students.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new StudentAdapter.StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position);
        holder.tvId.setText(student.getId());
        holder.tvNameStudent.setText(student.getFullName());
        holder.tvMajor.setText(student.getMajor());
        holder.itemStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("id", student.getId());
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.menu);
                popupMenu.inflate(R.menu.student_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Log.d("menu", "onMenuItemClick: " + menuItem.getItemId());
                        switch (menuItem.getItemId()) {
                            case App.UPDATE_STUDENT:
                                bundle.putString("id", student.getId());
                                UpdateStudentBottomSheetFragment updateStudentBottomSheetFragment = new UpdateStudentBottomSheetFragment();
                                updateStudentBottomSheetFragment.setArguments(bundle);
                                updateStudentBottomSheetFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), updateStudentBottomSheetFragment.getTag());
                                return true;
                            case App.DELETE_STUDENT:
                                alertDialog = new AlertDialog.Builder(context)
                                        .setTitle("Delete Student Information")
                                        .setMessage("Are you sure you want to delete this student information?")
                                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                                            studentModel.deleteStudent(student.getId(), new StudentModel.OnDeleteStudentListener() {
                                                @Override
                                                public void onCompleted(boolean isDelete) {
                                                    Log.d(TAG, "onCompleted: " + isDelete);
                                                    if (isDelete) {
                                                        students.remove(position);
                                                        notifyDataSetChanged();
                                                    }
                                                }
                                            });
                                        })
                                        .setNegativeButton("No", (dialogInterface, i) -> {
                                            Log.d(TAG, "onClick: cancel delete user");
                                            alertDialog.dismiss();
                                        })
                                        .show();
                            case App.CERTIFICATE:
                                bundle.putString("id", student.getId());
                                ViewAndUpdateCertificateOfStudentFragment viewAndUpdateCertificateOfStudentFragment = new ViewAndUpdateCertificateOfStudentFragment();
                                viewAndUpdateCertificateOfStudentFragment.setArguments(bundle);
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, viewAndUpdateCertificateOfStudentFragment).addToBackStack(null).commit();
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
        return students.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvNameStudent, tvMajor;
        private ImageView menu;
        private LinearLayout itemStudent;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tvId);
            tvNameStudent = (TextView) itemView.findViewById(R.id.tvNameStudent);
            tvMajor = (TextView) itemView.findViewById(R.id.tvMajor);
            itemStudent = (LinearLayout) itemView.findViewById(R.id.ll_item_student);
            menu = (ImageView) itemView.findViewById(R.id.menu_student);
        }
    }
}
