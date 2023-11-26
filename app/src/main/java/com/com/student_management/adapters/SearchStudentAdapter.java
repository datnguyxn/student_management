package com.com.student_management.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.com.student_management.fragments.DetailAndUpdateStudentFragment;
import com.com.student_management.middleware.RequireRole;
import com.com.student_management.models.StudentModel;
import com.com.student_management.utils.StringUtil;

import java.util.ArrayList;

public class SearchStudentAdapter extends RecyclerView.Adapter<SearchStudentAdapter.SeachStudentViewHolder> implements Filterable {
    private static final String TAG = "SearchStudentAdapter";
    private Context context;
    private ArrayList<Student> students;
    private ArrayList<Student> studentsFilter;
    private StudentModel studentModel = new StudentModel();
    private Bundle bundle = new Bundle();
    private AlertDialog alertDialog;
    private String role;

    public SearchStudentAdapter(Context context) {
        this.context = context;
        this.students = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");
        this.studentsFilter = students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
        notifyDataSetChanged();
    }

    public void setStudentsFilter(ArrayList<Student> studentsFilter) {
        this.studentsFilter = studentsFilter;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchStudentAdapter.SeachStudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new SearchStudentAdapter.SeachStudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchStudentAdapter.SeachStudentViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + studentsFilter.get(position).toString());
        Student student = studentsFilter.get(position);
        holder.tvId.setText(student.getId());
        holder.tvNameStudent.setText(student.getFullName());
        holder.tvMajor.setText(StringUtil.convertString(student.getMajor()));
        holder.itemStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    bundle.putString("studentId", student.getId());
                    DetailAndUpdateStudentFragment detailAndUpdateStudentFragment = new DetailAndUpdateStudentFragment();
                    detailAndUpdateStudentFragment.setArguments(bundle);
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, detailAndUpdateStudentFragment).addToBackStack(null).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.menu);
                popupMenu.inflate(R.menu.student_menu);

                if (!RequireRole.checkRole(role, context)) {
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            Log.d("menu", "onMenuItemClick: " + menuItem.getItemId());
                            if (menuItem.getItemId() == App.DELETE_STUDENT) {
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
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                Log.d(TAG, "performFiltering: " + charString);
                if (charString.isEmpty()) {
                    studentsFilter = students;
                    setStudentsFilter(studentsFilter);
                } else {
                    ArrayList<Student> filteredList = new ArrayList<>();
                    for (Student student : students) {
                        if (student.getId().toLowerCase().contains(charString.toLowerCase()))  {
                            Log.d(TAG, "performFiltering: " + student.toString());
                            filteredList.add(student);
                        } else if (student.getFullName().toLowerCase().contains(charString.toLowerCase())) {
                            Log.d(TAG, "performFiltering: " + student.toString());
                            filteredList.add(student);
                        } else if (student.getMajor().toLowerCase().contains(charString.toLowerCase())) {
                            Log.d(TAG, "performFiltering: " + student.toString());
                            filteredList.add(student);
                        } else if (student.getPhone().toLowerCase().contains(charString.toLowerCase())) {
                            Log.d(TAG, "performFiltering: " + student.toString());
                            filteredList.add(student);
                        } else if (student.getAddress().toLowerCase().contains(charString.toLowerCase())) {
                            Log.d(TAG, "performFiltering: " + student.toString());
                            filteredList.add(student);
                        } else if (student.getEmail().toLowerCase().contains(charString.toLowerCase())) {
                            Log.d(TAG, "performFiltering: " + student.toString());
                            filteredList.add(student);
                        } else if (student.getBirthday().toLowerCase().contains(charString.toLowerCase())) {
                            Log.d(TAG, "performFiltering: " + student.toString());
                            filteredList.add(student);
                        }
                    }
                    studentsFilter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = studentsFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                studentsFilter = (ArrayList<Student>) filterResults.values;
                setStudentsFilter(studentsFilter);
            }
        };
    }

    public static class SeachStudentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvNameStudent, tvMajor;
        private ImageView menu;
        private LinearLayout itemStudent;

        public SeachStudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tvId);
            tvNameStudent = (TextView) itemView.findViewById(R.id.tvNameStudent);
            tvMajor = (TextView) itemView.findViewById(R.id.tvMajor);
            itemStudent = (LinearLayout) itemView.findViewById(R.id.ll_item_student);
            menu = (ImageView) itemView.findViewById(R.id.menu_student);
        }
    }
}
